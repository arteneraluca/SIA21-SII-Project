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
@Table(name = "AVERAGE_RATING_BY_USER_VIEW")
public class AVERAGE_RATING_BY_USER_VIEW {
	@Id
	private String username;
	@Column(name = "rating_count")
	private Long ratingCount;
	@Column(name = "average_rating")
	private BigDecimal averageRating;
}
