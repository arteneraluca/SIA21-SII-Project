package org.datasource.csv.themes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor(force = true)
public class ThemeView {
	private Integer bggId;
	private Integer adventure;
	private Integer fantasy;
	private Integer environmental;
	private Integer economic;
	private Integer transportation;
	private Integer scienceFiction;
	private Integer spaceExploration;
	private Integer civilization;
	private Integer horror;
	private Integer medieval;
	private Integer ancient;
	private Integer pirates;
	private Integer zombies;
	private Integer sports;
	private Integer music;
	private Integer political;
	private Integer math;
	private Integer cityBuilding;
}
