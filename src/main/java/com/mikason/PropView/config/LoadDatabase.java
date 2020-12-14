package com.mikason.PropView.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties("spring.datasource")
@Slf4j
public class LoadDatabase {

    private String driverClassName;
    private String url;
    private String username;
    private String password;

    @Profile("dev")
    @Bean
    public String devDBConnection(){
        return "DB Connection for DEV - H2";
    }

    @Profile("test")
    @Bean
    public String testDBConnection() {
        return "DB Connection for TEST - MYSQL";
    }
    /**
    @Bean
    CommandLineRunner initDatabase(PropertyRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(new Property("", "10", "Torres Place", "St Ives", "NSW", "Australia", "2075")));
            log.info("Preloading " + repository.save(new Property("1a", "1", "Rialto Lane", "Manly", "NSW", "Australia", "2095")));
        };
    }
    */
}
