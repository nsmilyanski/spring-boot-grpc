package com.grpcflix.aggregator.service;

import com.example.grpcflix.common.Genre;
import com.example.grpcflix.movie.MovieSearchRequest;
import com.example.grpcflix.movie.MovieSearchResponse;
import com.example.grpcflix.movie.MovieServiceGrpc;
import com.example.grpcflix.user.UserGenreUpdateRequest;
import com.example.grpcflix.user.UserResponse;
import com.example.grpcflix.user.UserSearchRequest;
import com.example.grpcflix.user.UserServiceGrpc;
import com.grpcflix.aggregator.dto.RecommendedMove;
import com.grpcflix.aggregator.dto.UserGenre;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMovieService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

    @GrpcClient("movie-service")
    private MovieServiceGrpc.MovieServiceBlockingStub movieServiceBlockingStub;

    public List<RecommendedMove> getUserMovieSuggestions(String loginId) {
        UserSearchRequest userSearchRequest = UserSearchRequest.newBuilder().setLoginId(loginId).build();
        UserResponse userResponse = this.userServiceBlockingStub.getUserGenre(userSearchRequest);

        MovieSearchRequest movieSearchRequest = MovieSearchRequest.newBuilder().setGenre(userResponse.getGenre()).build();
        MovieSearchResponse movieSearchResponse = this.movieServiceBlockingStub.getMovies(movieSearchRequest);

        return movieSearchResponse.getMovieList()
                .stream()
                .map( movieDto -> new RecommendedMove(movieDto.getTitle(), movieDto.getYear(), movieDto.getRating()))
                .collect(Collectors.toList());
    }

    public void setUserGenre(UserGenre userGenre){
        UserGenreUpdateRequest userGenreUpdateRequest = UserGenreUpdateRequest.newBuilder()
                .setLoginId(userGenre.getLoginId())
                .setGenre(Genre.valueOf(userGenre.getGenre().toUpperCase()))
                .build();
        UserResponse userResponse = this.userServiceBlockingStub.updateUserGenre(userGenreUpdateRequest);
    }
}
