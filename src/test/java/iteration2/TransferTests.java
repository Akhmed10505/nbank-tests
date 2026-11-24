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
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;

public class TransferTests {

    @BeforeAll
    public static void setupRestAssured() {
        RestAssured.filters(
                List.of(new RequestLoggingFilter(),
                        new ResponseLoggingFilter()));
    }

    @Test
    public void userCanTransferValidAmountTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                        {
                          "senderAccountId": 1,
                          "receiverAccountId": 2,
                          "amount": 250.75
                        }
                        """)
                .when()
                .post("http://localhost:4111/api/v1/accounts/transfer")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void userCanGetAccountsAfterValidTransferTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .accept(ContentType.JSON)
                .when()
                .get("http://localhost:4111/api/v1/customer/accounts")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("find { it.id == 1 }.balance", equalTo(24699.52f))
                .body("find { it.id == 2 }.balance", equalTo(501.51f));
    }


    @Test
    public void userCanTransferMinimumAmountTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                        {
                          "senderAccountId": 1,
                          "receiverAccountId": 2,
                          "amount": 0.01
                        }
                        """)
                .when()
                .post("http://localhost:4111/api/v1/accounts/transfer")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void userCanGetAccountsAfterMinimumTransferTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .accept(ContentType.JSON)
                .when()
                .get("http://localhost:4111/api/v1/customer/accounts")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("find { it.id == 1 }.balance", equalTo(24699.51f))
                .body("find { it.id == 2 }.balance", equalTo(501.52f));
    }

    @Test
    public void userCanTransferAboveMinimumAmountTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                        {
                          "senderAccountId": 1,
                          "receiverAccountId": 2,
                          "amount": 0.02
                        }
                        """)
                .when()
                .post("http://localhost:4111/api/v1/accounts/transfer")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void userCanGetAccountsAfterAboveMinimumTransferTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .accept(ContentType.JSON)
                .when()
                .get("http://localhost:4111/api/v1/customer/accounts")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("find { it.id == 1 }.balance", equalTo(24699.49f))
                .body("find { it.id == 2 }.balance", equalTo(501.54f));
    }

    @Test
    public void userCanTransferBelowMaximumAmountTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                        {
                          "senderAccountId": 1,
                          "receiverAccountId": 2,
                          "amount": 9999.99
                        }
                        """)
                .when()
                .post("http://localhost:4111/api/v1/accounts/transfer")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void userCanGetAccountsAfterBelowMaximumTransferTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .accept(ContentType.JSON)
                .when()
                .get("http://localhost:4111/api/v1/customer/accounts")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("find { it.id == 1 }.balance", equalTo(14699.50f))
                .body("find { it.id == 2 }.balance", equalTo(10501.53f));
    }

    @Test
    public void userCanTransferMaximumAmountTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                        {
                          "senderAccountId": 1,
                          "receiverAccountId": 2,
                          "amount": 10000
                        }
                        """)
                .when()
                .post("http://localhost:4111/api/v1/accounts/transfer")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void userCanGetAccountsAfterMaximumTransferTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .accept(ContentType.JSON)
                .when()
                .get("http://localhost:4111/api/v1/customer/accounts")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("find { it.id == 1 }.balance", equalTo(4699.50f))
                .body("find { it.id == 2 }.balance", equalTo(20501.53f));
    }

    @Test
    public void userCannotTransferZeroAmountTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                        {
                          "senderAccountId": 1,
                          "receiverAccountId": 2,
                          "amount": 0
                        }
                        """)
                .when()
                .post("http://localhost:4111/api/v1/accounts/transfer")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void userCanGetAccountsAfterZeroTransferTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .accept(ContentType.JSON)
                .when()
                .get("http://localhost:4111/api/v1/customer/accounts")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("find { it.id == 1 }.balance", equalTo(4699.50f))
                .body("find { it.id == 2 }.balance", equalTo(20501.53f));
    }

    @Test
    public void userCannotTransferBelowMinimumAmountTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                        {
                          "senderAccountId": 1,
                          "receiverAccountId": 2,
                          "amount": 0.001
                        }
                        """)
                .when()
                .post("http://localhost:4111/api/v1/accounts/transfer")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void userCanGetAccountsAfterBelowMinimumTransferTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .accept(ContentType.JSON)
                .when()
                .get("http://localhost:4111/api/v1/customer/accounts")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("find { it.id == 1 }.balance", equalTo(4699.50f))
                .body("find { it.id == 2 }.balance", equalTo(20501.53f));
    }


    @Test
    public void userCannotTransferAboveMaximumAmountTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                        {
                          "senderAccountId": 1,
                          "receiverAccountId": 2,
                          "amount": 10000.01
                        }
                        """)
                .when()
                .post("http://localhost:4111/api/v1/accounts/transfer")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void userCanGetAccountsAfterAboveMaximumTransferTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .accept(ContentType.JSON)
                .when()
                .get("http://localhost:4111/api/v1/customer/accounts")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("find { it.id == 1 }.balance", equalTo(4699.50f))
                .body("find { it.id == 2 }.balance", equalTo(20501.53f));
    }

    @Test
    public void userCannotTransferNegativeAmountTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                        {
                          "senderAccountId": 1,
                          "receiverAccountId": 2,
                          "amount": -1
                        }
                        """)
                .when()
                .post("http://localhost:4111/api/v1/accounts/transfer")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void userCanGetAccountsAfterNegativeTransferTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .accept(ContentType.JSON)
                .when()
                .get("http://localhost:4111/api/v1/customer/accounts")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("find { it.id == 1 }.balance", equalTo(4699.50f))
                .body("find { it.id == 2 }.balance", equalTo(20501.53f));
    }

    @Test
    public void userCannotTransferWithInsufficientFundsTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                        {
                          "senderAccountId": 1,
                          "receiverAccountId": 2,
                          "amount": 6000
                        }
                        """)
                .when()
                .post("http://localhost:4111/api/v1/accounts/transfer")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void userCanGetAccountsAfterInsufficientFundsTransferTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .accept(ContentType.JSON)
                .when()
                .get("http://localhost:4111/api/v1/customer/accounts")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("find { it.id == 1 }.balance", equalTo(4699.50f))
                .body("find { it.id == 2 }.balance", equalTo(20501.53f));
    }

    @Test
    public void userCannotTransferFromInvalidSenderAccountTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                        {
                          "senderAccountId": 999,
                          "receiverAccountId": 2,
                          "amount": 100
                        }
                        """)
                .when()
                .post("http://localhost:4111/api/v1/accounts/transfer")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void userCanGetAccountsAfterInvalidSenderTransferTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .accept(ContentType.JSON)
                .when()
                .get("http://localhost:4111/api/v1/customer/accounts")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("find { it.id == 1 }.balance", equalTo(4699.50f))
                .body("find { it.id == 2 }.balance", equalTo(20501.53f));
    }


    @Test
    public void userCannotTransferToInvalidReceiverAccountTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                        {
                          "senderAccountId": 1,
                          "receiverAccountId": 999,
                          "amount": 100
                        }
                        """)
                .when()
                .post("http://localhost:4111/api/v1/accounts/transfer")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void userCanGetAccountsAfterInvalidReceiverTransferTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .accept(ContentType.JSON)
                .when()
                .get("http://localhost:4111/api/v1/customer/accounts")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("find { it.id == 1 }.balance", equalTo(4699.50f))
                .body("find { it.id == 2 }.balance", equalTo(20501.53f));
    }

    @Test
    public void userCanTransferToSameAccountTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                        {
                          "senderAccountId": 1,
                          "receiverAccountId": 1,
                          "amount": 100
                        }
                        """)
                .when()
                .post("http://localhost:4111/api/v1/accounts/transfer")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void userCanGetAccountsAfterSameAccountTransferTest() {
        given()
                .auth().preemptive().basic("testuser", "Test1234$")
                .accept(ContentType.JSON)
                .when()
                .get("http://localhost:4111/api/v1/customer/accounts")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("find { it.id == 1 }.balance", equalTo(4699.50f))
                .body("find { it.id == 2 }.balance", equalTo(20501.53f));
    }
}
