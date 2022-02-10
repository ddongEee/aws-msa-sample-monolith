package com.aws.peach.infrastructure.configuration;

import com.aws.peach.domain.inventory.entity.Inventory;
import com.aws.peach.domain.member.Member;
import com.aws.peach.domain.order.entity.Order;
import com.aws.peach.domain.order.vo.OrderLine;
import com.aws.peach.domain.payment.entity.Payment;
import com.aws.peach.domain.product.entity.Product;
import com.aws.peach.infrastructure.aurora.AuroraInfras;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackageClasses = {Member.class, Inventory.class, Order.class, Product.class, OrderLine.class, Payment.class})
@EnableJpaRepositories(basePackageClasses = {AuroraInfras.class})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class AuroraDataSourceConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    public DataSource routingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                        @Qualifier("slaveDataSource") DataSource slaveDataSource) {
        AbstractRoutingDataSource routingDataSource = new ReplicationRoutingDataSource();

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("master", masterDataSource);
        dataSourceMap.put("slave", slaveDataSource);
        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);

        return routingDataSource;
    }

    @Primary
    @Bean
    public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

    public static final class ReplicationRoutingDataSource extends AbstractRoutingDataSource {
        @Override
        protected Object determineCurrentLookupKey() {
            return TransactionSynchronizationManager.isCurrentTransactionReadOnly()
                    ? "slave"
                    : "master";
        }
    }
}