package functions;

/**
 * author: Thiago Carreira
 * license: Apache2
 */

import base.RestSpecs;
import io.vavr.control.Try;

import java.net.http.HttpResponse;
import java.util.function.Function;

public interface SyncFunctions extends HttpFunctions {

    //throws IOException, InterruptedException

    Function<RestSpecs, HttpResponse> syncRequestGET = specs ->
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
                    .getOrNull();

}
