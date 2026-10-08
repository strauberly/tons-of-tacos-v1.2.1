package com.adamstraub.tonsoftacos.services.security.JwtService;

import com.adamstraub.tonsoftacos.dto.securityDto.SubjectDTO;
import com.adamstraub.tonsoftacos.services.security.EncryptionService.IEncryptionService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService implements IJwtService{

    @Value("${KEY}")
    private String secret;

    @Value("${SIGNATURE_ALGORITHM}")
    private String sigAlg;

    @Autowired
    private IEncryptionService encryptionService;


// generate token

    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
//        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        log.info("Secret: {}", secret );
        log.info("bytes: {}", keyBytes );

        return Keys.hmacShaKeyFor(keyBytes);
    }


private String buildToken(SubjectDTO subject){
/*            5 min for access token, 4hrs for refresh token, front end is checking every minute since it needs to update the clock
            .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 ) * 5))
             application is set for 2 min for testing restore when done to above ie 5 min
             */
    return Jwts.builder()
//            .setSubject(subject.getUsername())
//            changedto for camel case subject dto also
            .claim("ownerName", subject.getOwnername())
            .claim("userName", subject.getUsername())
//added claim of refresh token 5/19/26
            .claim("refreshToken", subject.getRefreshToken())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + (1000 * 120)))
//            .signWith(getSignKey(), SignatureAlgorithm.forName(sigAlg)).compact();
            .signWith(getSignKey(), SignatureAlgorithm.forName(sigAlg)).compact();
}

    @Override
    public String generateToken(SubjectDTO subject){
        return buildToken(subject);
    }


//    validate token
    private Claims extractAllClaims(String token){
        log.info("all claims: {}", token);
        try {
            return
                    Jwts
                            .parserBuilder()
//                            .setSigningKey(getSignKey())
                            .setSigningKey(getSignKey())
                            .build()
                            .parseClaimsJws(token)
                            .getBody();
        } catch (Exception e) {
            log.error("uh-uh: {}", e.getMessage());
            throw new JwtException("Session expired.");
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
//    public String extractUsername(String token){
//        return extractClaim(token, Claims::getSubject);
//    }
public String extractUsername(String token){
    Claims claims = extractAllClaims(token);
    return claims.get("userName", String.class);
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
    public String extractRefreshToken(String token){
        log.info("extract refresh from: {}", token);
        Claims claims = extractAllClaims(token);
        log.info("claims: " + claims);
        log.info(String.valueOf(claims));
        log.info(claims.get("refreshToken").toString());
        log.info(claims.get("refreshToken", String.class));
        return claims.get("refreshToken", String.class);

    }
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = encryptionService.decrypt(extractUsername(token));
        try {
            return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
        } catch (Exception e){
            throw new JwtException("Invalid token.");
        }
    }
}
