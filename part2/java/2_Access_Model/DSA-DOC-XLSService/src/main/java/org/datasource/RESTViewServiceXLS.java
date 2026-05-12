package org.datasource;

import org.datasource.doc.views.games.GameView;
import org.datasource.doc.views.games.GameViewBuilder;
import org.datasource.doc.views.games.ThemeView;
import org.datasource.doc.views.games.ThemeViewBuilder;
import org.datasource.doc.views.games.ThemeWideView;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/rest/boardgames")
public class RESTViewServiceXLS {
    private static final Logger logger = Logger.getLogger(RESTViewServiceXLS.class.getName());

    private final GameViewBuilder gameViewBuilder;
    private final ThemeViewBuilder themeViewBuilder;

    public RESTViewServiceXLS(GameViewBuilder gameViewBuilder, ThemeViewBuilder themeViewBuilder) {
        this.gameViewBuilder = gameViewBuilder;
        this.themeViewBuilder = themeViewBuilder;
    }

    @GetMapping(value = "/ping", produces = MediaType.TEXT_PLAIN_VALUE)
    public String pingDataSource() {
        logger.info(">>>> DSA-DOC-XLSService:: RESTViewServiceXLS is Up!");
        return "Ping response from DSA-DOC-XLSService!";
    }

    @GetMapping(value = "/GameView", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<GameView> get_GameView() {
        return gameViewBuilder.getGameView();
    }

    @GetMapping(value = "/ThemeView", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<ThemeView> get_ThemeView() {
        return themeViewBuilder.getThemeView();
    }

    @GetMapping(value = "/ThemeWideView", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<ThemeWideView> get_ThemeWideView() {
        return themeViewBuilder.getThemeWideView();
    }
}
