package com.example.yeobee.config;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@Component
public class MysqlTestContainer {

    public static final String DATABASE_NAME = "yeobee";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "password";
    private static final DockerImageName IMAGE_NAME = DockerImageName.parse("mysql:8.0.33");
    public static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>(IMAGE_NAME)
        .withDatabaseName(DATABASE_NAME)
        .withUsername(USERNAME)
        .withPassword(PASSWORD);

    static {
        MYSQL_CONTAINER.start();
    }

    @PreDestroy
    public void stop() {
        MYSQL_CONTAINER.stop();
    }
}