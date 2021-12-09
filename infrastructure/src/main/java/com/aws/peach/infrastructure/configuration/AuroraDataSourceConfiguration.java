package com.aws.peach.infrastructure.configuration;

//@Configuration
//@EnableTransactionManagement
//@EntityScan(basePackageClasses = {Album.class}) // todo : 고민
//@EnableJpaRepositories(basePackageClasses = {AuroraInfras.class})
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
//public class AuroraDataSourceConfiguration {
//
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource.master")
//    public DataSource masterDataSource() {
//        return DataSourceBuilder.create().type(HikariDataSource.class).build();
//    }
//
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource.slave")
//    public DataSource slaveDataSource() {
//        return DataSourceBuilder.create().type(HikariDataSource.class).build();
//    }
//
//    @Bean
//    public DataSource routingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
//                                        @Qualifier("slaveDataSource") DataSource slaveDataSource) {
//        AbstractRoutingDataSource routingDataSource = new ReplicationRoutingDataSource();
//
//        Map<Object, Object> dataSourceMap = new HashMap<>();
//        dataSourceMap.put("master", masterDataSource);
//        dataSourceMap.put("slave", slaveDataSource);
//        routingDataSource.setTargetDataSources(dataSourceMap);
//        routingDataSource.setDefaultTargetDataSource(masterDataSource);
//
//        return routingDataSource;
//    }
//
//    @Primary
//    @Bean
//    public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
//        return new LazyConnectionDataSourceProxy(routingDataSource);
//    }
//}