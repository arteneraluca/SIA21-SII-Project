package org.datasource.ratings;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "boardgame_id", nullable = false)
    private Integer boardgameId;

    @Column(nullable = false, precision = 3, scale = 1)
    private BigDecimal rating;

    @Column(nullable = false, length = 100)
    private String username;

    public Long getId() {
        return id;
    }

    public Integer getBoardgameId() {
        return boardgameId;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public String getUsername() {
        return username;
    }
}
