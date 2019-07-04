package functions;

/**
 * author: Thiago Carreira
 * license: Apache2
 */

import base.RestSpecs;
import io.vavr.control.Try;

import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;


public interface AsyncFunctions extends HttpFunctions {

    //InterruptedException, ExecutionException

    Function<RestSpecs, HttpResponse> asyncRequestGET = specs -> (HttpResponse)
        Try.of(() ->
                    specs.getBaseClient().sendAsync(
                            requestGET.apply(specs),
                            specs.getResponseBodyHandler()
                    ).get()
            ).getOrNull();

    Function<RestSpecs, HttpResponse> asyncRequestDELETE = specs ->

            (HttpResponse) Try.of(() ->
                    specs.getBaseClient().sendAsync(
                            requestDELETE.apply(specs),
                            specs.getResponseBodyHandler()
                    ).get()
            ).getOrNull();


    Function<RestSpecs, HttpResponse> asyncRequestPOST = specs ->
            (HttpResponse) Try.of(() ->
                    specs.getBaseClient().sendAsync(
                            requestPOST.apply(specs),
                            specs.getResponseBodyHandler()
                    ).get()
            ).getOrNull();


    Function<RestSpecs, HttpResponse> asyncRequestPUT = specs ->
            (HttpResponse) Try.of(() ->
                            specs.getBaseClient().sendAsync(requestPUT.apply(specs),
                                    specs.getResponseBodyHandler()
                            ).get()
                    ).getOrNull();

    //Try.run(() -> {throw new ProtocolException("request error");}).andFinallyTry(()-> con.setRequestMethod("GET"));/**/
}
