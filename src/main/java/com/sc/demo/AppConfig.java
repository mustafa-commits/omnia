package com.sc.demo;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import javax.sql.DataSource;


@Configuration
public class AppConfig {

    @Autowired
    Environment environment;

    @Value("${url}")
    private String URL;

    @Value("${dbuser}")
    private String USER;

    @Value("${driver}")
    private String DRIVER;

    @Value("${dbpassword}")
    private String PASSWORD;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        System.out.println(DRIVER);

        config.setMaximumPoolSize(5);
        config.setMinimumIdle(1);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setPoolName("alayn-sponsor");

        System.out.println(config.getDataSourceProperties());
        System.out.println(config.getConnectionTimeout());
        System.out.println(config.getHealthCheckProperties());
        System.out.println(config.getConnectionTestQuery());
        System.out.println("max life time "+config.getMaxLifetime()); //1800000

        return new HikariDataSource(config);
    }

}