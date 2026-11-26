package generators;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomData {
    private RandomData() {
    }

    public static String getUsername() {
        // qa это префикс, я добавил в логин, который будет генериться, чтобы он выглядел как тестовый
        return "qa" + RandomStringUtils.randomAlphanumeric(8).toLowerCase();
    }

    public static String getPassword() {
        return RandomStringUtils.randomAlphabetic(3).toUpperCase()
                + RandomStringUtils.randomAlphabetic(5).toLowerCase()
                + RandomStringUtils.randomNumeric(3)
                + "$";
    }

    public static String getFullName() {
        return getNamePart() + " " + getNamePart();
    }

    private static String getNamePart() {
        return RandomStringUtils.randomAlphabetic(1).toUpperCase()
                + RandomStringUtils.randomAlphabetic(6).toLowerCase();
    }
}
