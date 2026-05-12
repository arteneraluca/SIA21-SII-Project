package org.spark.service.rest;

import org.apache.spark.sql.SparkSession;
import org.spark.service.SparkSessionRegistry;
import org.spark.service.exception.RESTSQLWorkflowException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/_sqlrest")
public class RESTEnabledSQLService {
    private static final Logger logger = Logger.getLogger(RESTEnabledSQLService.class.getName());

    private static String serverPort;
    private static String serverServletContextPath;

    private final SQLViewWorkflow sqlViewWorkflow;

    @Value("${sparksql.rest.enabled:true}")
    private Boolean sparkRestEnabled;

    public RESTEnabledSQLService(SQLViewWorkflow sqlViewWorkflow) {
        this.sqlViewWorkflow = sqlViewWorkflow;
    }

    @PostMapping(value = "/query", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @ResponseBody
    public SQLResponse executePostQuery(@RequestBody String sqlQuery) {
        validateEnabled();
        return sqlViewWorkflow.executeSQLQuery(sqlQuery);
    }

    @GetMapping(value = "/query", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @ResponseBody
    public SQLResponse executeGetQuery(@RequestParam("sql") String sqlQuery) {
        validateEnabled();
        return sqlViewWorkflow.executeSQLQuery(sqlQuery);
    }

    @PostMapping(value = "/create-json-view-from-rest", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @ResponseBody
    public SQLViewDefinition createJsonViewFromREST(
            @RequestParam("view_name") String viewName,
            @RequestBody String restDataServiceHttpURL
    ) {
        validateEnabled();
        return sqlViewWorkflow.createJsonViewFromREST(viewName, restDataServiceHttpURL);
    }

    public static String createJSONViewFromREST(String viewName, String restDataServiceHttpURL) {
        if (serverPort == null || serverServletContextPath == null) {
            throw new RESTSQLWorkflowException("REST initialization error: check server.port and server.servlet.context-path");
        }
        if (viewName == null || viewName.isBlank()) {
            throw new RESTSQLWorkflowException("REST Error: viewName is empty");
        }
        if (restDataServiceHttpURL == null || restDataServiceHttpURL.isBlank()) {
            throw new RESTSQLWorkflowException("REST Error: restDataServiceHttpURL is empty");
        }

        String restDataEndpoint = String.format(
                "http://localhost:%s%s/_sqlrest/create-json-view-from-rest?view_name=%s",
                serverPort,
                serverServletContextPath,
                viewName
        );
        logger.info("DEBUG: create JSON View From REST endpoint: " + restDataEndpoint);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        ResponseEntity<String> responseEntity = new RestTemplate().exchange(
                restDataEndpoint,
                HttpMethod.POST,
                new HttpEntity<>(restDataServiceHttpURL, headers),
                String.class
        );
        return responseEntity.getBody();
    }

    static String createTempJSONViewFromREST(String viewName, String url) {
        String json = QueryRESTDataService.getRESTDataDocument(url);
        SparkSession spark = SparkSessionRegistry.getSparkSession();
        spark.createDataset(List.of(json), org.apache.spark.sql.Encoders.STRING())
                .toDF("data")
                .createOrReplaceTempView(viewName);
        return viewName;
    }

    @Value("${server.port}")
    public void setServerPort(String port) {
        serverPort = port;
    }

    @Value("${server.servlet.context-path}")
    public void setServerServletContextPath(String path) {
        serverServletContextPath = path;
    }

    private void validateEnabled() {
        if (!Boolean.TRUE.equals(sparkRestEnabled)) {
            throw new RESTSQLWorkflowException("SparkSQL REST Enabled Service is not available!");
        }
    }
}
