package com.crib.server;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

public class Argon2Setup {

    private static Argon2Setup singleInstance = null;
    private Argon2PasswordEncoder argon2PasswordEncoder;

    private Argon2Setup() {
    }

    public static Argon2Setup getInstance() {
        if (singleInstance == null)
            singleInstance = new Argon2Setup();
        return singleInstance;
    }

    public void initialize() {
        argon2PasswordEncoder = new Argon2PasswordEncoder();
    }

    public String createHash(String input) {
        return argon2PasswordEncoder.encode(input);
    }

    public boolean rawAndHashAreEqual(String raw, String hashed) {
        return argon2PasswordEncoder.matches(raw, hashed);
    }
}
