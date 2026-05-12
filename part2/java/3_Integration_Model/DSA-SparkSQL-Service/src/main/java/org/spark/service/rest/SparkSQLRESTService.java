package org.spark.service.rest;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@RestController
public class SparkSQLRESTService {
    private final SparkSession sparkSession;

    public SparkSQLRESTService(SparkSession sparkSession) {
        this.sparkSession = sparkSession;
    }

    @GetMapping(value = "/rest/ping", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String pingDataSource() {
        return "PING response from SparkSQLRESTService!";
    }

    @GetMapping(value = "/rest/view/{viewName}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public List<Map<String, Object>> getView(
            @PathVariable String viewName,
            @RequestParam(defaultValue = "100") int limit
    ) {
        return sparkSession.sql("select * from " + viewName + " limit " + Math.max(1, Math.min(limit, 1000)))
                .collectAsList().stream()
                .map(this::toMap)
                .toList();
    }

    @GetMapping(value = "/rest/STRUCT/{viewName}", produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String getViewStructure(@PathVariable String viewName) {
        return sparkSession.sql("select * from " + viewName + " where 1 = 0").schema().sql();
    }

    @GetMapping(value = "/rest/auto", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> getAutoRestViewDefinitions() {
        Map<String, String> viewMap = new LinkedHashMap<>();
        sparkSession.catalog().listTables().collectAsList()
                .forEach(table -> viewMap.put(table.name().toUpperCase(), "/rest/view/" + table.name()));
        return viewMap;
    }

    private Map<String, Object> toMap(Row row) {
        String[] fields = row.schema().fieldNames();
        Map<String, Object> values = new LinkedHashMap<>();
        IntStream.range(0, fields.length).forEach(i -> values.put(fields[i], row.get(i)));
        return values;
    }
}
