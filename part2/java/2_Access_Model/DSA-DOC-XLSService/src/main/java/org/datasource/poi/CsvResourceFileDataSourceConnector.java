package org.datasource.poi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class CsvResourceFileDataSourceConnector {
    @Value("${doc.data.source.games.path}")
    private String gamesPath;

    @Value("${doc.data.source.themes.path}")
    private String themesPath;

    public Path getGamesPath() {
        return Path.of(gamesPath);
    }

    public Path getThemesPath() {
        return Path.of(themesPath);
    }
}
