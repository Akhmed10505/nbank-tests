package iteration2;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class DepositTests {

    @BeforeAll
    public static void setupRestAssured() {
        RestAssured.filters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter()));
    }

    @Test
    public void userCanDepositValidAmountTest() {

        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                        {
                        "id": 1,
                        "balance": 100.5
                        }
                        """)
                .when()
                .post("http://localhost:4111/api/v1/accounts/deposit")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void userCannotDepositZeroAmountTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                        {
                        "id": 1,
                        "balance": 0
                        }
                        """)
                .when()
                .post("http://localhost:4111/api/v1/accounts/deposit")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void userCannotDepositBelowMinimumAmountTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                    {
                      "id": 1,
                      "balance": 0.001
                    }
                    """)
                .when()
                .post("http://localhost:4111/api/v1/accounts/deposit")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void userCanDepositMinimumAmountTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                    {
                      "id": 1,
                      "balance": 0.01
                    }
                    """)
                .when()
                .post("http://localhost:4111/api/v1/accounts/deposit")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void userCanDepositAboveMinimumAmountTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                    {
                      "id": 1,
                      "balance": 0.02
                    }
                    """)
                .when()
                .post("http://localhost:4111/api/v1/accounts/deposit")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void userCanDepositBelowMaximumAmountTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                    {
                      "id": 1,
                      "balance": 4999.99
                    }
                    """)
                .when()
                .post("http://localhost:4111/api/v1/accounts/deposit")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void userCanDepositMaximumAmountTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                    {
                      "id": 1,
                      "balance": 5000
                    }
                    """)
                .when()
                .post("http://localhost:4111/api/v1/accounts/deposit")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void userCannotDepositAboveMaximumAmountTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                    {
                      "id": 1,
                      "balance": 5000.01
                    }
                    """)
                .when()
                .post("http://localhost:4111/api/v1/accounts/deposit")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void userCannotDepositNegativeAmountTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                    {
                      "id": 1,
                      "balance": -1
                    }
                    """)
                .when()
                .post("http://localhost:4111/api/v1/accounts/deposit")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void userCannotDepositWithoutBalanceTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                    {
                      "id": 1
                    }
                    """)
                .when()
                .post("http://localhost:4111/api/v1/accounts/deposit")
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void userCannotDepositWithoutAccountIdTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                    {
                      "balance": 100
                    }
                    """)
                .when()
                .post("http://localhost:4111/api/v1/accounts/deposit")
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}
