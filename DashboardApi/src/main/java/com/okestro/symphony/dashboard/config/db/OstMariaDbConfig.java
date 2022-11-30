package com.okestro.symphony.dashboard.config.db;

import com.okestro.symphony.dashboard.cmm.constant.DatabaseConstant;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

//@Configuration
//@PropertySource({ "classpath:application.yml" })
//@EnableJpaRepositories(
//        basePackages = {
//                DatabaseConstant.OST_BATCH_AGGREGATE_REPO
//                , DatabaseConstant.OST_BATCH_HOST_REPO
//                , DatabaseConstant.OST_BATCH_HYPERVISOR_REPO
//                , DatabaseConstant.OST_BATCH_PROJECT_REPO
//                , DatabaseConstant.OST_BATCH_QUOTA_REPO
//                , DatabaseConstant.OST_BATCH_VM_REPO
//        },
//        entityManagerFactoryRef = DatabaseConstant.OST_BATCH_ENTITY_MANAGER_REF,
//        transactionManagerRef = DatabaseConstant.OST_BATCH_TRANSACTION_MANAGER_REF
//)
public class OstMariaDbConfig {
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

//    @Bean
    public LocalContainerEntityManagerFactoryBean ostMariaEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(ostMariaDataSource());

        //Entity package 경로
        em.setPackagesToScan(DatabaseConstant.OST_BATCH_ENTITY);
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

//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource-openstack-maria")
    public DataSource ostMariaDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

//    @Bean
    public PlatformTransactionManager ostMariaTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(ostMariaEntityManager().getObject());
        return transactionManager;
    }
}