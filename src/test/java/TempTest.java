import com.fasterxml.jackson.databind.ObjectMapper;
import functions.Helpers;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TempTest implements Helpers {

    Stream<String> data = Stream.of("Um", "Dos", "Drei");


    @TestFactory
    Stream<DynamicTest> fact1(Stream<String> data){

        return data.map(i -> DynamicTest.dynamicTest(i, () -> assertNotNull(i)));
    }


    @ParameterizedTest(name = "nada")
    @CsvFileSource(resources = "/temp.csv", numLinesToSkip = 1)
    void jsonTest(ArgumentsAccessor accessor) throws IOException {
        var funcionalidade = accessor.getString(0);
        var account = accessor.getString(1);
        var specs = accessor.getString(2);
        var amount = accessor.getString(3);
        var type = accessor.getString(4);

        Account account1 = new ObjectMapper().readValue(account, Account.class);

        String spec = "{'scene':'ok','returnCode':'200', 'errorCode':'none','message':'algo'}";
        Specs specs1 = new ObjectMapper().readValue(specs, Specs.class);

        Specs specs2 = (Specs) stringToObject.apply(specs, Specs.class);


        System.out.println(
                funcionalidade + "\n" +
                        account + "\n" +
                        specs + "\n" +
                        amount+ "\n" +
                        type
        );


        System.out.println("Classe: " + account1.toString());
        System.out.println("Specs: " + specs2.toString());


    }



}
