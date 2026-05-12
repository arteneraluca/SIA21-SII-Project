package org.spark.service;

import org.apache.spark.sql.SparkSession;

public final class SparkSessionRegistry {
    private static SparkSession sparkSession;

    private SparkSessionRegistry() {
    }

    public static SparkSession getSparkSession() {
        if (sparkSession == null) {
            throw new IllegalStateException("SparkSession was not initialized");
        }
        return sparkSession;
    }

    public static void setSparkSession(SparkSession sparkSession) {
        SparkSessionRegistry.sparkSession = sparkSession;
    }
}
