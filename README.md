<h3>Projeto modelo para teste de API Rest com base funções pré-definidas.</h3>

@Author: Thiago Carreira

@License: Apache 2

[![CircleCI](https://circleci.com/gh/tcanascimento/functional-rest/tree/master.svg?style=svg)](https://circleci.com/gh/tcanascimento/functional-rest/tree/master)

Foram implementados duas interfaces: 
- uma conjunto de Funções para Request Assíncronos com assinatura:
 
```
Function<RestSpecs, HttpResponse> asyncRequestGET = (specs) ->
             (HttpResponse) Try.of(() ->
                     specs.getBaseClient().sendAsync(
                             requestGET.apply(specs),
                             specs.getResponseBodyHandler()
                     ).get()
             ).getOrNull();
```
- um conjunto de Funções, uma para cada método, de assinatura similar, porém, Síncronas:
```java
Function<RestSpecs, HttpResponse> syncResquestGET = (specs) ->
            Try.of(() ->
                    specs.getBaseClient().send(requestGET.apply(specs), specs.getResponseBodyHandler())
            ).getOrNull();
```
                                                                           
Um teste fazendo uso dessa função tem o seguinte formato: 

````java
@Tag("Function")
@DisplayName("Aplicando Function")
@Test
void testFunction(){

   AtomicReference<HttpResponse> response = new AtomicReference<>();
       
   response.lazySet(Lazy.val(()-> syncRequestPost.apply(restSpecs()), HttpResponse.class));
       
   assertAll("valida dia feliz",
         () -> assertNotNull(response.get()),
         () -> assertNotNull(response.get().headers().firstValue("x-access-token"))
   );
}
````

<p>Para executar testes com uma <i>tag</i> específica, por exemplo 'auth', execute via terminal: <code>gradle clean test -Dtag="auth"</code>. </p>
<p>Consulte a <a href="https://junit.org/junit5/docs/current/user-guide/#writing-tests-tagging-and-filtering">documentação oficial do Junit5</a> para maiores informações.</p> 

