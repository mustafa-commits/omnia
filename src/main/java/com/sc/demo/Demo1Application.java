package com.sc.demo;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;


@EnableConfigurationProperties(Demo1Application.KeyProperties.class)
@SpringBootApplication
public class Demo1Application {

	public static void main(String[] args) {
		SpringApplication.run(Demo1Application.class, args);
	}

	@Bean
	FirebaseMessaging firebaseMessaging() throws IOException {

		GoogleCredentials googleCredentials=GoogleCredentials.fromStream(
				new ClassPathResource("firebase-service-account.json").getInputStream()
		);
		FirebaseOptions firebaseOptions= FirebaseOptions.builder()
				.setCredentials(googleCredentials).build();
		FirebaseApp app=FirebaseApp.initializeApp(firebaseOptions,"sponsor");
		return  FirebaseMessaging.getInstance(app);
	}

	@ConfigurationProperties(prefix = "rsa")
	public static record KeyProperties(
			RSAPublicKey publicKey,
			RSAPrivateKey privateKey
		)
	{ }
}
