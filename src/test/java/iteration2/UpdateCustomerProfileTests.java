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

    public class UpdateCustomerProfileTests {

        @BeforeAll
        public static void setupRestAssured() {
            RestAssured.filters(
                    List.of(new RequestLoggingFilter(),
                            new ResponseLoggingFilter()));
        }


        @Test
        public void userCanUpdateProfileWithValidTwoWordNameTest() {
            given()
                    .auth().preemptive().basic("testuser", "Test1234$")
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body("""
                        {
                          "name": "John Smith"
                        }
                        """)
                    .when()
                    .put("http://localhost:4111/api/v1/customer/profile")
                    .then()
                    .statusCode(HttpStatus.SC_OK);
        }


        @Test
        public void userCannotUpdateProfileWithSingleWordNameTest() {
            given()
                    .auth().preemptive().basic("testuser", "Test1234$")
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body("""
                        {
                          "name": "John"
                        }
                        """)
                    .when()
                    .put("http://localhost:4111/api/v1/customer/profile")
                    .then()
                    .statusCode(HttpStatus.SC_BAD_REQUEST);
        }


        @Test
        public void userCannotUpdateProfileWithThreeWordNameTest() {
            given()
                    .auth().preemptive().basic("testuser", "Test1234$")
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body("""
                        {
                          "name": "John Michael Smith"
                        }
                        """)
                    .when()
                    .put("http://localhost:4111/api/v1/customer/profile")
                    .then()
                    .statusCode(HttpStatus.SC_BAD_REQUEST);
        }


        @Test
        public void userCannotUpdateProfileWithNumbersInNameTest() {
            given()
                    .auth().preemptive().basic("testuser", "Test1234$")
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body("""
                        {
                          "name": "John123 Smith"
                        }
                        """)
                    .when()
                    .put("http://localhost:4111/api/v1/customer/profile")
                    .then()
                    .statusCode(HttpStatus.SC_BAD_REQUEST);
        }


        @Test
        public void userCannotUpdateProfileWithMultipleSpacesInNameTest() {
            given()
                    .auth().preemptive().basic("testuser", "Test1234$")
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body("""
                        {
                          "name": "John  Smith"
                        }
                        """)
                    .when()
                    .put("http://localhost:4111/api/v1/customer/profile")
                    .then()
                    .statusCode(HttpStatus.SC_BAD_REQUEST);
        }


        @Test
        public void userCannotUpdateProfileWithSpecialCharacterInNameTest() {
            given()
                    .auth().preemptive().basic("testuser", "Test1234$")
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body("""
                        {
                          "name": "John@ Smith"
                        }
                        """)
                    .when()
                    .put("http://localhost:4111/api/v1/customer/profile")
                    .then()
                    .statusCode(HttpStatus.SC_BAD_REQUEST);
        }
    }

