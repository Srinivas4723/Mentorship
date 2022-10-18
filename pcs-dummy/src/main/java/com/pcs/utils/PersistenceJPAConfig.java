package com.pcs.utils;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@Component
@ComponentScan({"com.pcs"})
@EnableJpaRepositories(basePackages="com.pcs.repository")
public class PersistenceJPAConfig {
		@Value("${com.pcs.datasource.url}")
		private String url;
		@Value("${com.pcs.datasource.username}")
		private String username;
		@Value("${com.pcs.datasource.password}")
		private String password;
		
	    @Bean
	    public DataSource dataSource() {
	    	final DriverManagerDataSource dsb= new DriverManagerDataSource();
	    	//dsb.setDriverClassName("jdbc.mysql");
	    	dsb.setUrl(url);
	    	dsb.setUsername(username);
	    	dsb.setPassword(password);
	    	
	    	return dsb;
	    }
//	    @Bean
//	    public LocalSessionFactoryBean entityManagerFactory() {
//	    	final LocalSessionFactoryBean sessionFactory= new LocalSessionFactoryBean();
//	    	sessionFactory.setDataSource(dataSource());
//	    	sessionFactory.setPackagesToScan("com.pcs.model");
//	    	sessionFactory.setHibernateProperties(hibernateProperties());
//	    	return sessionFactory;
//	    }
//	    @Bean
//	    public PlatformTransactionManager hibernateTransactionManager() {
//	        HibernateTransactionManager transactionManager
//	          = new HibernateTransactionManager();
//	        transactionManager.setSessionFactory(entityManagerFactory().getObject());
//	        return transactionManager;
//	    }
	    @Bean
	    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
	    	final LocalContainerEntityManagerFactoryBean emb=new LocalContainerEntityManagerFactoryBean();
	    	emb.setDataSource(dataSource());
	    	emb.setPackagesToScan("com.pcs.model");
	    	final HibernateJpaVendorAdapter vendor= new HibernateJpaVendorAdapter();
	    	emb.setJpaVendorAdapter(vendor);
	    	emb.setJpaProperties(hibernateProperties());
	    	
	    	return emb;
	    }
	    @Bean
	    public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
	    	final JpaTransactionManager tm= new JpaTransactionManager();
	    	tm.setEntityManagerFactory(emf);
	    	return tm;
	    }
	    private final Properties hibernateProperties() {
	        final Properties hibernateProperties = new Properties();
	        hibernateProperties.setProperty(
	          "hibernate.ddl-auto", "create");
	        hibernateProperties.setProperty(
	          "hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
	        hibernateProperties.setProperty(
	  	          "hibernate.cache.use_second_level_cache", "false");
	        hibernateProperties.setProperty(
		  	          "hibernate.cache.use_query_cache", "false");
	        return hibernateProperties;
	    }
	}
