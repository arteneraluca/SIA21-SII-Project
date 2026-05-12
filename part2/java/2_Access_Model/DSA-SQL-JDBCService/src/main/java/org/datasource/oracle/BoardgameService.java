package org.datasource.oracle;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardgameService {
    private final BoardgameRepository repository;

    public BoardgameService(BoardgameRepository repository) {
        this.repository = repository;
    }

    public List<BoardgameDto> findAll() {
        return repository.findAll();
    }

    public BoardgameDto findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Boardgame not found: " + id));
    }
}
