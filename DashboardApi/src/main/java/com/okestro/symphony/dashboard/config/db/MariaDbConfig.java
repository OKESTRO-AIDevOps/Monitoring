package com.okestro.symphony.dashboard.config.db;

import com.okestro.symphony.dashboard.cmm.constant.DatabaseConstant;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
@Configuration
@PropertySource({ "classpath:application.yml" })
@EnableJpaRepositories(
//        basePackages = {DatabaseConstant.MARIA_KEYSTONE_HYPERVISOR_REPO
//                ,DatabaseConstant.MARIA_KEYSTONE_PROJECT_REPO
//                ,DatabaseConstant.MARIA_NOVA_COMPUTE_VM_REPO
//                ,DatabaseConstant.MARIA_BATCH_PROJECT_QUOTA_REPO
//                ,DatabaseConstant.MARIA_DASHBOARD_REPO
//                ,DatabaseConstant.MARIA_BATCH_PROJECT_QUOTA_REPO
//                ,DatabaseConstant.MARIA_BATCH_HOST_REPO
//                ,DatabaseConstant.MARIA_BATCH_VM_REPO
//                ,DatabaseConstant.MARIA_BATCH_PROJECT_REPO
//                ,DatabaseConstant.MARIA_BATCH_AGGREGATE_REPO},
        entityManagerFactoryRef = DatabaseConstant.MARIA_ENTITY_MANAGER_REF,
        transactionManagerRef = DatabaseConstant.MARIA_TRANSACTION_MANAGER_REF
)

public class MariaDbConfig {
    @Value("${spring.jpa.database-platform}")
    private String propDialect;
    @Value("${spring.jpa.properties.hibernate.show_sql}")
    private String propShowSql;
    @Value("${spring.jpa.properties.hibernate.format_sql}")
    private String propFormSql;
    @Value("${spring.jpa.properties.hibernate.use_sql_comments}")
    private String propSqlComments;
    @Value("${spring.jpa.generate-ddl}")
    private String propGenerateDdl;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String propDdlAuto;
    @Value("${spring.jpa.hibernate.naming.physical-strategy}")
    private String propNameStrategy;

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean mainEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(mainDataSource());

        //Entity package 경로
        em.setPackagesToScan(DatabaseConstant.MARIA_ENTITY);
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        //Hibernate 설정
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", propDialect);
        properties.put("hibernate.show_sql", propShowSql);
        properties.put("hibernate.format_sql", propFormSql);
        properties.put("hibernate.use_sql_comments", propSqlComments);
        properties.put("hibernate.ddl-auto", propDdlAuto);
        properties.put("hibernate.naming.physical-strategy", propNameStrategy);
        properties.put("generate-ddl", propGenerateDdl);

        em.setJpaPropertyMap(properties);

        return em;
    }

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource mainDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager mainTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(mainEntityManager().getObject());
        return transactionManager;
    }

}
