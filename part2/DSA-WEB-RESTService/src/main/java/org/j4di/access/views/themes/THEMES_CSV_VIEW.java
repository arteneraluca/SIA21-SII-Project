package org.j4di.access.views.themes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "THEMES_CSV_VIEW")
public class THEMES_CSV_VIEW {
	@Id
	@Column(name = "BGGId")
	private Long bggId;
	@Column(name = "Adventure")
	private Integer adventure;
	@Column(name = "Fantasy")
	private Integer fantasy;
	@Column(name = "Environmental")
	private Integer environmental;
	@Column(name = "Economic")
	private Integer economic;
	@Column(name = "Transportation")
	private Integer transportation;
	@Column(name = "Science_Fiction")
	private Integer scienceFiction;
	@Column(name = "Space_Exploration")
	private Integer spaceExploration;
	@Column(name = "Civilization")
	private Integer civilization;
	@Column(name = "Horror")
	private Integer horror;
	@Column(name = "Medieval")
	private Integer medieval;
	@Column(name = "Ancient")
	private Integer ancient;
	@Column(name = "Pirates")
	private Integer pirates;
	@Column(name = "Zombies")
	private Integer zombies;
	@Column(name = "Sports")
	private Integer sports;
	@Column(name = "Music")
	private Integer music;
	@Column(name = "Political")
	private Integer political;
	@Column(name = "Math")
	private Integer math;
	@Column(name = "City_Building")
	private Integer cityBuilding;
}
