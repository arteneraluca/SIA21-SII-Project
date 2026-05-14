package org.j4di.analytical.views;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "THEMES_CSV_THEME_COUNTS_VIEW")
public class THEMES_CSV_THEME_COUNTS_VIEW {
	@Id
	private String themeName;
	private Long gamesCount;
}
