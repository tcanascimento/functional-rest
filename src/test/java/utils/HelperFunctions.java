package utils;

import functions.HttpFunctions;
import io.vavr.API;

import java.net.http.HttpRequest;
import java.util.function.Function;

import static io.vavr.API.$;
import static io.vavr.API.Case;

public interface HelperFunctions extends HttpFunctions {

    Function<String, Function<RestSpecs, HttpRequest>> mapRequestMethod = method ->
            API.Match(method).of(
                    Case($("/get"), requestGET),
                    Case($("/post"), requestPOST),
                    Case($("/put"), requestPUT),
                    Case($("/delete"), requestDELETE),
                    Case($(), () -> requestGET));

}
