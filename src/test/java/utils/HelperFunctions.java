package utils;

import base.RestSpecs;
import functions.AsyncFunctions;
import functions.SyncFunctions;
import io.vavr.API;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static io.vavr.API.$;
import static io.vavr.API.Case;

public abstract class HelperFunctions implements AsyncFunctions, SyncFunctions {

    protected Function<String, Function<RestSpecs, CompletableFuture<HttpResponse>>> mapRequestMethodToAsync = method ->
            API.Match(method).of(
                    Case($("/get"), asyncRequestGET),
                    Case($("/post"), asyncRequestPOST),
                    Case($("/put"), asyncRequestPUT),
                    Case($("/delete"), asyncRequestDELETE),
                    Case($(), () -> asyncRequestGET));

    protected Function<String, Function<RestSpecs, HttpResponse>> mapRequestMethodToSync = method ->
            API.Match(method).of(
                    Case($("/get"), syncRequestGET),
                    Case($("/post"), syncRequestPost),
                    Case($("/put"), syncRequestPUT),
                    Case($("/delete"), syncRequestDELETE),
                    Case($(), () -> syncRequestGET));

}
