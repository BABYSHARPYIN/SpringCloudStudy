import io.jsonwebtoken.*;

import java.util.Date;
import java.util.UUID;

public class Test {
    private String signature = "admin";

    @org.junit.Test
    public void jwt(){
        //构建 jwtBuilder
        JwtBuilder jwtBuilder = Jwts.builder();
        String jwtToken = jwtBuilder
                //Header
                .setHeaderParam("type", "JWT")
                .setHeaderParam("alg", "HS256")
                //Payload
                .claim("username", "tom")
                .claim("role", "admin")
                .setSubject("admin-test")
                //设置过期时间
                .setExpiration(new Date(System.currentTimeMillis() +86400000))
                .setId(UUID.randomUUID().toString().replace("-", ""))
                //signature
                .signWith(SignatureAlgorithm.HS256,signature)
                .compact();
        System.out.println(jwtToken);
    }
    @org.junit.Test
    public void parse(){
        String jwtToken = "eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJ1c2VybmFtZSI6InRvbSIsInJvbGUiOiJhZG1pbiIsInN1YiI6ImFkbWluLXRlc3QiLCJleHAiOjE2Njk4ODM0MTQsImp0aSI6ImVlYTVmZDFmMzljZTQzYTJiODc5MjA5MTE2YzJlNmUyIn0.-RQRu0vNW1ua_Qf_MMlAu8TQzUtJ-Ne32ZwR8m5SZ9M";
        JwtParser parser = Jwts.parser();
        Jwt parse = null;
        try {
            parse = parser.setSigningKey(signature)
                    .parse(jwtToken);
        } catch (ExpiredJwtException e) {
            throw new RuntimeException(e);
        } catch (MalformedJwtException e) {
            throw new RuntimeException(e);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        System.out.println(parse.getHeader());
        System.out.println(parse.getBody());
    }
}
