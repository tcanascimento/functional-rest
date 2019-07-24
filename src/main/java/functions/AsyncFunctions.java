package functions;

/**
 * author: Thiago Carreira
 * license: Apache2
 */

import base.RestSpecs;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;


public interface AsyncFunctions extends HttpFunctions {

    //InterruptedException, ExecutionException
    //Try.run(() -> {throw new ProtocolException("request error");}).andFinallyTry(()-> con.setRequestMethod("GET"));/**/

    Function<RestSpecs, CompletableFuture<HttpResponse>> asyncRequestGET = specs ->
                    specs.getBaseClient().sendAsync(requestGET.apply(specs),
                            specs.getResponseBodyHandler());

    Function<RestSpecs, CompletableFuture<HttpResponse>> asyncRequestDELETE = specs ->
                    specs.getBaseClient().sendAsync(requestDELETE.apply(specs),
                            specs.getResponseBodyHandler());


    Function<RestSpecs, CompletableFuture<HttpResponse>> asyncRequestPOST = specs ->
            specs.getBaseClient().sendAsync(requestPOST.apply(specs),
                            specs.getResponseBodyHandler());


    Function<RestSpecs, CompletableFuture<HttpResponse>> asyncRequestPUT = specs ->
            specs.getBaseClient().sendAsync(requestPUT.apply(specs),
                                    specs.getResponseBodyHandler());

}
