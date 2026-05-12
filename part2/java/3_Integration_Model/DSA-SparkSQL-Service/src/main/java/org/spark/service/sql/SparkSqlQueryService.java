package org.spark.service.sql;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Service
public class SparkSqlQueryService {
    private final SparkSession sparkSession;

    public SparkSqlQueryService(SparkSession sparkSession) {
        this.sparkSession = sparkSession;
    }

    public List<Map<String, Object>> query(String sql) {
        return sparkSession.sql(sql).collectAsList().stream()
                .map(this::toMap)
                .toList();
    }

    public List<String> tables() {
        return sparkSession.catalog().listTables().collectAsList().stream()
                .map(table -> table.database() + "." + table.name())
                .toList();
    }

    private Map<String, Object> toMap(Row row) {
        String[] fields = row.schema().fieldNames();
        Map<String, Object> values = new LinkedHashMap<>();
        IntStream.range(0, fields.length).forEach(i -> values.put(fields[i], row.get(i)));
        return values;
    }
}
