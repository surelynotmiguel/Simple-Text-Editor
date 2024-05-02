package GUI;

import java.awt.*;

public class Global {
    public static final String APP_NAME = "MIGS Text Editor";
    public static final String APP_VERSION = "1.0.0";
    public static final String APP_AUTHOR = "surelynotmiguel";
    public static final String AUTHOR_ICON_PATH = "resources\\icons\\icon.jpeg";
    public static final String AUTHOR_GITHUB = "https://www.github.com/surelynotmiguel/";
    public static final String APP_DESCRIPTION = "A simple text editor made in Java.";
    public static final String LAST_MODIFIED_DATE = "May 02, 2024";
    public static final Font FONT = new Font("Arial", Font.PLAIN, 14);

    /**
     * Returns the name and version of the software.
     *
     * @return The software name and version as a String
     */
    public static String getNameVersion() { return (APP_NAME + " - " + APP_VERSION); }

    /**
     * Prints the error message and terminates the program.
     *
     * @param message The error message to be displayed
     * @param e       The occurred exception
     */
    public static void printErrorAndFinish(String message, Exception e) {
        System.err.println("Error message:\t" + message);
        System.err.println("Exception text:\t" + e.getMessage());
        System.exit(1);
    }
}
