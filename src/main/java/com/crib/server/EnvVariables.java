package com.crib.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class EnvVariables {

    public static String BASE_URL;

    static {
        try {
            BufferedReader br = new BufferedReader(new FileReader("/Users/arvind/Documents/Coding/Apps/Crib/CribServer/src/main/java/com/crib/server/private/environment-variables.txt"));
            String st;
            boolean currentlyKey = false;
            String key = "";
            Map<String, String> variables = new HashMap<String, String>();
            while ((st = br.readLine()) != null) {
                currentlyKey = !currentlyKey;
                if (currentlyKey) {
                    key = st;
                }
                else {
                    variables.put(key, st);
                }
            }

            BASE_URL = variables.get("BASE_URL");
        }
        catch (Exception e) {

        }
    }
}
