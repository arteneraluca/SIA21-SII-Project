package org.datasource;

import org.datasource.jdbc.JDBCDataSourceConnector;
import org.datasource.jdbc.views.userratings.UserAverageRatingView;
import org.datasource.jdbc.views.userratings.UserAverageRatingViewBuilder;
import org.datasource.jdbc.views.userratings.UserRatingSummaryView;
import org.datasource.jdbc.views.userratings.UserRatingSummaryViewBuilder;
import org.datasource.jdbc.views.userratings.UserRatingView;
import org.datasource.jdbc.views.userratings.UserRatingViewBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

/*	REST Service URL
 	http://localhost:8090/DSA-SQL-JDBCService/rest/ratings/UserRatingView
 	http://localhost:8090/DSA-SQL-JDBCService/rest/ratings/UserRatingSummaryView
 	http://localhost:8090/DSA-SQL-JDBCService/rest/ratings/UserAverageRatingView
*/
@RestController
@RequestMapping("/ratings")
public class RESTViewServiceJDBC {
	private static Logger logger = Logger.getLogger(RESTViewServiceJDBC.class.getName());
	
	@RequestMapping(value = "/ping", method = RequestMethod.GET,
			produces = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public String ping() {
		logger.info(">>>> DSA-SQL-JDBCService:: RESTViewService is Up!");
		return "Ping response from DSA-SQL-JDBCService!";
	}
	
	@RequestMapping(value = "/UserRatingView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<UserRatingView> get_UserRatingView() {
		List<UserRatingView> viewList = userRatingViewBuilder.build().getViewList();
		return viewList;
	}

	@RequestMapping(value = "/UserRatingViewData", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<UserRatingView> get_UserRatingView(
			@RequestParam("fetch_offset") Integer fetchOffset,
			@RequestParam("fetch_size") Integer fetchSize
			) {
		List<UserRatingView> viewList = userRatingViewBuilder
				.setFetchOffset(fetchOffset)
				.setFetchSize(fetchSize)
				.build().getViewList();
		return viewList;
	}

	@RequestMapping(value = "/UserRatingSummaryView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<UserRatingSummaryView> get_UserRatingSummaryView() {
		List<UserRatingSummaryView> viewList = userRatingSummaryViewBuilder.build().getViewList();
		return viewList;
	}

	@RequestMapping(value = "/UserAverageRatingView", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<UserAverageRatingView> get_UserAverageRatingView() {
		List<UserAverageRatingView> viewList = userAverageRatingViewBuilder.build().getViewList();
		return viewList;
	}
	// Set-up
	@Autowired private JDBCDataSourceConnector jdbcConnector;
	//
	@Autowired private UserRatingViewBuilder userRatingViewBuilder;
	@Autowired private UserRatingSummaryViewBuilder userRatingSummaryViewBuilder;
	@Autowired private UserAverageRatingViewBuilder userAverageRatingViewBuilder;
	//
}
