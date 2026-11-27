package iteration2;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.ResponseSpecification;
import models.TransferRequest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import requests.CustomerAccountsRequester;
import requests.TransferRequester;
import specs.RequestSpecs;
import specs.ResponseSpecs;
import testdata.TestData;
import java.util.stream.Stream;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.within;

public class TransferTests {

    public static Stream<Arguments> validTransferCases() {
        return Stream.of(
                Arguments.of("минимальный перевод", TestData.MIN_AMOUNT),
                Arguments.of("выше минимального", TestData.AMOUNT_ABOVE_MINIMUM),
                Arguments.of("ниже максимального", TestData.TRANSFER_BELOW_MAXIMUM),
                Arguments.of("максимальный перевод", TestData.MAX_TRANSFER_AMOUNT)
        );
    }

    public static Stream<Arguments> invalidTransferCases() {
        return Stream.of(
                Arguments.of("нулевой перевод", 0.0),
                Arguments.of("ниже минимального", TestData.AMOUNT_BELOW_MINIMUM),
                Arguments.of("выше максимального", TestData.TRANSFER_ABOVE_MAXIMUM),
                Arguments.of("отрицательный перевод", -1.0),
                Arguments.of("недостаточно средств", 40_000.0)
        );
    }

    @ParameterizedTest(name = "Перевод проходит: {0}")
    @MethodSource("validTransferCases")
    public void userCanTransferValidAmount(String caseName, double amount) {
        double senderBalanceBefore = getBalance(TestData.FIRST_ACCOUNT_ID);
        double receiverBalanceBefore = getBalance(TestData.SECOND_ACCOUNT_ID);

        sendTransfer(
                TestData.FIRST_ACCOUNT_ID,
                TestData.SECOND_ACCOUNT_ID,
                amount,
                ResponseSpecs.returnsOk());

        double senderBalanceAfter = getBalance(TestData.FIRST_ACCOUNT_ID);
        double receiverBalanceAfter = getBalance(TestData.SECOND_ACCOUNT_ID);

        assertThat(senderBalanceBefore - senderBalanceAfter)
                .isCloseTo(amount, within(0.001));

        assertThat(receiverBalanceAfter - receiverBalanceBefore)
                .isCloseTo(amount, within(0.001));
    }

    @ParameterizedTest(name = "Перевод отклоняется: {0}")
    @MethodSource("invalidTransferCases")
    public void userCannotTransferInvalidAmount(String caseName, double amount) {
        double senderBalanceBefore = getBalance(TestData.FIRST_ACCOUNT_ID);
        double receiverBalanceBefore = getBalance(TestData.SECOND_ACCOUNT_ID);

        sendTransfer(
                TestData.FIRST_ACCOUNT_ID,
                TestData.SECOND_ACCOUNT_ID,
                amount,
                ResponseSpecs.returnsBadRequest()
        );

        double senderBalanceAfter = getBalance(TestData.FIRST_ACCOUNT_ID);
        double receiverBalanceAfter = getBalance(TestData.SECOND_ACCOUNT_ID);

        assertThat(senderBalanceAfter)
                .isCloseTo(senderBalanceBefore, within(0.001));

        assertThat(receiverBalanceAfter)
                .isCloseTo(receiverBalanceBefore, within(0.001));
    }

    private void sendTransfer(long senderId, long receiverId, double amount, ResponseSpecification responseSpecification) {
        new TransferRequester(RequestSpecs.userSpec(), responseSpecification)
                .post(TransferRequest.builder()
                        .senderAccountId(senderId)
                        .receiverAccountId(receiverId)
                        .amount(amount)
                        .build());
    }

    private double getBalance(long accountId) {
        ValidatableResponse response = new CustomerAccountsRequester(
                RequestSpecs.userSpec(),
                ResponseSpecs.returnsOk())
                .get();

        return response.extract()
                .jsonPath()
                .getDouble("find { it.id == " + accountId + " }.balance");
    }
}
