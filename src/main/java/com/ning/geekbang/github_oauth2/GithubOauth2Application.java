package com.ning.geekbang.github_oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class GithubOauth2Application {

    public static void main(String[] args) {
        SpringApplication.run(GithubOauth2Application.class, args);
    }

}
