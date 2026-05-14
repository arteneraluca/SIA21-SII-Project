package org.j4di;

import org.j4di.access.views.games.GAMES_VIEW;
import org.j4di.access.views.games.GAMES_VIEW_Repository;
import org.j4di.access.views.ratings.USER_RATINGS_VIEW;
import org.j4di.access.views.ratings.USER_RATINGS_VIEW_Repository;
import org.j4di.access.views.themes.THEMES_CSV_VIEW;
import org.j4di.access.views.themes.THEMES_CSV_VIEW_Repository;
import org.j4di.analytical.views.AVERAGE_RATING_BY_USER_VIEW;
import org.j4di.analytical.views.AVERAGE_RATING_BY_USER_VIEW_Repository;
import org.j4di.analytical.views.RATINGS_GAMES_VIEW;
import org.j4di.analytical.views.RATINGS_GAMES_VIEW_Repository;
import org.j4di.analytical.views.THEMES_CSV_THEME_COUNTS_VIEW;
import org.j4di.analytical.views.THEMES_CSV_THEME_COUNTS_VIEW_Repository;
import org.j4di.integration.views.RATINGS_BY_BGG_ID_VIEW;
import org.j4di.integration.views.RATINGS_BY_BGG_ID_VIEW_Repository;
import org.j4di.integration.views.TOP_RATED_GAMES_VIEW;
import org.j4di.integration.views.TOP_RATED_GAMES_VIEW_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

/*	REST Service URL
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/GAMES_VIEW
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/GAMES_VIEW/174430
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/USER_RATINGS_VIEW
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/THEMES_CSV_VIEW

	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/RATINGS_BY_BGG_ID_VIEW
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/TOP_RATED_GAMES_VIEW

	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/THEMES_CSV_THEME_COUNTS_VIEW
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/AVERAGE_RATING_BY_USER_VIEW
	http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/RATINGS_GAMES_VIEW
*/
@RestController
@RequestMapping("/OLAP")
public class RESTViewService {
	private static final Logger logger = Logger.getLogger(RESTViewService.class.getName());

	@RequestMapping(value = "/ping", method = RequestMethod.GET,
			produces = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public String pingDataSource() {
		logger.info(">>>> DSA-WEB-RESTService:: RESTViewService is Up!");
		return "Ping response from DSA-WEB-RESTService!";
	}

	@Autowired private GAMES_VIEW_Repository GAMES_VIEW_Repository;
	@Autowired private USER_RATINGS_VIEW_Repository USER_RATINGS_VIEW_Repository;
	@Autowired private THEMES_CSV_VIEW_Repository THEMES_CSV_VIEW_Repository;

	@GetMapping(value = "/GAMES_VIEW",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<GAMES_VIEW> get_GAMES_VIEW() {
		return this.GAMES_VIEW_Repository.get_GAMES_VIEW();
	}

	@GetMapping(value = "/GAMES_VIEW/{bggId}",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<GAMES_VIEW> get_GAMES_VIEW_ByBggId(@PathVariable Long bggId) {
		return this.GAMES_VIEW_Repository.get_GAMES_VIEW_ByBggId(bggId);
	}

	@GetMapping(value = "/USER_RATINGS_VIEW",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<USER_RATINGS_VIEW> get_USER_RATINGS_VIEW() {
		return this.USER_RATINGS_VIEW_Repository.get_USER_RATINGS_VIEW();
	}

	@GetMapping(value = "/USER_RATINGS_VIEW/{bggId}",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<USER_RATINGS_VIEW> get_USER_RATINGS_VIEW_ByBggId(@PathVariable Long bggId) {
		return this.USER_RATINGS_VIEW_Repository.get_USER_RATINGS_VIEW_ByBggId(bggId);
	}

	@GetMapping(value = "/THEMES_CSV_VIEW",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<THEMES_CSV_VIEW> get_THEMES_CSV_VIEW() {
		return this.THEMES_CSV_VIEW_Repository.get_THEMES_CSV_VIEW();
	}

	@GetMapping(value = "/THEMES_CSV_VIEW/{bggId}",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<THEMES_CSV_VIEW> get_THEMES_CSV_VIEW_ByBggId(@PathVariable Long bggId) {
		return this.THEMES_CSV_VIEW_Repository.get_THEMES_CSV_VIEW_ByBggId(bggId);
	}

	@Autowired private RATINGS_BY_BGG_ID_VIEW_Repository RATINGS_BY_BGG_ID_VIEW_Repository;
	@Autowired private TOP_RATED_GAMES_VIEW_Repository TOP_RATED_GAMES_VIEW_Repository;

	@GetMapping(value = "/RATINGS_BY_BGG_ID_VIEW",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<RATINGS_BY_BGG_ID_VIEW> get_RATINGS_BY_BGG_ID_VIEW() {
		return this.RATINGS_BY_BGG_ID_VIEW_Repository.get_RATINGS_BY_BGG_ID_VIEW();
	}

	@GetMapping(value = "/TOP_RATED_GAMES_VIEW",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<TOP_RATED_GAMES_VIEW> get_TOP_RATED_GAMES_VIEW() {
		return this.TOP_RATED_GAMES_VIEW_Repository.get_TOP_RATED_GAMES_VIEW();
	}

	@Autowired private THEMES_CSV_THEME_COUNTS_VIEW_Repository THEMES_CSV_THEME_COUNTS_VIEW_Repository;
	@Autowired private AVERAGE_RATING_BY_USER_VIEW_Repository AVERAGE_RATING_BY_USER_VIEW_Repository;
	@Autowired private RATINGS_GAMES_VIEW_Repository RATINGS_GAMES_VIEW_Repository;

	@GetMapping(value = "/THEMES_CSV_THEME_COUNTS_VIEW",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<THEMES_CSV_THEME_COUNTS_VIEW> get_THEMES_CSV_THEME_COUNTS_VIEW() {
		return this.THEMES_CSV_THEME_COUNTS_VIEW_Repository.get_THEMES_CSV_THEME_COUNTS_VIEW();
	}

	@GetMapping(value = "/AVERAGE_RATING_BY_USER_VIEW",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<AVERAGE_RATING_BY_USER_VIEW> get_AVERAGE_RATING_BY_USER_VIEW() {
		return this.AVERAGE_RATING_BY_USER_VIEW_Repository.get_AVERAGE_RATING_BY_USER_VIEW();
	}

	@GetMapping(value = "/RATINGS_GAMES_VIEW",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<RATINGS_GAMES_VIEW> get_RATINGS_GAMES_VIEW() {
		return this.RATINGS_GAMES_VIEW_Repository.get_RATINGS_GAMES_VIEW();
	}
}
