package com.jcgarciam.n26;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan({
        "com.jcgarciam.n26.rest",
        "com.jcgarciam.n26.repositories",
        "com.jcgarciam.n26.repositories.cleanup"
})
public class N26ChallengeMain {
    public static void main(String[] args) {
        SpringApplication.run(N26ChallengeMain.class, args);
    }
}