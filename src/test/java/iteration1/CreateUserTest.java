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

public class CreateUserTest {

    @BeforeAll
    public static void setupRestAssured() {
        RestAssured.filters(
                List.of(new RequestLoggingFilter(),
                        new ResponseLoggingFilter()));
    }

    @Test
    public void adminCanCreateUserTest() {
        given()
                .auth().preemptive().basic("admin", "admin")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                        {
                        "username": "testuser2",
                        "password": "Test1234$",
                        "role": "USER"
                        }
                        """)
                .when()
                .post("http://localhost:4111/api/v1/admin/users")
                .then()
                .statusCode(HttpStatus.SC_CREATED);
    }
}
