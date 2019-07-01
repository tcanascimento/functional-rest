
import base.RestSpecsBuilder;
import functions.AsyncFunctions;
import functions.BaseUtils;
import functions.HttpFunctions;
import functions.SyncFunctions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.TestUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Tag("specs")
class RestSpecsTest implements AsyncFunctions, BaseUtils, HttpFunctions, SyncFunctions, TestUtils {

    private static final String SPECS_CONF = "src/test/resources/contabil-core.conf";
    private static final Map<String, Object> QUERY_PARAMS = Map.of("ano", 2019, "mesInicial", 1, "mesFinal",12);
    private static final Map<String, Object> PATH_PARAMS = Map.of("cnpj", "12312312312312","conta", "312312312312");
    private static final Map<String, Object> HEADERS_PARS = Map.of("Content-Type", "application/json", "Authorization", "Basic Y29udGFiaWxpemVpOk5EUTRaR015TWpnMVlXRTNPR1kxTWpaallUZGtOelkz");
    private static final String[] HEADERS = {"Content-Type", "application/json", "Authorization", "Basic Y29udGFiaWxpemVpOk5EUTRaR015TWpnMVlXRTNPR1kxTWpaallUZGtOelkz"};
    private static final String BASE_URI = "https://api-gateway-dev.contabilizei.com";
    private static final String ENDPOINT = "/contabil-core/v0/relatorios/{cnpj}/razao/lancamento/{conta}/";


    @Test
    @DisplayName("Load Specs From Conf")
    void loadSpecsFromConfTest(){

        var specs = specsFromFile.apply(SPECS_CONF);

        assertAll("Valida construcao de Specs a partir do Conf",
                () -> assertNotNull(specs),
                () -> assertEquals(BASE_URI, specs.getBaseUrl().toString()),
                () -> assertEquals(6, specs.getHeaders().length),
                () -> assertEquals(QUERY_PARAMS, specs.getQueryParams()),
                () -> assertNotNull(specs.getPathParams())
                );

    }

    @Test
    void validaSpecs(){

        final String baseURL = "http://www.google.com";
        final Map<String, Object> headers = Map.of("Content-Type", "application/json");
        var specs2 = new RestSpecsBuilder().with(
                ($) -> {
                    $.baseUrl2 = baseURL;
                    $.headersParams = headers;
                }).createSpecs();

        assertAll("apenas baseURL e headers",
                () -> assertNotNull(specs2),
                () -> assertTrue(specs2.getURI().toString().equalsIgnoreCase(baseURL)),
                () -> assertEquals(2, specs2.getHeaders().length));

        var response = syncRequestGET.apply(specs2);

        System.out.println("status code: " + response.statusCode() + "\nbody: " + response.body() + "\nheaders: " + response.headers());
    }

}
