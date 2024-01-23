package com.adamstraub.tonsoftacos.services.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

//implement exception handling

@Service
public class JwtService {

    //create token

//    make these all caps and dash between
    @Value("${key}")
    private String secret;

    @Value("${BEGIN_KEY}")
    private int beginKey;
    @Value("${END_KEY}")
    private int endKey;

    @Value("${CHAR_MIN}")
    private int charMin;

    @Value("${CHAR_MAX}")
    private int charMax;

    @Value("${SUB_MIN}")
    private int subMin;

    @Value("${SUB_MAX}")
    private int subMax;

    @Value("${EX1}")
    private int ex1;
    @Value("${EX2}")
    private int ex2;
    @Value("${EX3}")
    private int ex3;

    @Value("${CHARSET}")
    private String CHARSET;

    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    private String buildToken(String username){
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
//                16 hours, reflective of our owners work day - to be altered to facilitate mitigation of token theft
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60) * 16))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
//        System.out.println(token);
//        System.out.println("token issued: " + new Date(System.currentTimeMillis()));
//        System.out.println("token expires: " + new Date(System.currentTimeMillis() + (1000 * 60 * 60) * 16));
        return token;
    }


    public String generateToken(String username){
        return buildToken(username);
    }

//    validate token
    private Claims extractAllClaims(String token){
        try {
            return
                    Jwts
                            .parserBuilder()
                            .setSigningKey(getSignKey())
                            .build()
                            .parseClaimsJws(token)
                            .getBody();
        } catch (Exception e) {
            throw new JwtException("Session expired.");
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }
    public Date extractIssuedAt(String token){
        return extractClaim(token, Claims::getIssuedAt);
    }
    private Boolean isTokenExpired(String token){
            return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = decrypt(extractUsername(token));
        try {
            return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
        } catch (Exception e){
            throw new JwtException("Invalid token.");
        }
        }


//  encrypt - helper method used during development as a means to encrypt credentials
//  before storing them and facilitating decryption means. Algorithm will be implemented from the front end.

    public String encrypt(String string){

//        System.out.println("value to be encrypted: " + string);



                byte[] codeBytes = string.getBytes(StandardCharsets.UTF_8);
        List<Integer> rolledCodeBytes = new ArrayList<>();
        int codeByteValue;
        for (byte codeByte : codeBytes) {
            codeByteValue = codeByte;
            codeByteValue += beginKey;
            rolledCodeBytes.add(codeByteValue);
        }

//      new collection with altered char values
        List<Character> chars = new ArrayList<>();
        for (int integer : rolledCodeBytes) {
            chars.add((char) integer);
        }
//      for each element insert three new random chars
        for (int i = 0; i < chars.size(); i++) {
            chars.add(i, randomChar());
            i++;
            chars.add(i, randomChar());
            i++;
            chars.add(i, randomChar());
            i++;
        }
        chars.add(randomChar());
        chars.add(randomChar());
        chars.add(randomChar());

        StringBuilder encryptionBuilder = new StringBuilder(chars.size());
        for (Character ch : chars) {
            encryptionBuilder.append(ch);
        }
//        System.out.println("value encrypted: " + encryptionBuilder);
        return encryptionBuilder.toString();
    }

//    decrypt
public String decrypt(String encodedString)  {
//        System.out.println("encoded: " + encodedString);

        String decodedStart = String.valueOf(encodedString.charAt(beginKey));
        String decodedEnd = String.valueOf(encodedString.charAt(encodedString.length() - endKey));
        String wholeDecoded = "";
        StringBuilder decoded = new StringBuilder();
        for (int i = beginKey; i < encodedString.length(); i = i + endKey) {
            decoded.append(encodedString.charAt(i));
        }
        decoded = new StringBuilder(decoded.substring(subMin, decoded.toString().length() - subMax));
        wholeDecoded = wholeDecoded.concat(decodedStart + decoded + decodedEnd);
//        System.out.println("decoded: " + wholeDecoded);
        byte[] decodedBytes = wholeDecoded.getBytes(StandardCharsets.UTF_8);
        int decodeByteValue;
        List<Character> decodedChars = new ArrayList<>();
        StringBuilder decrypt = new StringBuilder(0);
        for (byte codeByte : decodedBytes) {
            decodeByteValue = codeByte;
            decodeByteValue -= beginKey;
            decodedChars.add((char) decodeByteValue);
        }
        for (Character ch : decodedChars) {
            decrypt.append(ch);
        }
//        System.out.println("decrypted: " + decrypt);
        return decrypt.toString();
    }

    private char randomChar() {
        int min = charMin, max = charMax;
        int random = (int) (Math.random() * ((max - min)) + min);
        int[] excluded = {ex1, ex2, ex3};
        char choice = 0;
        for (int ex : excluded) {
            choice = random == ex ? randomChar() : (char) random;
        }
        return choice;
    }
}
