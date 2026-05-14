package org.datasource;

import org.datasource.jpa.views.games.GameRatingStatsView;
import org.datasource.jpa.views.games.GameRatingStatsViewBuilderSQL;
import org.datasource.jpa.views.games.GameView;
import org.datasource.jpa.views.games.GameViewBuilder;
import org.datasource.springdata.views.GameViewRepository;
import org.datasource.springdata.views.TopGameView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

/*	REST Service URL
	http://localhost:8091/DSA_SQL_JPAService/rest/games/GamesView
	http://localhost:8091/DSA_SQL_JPAService/rest/games/TopGamesView
	http://localhost:8091/DSA_SQL_JPAService/rest/games/TwoPlayerGamesView
	http://localhost:8091/DSA_SQL_JPAService/rest/games/GameRatingStatsView
*/
@RestController
@RequestMapping("/games")
public class RESTViewServiceJPA {
	private static Logger logger = Logger.getLogger(RESTViewServiceJPA.class.getName());
	
	@RequestMapping(value = "/ping", method = RequestMethod.GET,
			produces = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public String pingDataSource() {
		logger.info(">>>> DSA-SQL-JPAService:: RESTViewService is Up!");
		return "Ping response from DSA-SQL-JPAService!";
	}
	
	@RequestMapping(value = "/GamesView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<GameView> get_GamesView() {
		List<GameView> viewList = this.gameViewBuilder.build().getGameViewList();
		return viewList;
	}

	@RequestMapping(value = "/TopGamesView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<TopGameView> get_TopGamesView() {
		List<TopGameView> viewList = this.gameViewRepository.getTopGamesViewList();
		return viewList;
	}

	@RequestMapping(value = "/TwoPlayerGamesView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<GameView> get_TwoPlayerGamesView() {
		List<GameView> viewList = this.gameViewRepository.getTwoPlayerGamesViewList();
		return viewList;
	}

	@RequestMapping(value = "/GameRatingStatsView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<GameRatingStatsView> get_GameRatingStatsView() {
		List<GameRatingStatsView> viewList = this.gameRatingStatsViewBuilderSQL.build().getGameRatingStatsViewList();
		return viewList;
	}

	// Set-up
	@Autowired private GameViewBuilder gameViewBuilder;
	@Autowired private GameRatingStatsViewBuilderSQL gameRatingStatsViewBuilderSQL;
	@Autowired private GameViewRepository gameViewRepository;
	//
}
