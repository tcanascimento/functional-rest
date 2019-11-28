<h3>Projeto modelo para teste de API Rest com base funções pré-definidas.</h3>

@Author: Thiago Carreira

@License: Apache 2

[![CircleCI](https://circleci.com/gh/tcanascimento/functional-rest/tree/master.svg?style=svg)](https://circleci.com/gh/tcanascimento/functional-rest/tree/master)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=tcanascimento_functional-rest&metric=alert_status)](https://sonarcloud.io/dashboard?id=tcanascimento_functional-rest)

[![](https://jitpack.io/v/tcanascimento/functional-rest.svg)](https://jitpack.io/#tcanascimento/functional-rest)

Fora implementado uma interface com duas funções com duas assinaturas:: 
- duas Funções para _Request_ Assíncronos com assinaturas:
 
```java
    Function<RestSpecs, CompletableFuture<HttpResponse>> asyncRequest = specs ->
            specs.getBaseClient().sendAsync(specs.getRequestMethod(),
                    specs.getResponseBodyHandler());
```
_e_
```java
    BiFunction<HttpRequest, RestSpecs, CompletableFuture<HttpResponse>> asyncHttpRequest = (request, specs) ->
            specs.getBaseClient().sendAsync(request,
                    specs.getResponseBodyHandler());
```

- duas Funções para _Request_ Síncronos com assinaturas:
```java
    Function<RestSpecs, Either<String, HttpResponse>> syncRequest = specs -> {
        try {
            return Right(specs.getBaseClient().send(specs.getRequestMethod(), specs.getResponseBodyHandler()));
        } catch (IOException | InterruptedException e) {
            return Left(e.getMessage());
        }
    };
```
_e_
```java
    BiFunction<HttpRequest, RestSpecs, HttpResponse> syncHttpRequest = (request, specs) ->
            Try.of(() -> specs.getBaseClient().send(request, specs.getResponseBodyHandler()))
                    .get();
```
                                                                           
Um teste fazendo uso dessa função tem o seguinte formato: 

````java
@Tag("Function")
@DisplayName("Aplicando Function")
@Test
void testFunction(){

   AtomicReference<HttpResponse> response = new AtomicReference<>();
 
   var specs = specsFromFile.apply("specs.conf");
       
   response.lazySet(Lazy.val(()-> syncRequestPost.apply(specs), HttpResponse.class));
       
   assertAll("valida dia feliz",
         () -> assertNotNull(response.get()),
         () -> assertNotNull(response.get().headers().firstValue("x-access-token"))
   );
}
````
       assertAll("valida dia feliz",
             () -> assertNotNull(response.get()),
             () -> assertNotNull(response.get().headers().firstValue("x-access-token")));
    }
</code></pre>

```java
    @Test
    void asyncSampleTest() {

        var specs = new RestSpecs("https://httpbin.org/get", headers.get(), "", "get");
        var response = asyncRequest.apply(specs);

        assertAll( "Validação básica",
                () -> assertNotNull(response.get().body()),
                () -> assertEquals(200, response.get().statusCode()));

    }
```

<p>Para executar testes com uma <i>tag</i> específica, por exemplo 'auth', execute via terminal: <code>gradle clean test -Dtag="auth"</code>. </p>
<p>Consulte a <a href="https://junit.org/junit5/docs/current/user-guide/#writing-tests-tagging-and-filtering">documentação oficial do Junit5</a> para maiores informações.</p> 

<p><a href="https://tcanascimento.github.io/functional-rest/">GitHub Page</a></p>
