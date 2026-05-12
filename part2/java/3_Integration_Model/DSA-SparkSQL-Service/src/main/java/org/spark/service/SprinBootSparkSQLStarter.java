package org.spark.service;

import org.spark.service.config.SparkAccessProperties;
import org.spark.service.config.SparkSqlProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({SparkSqlProperties.class, SparkAccessProperties.class})
public class SprinBootSparkSQLStarter {
    public static void main(String[] args) {
        SpringApplication.run(SprinBootSparkSQLStarter.class, args);
    }
}
