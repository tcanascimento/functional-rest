package samples;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.util.stream.Stream;

public class ReversalProvider implements ArgumentsProvider, AnnotationConsumer<ReversalSource> {

    private String fieldSource;

    @Override
    public void accept(ReversalSource reversalSource) {
        this.fieldSource = reversalSource.fieldSource();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {

        Object testInstance = context.getTestInstance().orElse(null);
        var field = context.getTestClass().get().getDeclaredField(fieldSource);
        field.setAccessible(true);
        var res = field.get(testInstance);
        var className = field.get(testInstance).getClass().getTypeName();

        return Stream.of(res).filter(Class.forName(className)::isInstance).map(Arguments::of);
//        return ((Set<String>)res).stream().map(Arguments::of);

    }
}
