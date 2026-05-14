package org.j4di.access.views.ratings;

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
@Table(name = "USER_RATINGS_VIEW")
public class USER_RATINGS_VIEW {
	@Id
	@Column(name = "bgg_id")
	private Long bggId;
	private BigDecimal rating;
	private String username;
}
