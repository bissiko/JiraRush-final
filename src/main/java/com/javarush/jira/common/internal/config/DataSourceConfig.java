package com.javarush.jira.common.internal.config;

//import jakarta.activation.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

//import java.sql.DriverManager;

@Configuration
public class DataSourceConfig {

    @Bean
    @Profile("prod")
    public DataSource prodDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/jira");
        dataSource.setUsername("jira");
        dataSource.setPassword("JiraRush");
        return (DataSource) dataSource;
    }

    @Bean
    @Profile("test")
    public DataSource testDataSource() {
        try {
            // Завантажуємо драйвер H2
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Драйвер H2 не знайдено", e);
        }

        // Створюємо вбудовану базу даних H2
        return (DataSource) new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .setName("kmpk")
                .build();
    }
}