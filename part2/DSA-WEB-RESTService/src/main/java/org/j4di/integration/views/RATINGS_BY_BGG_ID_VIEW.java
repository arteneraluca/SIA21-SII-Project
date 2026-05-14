package org.j4di.integration.views;

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
@Table(name = "RATINGS_BY_BGG_ID_VIEW")
public class RATINGS_BY_BGG_ID_VIEW {
	@Id
	@Column(name = "bgg_id")
	private Long bggId;
	@Column(name = "rating_count")
	private Long ratingCount;
	@Column(name = "average_rating")
	private BigDecimal averageRating;
	@Column(name = "minimum_rating")
	private BigDecimal minimumRating;
	@Column(name = "maximum_rating")
	private BigDecimal maximumRating;
}
