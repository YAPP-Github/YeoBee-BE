package com.example.yeobee.config;

import static com.example.yeobee.config.MysqlTestContainer.*;

import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class TestDataSourceConfig {

    @Bean
    @DependsOn("mysqlTestContainer")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
            .url("jdbc:mysql://localhost:" +
                 MYSQL_CONTAINER.getMappedPort(3306) +
                 "/" + DATABASE_NAME)
            .driverClassName("com.mysql.cj.jdbc.Driver")
            .username(USERNAME)
            .password(PASSWORD)
            .build();
    }
}
