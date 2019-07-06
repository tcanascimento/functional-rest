package functions;

/**
 * author: Thiago Carreira
 * license: Apache2
 */

import base.RestSpecs;
import cyclops.control.Either;
import cyclops.control.Try;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.function.Function;

public interface SyncFunctions extends HttpFunctions {

    //throws IOException, InterruptedException

    /*Function<RestSpecs, HttpResponse> syncRequestGET = specs ->
            Try.of(() ->
                    specs.getBaseClient().send(requestGET.apply(specs), specs.getResponseBodyHandler())
            ).getOrNull();

    Function<RestSpecs, HttpResponse> syncRequestDELETE = specs ->
            Try.of(() -> specs.getBaseClient().send(requestDELETE.apply(specs), specs.getResponseBodyHandler()))
                    .getOrNull();


    Function<RestSpecs, HttpResponse> syncRequestPost = specs ->
            Try.of(() -> specs.getBaseClient().send(HttpFunctions.requestPOST.apply(specs), specs.getResponseBodyHandler()))
                    .getOrNull();

    Function<RestSpecs, HttpResponse> syncRequestPUT = specs ->
            Try.of(() -> specs.getBaseClient().send(requestPUT.apply(specs), specs.getResponseBodyHandler()))
                    .getOrNull();*/

    Function<RestSpecs, Either<Exception, HttpResponse>> syncRequestGET = specs ->
            cyclops.control.Try.withCatch(
                    () -> specs.getBaseClient().send(requestGET.apply(specs),
                            specs.getResponseBodyHandler()), IOException.class
            ).toEither();

    Function<RestSpecs, Either<Exception, HttpResponse>> syncRequestDELETE = specs ->
            cyclops.control.Try.withCatch(
                    () -> specs.getBaseClient().send(requestDELETE.apply(specs),
                            specs.getResponseBodyHandler()), IOException.class
            ).toEither();


    Function<RestSpecs, Either<Exception, HttpResponse>> syncRequestPost = specs ->
            cyclops.control.Try.withCatch(
                    () -> specs.getBaseClient().send(requestPOST.apply(specs),
                            specs.getResponseBodyHandler()), IOException.class
            ).toEither();

    Function<RestSpecs, Either<Exception, HttpResponse>> syncRequestPUT = specs ->
            Try.withCatch(
                    () -> specs.getBaseClient().send(requestPUT.apply(specs),
                            specs.getResponseBodyHandler()), IOException.class
            ).toEither();

}
