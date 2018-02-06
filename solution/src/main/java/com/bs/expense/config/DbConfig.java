package com.bs.expense.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
@ComponentScan(basePackages = "com.bs.expense")
@EnableConfigurationProperties(DataSourceProperties.class)
public class DbConfig extends HikariConfig {

    @Bean
    public HikariDataSource dataSource(final DataSourceProperties properties) throws SQLException {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(properties.getUrl());
        hikariConfig.setUsername(properties.getUsername());
        hikariConfig.setPassword(properties.getPassword());
        HikariDataSource ds = new HikariDataSource(hikariConfig);
        return new HikariDataSource(ds);
    }

}