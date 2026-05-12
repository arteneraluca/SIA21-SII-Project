package org.datasource;

import org.datasource.doc.views.games.ThemeView;
import org.datasource.doc.views.games.ThemeViewBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DocThemesController {
    private final ThemeViewBuilder themeViewBuilder;

    public DocThemesController(ThemeViewBuilder themeViewBuilder) {
        this.themeViewBuilder = themeViewBuilder;
    }

    @GetMapping(value = "/api/doc/themes", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ThemeView> getThemes() {
        return themeViewBuilder.getThemeView();
    }

    @GetMapping(value = "/api/doc/themes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ThemeView getTheme(@PathVariable int id) {
        List<ThemeView> themes = themeViewBuilder.getThemeView();
        if (id < 1 || id > themes.size()) {
            throw new IllegalArgumentException("Theme row not found: " + id);
        }
        return themes.get(id - 1);
    }

    @GetMapping(value = "/api/doc/themes/boardgame/{boardgameId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ThemeView> getThemesByBoardgame(@PathVariable Integer boardgameId) {
        return themeViewBuilder.getThemeView().stream()
                .filter(theme -> theme.bggId().equals(boardgameId))
                .toList();
    }
}
