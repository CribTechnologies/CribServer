package com.crib.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class EnvVariables {

    private static EnvVariables singleInstance;
    public String BASE_URL;
    public String EMAIL;
    public String PASSWORD;

    private EnvVariables() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("/Users/arvind/Documents/Coding/Apps/Crib/CribServer/src/main/java/com/crib/server/private/environment-variables.txt"));
            String st;
            boolean currentlyKey = false;
            String key = "";
            Map<String, String> variables = new HashMap<>();
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
            EMAIL = variables.get("EMAIL");
            PASSWORD = variables.get("PASSWORD");
        }
        catch (Exception e) {
            System.out.println("ERROR: Error in initializing environment variables!");
        }
    }

    public static EnvVariables getInstance() {
        if (singleInstance == null)
            singleInstance = new EnvVariables();
        return singleInstance;
    }
}
