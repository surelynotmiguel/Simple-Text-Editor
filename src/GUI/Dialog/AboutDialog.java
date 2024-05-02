package GUI.Dialog;

import GUI.Global;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class AboutDialog extends JDialog {
    public AboutDialog(JFrame parent) {
        super(parent, "About", true);
        initializeUI();
        setResizable(false);
    }

    private void initializeUI() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        addAuthor();

        add(Box.createVerticalStrut(10));

        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditable(false);
        editorPane.setOpaque(false);
        editorPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        editorPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        editorPane.setFont(Global.FONT);

        editorPane.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Desktop.getDesktop().browse(e.getURL().toURI());
                } catch (IOException | URISyntaxException ex) {
                    System.err.println("An error occurred while trying to open the link: " + e.getURL());
                }
            }
        });

        String htmlText = "<html> Description: " + Global.APP_DESCRIPTION + "<br><br>" +
                "Github: <a href=\"" + Global.AUTHOR_GITHUB + "\">" + Global.AUTHOR_GITHUB + "</a><br><br>" +
                "Last Modified: " + Global.LAST_MODIFIED_DATE + "</html>";
        editorPane.setText(htmlText);

        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane);

        pack();
        setLocationRelativeTo(null);
    }

    private void addAuthor() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));

        // Load the image
        ImageIcon image = new ImageIcon(Global.AUTHOR_ICON_PATH);
        JLabel imageLabel = new JLabel(new ImageIcon(image.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
        JLabel nameLabel = new JLabel("Made by: " + Global.APP_AUTHOR);
        nameLabel.setFont(Global.FONT.deriveFont(Font.BOLD));

        panel.add(imageLabel);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(nameLabel);
        add(panel);
    }
}
