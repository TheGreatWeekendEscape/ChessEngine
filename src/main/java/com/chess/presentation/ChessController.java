package com.chess.presentation;

import com.chess.integration.engine.board.Board;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class ChessController {

    @GetMapping
    public Board getAll() {
        return Board.createStandardBoard();
    }
}
