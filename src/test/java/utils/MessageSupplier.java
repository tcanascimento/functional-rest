package utils;

import java.util.function.Supplier;

public interface MessageSupplier {

    Supplier<String> statusCode200 = () -> "Status Code deve ser 200";

    Supplier<String> notNull = () -> "Retorno não pode ser nulo!";

    Supplier<String> objectEqual = () -> "Object must be Equals!";

    Supplier<String> urlMustBeEqual = () -> "Url must be Equal!";
}
