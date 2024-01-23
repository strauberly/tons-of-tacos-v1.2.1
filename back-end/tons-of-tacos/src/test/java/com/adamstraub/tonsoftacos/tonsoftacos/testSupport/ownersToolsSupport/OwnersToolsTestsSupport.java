package com.adamstraub.tonsoftacos.tonsoftacos.testSupport.ownersToolsSupport;

import com.adamstraub.tonsoftacos.services.security.JwtService;
import com.adamstraub.tonsoftacos.tonsoftacos.testSupport.TestUris;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;

import java.security.Key;
import java.util.Date;

public class OwnersToolsTestsSupport extends TestUris {
//    holders for test bodies and auth tokens

    @Value("${key}")
    private String SECRET;

    @Autowired
    private JwtService jwtService = new JwtService();

    protected String validCredentials(){

        return """
               {
               "username": "jcast22",
               "psswrd": "tacoocat"
               }""";
    }

    protected String encryptedCredentials(){
        return """
               {
               "username":"m)Km7y{f0~nd$,hvNLOw0.F5FlP5u?5",
               "psswrd":"?aNwlfCd7glf(E&r)lLr}W?fT#Ld?aFw_ic"
               }""";
    }


    protected String badUsername(){
        String badUsername = jwtService.encrypt("tony");
        return
                "{ \"username\": " + '"' + badUsername + "\"," +  "\n" +
                "\"psswrd\" : \"?aNwlfCd7glf(E&r)lLr}W?fT\" " +
                "}";
    }

    protected String badPassword(){
        String badPassword = jwtService.encrypt("bigTony22");
        return "{ \"username\" :  \"m)Km7y{f0~nd$,hvNLOw0.F5FlP5u?5\"," + "\n" +
                "\"psswrd\" : " + '"' + badPassword + '"' +
                "}";
    }


    protected String validToken(){
        String body = validCredentials();
        System.out.println(body);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> tokenEntity = new HttpEntity<>(body, headers);

        String uri = getBaseUriForOwnersLogin();
        ResponseEntity<String> response = getRestTemplate().exchange(uri, HttpMethod.POST, tokenEntity,
                String.class);
        return response.getBody();
    }

    protected String encryptedToken(){
        String body = encryptedCredentials();
        System.out.println(body);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> tokenEntity = new HttpEntity<>(body, headers);

        String uri = getBaseUriForOwnersLogin();
        ResponseEntity<String> response = getRestTemplate().exchange(uri, HttpMethod.POST, tokenEntity,
                String.class);
        return response.getBody();
    }


    protected String expiredToken(){
        return "eyJhbGciOiJIUzI1NiJ9" +
                ".eyJzdWIiOiJtKUttN3l7ZjB-bmQkLGh2TkxPdzAuRjVGbFA1dT81IiwiaWF0IjoxNjg0ODk1ODI1LCJleHAiOjE2ODQ3NTM0MjV9" +
                ".cpfDr8dIxCv0VexVf99g5gbeQfTlSerEP9X8X3c_qas";
    }

    protected String badToken(){
        return buildBadToken();
    }

    protected String goodToken(){
        return buildGoodToken();
    }

//    public String generateToken(String username){
//        return buildToken(username);
//    }

    private String buildBadToken(){
//        set time variable instead of creating new
        String token = Jwts.builder()
                .setSubject(jwtService.encrypt("jerry"))
                .setIssuedAt(new Date(System.currentTimeMillis()))
//                testing what happens if expired time is before issued time
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60)))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
        System.out.println("token: " + token);
        System.out.println("token issued: " + new Date(System.currentTimeMillis()));
        System.out.println("token expires: " + new Date(System.currentTimeMillis() + (1000 * 60 * 60)));
        return token;
    }

    protected String buildGoodToken(){
//        set time variable instead of creating new
        String token = Jwts.builder()
                .setSubject("m)Km7y{f0~nd$,hvNLOw0.F5FlP5u?5")
                .setIssuedAt(new Date(System.currentTimeMillis()))
//                testing what happens if expired time is before issued time
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60)))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
        System.out.println("token: " + token);
        System.out.println("token issued: " + new Date(System.currentTimeMillis()));
        System.out.println("token expires: " + new Date(System.currentTimeMillis() + (1000 * 60 * 60)));
//        try {
//            return token;
//        }catch (io.jsonwebtoken.security.SignatureException exception){
//            System.out.println(exception.getLocalizedMessage());
//        }
        return token;
    }

    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
