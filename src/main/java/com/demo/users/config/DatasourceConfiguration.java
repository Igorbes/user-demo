package com.demo.users.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;

@Order(Integer.MIN_VALUE)
@Configuration
public class DatasourceConfiguration {

    @Value("${app.datasource.url}")
    private String dbUrl;
    @Value("${app.datasource.username}")
    private String dbUsername;
    @Value("${app.datasource.password}")
    private String dbPassword;
    @Value("${app.datasource.max.connection}")
    private int maxConnection;
    @Value("${app.datasource.idle.timeout}")
    private int idleTimeout;
    @Value("${app.datasource.idle.min}")
    private double idleMin;
    @Value("${app.datasource.max.lifetime}")
    private int maxLifeTime;
    @Value("${app.datasource.timeout.connection}")
    private int connectionTimeout;

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUsername);
        config.setPassword(dbPassword);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "1024");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "4096");
        config.setMaximumPoolSize(maxConnection);
        config.setConnectionTimeout(connectionTimeout);
        config.setIdleTimeout(idleTimeout);
        config.setMaxLifetime(maxLifeTime);
        config.setMinimumIdle((int) Math.max(0, maxConnection * idleMin));
        config.setPoolName("Hikari postgres pool");
        config.setDriverClassName("org.postgresql.Driver");
        return new HikariDataSource(config) {
            @PreDestroy
            public void deinit() {
                close();
            }
        };
    }

    @Bean("defaultJdbcTemplate")
    public VelocityJdbcTemplate jdbcTemplate(@Autowired DataSource dataSource) {
        return new VelocityJdbcTemplate(dataSource);
    }
}
