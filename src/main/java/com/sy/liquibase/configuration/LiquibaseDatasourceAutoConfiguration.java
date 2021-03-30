package com.sy.liquibase.configuration;


import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(prefix = "datasource.primary", value = {"liquibase"})
public class LiquibaseDatasourceAutoConfiguration {

    @Bean
    @Primary
    @ConditionalOnProperty(prefix = "datasource.primary", value = {"liquibase"})
    @ConditionalOnMissingBean
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.primary.liquibase")
    @ConditionalOnProperty(prefix = "datasource.primary", value = {"liquibase"})
    @ConditionalOnMissingBean
    public LiquibaseProperties primaryLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    @ConditionalOnProperty(prefix = "datasource.primary", value = {"liquibase"})
    @ConditionalOnMissingBean
    public SpringLiquibase primaryLiquibase() {
        return springLiquibase(primaryDataSource(), primaryLiquibaseProperties());
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.secondary")
    @ConditionalOnProperty(prefix = "datasource.secondary", value = {"liquibase"})
    @ConditionalOnMissingBean
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.secondary.liquibase")
    @ConditionalOnProperty(prefix = "datasource.secondary", value = {"liquibase"})
    @ConditionalOnMissingBean
    public LiquibaseProperties secondaryLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    @ConditionalOnProperty(prefix = "datasource.secondary", value = {"liquibase"})
    @ConditionalOnMissingBean
    public SpringLiquibase secondaryLiquibase() {
        return springLiquibase(secondaryDataSource(), secondaryLiquibaseProperties());
    }

    private static SpringLiquibase springLiquibase(DataSource dataSource, LiquibaseProperties properties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(properties.getChangeLog());
        liquibase.setContexts(properties.getContexts());
        liquibase.setDefaultSchema(properties.getDefaultSchema());
        liquibase.setDropFirst(properties.isDropFirst());
        liquibase.setShouldRun(properties.isEnabled());
        liquibase.setLabels(properties.getLabels());
        liquibase.setChangeLogParameters(properties.getParameters());
        liquibase.setRollbackFile(properties.getRollbackFile());
        return liquibase;
    }
}