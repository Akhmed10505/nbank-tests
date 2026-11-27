package iteration2;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.ResponseSpecification;
import models.DepositRequest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import requests.CustomerAccountsRequester;
import requests.DepositRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;
import testdata.TestData;


import java.util.stream.Stream;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.within;


public class DepositTests {

    public static Stream<Arguments> validDepositCases() {
        return Stream.of(
                Arguments.of("минимальный депозит", TestData.MIN_AMOUNT),
                Arguments.of("выше минимального", TestData.AMOUNT_ABOVE_MINIMUM),
                Arguments.of("ниже максимального", TestData.DEPOSIT_BELOW_MAXIMUM),
                Arguments.of("максимальный депозит", TestData.MAX_DEPOSIT_AMOUNT)
        );
    }

    public static Stream<Arguments> invalidDepositCases() {
        return Stream.of(
                Arguments.of("нулевой депозит", 0.0),
                Arguments.of("ниже минимального", TestData.AMOUNT_BELOW_MINIMUM),
                Arguments.of("выше максимального", TestData.DEPOSIT_ABOVE_MAXIMUM),
                Arguments.of("отрицательный депозит", -1.0)
        );
    }

    @ParameterizedTest(name = "Успешное пополнение: {0}")
    @MethodSource("validDepositCases")
    public void userCanDepositValidAmount(String caseName, double amount) {
        double balanceBefore = getBalance(TestData.FIRST_ACCOUNT_ID);

        sendDeposit(amount, ResponseSpecs.returnsOk());

        double balanceAfter = getBalance(TestData.FIRST_ACCOUNT_ID);

        assertThat(balanceAfter - balanceBefore)
                .isCloseTo(amount, within(0.001));
    }

    @ParameterizedTest(name = "Отклонение пополнения: {0}")
    @MethodSource("invalidDepositCases")
    public void userCannotDepositInvalidAmount(String caseName, double amount) {
        double balanceBefore = getBalance(TestData.FIRST_ACCOUNT_ID);

        sendDeposit(amount, ResponseSpecs.returnsBadRequest());

        double balanceAfter = getBalance(TestData.FIRST_ACCOUNT_ID);

        assertThat(balanceAfter)
                .isCloseTo(balanceBefore, within(0.001));
    }

    private void sendDeposit(double amount, ResponseSpecification responseSpecification) {
        new DepositRequester(RequestSpecs.userSpec(),
                responseSpecification)
                .post(DepositRequest.builder()
                        .id(TestData.FIRST_ACCOUNT_ID)
                        .balance(amount)
                        .build());
    }

    private double getBalance(long accountId) {
        ValidatableResponse response = new CustomerAccountsRequester(
                RequestSpecs.userSpec(), ResponseSpecs.returnsOk())
                .get();

        return response.extract()
                .jsonPath()
                .getDouble("find { it.id == " + accountId + " }.balance");
    }
}
