package com.sc.demo;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@SpringBootApplication
public class Demo1Application {

	public static void main(String[] args) {
		SpringApplication.run(Demo1Application.class, args);
	}




	@Bean
	FirebaseMessaging firebaseMessaging() throws IOException {

//		FirebaseAuth auth = FirebaseAuth.getInstance();
//
//		String customToken = auth.createCustomToken("155");

		GoogleCredentials googleCredentials=GoogleCredentials.fromStream(
				new ClassPathResource("firebase-service-account.json").getInputStream()
		);
		FirebaseOptions firebaseOptions= FirebaseOptions.builder()
				.setCredentials(googleCredentials).build();
		FirebaseApp app=FirebaseApp.initializeApp(firebaseOptions,"sponsor");

//        System.out.println(googleCredentials.refreshAccessToken().getTokenValue());
		return  FirebaseMessaging.getInstance(app);
	}


}
