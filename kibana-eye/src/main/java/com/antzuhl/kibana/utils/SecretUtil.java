package com.antzuhl.kibana.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;

/**
 * JWT认证工具类
 * @author AntzUhl
 * @Date 16:51
 */
public class SecretUtil {

    /**
     * 根据salt + 月份生成密钥
     * @param userId 用户Id
     * */
    public static String create(Long userId, String username){
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            String token = JWT.create()
                    .withIssuer("auth0")
                    .withSubject("subject")
                    .withClaim("username",username)
                    .withClaim("userId",userId)
                    .sign(algorithm);
            System.out.println(token);
            return token;
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
            throw exception;
        }
    }

    public static Long verify(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            String payload = jwt.getPayload();
//            String username = jwt.getClaim("username").asString();
            Long userId = jwt.getClaim("userId").asLong();
//            System.out.prin/**/tln(payload);
//            System.out.println(username);
//            System.out.println(userId);
            return userId;
        } catch (JWTVerificationException exception){
            //Invalid signature/claims
            return -1L;
        }
    }



}
