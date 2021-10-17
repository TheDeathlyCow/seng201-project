package nz.ac.gitlab.mwa172.seng201.group8.interfaces;

import java.util.regex.Pattern;

/**
 * Handles validation for a particular input.
 */
public interface InputValidator {
    /**
     * Determines if an input is valid.
     *
     * @param arg Input to validate.
     * @return Returns true if the input is valid, false otherwise.
     */
    boolean isValid(String arg);

    /**
     * Validator for player names that allows for unicode characters.
     */
    InputValidator UNICODE_NAME_VALIDATOR = (input) -> Pattern.matches("^(?!.*\\s\\s)[\\p{L}\\p{M}*][\\p{L}\\p{M}* ]{1,13}[\\p{L}\\p{M}*]$", input);

    /**
     * Validator for player names that allows for English characters only.
     */
    InputValidator ASCII_NAME_VALIDATOR = (input) -> Pattern.matches("^(?!.*\\s\\s)[a-zA-Z][a-zA-Z ]{1,13}[a-zA-Z]$", input);

    /**
     * Determines if a given input is numeric.
     *
     * @param input Input to test.
     * @return Returns true if the input only contains positive integers, false otherwise.
     */
    static boolean isNumeric(String input) {
        return Pattern.matches("^[0-9]+$", input);
    }
}
