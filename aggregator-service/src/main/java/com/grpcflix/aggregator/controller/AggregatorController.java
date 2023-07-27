package com.grpcflix.aggregator.controller;

import com.grpcflix.aggregator.dto.RecommendedMove;
import com.grpcflix.aggregator.dto.UserGenre;
import com.grpcflix.aggregator.service.UserMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AggregatorController {

    private final UserMovieService userMovieService;

    @GetMapping("/{loginId}")
    public List<RecommendedMove> detMovies (@PathVariable String loginId) {
        return userMovieService.getUserMovieSuggestions(loginId);
    }

    @PutMapping
    public void setUserGenre(@RequestBody UserGenre userGenre) {
        this.userMovieService.setUserGenre(userGenre);
    }
}
