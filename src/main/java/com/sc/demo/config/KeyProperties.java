package com.sc.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa")
public record KeyProperties(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
}
