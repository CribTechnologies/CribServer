package com.crib.server;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseSetup {

    private static FirebaseSetup singleInstance = null;
    private Firestore database;

    private String SERVICE_ACCOUNT_FILE_PATH = "/Users/arvind/Documents/Coding/Apps/Crib/CribServer/src/main/java/com/crib/server/private/crib-3e0df-firebase-adminsdk-wx1q9-8732702fe7.json";
    private String DATABASE_URL = "https://crib-3e0df.firebaseio.com";

    private FirebaseSetup() {
    }
    
    public static FirebaseSetup getInstance() {
        if (singleInstance == null) {
            singleInstance = new FirebaseSetup();
        }
        return singleInstance;
    }

    public void initialize() throws IOException {
        FileInputStream serviceAccount = new FileInputStream(SERVICE_ACCOUNT_FILE_PATH);
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .setDatabaseUrl(DATABASE_URL)
                .build();

        FirebaseApp.initializeApp(options);
        database = FirestoreClient.getFirestore();
    }

    public Firestore getDatabase() {
        return database;
    }
}
