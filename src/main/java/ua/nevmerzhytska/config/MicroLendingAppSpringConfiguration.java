package ua.nevmerzhytska.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = {
        "ua.nevmerzhytska"
})
public class MicroLendingAppSpringConfiguration {

    @Bean
    public Properties properties() throws IOException {
        Properties properties = new Properties();
        try(InputStream inputStream = MicroLendingAppSpringConfiguration.class.getClassLoader().getResourceAsStream("micro-lending-app.properties")) {
            properties.load(inputStream);
        }
        return properties;
    }

    @Bean
    @Autowired
    public DataSource dataSource(Properties properties) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(properties.getProperty("db.driverClass"));
        dataSource.setUrl(properties.getProperty("db.url"));
        dataSource.setUsername(properties.getProperty("db.username"));
        dataSource.setPassword(properties.getProperty("db.password"));
        return dataSource;
    }

    @Bean
    @Autowired
    public EntityManagerFactory entityManagerFactory(Properties properties) {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(Boolean.valueOf(properties.getProperty("hibernate.show.sql")));
        vendorAdapter.setGenerateDdl(Boolean.valueOf(properties.getProperty("hibernate.generate.ddl")));

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", properties.getProperty("hibernate.dialect"));

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("ua.nevmerzhytska");
        factory.setJpaProperties(jpaProperties);
        factory.setDataSource(dataSource(properties));
        factory.afterPropertiesSet();

        return factory.getObject();
    }
}
