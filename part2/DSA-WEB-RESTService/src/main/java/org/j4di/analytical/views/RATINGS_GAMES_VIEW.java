package org.j4di.analytical.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

@Getter
@Entity
@Immutable
@Table(name = "RATINGS_GAMES_VIEW")
public class RATINGS_GAMES_VIEW {
	@Id
	@Column(name = "bgg_id")
	private Long bggId;
	private String name;
	private BigDecimal rating;
	private String username;
	private Integer yearPublished;
	private BigDecimal avgRating;
}
