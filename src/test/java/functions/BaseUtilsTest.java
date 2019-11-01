package functions;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.TestUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;


@Tag("utils")
class BaseUtilsTest implements BaseUtils, TestUtils {

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
        System.out.println(formatedEndpoint);

        assertAll(
                () -> assertNotNull(specs, "Erro ao carregar arquivo!"),
                () -> assertEquals(baseURL.get(), specs.getBaseUrl().toString(), "baseURL diferente!"),
                () -> assertEquals(headers, specs.getHeadersMap(), "Headers diferentes!"),
                () -> assertEquals(endpoint.get(), specs.getRawEndpoint(), "Endpoint errado!"),
                () -> assertFalse(formatedEndpoint.contains("{cnpj}"), "Endpoint no formato errado!"),
                () -> assertEquals(queryPars, specs.getQueryParams(), "Query Parameters não conferem"),
                () -> assertEquals(pathPars, specs.getPathParams(), "Path Parameters não conferem"),
                () -> assertNotNull(specs.getURI()));
    }

    @Tag("path")
    @Test
    void pathParametersTest(){

        var formatedPath = setPathParameters.apply(endpoint.get(), pathPars);
        var expected = "/relatorios/23494210000137/razao/lancamento/2.01.01.01.03";

        assertAll(
                () -> assertNotNull(formatedPath, "Erro ao construir objeto"),
                () -> assertEquals(expected, formatedPath, "Parâmetros não existentes"));
    }

    @Tag("array")
    @Test
    void mapToStringArrayTest(){
        Map<String, Object> basePars = Map.of("par1", "par2", "par3", "par4");
        String[] expected = {"par1", "par2", "par3", "par4"};
        var res = mapToStringArray.apply(basePars);

        assertAll(
                () -> assertNotNull(res, "Objeto não pode ser nulo"),
                () -> assertEquals(expected.length, res.length, "Arrays diferentes em conteúdo"),
                () -> assertTrue(equalsList.test(Arrays.asList(expected), Arrays.asList(res)), "Conteúdo deve ser o mesmo"));

    }

    @Tag("empty-array")
    @Test
    void mapToStringEmptyArrayTest(){
        Map<String, Object> basePars = Map.of();
        String[] expected = {};
        var res = mapToStringArray.apply(basePars);

        assertAll(
                () -> assertNotNull(res, "Objeto não pode ser nulo"),
                () -> assertEquals(expected.length, res.length, "Arrays diferentes em conteúdo"),
                () -> assertTrue(equalsList.test(Arrays.asList(expected), Arrays.asList(res)), "Conteúdo deve ser o mesmo"));

    }


    @Tag("query")
    @Test
    void queryParsCompositionTest(){

        var res = queryParametersComposition.apply(endpoint.get(), queryPars);
        System.out.println(res);
        assertAll(
                () -> assertNotNull(res, "Objeto não pode ser nulo"),
                () -> assertTrue(res.length() >= endpoint.get().length(), "Arrays diferentes em conteúdo"),
                () -> assertTrue(containsOn.test(res, queryPars), "Conteúdo deve ser o mesmo"));

    }

    @Tag("map")
    @Test
    void mapFromStringTest(){

        var str = "{\"ano\": 2019, \"mesInicial\": '1' , \"mesFinal\": 12}";
        var res = generateMapFromString.apply(str);

        assertAll(
                () -> assertNotNull(res, "Objeto não pode ser nulo"),
                () -> assertSame(res.getClass(), HashMap.class, "Objeto tem que ser do tipo Map")
        );
    }


}