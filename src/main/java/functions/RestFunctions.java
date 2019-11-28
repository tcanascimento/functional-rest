package functions;

import base.RestSpecs;
import io.vavr.control.Either;
import io.vavr.control.Try;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

import static io.vavr.API.Left;
import static io.vavr.API.Right;

public interface RestFunctions {

    BiFunction<HttpRequest, RestSpecs, HttpResponse> syncHttpRequest = (request, specs) ->
            Try.of(() -> specs.getBaseClient().send(request, specs.getResponseBodyHandler()))
                    .get();

    BiFunction<HttpRequest, RestSpecs, CompletableFuture<HttpResponse>> asyncHttpRequest = (request, specs) ->
            specs.getBaseClient().sendAsync(request,
                    specs.getResponseBodyHandler());

    Function<RestSpecs, Either<String, HttpResponse>> syncRequest = specs -> {
        try {
            return Right(specs.getBaseClient().send(specs.getRequestMethod(), specs.getResponseBodyHandler()));
        } catch (IOException | InterruptedException e) {
            return Left(e.getMessage());
        }
    };

    Function<RestSpecs, CompletableFuture<HttpResponse>> asyncRequest = specs ->
            specs.getBaseClient().sendAsync(specs.getRequestMethod(),
                    specs.getResponseBodyHandler());


}
