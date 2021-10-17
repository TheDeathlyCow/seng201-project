package nz.ac.gitlab.mwa172.seng201.group8.interfaces.gui.startup;

import nz.ac.gitlab.mwa172.seng201.group8.file_manager.IslandTraderConfig;
import nz.ac.gitlab.mwa172.seng201.group8.interfaces.InputValidator;

import javax.swing.*;

/**
 * Validates the input of the create game screen.
 */
public class CreateGameValidator {

    /**
     * Error messages for when the player inputs a bad name.
     */
    private static final String[] NAME_ERROR_MESSAGES;
    /**
     * Error messages for when the player inputs a bad number of days.
     */
    private static final String[] DAYS_ERROR_MESSAGE;

    /**
     * The validator for names.
     */
    private static final InputValidator NAME_VALIDATOR;
    /**
     * The validator for days.
     */
    private static final InputValidator DAYS_VALIDATOR;

    static {
        NAME_ERROR_MESSAGES = new String[]{
                "Name must be 3-15 characters, letters and spaces only.",
                "Error Entering Name"
        };

        DAYS_ERROR_MESSAGE = new String[]{
                "Enter between 20 and 50 days (inclusive)",
                "Error Entering Days"
        };

        NAME_VALIDATOR = IslandTraderConfig.CONFIG.isUsingUnicode() ?
                InputValidator.ASCII_NAME_VALIDATOR : InputValidator.UNICODE_NAME_VALIDATOR;
        DAYS_VALIDATOR = (input) -> {
            if (!InputValidator.isNumeric(input))
                return false;
            int days = Integer.parseInt(input);
            return days >= 20 && days <= 50;
        };
    }

    /**
     * Validates the given name field and slider.
     *
     * @param nameField Name field to validate.
     * @param daysSlider Slider to validate.
     * @return Returns true if both fields are valid, false otherwise.
     */
    public static boolean isValid(JTextField nameField, JSlider daysSlider) {
        boolean isNameValid = validateField(nameField, NAME_VALIDATOR, NAME_ERROR_MESSAGES);
        boolean isDaysValid = true;
        return isNameValid && isDaysValid;
    }

    /**
     * Validates a particular field.
     *
     * @param field Field to validate.
     * @param validator The validator to use.
     * @param errorMessages Error messages to display if it cannot validate.
     * @return Returns true if the valid is valid, false otherwise.
     */
    private static boolean validateField(JTextField field, InputValidator validator, final String[] errorMessages) {
        assert errorMessages.length == 2;
        String text = field.getText();
        boolean isValid = validator.isValid(text);
        if (!isValid) {
            JOptionPane.showMessageDialog(null, errorMessages[0], errorMessages[1], JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

}

