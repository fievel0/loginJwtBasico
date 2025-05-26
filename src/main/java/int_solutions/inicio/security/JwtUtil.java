package int_solutions.inicio.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key = Keys.hmacShaKeyFor("x*9_1~R7i+f`pQSs9A94OBmaokjbbG40".getBytes());

    public String generateToken(String username, String groupName){
        return Jwts.builder()
                .setSubject(username)
                .claim("group", groupName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public boolean validationToken(String token, String username){
        try {
            return (username.equals(extractUsername(token)) && !isTokenExpired(token));
        }catch (ExpiredJwtException e){
            System.out.println(">>> token expirado");
            return false;
        } catch (JwtException e){
            System.out.printf(">>> Token invalido");
            return false;
        }
    }


    public  String extractUsername(String token){
        return extractClaims(token).getSubject();
    }
    
    public Claims extractClaims(String token){
       JwtParser parser = Jwts.parser()
                .setSigningKey(key)
                .build();
       return parser.parseClaimsJws(token).getBody();
    }

    public boolean isTokenExpired(String token){
        return extractClaims(token).getExpiration().before(new Date());
    }

    public String extractGroup(String token){
        return extractClaims(token).get("group", String.class);
    }
}
