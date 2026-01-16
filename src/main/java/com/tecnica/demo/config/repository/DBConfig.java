package com.tecnica.demo.config.repository;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
public class DBConfig {

  private static final String DRIVER_CLASS_NAME = "oracle.jdbc.OracleDriver";

  /*
   * Estas variables urlDBConnection, username y password deben ir almacenadas en
   * un vault de secretos
   */
  @Value("${database.urlconnection}")
  private String urlDBConnection;

  @Value("${database.username}")
  private String username;

  @Value("${database.password}")
  private String password;

  @Value("${database.maximum-pool-size}")
  private int maximumPoolsize;

  @Value("${database.pool-name}")
  private String poolName;

  @Value("${database.max-life-time}")
  private long maxLifeTime;

  @Value("${database.idle-timeout}")
  private long idleTimeOut;

  @Value("${database.connection-timeout}")
  private long connectionTimeOut;

  @Value("${database.minimum-idle}")
  private int minimumIdle;

  /**
   * @param dataSource
   * @return JdbcTemplate
   */
  @Bean
  JdbcTemplate jdbcTemplate(DataSource dataSource) {

    return new JdbcTemplate(dataSource);
  }

  /**
   * @return DataSource
   */
  @Bean(destroyMethod = "close")
  DataSource dataSource() {

    HikariConfig hikariConfig = new HikariConfig();

    hikariConfig.setDriverClassName(DRIVER_CLASS_NAME);
    hikariConfig.setJdbcUrl(this.urlDBConnection);
    hikariConfig.setUsername(this.username);
    hikariConfig.setPassword(this.password);
    /*
     * MaximumPoolSize = ((core_count * 2) + effective_spindle_count)
     *
     * Dónde core_count son los núcleos del CPU y effective_spindle_count la
     * cantidad de discos en un RAID. Connection to database
     */
    hikariConfig.setMaximumPoolSize(this.maximumPoolsize);
    hikariConfig.setPoolName(this.poolName);
    hikariConfig.setMaxLifetime(this.maxLifeTime);
    hikariConfig.setIdleTimeout(this.idleTimeOut);
    hikariConfig.setAutoCommit(false);
    hikariConfig.setConnectionTimeout(this.connectionTimeOut);
    hikariConfig.setMinimumIdle(this.minimumIdle);

    return new HikariDataSource(hikariConfig);
  }
}
