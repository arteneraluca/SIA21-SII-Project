package org.datasource.ratings;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RatingController {
    private final RatingService service;

    public RatingController(RatingService service) {
        this.service = service;
    }

    @GetMapping("/api/ratings")
    public List<RatingDto> getRatings() {
        return service.findAll();
    }

    @GetMapping("/api/ratings/{id}")
    public RatingDto getRating(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/api/ratings/boardgame/{boardgameId}")
    public List<RatingDto> getRatingsByBoardgame(@PathVariable Integer boardgameId) {
        return service.findByBoardgameId(boardgameId);
    }
}
