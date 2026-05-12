package org.spark.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spark.access")
public record SparkAccessProperties(
        String docBaseUrl,
        String postgresBaseUrl,
        String oracleBaseUrl
) {
}
