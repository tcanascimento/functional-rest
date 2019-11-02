package utils;

import java.util.function.Supplier;

public interface MessageSupplier {

    Supplier<String> statusCode200 = () -> "Status Code should be 200";

    Supplier<String> notNull = () -> "Return should not be Null!";

    Supplier<String> objectEqual = () -> "Object must be Equals!";

    Supplier<String> urlMustBeEqual = () -> "Url must be Equal!";

    Supplier<String> specsNotNull = () -> "Specs cannot be null!";

    Supplier<String> baseURLNotNull = () -> "BaseURL cannot be null!";

    Supplier<String> rawEndpointNotNull = () -> "Raw endpoint cannot be null!";

    Supplier<String> formatedEndpointNotNull = () -> "Formated endpoint cannot be null";

    Supplier<String> headersNotNull = () -> "Headers cannot be null!";

    Supplier<String> pathParametersNotNull = () -> "PathParameters cannot be null!";

    Supplier<String> queryParametersNotNull = () -> "QueryParameters cannot be null!";

    Supplier<String> baseClientNotNull = () -> "BaseClient cannot be null!";

    Supplier<String> uriNotNull = () -> "URI cannot be null!";

}
