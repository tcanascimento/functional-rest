package samples;

import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ArgumentsSource(ReversalProvider.class)
public @interface ReversalSource {
    /**
     * Nome do do campo ou objecto que é a fonte para a injeção de dados.
     */

    String fieldSource()  default "";
}
