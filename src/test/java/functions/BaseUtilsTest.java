package functions;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.MessageSupplier;
import utils.TestUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;


@Tag("utils")
class BaseUtilsTest implements BaseUtils, TestUtils, MessageSupplier {

    private Supplier<String> baseURL = () -> "https://www.gateway-empresa.com";
    private Supplier<String> endpoint = () -> "/relatorios/{cnpj}/razao/lancamento/{conta}";
    private Map<String, Object> headers = Map.of("Content-Type", "application/x-www-form-urlencoded", "client_id", "CLIENTE_ID", "access_token", "QzBOdMOhQjFsLUMwcjMtQjRDay1UMGszbgo=");
    private Map<String, Object> queryPars = Map.of( "mesInicial", 1, "ano", 2019,"mesFinal", 12);
    private Map<String, Object> pathPars =  Map.of("cnpj","23494210000137", "conta", "2.01.01.01.03");

    @Tag("specs")
    @Test
    void loadConfigFileTest(){

        var specs = specsFromFile.apply(configFile.get());

        var formatedEndpoint = queryParametersComposition.apply(setPathParameters.apply(endpoint.get(), pathPars), queryPars);
//        System.out.println(formatedEndpoint);

        assertAll(
                () -> assertNotNull(specs, loadFileError.get()),
                () -> assertEquals(baseURL.get(), specs.getBaseUrl().toString(), urlMustBeEqual.get()),
                () -> assertEquals(headers, specs.getHeadersMap(), matchHeaders.get()),
                () -> assertEquals(endpoint.get(), specs.getRawEndpoint(), endpointFormatError.get()),
                () -> assertFalse(formatedEndpoint.contains("{cnpj}"), endpointFormatError.get()),
                () -> assertEquals(queryPars, specs.getQueryParams(), matchQueryPars.get()),
                () -> assertEquals(pathPars, specs.getPathParams(), matchPathPars.get()),
                () -> assertNotNull(specs.getURI()));
    }

    @Tag("path")
    @Test
    void pathParametersTest(){

        var formatedPath = setPathParameters.apply(endpoint.get(), pathPars);
        var expected = "/relatorios/23494210000137/razao/lancamento/2.01.01.01.03";

        assertAll(
                () -> assertNotNull(formatedPath, objectConstructError.get()),
                () -> assertEquals(expected, formatedPath, nonParameters.get()));
    }

    @Tag("array")
    @Test
    void mapToStringArrayTest(){
        Map<String, Object> basePars = Map.of("par1", "par2", "par3", "par4");
        String[] expected = {"par1", "par2", "par3", "par4"};
        var res = mapToStringArray.apply(basePars);

        assertAll(
                () -> assertNotNull(res, notNull.get()),
                () -> assertEquals(expected.length, res.length, objectContentEquals.get()),
                () -> assertTrue(equalsList.test(Arrays.asList(expected), Arrays.asList(res)), objectContentEquals.get()));

    }

    @Tag("empty-array")
    @Test
    void mapToStringEmptyArrayTest(){
        Map<String, Object> basePars = Map.of();
        String[] expected = {};
        var res = mapToStringArray.apply(basePars);

        assertAll(
                () -> assertNotNull(res, notNull.get()),
                () -> assertEquals(expected.length, res.length, objectContentEquals.get()),
                () -> assertTrue(equalsList.test(Arrays.asList(expected), Arrays.asList(res)), objectContentEquals.get()));

    }


    @Tag("query")
    @Test
    void queryParsCompositionTest(){

        var res = queryParametersComposition.apply(endpoint.get(), queryPars);
        System.out.println(res);
        assertAll(
                () -> assertNotNull(res, notNull.get()),
                () -> assertTrue(res.length() >= endpoint.get().length(), objectContentEquals.get()),
                () -> assertTrue(containsOn.test(res, queryPars), objectContentEquals.get()));

    }

    @Tag("map")
    @Test
    void mapFromStringTest(){

        var str = "{\"ano\": 2019, \"mesInicial\": '1' , \"mesFinal\": 12}";
        var res = generateMapFromString.apply(str);

        assertAll(
                () -> assertNotNull(res, notNull.get()),
                () -> assertSame(res.getClass(), HashMap.class, objectMapType.get())
        );
    }


}