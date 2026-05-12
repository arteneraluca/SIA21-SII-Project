package org.spark.service.init;

import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spark.service.config.SparkSqlProperties;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;

@Component
@Order(1)
public class SparkSqlInitializer implements ApplicationRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(SparkSqlInitializer.class);

    private final SparkSession sparkSession;
    private final SparkSqlProperties properties;

    public SparkSqlInitializer(SparkSession sparkSession, SparkSqlProperties properties) {
        this.sparkSession = sparkSession;
        this.properties = properties;
    }

    @Override
    public void run(ApplicationArguments args) {
        String databaseName = properties.databaseName();
        sparkSession.sql("CREATE DATABASE IF NOT EXISTS " + databaseName);
        sparkSession.sql("USE " + databaseName);
        LOGGER.info("Spark SQL virtual database ready: {}", databaseName);

        initializeViews();
    }

    private void initializeViews() {
        File sqlDir = new File("/app/sql");
        if (!sqlDir.exists() || !sqlDir.isDirectory()) {
            LOGGER.info("SQL initialization directory /app/sql not found or not a directory, skipping auto-initialization");
            return;
        }

        File[] sqlFiles = sqlDir.listFiles((dir, name) -> name.endsWith(".sql"));
        if (sqlFiles == null || sqlFiles.length == 0) {
            LOGGER.info("No SQL files found in /app/sql");
            return;
        }

        Arrays.sort(sqlFiles, Comparator.comparing(File::getName));

        for (File sqlFile : sqlFiles) {
            LOGGER.info("Executing SQL file: {}", sqlFile.getName());
            try {
                String content = Files.readString(sqlFile.toPath());
                String[] statements = content.split(";");
                for (String statement : statements) {
                    String trimmed = statement.trim();
                    if (!trimmed.isEmpty()) {
                        LOGGER.debug("Executing statement: {}", trimmed);
                        sparkSession.sql(trimmed);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Failed to execute SQL file: {}", sqlFile.getName(), e);
            }
        }
    }
}
