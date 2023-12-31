package com.grpcflix.user.service;

import com.example.grpcflix.common.Genre;

import com.example.grpcflix.user.UserGenreUpdateRequest;
import com.example.grpcflix.user.UserResponse;
import com.example.grpcflix.user.UserSearchRequest;
import com.example.grpcflix.user.UserServiceGrpc;
import com.grpcflix.user.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void getUserGenre(UserSearchRequest request, StreamObserver<UserResponse> responseObserver) {
        UserResponse.Builder builder = UserResponse.newBuilder();
        this.userRepository.findById(request.getLoginId())
                .ifPresent(user -> {
                    builder.setName(user.getName())
                            .setLoginId(user.getLogin())
                            .setGenre(Genre.valueOf(user.getGenre().toUpperCase()));
                });
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public void updateUserGenre(UserGenreUpdateRequest request, StreamObserver<UserResponse> responseObserver) {
        UserResponse.Builder builder = UserResponse.newBuilder();
        this.userRepository.findById(request.getLoginId()).ifPresent(
                user -> {
                    user.setGenre(request.getGenre().toString());
                    builder.setName(user.getName());
                    builder.setLoginId(user.getLogin());
                    builder.setGenre(Genre.valueOf(user.getGenre().toUpperCase()));
                }
        );
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
