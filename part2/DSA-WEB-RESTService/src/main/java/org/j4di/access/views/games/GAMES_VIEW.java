package org.j4di.access.views.games;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

@Getter
@Entity
@Immutable
@Table(name = "GAMES_VIEW")
public class GAMES_VIEW {
	@Id
	private Long bggId;
	private String name;
	private Integer yearPublished;
	private Integer minPlayers;
	private Integer maxPlayers;
	private BigDecimal avgRating;
}
