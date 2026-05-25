package com.sc.demo.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.sc.demo.Demo1Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private Demo1Application.KeyProperties keyProperties;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**","/swagger-ui","/swagger-ui/**").permitAll()
                        .requestMatchers("/V1/api/sc/loginByPhone/**","/loginByPhone/**").permitAll()
                        .requestMatchers("/V1/api/sc/ChekLogin/**").permitAll()
                        .requestMatchers("/V1/api/sc/createNotification/**","/createNotification","/createNotification/**").permitAll()
                        .requestMatchers("/V1/api/sc/createAnnouncements/**","/createAnnouncements","/createAnnouncements/**").permitAll()
                        .requestMatchers("/V1/api/sc/createChat/**","/createChat","/createChat/**").permitAll()
                        .requestMatchers("/V1/sc/api/getFamilyBasicInformation/**","/getFamilyBasicInformation","/getFamilyBasicInformation/**").permitAll()
                        .requestMatchers("/V1/sc/api/getFamilyHousingInformation/**","/getFamilyHousingInformation","/getFamilyHousingInformation/**").permitAll()
                        .requestMatchers("/V1/sc/api/getChildrenAndMailyFamilyMambersResponse/**","/getChildrenAndMailyFamilyMambersResponse","/getChildrenAndMailyFamilyMambersResponse/**").permitAll()
                        .requestMatchers("/V1/api/sc/getPhoneChats/**","/getPhoneChats","/getPhoneChats/**").permitAll()
                        .requestMatchers("/V1/api/sc/writeMessages/**","/writeMessages","/writeMessages/**").permitAll()
                        .requestMatchers("/V1/api/sc/getMessagesChat/**","/getMessagesChat","/getMessagesChat/**").permitAll()
                        .requestMatchers("/V1/api/sc/SearchByName/**","/SearchByName","/SearchByName/**").permitAll()
                        .requestMatchers("/V2/api/sc/SearchByNameV2/**","/SearchByNameV2","/SearchByNameV2/**").permitAll()
                        .requestMatchers("/V1/sc/api/checkNumber/**","/checkNumber","/checkNumber/**").permitAll()
                        .requestMatchers("/V1/api/sc/getAllNotificationFamily/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt(Customizer.withDefaults())
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(keyProperties.publicKey()).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(keyProperties.publicKey()).privateKey(keyProperties.privateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

}
