package org.datasource.ratings;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {
    private final RatingRepository repository;

    public RatingService(RatingRepository repository) {
        this.repository = repository;
    }

    public List<RatingDto> findAll() {
        return repository.findAll().stream().map(RatingDto::from).toList();
    }

    public RatingDto findById(Long id) {
        return repository.findById(id).map(RatingDto::from)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found: " + id));
    }

    public List<RatingDto> findByBoardgameId(Integer boardgameId) {
        return repository.findByBoardgameIdOrderById(boardgameId).stream().map(RatingDto::from).toList();
    }
}
