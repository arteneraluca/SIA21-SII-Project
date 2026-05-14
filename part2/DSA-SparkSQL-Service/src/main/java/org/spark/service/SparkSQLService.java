package org.spark.service;

import jakarta.annotation.PostConstruct;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.hive.thriftserver.HiveThriftServer2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class SparkSQLService {
    private static Logger logger = Logger.getLogger(SparkSQLService.class.getName());

    //
    private SparkSession spark;

    @Value("${spark.sql.master:local[*]}")
    private String sparkMaster;

    @Value("${spark.sql.app-name:SparkSQL-REST.Server}")
    private String sparkAppName;

    @Value("${spark.sql.ui-port:8081}")
    private String sparkUiPort;

    @Value("${spark.sql.driver.host:127.0.0.1}")
    private String sparkDriverHost;

    @Value("${spark.sql.driver.bind-address:127.0.0.1}")
    private String sparkDriverBindAddress;

    @Value("${spark.sql.warehouse-dir:file:/tmp/sia21-spark-warehouse}")
    private String sparkWarehouseDir;

    @Value("${spark.sql.metastore-url:jdbc:derby:;databaseName=/tmp/sia21-spark-metastore;create=true}")
    private String sparkMetastoreUrl;

    @Value("${spark.sql.thrift.host:0.0.0.0}")
    private String thriftHost;

    @Value("${spark.sql.thrift.port:10000}")
    private String thriftPort;

    public SparkSession getSpark() {
        return spark;
    }

    @PostConstruct
    public void init() {
        startThriftServer2();
    }
    /*
    * https://spark.apache.org/docs/latest/sql-distributed-sql-engine.html
    *
     */
    private void startThriftServer2(){
        logger.info(">>> HiveThriftServer2 Starting ....");
        System.setProperty("hive.server2.thrift.bind.host", thriftHost);
        System.setProperty("hive.server2.thrift.port", thriftPort);
        // Create a SparkSession with Hive support
        this.spark = SparkSession.builder()
                .master(sparkMaster)
                .config("spark.ui.port", sparkUiPort)
                .config("spark.driver.host", sparkDriverHost)
                .config("spark.driver.bindAddress", sparkDriverBindAddress)
                .appName(sparkAppName)
                .config("spark.sql.warehouse.dir", sparkWarehouseDir)
                .config("spark.sql.catalogImplementation", "hive")
                .config("javax.jdo.option.ConnectionURL", sparkMetastoreUrl)
                .config("javax.jdo.option.ConnectionDriverName", "org.apache.derby.jdbc.EmbeddedDriver")
                .config("datanucleus.schema.autoCreateAll", "true")
                .config("hive.metastore.schema.verification", "false")
                .config("spark.hadoop.hive.server2.thrift.bind.host", thriftHost)
                .config("hive.server2.thrift.bind.host", thriftHost)
                .config("hive.server2.thrift.port", thriftPort)
                .enableHiveSupport()
                .getOrCreate();

        // Start the Thrift server
        HiveThriftServer2.startWithContext(spark.sqlContext());
        logger.info(">>> HiveThriftServer2 started successfully on " + thriftHost + ":" + thriftPort + "!");
    }
}

// Check WebUI: http://localhost:8081/
// java -jar target/DSA-SparkSQL-Service-2026.1.jar
//                .config("spark.sql.warehouse.dir", "file:////home/catalin-strimbei/Professionals/IIS/apps/IIS_DSA/spark-warehouse")
//                .config("javax.jdo.option.ConnectionURL", "jdbc:derby:;databaseName=/home/catalin-strimbei/Professionals/IIS/apps/IIS_DSA/metastore_db;create=true")
