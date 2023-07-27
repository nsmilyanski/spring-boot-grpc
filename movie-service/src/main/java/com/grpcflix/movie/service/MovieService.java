package com.grpcflix.movie.service;

import com.example.grpcflix.movie.MovieDto;
import com.example.grpcflix.movie.MovieSearchRequest;
import com.example.grpcflix.movie.MovieSearchResponse;
import com.example.grpcflix.movie.MovieServiceGrpc;
import com.grpcflix.movie.repository.MovieRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
public class MovieService extends MovieServiceGrpc.MovieServiceImplBase {

    private final MovieRepository movieRepository;

    @Override
    public void getMovies(MovieSearchRequest request, StreamObserver<MovieSearchResponse> responseObserver) {
        List<MovieDto> movieDtoList = this.movieRepository.getMovieByGenreOrderByYearDesc(request.getGenre().toString())
                .stream()
                .map(movie -> MovieDto.newBuilder()
                        .setTitle(movie.getTitle())
                        .setRating(movie.getRating())
                        .setYear(movie.getYear())
                        .build())
                .collect(Collectors.toList());


        responseObserver.onNext(
                MovieSearchResponse.newBuilder()
                        .addAllMovie(movieDtoList).build());
        responseObserver.onCompleted();
    }
}
