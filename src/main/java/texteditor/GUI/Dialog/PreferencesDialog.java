package texteditor.GUI.Dialog;

import texteditor.GUI.ApplicationFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PreferencesDialog extends JDialog{
    private final ApplicationFrame parentFrame;

    public PreferencesDialog(ApplicationFrame parent) {
        super(parent, "Preferences", true);
        this.parentFrame = parent;
        initializeUI();
        setResizable(false);
    }

    private void initializeUI() {
        JPanel contentPanel;
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(5, 10, 10, 10));

        JLabel tabSizeLabel = new JLabel("Tab Size:");
        JTextField tabSizeField = new JTextField(5);

        contentPanel.add(createPanel(tabSizeLabel, tabSizeField));
        add(contentPanel);

        contentPanel.add(Box.createVerticalStrut(10));

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(_ -> {
            String tabSizeFieldText = tabSizeField.getText();
            parentFrame.applyTabSizeChanges(Integer.parseInt(tabSizeFieldText));

            dispose();
        });
        contentPanel.add(saveButton);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(_ -> parentFrame.applyTabSizeChanges(2));
        contentPanel.add(resetButton);

        getContentPane().add(contentPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createPanel(JLabel label, JTextField field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Uses FlowLayout to align the components to the right
        panel.add(label);
        panel.add(field);
        return panel;
    }
}
