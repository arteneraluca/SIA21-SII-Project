package org.datasource.doc.views.games;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

final class CsvDocumentReader {
    private CsvDocumentReader() {
    }

    static List<String[]> read(Path path) {
        try {
            List<String> lines = Files.readAllLines(path);
            List<String[]> rows = new ArrayList<>();
            for (String line : lines) {
                rows.add(parseLine(line));
            }
            return rows;
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read CSV data source: " + path.toAbsolutePath(), e);
        }
    }

    private static String[] parseLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '"') {
                quoted = !quoted;
            } else if (ch == ',' && !quoted) {
                values.add(value.toString());
                value.setLength(0);
            } else {
                value.append(ch);
            }
        }
        values.add(value.toString());
        return values.toArray(String[]::new);
    }
}
