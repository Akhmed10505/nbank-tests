package requests;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.BaseModel;

import static io.restassured.RestAssured.given;

public class CustomerAccountsRequester extends Request<BaseModel> {
    public CustomerAccountsRequester(
            RequestSpecification requestSpecification,
            ResponseSpecification responseSpecification
    ) {
        super(requestSpecification, responseSpecification);
    }

    @Override
    public ValidatableResponse post(BaseModel model) {
        throw new UnsupportedOperationException("Для получения счетов клиента используется GET-запрос");
    }

    @Override
    public ValidatableResponse get() {
        return given()
                .spec(requestSpecification)
                .get("/api/v1/customer/accounts")
                .then()
                .spec(responseSpecification);
    }
}
