package GUI.Dialog;

import GUI.Global;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HelpDialog extends JDialog{
    public HelpDialog(JFrame parent) {
        super(parent, "Help", true);
        initializeUI();
        setResizable(false);
    }

    private void initializeUI() {
        setLayout(new FlowLayout());
        JLabel label = new JLabel("<html>" + "Welcome to " + Global.getNameVersion() + "<br><br>" +
                "Want to create a new file? Go to:&nbsp;&nbsp;File ➜ New File" + "</html>");
        label.setBorder(new EmptyBorder(10, 20, 20, 20));
        add(label);
        pack();
        setLocationRelativeTo(null);
    }
}
