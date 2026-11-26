package requests;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.BaseModel;

public abstract class Request <T extends BaseModel> {
    protected final RequestSpecification requestSpecification;
    protected final ResponseSpecification responseSpecification;

    protected Request(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
        this.requestSpecification = requestSpecification;
        this.responseSpecification = responseSpecification;
    }

    public abstract ValidatableResponse post(T model);

    public ValidatableResponse get() {
        throw new UnsupportedOperationException("GET не поддерживается этим классом запросов");
    }

    public ValidatableResponse put(T model) {
        throw new UnsupportedOperationException("PUT не поддерживается этим классом запросов");
    }
}
