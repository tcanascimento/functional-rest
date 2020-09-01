//package base;
//
///**
// * author: Thiago Carreira
// * license: Apache2
// */
//
//import functions.BaseUtils;
//
//import java.util.Map;
//import java.util.function.Consumer;
//
///**
// * Builder funcional para RestSpecs.
// * Exemplo de uso:
// *         RestSpecs teste = new RestSpecsBuilder().with(
// *                 $ -> {
// *                     $.baseUrl = URI.create(uri);
// *                     $.headers = h;
// *                     $.baseClient = client;
// *                 }).createSpecsOld();
// */
//public class RestSpecsBuilder implements BaseUtils {
//
//    public Map<String, Object> queryParams;
//    public Map<String, Object> pathParams;
//    public Map<String, Object> headersParams;
//    public String baseUrl2;
//    public String endpoint2;
//    public String bodyString;
//    public String requestMethod;
//
//    public RestSpecsBuilder with(Consumer<RestSpecsBuilder> builderFunction) {
//        builderFunction.accept(this);
//        return this;
//    }
//
//    public RestSpecs createSpecs(){
//        return new RestSpecs(baseUrl2, endpoint2, headersParams, queryParams, pathParams, bodyString, requestMethod);
//    }
//
//}
//
///*
//public class UserBuilder {
//
//    private final String email;
//    private final String password;
//    private boolean active;
//    private LocalDateTime lastLogin;
//
//    // Only one constructor with the required fields.
//    // This way we won't forget to set them.
//    public UserBuilder(String email,
//                       String password) {
//        this.email = email;
//        this.password = password;
//    }
//
//    public UserBuilder active(boolean active) {
//        this.active = active;
//        return this;
//    }
//
//    public UserBuilder lastLogin(LocalDateTime lastLogin) {
//        this.lastLogin = lastLogin;
//        return this;
//    }
//
//    public User build() {
//        return new User(this.email,
//                        this.password,
//                        this.active,
//                        Optional.ofNullable(this.lastLogin));
//    }
//}
// */