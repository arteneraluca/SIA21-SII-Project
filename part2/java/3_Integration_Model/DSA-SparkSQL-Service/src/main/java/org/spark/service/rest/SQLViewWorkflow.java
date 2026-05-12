package org.spark.service.rest;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.spark.service.exception.RESTSQLWorkflowException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class SQLViewWorkflow {
    private static final Logger logger = Logger.getLogger(SQLViewWorkflow.class.getName());

    private final SparkSession spark;

    public SQLViewWorkflow(SparkSession spark) {
        this.spark = spark;
    }

    public SQLViewDefinition createJsonViewFromREST(String viewName, String restDataServiceHttpURL) {
        validate(viewName, "viewName");
        validate(restDataServiceHttpURL, "restDataServiceHttpURL");
        String jsonViewSchema = generateJsonSchema(restDataServiceHttpURL);
        return createJsonSQLView(viewName, jsonViewSchema, restDataServiceHttpURL);
    }

    public String generateJsonSchema(String restDataServiceHttpURL) {
        validate(restDataServiceHttpURL, "restDataServiceHttpURL");
        String viewList = QueryRESTDataService.getRESTDataDocument(restDataServiceHttpURL);
        String escapedJson = viewList.replace("\\", "\\\\").replace("'", "\\'");
        String jsonSchemaQuery = "SELECT schema_of_json('" + escapedJson + "') AS json_schema";
        try {
            return spark.sql(jsonSchemaQuery).first().getAs("json_schema").toString();
        } catch (Exception e) {
            throw new RESTSQLWorkflowException("STEP_2: Get JSON schema from REST endpoint failed", e);
        }
    }

    public SQLViewDefinition createJsonSQLView(String viewName, String jsonViewSchema, String restDataServiceHttpURL) {
        String createViewQuery = String.format("""
                CREATE OR REPLACE VIEW %1$s AS
                SELECT from_json(json_raw.data, '%2$s') array
                FROM (
                    SELECT java_method(
                        'org.spark.service.rest.QueryRESTDataService',
                        'getRESTDataDocument',
                        '%3$s'
                    ) AS data
                ) json_raw
                """, viewName, jsonViewSchema, restDataServiceHttpURL);
        try {
            logger.info("DEBUG: createJsonSQLView: " + createViewQuery);
            spark.sql(createViewQuery);
            spark.sql("SELECT * FROM " + viewName + " LIMIT 1").collectAsList();
            return new SQLViewDefinition(viewName, restDataServiceHttpURL, jsonViewSchema, createViewQuery, "/rest/view/" + viewName);
        } catch (Exception e) {
            throw new RESTSQLWorkflowException("STEP_3: CREATE VIEW failed for " + viewName, e);
        }
    }

    public SQLResponse executeSQLQuery(String sqlQuery) {
        validate(sqlQuery, "sqlQuery");
        try {
            Dataset<Row> sqlDataSet = spark.sql(sqlQuery);
            String sqlResults = sqlDataSet.toJSON().collectAsList().toString();
            return new SQLResponse(sqlQuery, sqlResults);
        } catch (Exception e) {
            throw new RESTSQLWorkflowException("STEP_1: SQL execution failed for query: " + sqlQuery, e);
        }
    }

    private void validate(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new RESTSQLWorkflowException(name + " is null or empty");
        }
    }
}
