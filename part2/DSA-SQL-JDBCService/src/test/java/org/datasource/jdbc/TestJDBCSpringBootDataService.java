package org.datasource.jdbc;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestJDBCSpringBootDataService {
	private static Logger logger = Logger.getLogger(TestJDBCSpringBootDataService.class.getName());

	private static String serviceURL = "http://localhost:8090/DSA-SQL-JDBCService/rest/ratings";
    private RestTemplate restTemplate = new RestTemplate();

	@Test
	public void test1_get_UserRatingView() {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		headers.setBasicAuth("developer", "iis");
		String restDataEndpoint = serviceURL + "/UserRatingView";
		logger.info(">>> test1_get_UserRatingView REST Data Endpoint: " + restDataEndpoint);
		ResponseEntity<String>  responseEntity = this.restTemplate.exchange(
				restDataEndpoint,
				HttpMethod.GET,
				new HttpEntity<>(null, headers),
				String.class
			);
		
		logger.info("ResultSet JSON (test 1): " + responseEntity.getBody());
	}

	@Test
	public void test2_get_UserRatingView_fetch() {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		headers.setBasicAuth("developer", "iis");
		String restDataEndpoint = serviceURL + "/UserRatingViewData?fetch_offset=2&fetch_size=2";
		logger.info(">>> test2_get_UserRatingView_fetch REST Data Endpoint: " + restDataEndpoint);
		ResponseEntity<String>  responseEntity = this.restTemplate.exchange(
				restDataEndpoint,
				HttpMethod.GET,
				new HttpEntity<>(null, headers),
				String.class
		);

		logger.info("ResultSet JSON (test 2): " + responseEntity.getBody());
	}
}
