package com.adamstraub.tonsoftacos.services.security.JwtService;

import com.adamstraub.tonsoftacos.dto.securityDto.SubjectDTO;
import com.adamstraub.tonsoftacos.services.security.EncryptionService.IEncryptionService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;


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
        return Keys.hmacShaKeyFor(keyBytes);
    }


private String buildToken(SubjectDTO subject){
/*            5 min for access token, 4hrs for refresh token, front end is checking every minute since it needs to update the clock
            .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 ) * 5))
             application is set for 2 min for testing restore when done to above
             */
    return Jwts.builder()
            .setSubject(subject.getUsername())
            .claim("ownername", subject.getOwnername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + (1000 * 120 )))
            .signWith(getSignKey(), SignatureAlgorithm.forName(sigAlg)).compact();
}
    @Override
    public String generateToken(SubjectDTO subject){
        return buildToken(subject);
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
        final String username = encryptionService.decrypt(extractUsername(token));
        try {
            return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
        } catch (Exception e){
            throw new JwtException("Invalid token.");
        }
    }
}
