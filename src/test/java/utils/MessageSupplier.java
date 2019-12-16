package utils;

import io.vavr.Lazy;

public interface MessageSupplier {

    Lazy<String> statusCode200 = Lazy.of(() -> "Status Code should be 200");

    Lazy<String> notNull = Lazy.of(() -> "Return should not be Null!");

    Lazy<String> objectContentEquals = Lazy.of(() -> "Object or Content must be Equals!");

    Lazy<String> urlMustBeEqual = Lazy.of(() -> "Url must be Equal!");

    Lazy<String> specsNotNull = Lazy.of(() -> "Specs cannot be null!");

    Lazy<String> baseURLNotNull = Lazy.of(() -> "BaseURL cannot be null!");

    Lazy<String> rawEndpointNotNull = Lazy.of(() -> "Raw endpoint cannot be null!");

    Lazy<String> formatedEndpointNotNull = Lazy.of(() -> "Formated endpoint cannot be null");

    Lazy<String> headersNotNull = Lazy.of(() -> "Headers cannot be null!");

    Lazy<String> pathParametersNotNull = Lazy.of(() -> "PathParameters cannot be null!");

    Lazy<String> queryParametersNotNull = Lazy.of(() -> "QueryParameters cannot be null!");

    Lazy<String> baseClientNotNull = Lazy.of(() -> "BaseClient cannot be null!");

    Lazy<String> uriNotNull = Lazy.of(() -> "URI cannot be null!");

    Lazy<String> objectMapType= Lazy.of(() -> "Objeto tem que ser do tipo Map");

    Lazy<String> objectConstructError = Lazy.of(() -> "Error on construct the Object");

    Lazy<String> nonParameters = Lazy.of(() -> "No Parameters found");

    Lazy<String> matchPathPars = Lazy.of(() -> "Path Parameters do not match");

    Lazy<String> matchQueryPars = Lazy.of(() -> "Query Parameters do not match");

    Lazy<String> matchHeaders = Lazy.of(() -> "Headers do not match");

    Lazy<String> endpointFormatError = Lazy.of(() -> "Endpoint on wrong format");

    Lazy<String> loadFileError = Lazy.of(() -> "Error on loading file");

    Lazy<String> requestMethodNotNull = Lazy.of(() -> "Request Method can not be null!");


}
