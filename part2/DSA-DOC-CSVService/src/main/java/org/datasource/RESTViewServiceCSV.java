package org.datasource;

import org.datasource.csv.themes.ThemeCSVViewBuilder;
import org.datasource.csv.themes.ThemeView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


/*	REST Service URL
	http://localhost:8097/DSA-DOC-CSVService/rest/customers/CustomerEmployeesCategoryViewCSV
*/
@RestController @RequestMapping("/")
public class RESTViewServiceCSV {
	private static Logger logger = Logger.getLogger(RESTViewServiceCSV.class.getName());

	@RequestMapping(value = "/customers/CustomerEmployeesCategoryViewCSV", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<ThemeView> get_CustomerEmployeesCategoryViewCSV() throws Exception {
		return getThemes();
	}

	@RequestMapping(value = "/themes", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<ThemeView> getThemes() throws Exception {
		List<ThemeView> viewList;
		if (this.themeCSVViewBuilder.getViewList().isEmpty())
			viewList = this.themeCSVViewBuilder.build().getViewList();
		else
			viewList = this.themeCSVViewBuilder.getViewList();
		return viewList;
	}

	@RequestMapping(value = "/themes/{id}", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public ResponseEntity<ThemeView> getThemeById(@PathVariable("id") Integer id) throws Exception {
		Optional<ThemeView> theme = this.themeCSVViewBuilder.getThemeById(id);
		if (theme.isPresent())
			return ResponseEntity.ok(theme.get());
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/themes/search", method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public List<ThemeView> searchThemes(@RequestParam("theme") String theme) throws Exception {
		return this.themeCSVViewBuilder.searchByTheme(theme);
	}

	// Set-up
	@Autowired private ThemeCSVViewBuilder themeCSVViewBuilder;
}
