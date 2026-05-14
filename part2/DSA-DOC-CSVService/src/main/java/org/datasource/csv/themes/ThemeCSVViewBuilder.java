package org.datasource.csv.themes;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.datasource.csv.CSVResourceFileDataSourceConnector;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class ThemeCSVViewBuilder {
	private static Logger logger = Logger.getLogger(ThemeCSVViewBuilder.class.getName());

	private List<ThemeView> viewList = new java.util.ArrayList<>();

	public List<ThemeView> getViewList() {
		return viewList;
	}

	private CSVResourceFileDataSourceConnector dataSourceConnector;
	private File csvFile;

	public ThemeCSVViewBuilder(CSVResourceFileDataSourceConnector dataSourceConnector) throws Exception {
		this.dataSourceConnector = dataSourceConnector;
		csvFile = dataSourceConnector.getCSVFile();
	}

	// Builder Workflow
	public ThemeCSVViewBuilder build() throws Exception {
		try (Reader in = new FileReader(this.csvFile)) {
			CSVFormat format = CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(',');
			Iterable<CSVRecord> records = format.parse(in);
			viewList = new ArrayList<>();
			for (CSVRecord record : records) {
				Optional<ThemeView> themeView = buildThemeView(record);
				if (themeView.isPresent())
					this.viewList.add(themeView.get());
			}
		}
		return this;
	}

	public Optional<ThemeView> getThemeById(Integer bggId) throws Exception {
		List<ThemeView> themes = getThemes();
		return themes.stream()
				.filter(theme -> theme.getBggId() != null && theme.getBggId().equals(bggId))
				.findFirst();
	}

	public List<ThemeView> searchByTheme(String themeName) throws Exception {
		List<ThemeView> themes = getThemes();
		String normalizedThemeName = normalizeThemeName(themeName);
		return themes.stream()
				.filter(theme -> hasTheme(theme, normalizedThemeName))
				.toList();
	}

	private List<ThemeView> getThemes() throws Exception {
		if (this.getViewList().isEmpty() == true)
			return this.build().getViewList();
		return this.getViewList();
	}

	private Optional<ThemeView> buildThemeView(CSVRecord record) {
		try {
			Integer bggId = parseRequiredInteger(record, "BGGId");
			if (bggId == null) {
				logger.warning("Skipping CSV row " + record.getRecordNumber() + " because BGGId is missing.");
				return Optional.empty();
			}

			return Optional.of(new ThemeView(
					bggId,
					parseInteger(record, "Adventure"),
					parseInteger(record, "Fantasy"),
					parseInteger(record, "Environmental"),
					parseInteger(record, "Economic"),
					parseInteger(record, "Transportation"),
					parseInteger(record, "Science_Fiction"),
					parseInteger(record, "Space_Exploration"),
					parseInteger(record, "Civilization"),
					parseInteger(record, "Horror"),
					parseInteger(record, "Medieval"),
					parseInteger(record, "Ancient"),
					parseInteger(record, "Pirates"),
					parseInteger(record, "Zombies"),
					parseInteger(record, "Sports"),
					parseInteger(record, "Music"),
					parseInteger(record, "Political"),
					parseInteger(record, "Math"),
					parseInteger(record, "City_Building")
			));
		} catch (Exception exception) {
			logger.warning("Skipping malformed CSV row " + record.getRecordNumber() + ": " + exception.getMessage());
			return Optional.empty();
		}
	}

	private Integer parseRequiredInteger(CSVRecord record, String columnName) {
		return parseInteger(record, columnName);
	}

	private Integer parseInteger(CSVRecord record, String columnName) {
		if (!record.isMapped(columnName))
			throw new IllegalArgumentException("Missing CSV column: " + columnName);

		String value = record.get(columnName);
		if (value == null || value.trim().isEmpty())
			return null;

		try {
			return Integer.parseInt(value.trim());
		} catch (NumberFormatException exception) {
			logger.warning("Invalid integer value for column " + columnName + " at row "
					+ record.getRecordNumber() + ": " + value);
			return null;
		}
	}

	private boolean hasTheme(ThemeView theme, String normalizedThemeName) {
		if (normalizedThemeName == null || normalizedThemeName.isEmpty())
			return false;

		return switch (normalizedThemeName) {
			case "adventure" -> isSelected(theme.getAdventure());
			case "fantasy" -> isSelected(theme.getFantasy());
			case "environmental" -> isSelected(theme.getEnvironmental());
			case "economic" -> isSelected(theme.getEconomic());
			case "transportation" -> isSelected(theme.getTransportation());
			case "sciencefiction" -> isSelected(theme.getScienceFiction());
			case "spaceexploration" -> isSelected(theme.getSpaceExploration());
			case "civilization" -> isSelected(theme.getCivilization());
			case "horror" -> isSelected(theme.getHorror());
			case "medieval" -> isSelected(theme.getMedieval());
			case "ancient" -> isSelected(theme.getAncient());
			case "pirates" -> isSelected(theme.getPirates());
			case "zombies" -> isSelected(theme.getZombies());
			case "sports" -> isSelected(theme.getSports());
			case "music" -> isSelected(theme.getMusic());
			case "political" -> isSelected(theme.getPolitical());
			case "math" -> isSelected(theme.getMath());
			case "citybuilding" -> isSelected(theme.getCityBuilding());
			default -> false;
		};
	}

	private boolean isSelected(Integer value) {
		return value != null && value == 1;
	}

	private String normalizeThemeName(String themeName) {
		if (themeName == null)
			return null;
		return themeName.toLowerCase().replace("_", "").replace(" ", "").replace("-", "");
	}
}
