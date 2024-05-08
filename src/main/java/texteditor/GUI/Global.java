package texteditor.GUI;

import texteditor.DTO.FileHandler;

import java.awt.*;

public class Global {
    // Application information
    public static final String APP_NAME = "MIGS Text Editor";
    public static final String APP_VERSION = "1.0.0";
    public static final String APP_AUTHOR = "surelynotmiguel";
    public static final String AUTHOR_ICON_PATH = "rsrcs\\icons\\icon.jpeg";
    public static final String AUTHOR_GITHUB = "https://www.github.com/surelynotmiguel/";
    public static final String APP_DESCRIPTION = "A simple text editor made in Java.";
    public static final String LAST_MODIFIED_DATE = "May 02, 2024";

    // Default font values for program
    public static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 14);
    public static final Font DEFAULT_FONT_BOLD = new Font("Arial", Font.BOLD, 14);
    public static final Font DEFAULT_FONT_ITALIC = new Font("Arial", Font.ITALIC, 14);
    public static final Font DEFAULT_FONT_BOLD_ITALIC = new Font("Arial", Font.BOLD + Font.ITALIC, 14);

    // Default font values for text area
    public static final Font TEXTAREA_DEFAULT_FONT_BOLD = new Font("Consolas", Font.BOLD, 14);

    // Available font values - Not Yet Implemented
    public static final String[] FONT_NAMES = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    public static final String[] FONT_SIZES = {"8", "10", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48", "72"};
    public static final String[] FONT_STYLES = {"Plain", "Bold", "Italic", "Bold Italic"};

    // Menu items
    public static final String[] MENU_NAMES = {"File", "Edit", "Format", "Help"};
    public static final String[] FILE_MENU_ITEMS = {"New", "Open", "Open Folder", "Save", "Save As", "Exit"};
    public static final String[] EDIT_MENU_ITEMS = {"Cut", "Copy", "Paste", "Select All", "Preferences"};
    public static final String[] FORMAT_MENU_ITEMS = {"Font", "Font Size", "Font Style"};
    public static final String[] HELP_MENU_ITEMS = {"Help", "About"};

    // File explorer side tab
    public static final String FILE_EXPLORER_TITLE = "EXPLORER";
    public static final String FILE_EXPLORER_DEFAULT_PATH = FileHandler.getDefaultWorkSpaceFolderPath();

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
