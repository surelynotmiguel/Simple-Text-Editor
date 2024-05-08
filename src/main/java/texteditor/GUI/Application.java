package texteditor.GUI;

import javax.swing.*;
import java.awt.*;

public class Application {
    /**
     * The main method that initiates the chat application.
     *
     * @param args Command-line arguments (not used in this context)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                ApplicationFrame applicationFrame = new ApplicationFrame();
                applicationFrame.start();
            } catch (HeadlessException e) {
                String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
                Global.printErrorAndFinish("Program terminated by a HeadlessException in the " + methodName + "() method", e);
            } catch (Exception e) {
                String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
                Global.printErrorAndFinish("Program terminated by a generic Exception in the " + methodName + "() method", e);
            }
        });
    }
}