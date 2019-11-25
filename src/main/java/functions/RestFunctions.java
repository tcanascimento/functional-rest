package functions;

import base.RestSpecs;
import io.vavr.control.Try;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface RestFunctions {

    BiFunction<HttpRequest, RestSpecs, HttpResponse> syncRequest = (request, specs) ->
            Try.of(() -> specs.getBaseClient().send(request, specs.getResponseBodyHandler()))
                    .get();

    BiFunction<HttpRequest, RestSpecs, CompletableFuture<HttpResponse>> asyncRequest = (request, specs) ->
            specs.getBaseClient().sendAsync(request,
                    specs.getResponseBodyHandler());

    BiFunction<Function<RestSpecs, HttpRequest>, RestSpecs, HttpResponse> syncRequestFunction = (request, specs) ->
            Try.of(() -> specs.getBaseClient().send(request.apply(specs), specs.getResponseBodyHandler()))
            .get();

    BiFunction<Function<RestSpecs, HttpRequest>, RestSpecs, CompletableFuture<HttpResponse>> asyncRequestFunction = (request, specs) ->
            specs.getBaseClient().sendAsync(request.apply(specs),
                    specs.getResponseBodyHandler());
}
