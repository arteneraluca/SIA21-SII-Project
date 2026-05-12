package org.datasource.doc.views.games;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

@Component
public class GameViewBuilder {
    private final Path gamesPath;

    public GameViewBuilder(@Value("${doc.data.source.games.path}") String gamesPath) {
        this.gamesPath = Path.of(gamesPath);
    }

    public List<GameView> getGameView() {
        return CsvDocumentReader.read(gamesPath).stream()
                .skip(1)
                .map(row -> new GameView(
                        Integer.valueOf(row[0]),
                        row[1],
                        Integer.valueOf(row[2]),
                        Integer.valueOf(row[3]),
                        Integer.valueOf(row[4]),
                        Double.valueOf(row[5])
                ))
                .toList();
    }
}
