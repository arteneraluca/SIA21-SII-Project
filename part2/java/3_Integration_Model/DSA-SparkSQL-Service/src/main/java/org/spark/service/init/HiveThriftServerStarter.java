package org.spark.service.init;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.hive.thriftserver.HiveThriftServer2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spark.service.config.SparkSqlProperties;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class HiveThriftServerStarter implements ApplicationRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(HiveThriftServerStarter.class);

    private final SparkSession sparkSession;
    private final SparkSqlProperties properties;

    public HiveThriftServerStarter(SparkSession sparkSession, SparkSqlProperties properties) {
        this.sparkSession = sparkSession;
        this.properties = properties;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!Boolean.TRUE.equals(properties.thrift().enabled())) {
            LOGGER.info("Hive Thrift Server disabled");
            return;
        }
        System.setProperty("hive.server2.thrift.port", String.valueOf(properties.thrift().port()));
        System.setProperty("hive.server2.thrift.bind.host", properties.thrift().host());
        HiveThriftServer2.startWithContext(sparkSession.sqlContext());
        LOGGER.info("Hive Thrift Server ready on {}:{}", properties.thrift().host(), properties.thrift().port());
    }
}
