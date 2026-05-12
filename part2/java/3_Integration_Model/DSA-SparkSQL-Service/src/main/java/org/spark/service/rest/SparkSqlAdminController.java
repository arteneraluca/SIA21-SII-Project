package org.spark.service.rest;

import org.spark.service.config.SparkAccessProperties;
import org.spark.service.config.SparkSqlProperties;
import org.spark.service.sql.SparkSqlQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SparkSqlAdminController {
    private final SparkSqlProperties sparkSqlProperties;
    private final SparkAccessProperties sparkAccessProperties;
    private final SparkSqlQueryService sparkSqlQueryService;

    public SparkSqlAdminController(
            SparkSqlProperties sparkSqlProperties,
            SparkAccessProperties sparkAccessProperties,
            SparkSqlQueryService sparkSqlQueryService
    ) {
        this.sparkSqlProperties = sparkSqlProperties;
        this.sparkAccessProperties = sparkAccessProperties;
        this.sparkSqlQueryService = sparkSqlQueryService;
    }

    @GetMapping("/rest/status")
    public Map<String, Object> status() {
        return Map.of(
                "database", sparkSqlProperties.databaseName(),
                "thriftJdbcUrl", "jdbc:hive2://localhost:" + sparkSqlProperties.thrift().port() + "/" + sparkSqlProperties.databaseName(),
                "docBaseUrl", sparkAccessProperties.docBaseUrl(),
                "postgresBaseUrl", sparkAccessProperties.postgresBaseUrl(),
                "oracleBaseUrl", sparkAccessProperties.oracleBaseUrl()
        );
    }

    @GetMapping("/rest/tables")
    public List<String> tables() {
        return sparkSqlQueryService.tables();
    }
}
