package functions;

import io.vavr.Lazy;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pojo.Dummy;
import utils.MessageSupplier;
import utils.TestUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@Tag("utils")
class BaseUtilsTest implements BaseUtils, TestUtils, MessageSupplier, Helpers {

    private Lazy<String> baseURL = Lazy.of(() -> "https://www.gateway-empresa.com");
    private Lazy<String> endpoint = Lazy.of(() -> "/rels/{cnpj}/something/lanc/{conta}");
    private Lazy<Map<String, Object>> headers = Lazy.of(() -> Map.of("Content-Type", "application/x-www-form-urlencoded", "client_id", "CLIENTE_ID", "access_token", "QzBOdMOhQjFsLUMwcjMtQjRDay1UMGszbgo="));
    private Lazy<Map<String, Object>> queryPars = Lazy.of(() -> Map.of( "mesInicial", 1, "ano", 2019,"mesFinal", 12));
    private Lazy<Map<String, Object>> pathPars =  Lazy.of(() -> Map.of("cnpj","12312312312312", "conta", "2.01.01.01.10"));

    @Test
    @Tag("updates")
    void updatesTest(){

        var specs = specsFromFile.apply(configFile.get());
        Map<String, Object> tempPathPars = new HashMap<>(pathPars.get());
        Map<String, Object> tempQuery = new HashMap<>(queryPars.get());
        tempPathPars.put("cnpj", "1234567890");
        tempQuery.put("mesFinal", 13);
        var updatePath = updatePathParams.apply(tempPathPars, specs);
        var updateQuery = updateQueryParams.apply(tempQuery, specs);
        var tempEndpoint = endpoint.get().concat("/nada");
        var updatedEndpoint = updateEndpoint.apply(tempEndpoint, specs);
        var tempBody = clazzToJson.apply(new Dummy("new Body"));
        var updatedBody = updateBody.apply(tempBody, specs);

        assertAll("Updates test",
                () -> assertNotNull(updatePath, objectConstructError.get()),
                () -> assertNotNull(updateQuery, objectConstructError.get()),
                () -> assertNotNull(updatedEndpoint, objectConstructError.get()),
                () -> assertNotNull(updatedBody, objectConstructError.get()),
                () -> assertNotNull(updatedBody.getBody(), notNull.get()),
                () -> assertEquals("1234567890", tempPathPars.get("cnpj"), objectContentEquals.get()),
                () -> assertEquals(13, tempQuery.get("mesFinal"), objectContentEquals.get()),
                () -> assertEquals(updatePath.getPathParams(), tempPathPars, objectContentEquals.get()),
                () -> assertEquals(updateQuery.getQueryParams(), tempQuery, objectContentEquals.get()),
                () -> assertEquals("/rels/{cnpj}/something/lanc/{conta}/nada",updatedEndpoint.getRawEndpoint(), objectContentEquals.get())

        );
    }

    //updateQueryParams

    @Tag("specs")
    @Test
    void loadConfigFileTest(){

        var specs = specsFromFile.apply(configFile.get());

        var formatedEndpoint = queryParametersComposition.apply(setPathParameters.apply(endpoint.get(), pathPars.get()), queryPars.get());

        assertAll("Load ConfigFile Test",
                () -> assertNotNull(specs, loadFileError.get()),
                () -> assertEquals(baseURL.get(), specs.getBaseUrl().toString(), urlMustBeEqual.get()),
                () -> assertEquals(headers.get(), specs.getHeadersMap(), matchHeaders.get()),
                () -> assertEquals(endpoint.get(), specs.getRawEndpoint(), endpointFormatError.get()),
                () -> assertFalse(formatedEndpoint.contains("{cnpj}"), endpointFormatError.get()),
                () -> assertEquals(queryPars.get(), specs.getQueryParams(), matchQueryPars.get()),
                () -> assertEquals(pathPars.get(), specs.getPathParams(), matchPathPars.get()),
                () -> assertNotNull(specs.getURI()));
    }

    @Tag("path")
    @Test
    void pathParametersTest(){

        var formatedPath = setPathParameters.apply(endpoint.get(), pathPars.get());
        var expected = "/rels/12312312312312/something/lanc/2.01.01.01.10";

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

        var res = queryParametersComposition.apply(endpoint.get(), queryPars.get());
        assertAll(
                () -> assertNotNull(res, notNull.get()),
                () -> assertTrue(res.length() >= endpoint.get().length(), objectContentEquals.get()),
                () -> assertTrue(containsOn.test(res, queryPars.get()), objectContentEquals.get()));

    }

    @Tag("map")
    @Test
    void mapFromStringTest(){

        var str = "{\"ano\": 2019, \"mesInicial\": 1 , \"mesFinal\": 12}";
        var res = generateMapFromString.apply(str);

        Set<String> keys = Set.of("\"ano\"", "\"mesInicial\"", "\"mesFinal\"");
        Set<String> values = Set.of(String.valueOf(2019), String.valueOf(1), String.valueOf(12));

        assertAll(
                () -> assertNotNull(res, notNull.get()),
                () -> assertSame(res.getClass(), HashMap.class, objectMapType.get()),
                () -> assertTrue(res.keySet().containsAll(keys)),
                () -> assertTrue(res.values().containsAll(values))
        );
    }

}