package iteration1;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class CreateAccountTests {

    @BeforeAll
    public static void setupRestAssured() {
        RestAssured.filters(
                List.of(new RequestLoggingFilter(),
                        new ResponseLoggingFilter()));
    }

    @Test
    public void userCanCreateAccountTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .accept(ContentType.JSON)
                .when()
                .post("http://localhost:4111/api/v1/accounts")
                .then()
                .statusCode(HttpStatus.SC_CREATED);

    }

    @Test
    public void userCanGetAccountsTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .accept(ContentType.JSON)
                .when()
                .get("http://localhost:4111/api/v1/customer/accounts")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
