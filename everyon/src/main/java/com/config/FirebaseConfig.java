package com.config;



import java.io.FileInputStream;
import java.io.IOException;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

@Configuration
public class FirebaseConfig {


    
    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        //log.info("Initializing Firebase.");
        FileInputStream serviceAccount =
        new FileInputStream("./src/main/resources/serviceAccountKey.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .setStorageBucket("heroku-sample.appspot.com")
        .build();
        
        FirebaseApp app = FirebaseApp.initializeApp(options);
        //log.info("FirebaseApp initialized" + app.getName());
        return app;
    }


	@Bean
	public FirebaseAuth getFirebaseAuth() throws IOException{
		FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(firebaseApp());
		return firebaseAuth;
	}
    
}