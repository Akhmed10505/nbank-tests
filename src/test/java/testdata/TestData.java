package testdata;

public final class TestData {
    private TestData() {
    }


    public static final long FIRST_ACCOUNT_ID = 1L;
    public static final long SECOND_ACCOUNT_ID = 2L;
    public static final long INVALID_ACCOUNT_ID = 999L;


    // Общая минимальная сумма депозита и перевода
    public static final double MIN_AMOUNT = 0.01;
    public static final double AMOUNT_BELOW_MINIMUM = 0.001;
    public static final double AMOUNT_ABOVE_MINIMUM = 0.02;

    // Границы депозита
    public static final double MAX_DEPOSIT_AMOUNT = 5_000.00;
    public static final double DEPOSIT_BELOW_MAXIMUM = 4_999.99;
    public static final double DEPOSIT_ABOVE_MAXIMUM = 5_000.01;

    // Границы перевода
    public static final double MAX_TRANSFER_AMOUNT = 10_000.00;
    public static final double TRANSFER_BELOW_MAXIMUM = 9_999.99;
    public static final double TRANSFER_ABOVE_MAXIMUM = 10_000.01;
}
