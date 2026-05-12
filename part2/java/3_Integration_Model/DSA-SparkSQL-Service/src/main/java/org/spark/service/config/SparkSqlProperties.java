package org.spark.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spark.sql")
public record SparkSqlProperties(
        String appName,
        String master,
        String warehouseDir,
        String metastoreUrl,
        String databaseName,
        String localDir,
        Boolean uiEnabled,
        Boolean adaptiveEnabled,
        Thrift thrift
) {
    public record Thrift(Boolean enabled, String host, Integer port) {
    }
}
