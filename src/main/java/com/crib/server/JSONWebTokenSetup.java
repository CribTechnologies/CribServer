package com.crib.server;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class JSONWebTokenSetup {

    private static JSONWebTokenSetup singleInstance = null;
    private byte[] jwtSecretKey;
    private Calendar calendar;

    private JSONWebTokenSetup() {
    }

    public static JSONWebTokenSetup getInstance() {
        if (singleInstance == null)
            singleInstance = new JSONWebTokenSetup();
        return singleInstance;
    }

    public void initialize() throws IOException {
        calendar = Calendar.getInstance();

        BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/arvind/Documents/Coding/Apps/Crib/CribServer/src/main/java/com/crib/server/private/jwt-private-key.txt"));
        jwtSecretKey = bufferedReader.readLine().getBytes();
    }

    public String generateSignInToken(String userId) {
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 7);

        return Jwts.builder()
                .setSubject("users/" + userId)
                .setExpiration(calendar.getTime())
                .signWith(
                    SignatureAlgorithm.HS256,
                    jwtSecretKey
                )
                .compact();
    }

    public boolean signInTokenIsValid(String userId, String token) {
        String subject = Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return subject.equals("users/" + userId);
    }
}
