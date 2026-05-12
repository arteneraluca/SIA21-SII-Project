package org.datasource.oracle;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BoardgameController {
    private final BoardgameService service;

    public BoardgameController(BoardgameService service) {
        this.service = service;
    }

    @GetMapping("/api/oracle/boardgames")
    public List<BoardgameDto> getBoardgames() {
        return service.findAll();
    }

    @GetMapping("/api/oracle/boardgames/{id}")
    public BoardgameDto getBoardgame(@PathVariable Long id) {
        return service.findById(id);
    }
}
