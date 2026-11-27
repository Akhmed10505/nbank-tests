package iteration2;



import io.restassured.response.ValidatableResponse;
import io.restassured.specification.ResponseSpecification;
import models.UpdateCustomerProfileRequest;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import requests.UpdateCustomerProfileRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;


import java.util.stream.Stream;
import static org.hamcrest.Matchers.equalTo;

public class UpdateCustomerProfileTests {

    public static Stream<Arguments> validNameCases() {
        return Stream.of(
                Arguments.of("два слова", "John Smith")
        );
    }

    public static Stream<Arguments> invalidNameCases() {
        return Stream.of(
                Arguments.of("одно слово", "John"),
                Arguments.of("три слова", "John Michael Smith"),
                Arguments.of("цифры в имени", "John123 Smith"),
                Arguments.of("несколько пробелов", "John  Smith"),
                Arguments.of("специальный символ", "John@ Smith")
        );
    }

    @ParameterizedTest(name = "Профиль принимает имя: {0}")
    @MethodSource("validNameCases")
    public void userCanUpdateProfileWithValidName(String caseName, String name) {
        updateProfile(name, ResponseSpecs.returnsOk());

        getProfile()
                .body("name", equalTo(name));
    }

    @ParameterizedTest(name = "Профиль отклоняет имя: {0}")
    @MethodSource("invalidNameCases")
    public void userCannotUpdateProfileWithInvalidName(String caseName, String name) {
        updateProfile(name, ResponseSpecs.returnsBadRequest());

        getProfile()
                .body("name", equalTo("John Smith"));
    }

    private void updateProfile(String name, ResponseSpecification responseSpecification) {
        new UpdateCustomerProfileRequester(RequestSpecs.userSpec(), responseSpecification)
                .put(UpdateCustomerProfileRequest.builder()
                        .name(name)
                        .build());
    }

    private ValidatableResponse getProfile() {
        return new UpdateCustomerProfileRequester(RequestSpecs.userSpec(), ResponseSpecs.returnsOk())
                .get();
    }
}

