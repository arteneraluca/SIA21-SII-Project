package org.datasource.doc.views.games;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class ThemeViewBuilder {
    private final Path themesPath;

    public ThemeViewBuilder(@Value("${doc.data.source.themes.path}") String themesPath) {
        this.themesPath = Path.of(themesPath);
    }

    public List<ThemeView> getThemeView() {
        List<String[]> rows = CsvDocumentReader.read(themesPath);
        String[] headers = rows.getFirst();
        return rows.stream()
                .skip(1)
                .flatMap(row -> {
                    Integer bggId = Integer.valueOf(row[0]);
                    return java.util.stream.IntStream.range(1, headers.length)
                            .filter(i -> i < row.length && "1".equals(row[i]))
                            .mapToObj(i -> new ThemeView(bggId, headers[i]));
                })
                .toList();
    }

    public List<ThemeWideView> getThemeWideView() {
        List<String[]> rows = CsvDocumentReader.read(themesPath);
        String[] headers = rows.getFirst();
        return rows.stream()
                .skip(1)
                .map(row -> {
                    Map<String, Integer> themes = new LinkedHashMap<>();
                    for (int i = 1; i < headers.length; i++) {
                        themes.put(headers[i], i < row.length ? Integer.valueOf(row[i]) : 0);
                    }
                    return new ThemeWideView(Integer.valueOf(row[0]), themes);
                })
                .toList();
    }
}
