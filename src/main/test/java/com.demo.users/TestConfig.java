package com.demo.users;

import com.demo.users.service.UserService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;

@TestConfiguration
public class TestConfig {

    @Bean
    public PostgreSQLContainer<?> container() {
        PostgreSQLContainer<?> selfPostgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));
        selfPostgreSQLContainer.start();
        return selfPostgreSQLContainer;
    }

    @Bean
    public DataSource dataSource(PostgreSQLContainer<?> postgreSQLContainer) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
        config.setUsername(postgreSQLContainer.getUsername());
        config.setPassword(postgreSQLContainer.getPassword());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "1024");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "4096");
        config.setPoolName("Hikari postgres pool");
        config.setDriverClassName(postgreSQLContainer.getDriverClassName());
        return new HikariDataSource(config) {
            @PreDestroy
            public void deinit() {
                close();
            }
        };
    }
}
