package org.spark.service.config;

import org.apache.spark.sql.SparkSession;
import org.spark.service.SparkSessionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkSessionConfig {
    @Bean(destroyMethod = "stop")
    SparkSession sparkSession(SparkSqlProperties properties) {
        SparkSession spark = SparkSession.builder()
                .appName(properties.appName())
                .master(properties.master())
                .config("spark.sql.warehouse.dir", properties.warehouseDir())
                .config("spark.local.dir", properties.localDir())
                .config("spark.ui.enabled", value(properties.uiEnabled()))
                .config("spark.sql.adaptive.enabled", value(properties.adaptiveEnabled()))
                .config("spark.sql.catalogImplementation", "hive")
                .config("javax.jdo.option.ConnectionURL", properties.metastoreUrl())
                .config("javax.jdo.option.ConnectionDriverName", "org.apache.derby.jdbc.EmbeddedDriver")
                .config("datanucleus.schema.autoCreateAll", "true")
                .config("hive.metastore.schema.verification", "false")
                .config("spark.hadoop.hive.server2.thrift.bind.host", properties.thrift().host())
                .config("hive.server2.thrift.bind.host", properties.thrift().host())
                .config("spark.hadoop.hive.server2.thrift.port", String.valueOf(properties.thrift().port()))
                .config("hive.server2.thrift.port", String.valueOf(properties.thrift().port()))
                .enableHiveSupport()
                .getOrCreate();
        SparkSessionRegistry.setSparkSession(spark);
        return spark;
    }

    private String value(Boolean flag) {
        return String.valueOf(Boolean.TRUE.equals(flag));
    }
}
