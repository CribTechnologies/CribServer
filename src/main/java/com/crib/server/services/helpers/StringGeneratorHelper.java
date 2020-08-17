package com.crib.server.services.helpers;

public class StringGeneratorHelper {

    private static final char[] alphanumeric;

    static {
         alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    }

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= length; i++) {
            int randomIndex = (int) (alphanumeric.length * Math.random());
            sb.append(alphanumeric[randomIndex]);
        }
        return sb.toString();
    }
}
