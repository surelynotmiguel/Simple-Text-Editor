package GUI;

import DTO.FileHandler;
import GUI.Dialog.AboutDialog;
import GUI.Dialog.HelpDialog;
import GUI.Dialog.PreferencesDialog;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.util.HashMap;
import java.util.List;

public class ApplicationFrame extends JFrame implements ActionListener {
    @Serial
    private static final long serialVersionUID = 1L;

    private JTextArea textArea;
    private JPanel contentPanel;
    private JMenuItem createItem;
    private JMenuItem openItem;
    private JMenuItem saveItem;
    private JMenuItem preferencesItem;
    private JMenuItem aboutItem;
    private JMenuItem helpItem;
    private JMenuItem exitItem;
    private File currentFile;
    private FileDialog dialog;
    private BufferedImage icon;
    private static ApplicationFrame instance;

    public ApplicationFrame(){
        super(Global.getNameVersion());
        instance = this;
        configureFrame();
        createAndAddMenuBar();
        addListenersMenu(this);
        addComponents();
    }

    public void start() {
        this.setVisible(true);
    }

    public static ApplicationFrame getInstance() {
        return instance;
    }

    public void applyTabSizeChanges(int tabSize) { this.textArea.setTabSize(tabSize); }

    private void configureFrame(){
        this.setTitle(Global.getNameVersion());
        this.setSize(800, 600);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        this.setBackground(Color.white);

        try {
            this.icon = ImageIO.read(new File(Global.AUTHOR_ICON_PATH));
            setIconImage(this.icon);
        } catch (IOException e) {
            Global.printErrorAndFinish("An error occurred while loading the icon.", e);
        }

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                verifyPresenceOfUnsavedText(0);
            }
        });
    }

    private void verifyPresenceOfUnsavedText(int smirtirlaine) {
        String[] windowTitle = {"Exit", "Warning"};
        String[] windowMessage = {"Do you want to save the file before exiting?", "Do you want to save the current file?"};

        if((textArea.getText().isEmpty() || textArea.getText().isBlank()) || List.of(textArea.getText().split("\n")).equals(FileHandler.readFromFile(currentFile))) {
            int option = JOptionPane.showConfirmDialog(ApplicationFrame.this, "Are you sure?", windowTitle[smirtirlaine], JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.YES_OPTION){
                if(smirtirlaine == 0){
                    System.exit(0);
                }
            }
        } else{
            int option = JOptionPane.showConfirmDialog(ApplicationFrame.this, windowMessage[smirtirlaine], windowTitle[smirtirlaine], JOptionPane.YES_NO_CANCEL_OPTION);
            if(option == JOptionPane.YES_OPTION){
                dialog = new FileDialog((Frame) null, "Save As", FileDialog.SAVE);

                dialog.setVisible(true);

                String directory = dialog.getDirectory();
                String filename = dialog.getFile();

                String filePath = directory + filename;
                FileHandler.writeToFile(filePath, List.of(textArea.getText().split("\n")), false);
                if(smirtirlaine == 0){
                    System.exit(0);
                }
            } else if(option == JOptionPane.NO_OPTION){
                if(smirtirlaine == 0){
                    System.exit(0);
                }
            }
        }
    }

    private void createAndAddMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic('E');
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');

        createItem = new JMenuItem("New");
        createItem.setMnemonic('N');
        openItem = new JMenuItem("Open");
        openItem.setMnemonic('O');
        saveItem = new JMenuItem("Save");
        saveItem.setMnemonic('S');
        preferencesItem = new JMenuItem("Preferences");
        preferencesItem.setMnemonic('P');
        aboutItem = new JMenuItem("About");
        aboutItem.setMnemonic('A');
        helpItem =  new JMenuItem("Help");
        helpItem.setMnemonic('H');
        exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic('E');

        createItem.addActionListener(this);
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        preferencesItem.addActionListener(this);
        aboutItem.addActionListener(this);
        helpItem.addActionListener(this);
        exitItem.addActionListener(this);

        fileMenu.add(createItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
        editMenu.add(preferencesItem);
        helpMenu.add(helpItem);
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        menuBar.setBackground(Color.LIGHT_GRAY);

        this.setJMenuBar(menuBar);
    }

    private void addComponents() {
        addContentPanel();
        addTextArea();
    }

    private void addContentPanel() {
        contentPanel = new JPanel(new BorderLayout());

        JPanel leftSpace = new JPanel();
        leftSpace.setPreferredSize(new Dimension(300, 0));

        contentPanel.add(leftSpace, BorderLayout.WEST);
        this.add(contentPanel, BorderLayout.CENTER);
    }

    private void addTextArea() {
        JPanel textPanel = new JPanel(new BorderLayout());
        textArea = new JTextArea();
        textArea.setFont(Global.FONT);
        int tabCharSize = 2;
        textArea.setTabSize(tabCharSize);

        JPanel spaceTop = new JPanel();
        spaceTop.setPreferredSize(new Dimension(Integer.MAX_VALUE, 0));
        textPanel.add(spaceTop, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(textArea);
        textPanel.add(scrollPane, BorderLayout.CENTER);
        textArea.setBorder(BorderFactory.createCompoundBorder(null, BorderFactory.createEmptyBorder(5, 10, 10, 10)));
        scrollPane.setRowHeaderView(new TextLineNumerator(textArea));

        contentPanel.add(textPanel, BorderLayout.CENTER);
    }

    public void addFileArea(){
        //TODO: Add file area
    }

    private void addListenersMenu(ActionListener listener) {
        for(Component menu : this.getJMenuBar().getComponents()) {
            if(menu instanceof JMenu) {
                addListenersItemMenu(listener, (JMenu) menu);
            }
        }
    }

    private void addListenersItemMenu(ActionListener listener, JMenu menu) {
        for (Component item : menu.getMenuComponents()) {
            if(item instanceof JMenuItem) {
                ((JMenuItem) item).addActionListener(listener);
            }
        }
    }

    public void setDialogIcon(FileDialog dialog){ dialog.setIconImage(this.icon); }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == createItem){
            dialog = new FileDialog((Frame) null, "Save As", FileDialog.SAVE);

            setDialogIcon(dialog);
            dialog.setVisible(true);

            String directory = dialog.getDirectory();
            String filename = dialog.getFile();

            if (directory != null && filename != null) {
                String filePath = directory + filename;
                FileHandler.createFile(filePath);
                System.out.println("File saved on: " + filePath);
            } else {
                System.out.println("No directory selected.");
            }
        }
        if(e.getSource() == openItem){
            dialog = new FileDialog((Frame) null, "Open File", FileDialog.LOAD);

            setDialogIcon(dialog);
            dialog.setVisible(true);

            if(dialog.getFile() != null){
                File file = new File(dialog.getDirectory() + dialog.getFile());
                this.currentFile = file;

                if(!textArea.getText().isEmpty() && !textArea.getText().isBlank()){
                    verifyPresenceOfUnsavedText(1);
                    textArea.setText("");
                }

                for(String line : FileHandler.readFromFile(file)){
                    textArea.append(line + "\n");
                }
            }
        }
        if(e.getSource() == saveItem || e.getSource() == KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK)){
            if(currentFile != null){
                String directory = currentFile.getParent() + "\\";
                String filename = currentFile.getName();

                String filePath = directory + filename;

                if(List.of(textArea.getText().split("\n")).equals(FileHandler.readFromFile(currentFile))){
                    System.out.println("No changes were made to the file.");
                } else{
                    FileHandler.writeToFile(filePath, List.of(textArea.getText().split("\n")), false);
                    System.out.println("File saved on: " + filePath);
                }
            } else{
                dialog = new FileDialog((Frame) null, "Save As", FileDialog.SAVE);

                setDialogIcon(dialog);
                dialog.setVisible(true);

                String directory = dialog.getDirectory();
                String filename = dialog.getFile();

                String filePath = directory + filename;
                FileHandler.createFile(filePath, List.of(textArea.getText().split("\n")), true);
            }
        }
        if(e.getSource() == preferencesItem){
            (new PreferencesDialog(ApplicationFrame.this)).setVisible(true);
        }
        if(e.getSource() == aboutItem){
            (new AboutDialog(ApplicationFrame.this)).setVisible(true);
        }
        if(e.getSource() == helpItem){
            (new HelpDialog(ApplicationFrame.this)).setVisible(true);
        }
        if(e.getSource() == exitItem){
            verifyPresenceOfUnsavedText(0);
        }
    }

    /**
     *  This class will display line numbers for a related text component. The text
     *  component must use the same line height for each line. TextLineNumber
     *  supports wrapped lines and will highlight the line number of the current
     *  line in the text component.
     * <p>
     *  This class was designed to be used as a component added to the row header
     *  of a JScrollPane.
     *
     * @author tips4java
     * @see <a href="https://github.com/tips4java/tips4java/blob/main/source/TextLineNumber.java">TextLineNumber</a>
     */
    @SuppressWarnings({"deprecation", "unused"})
    public static class TextLineNumerator extends JPanel implements CaretListener, DocumentListener, PropertyChangeListener {
        public final static float LEFT = 0.0f;
        public final static float CENTER = 0.5f;
        public final static float RIGHT = 1.0f;

        private final static Border OUTER = new MatteBorder(0, 0, 0, 2, Color.GRAY);

        private final static int HEIGHT = Integer.MAX_VALUE - 1000000;

        //  Text component this TextTextLineNumber component is in sync with

        private final JTextComponent component;

        //  Properties that can be changed

        private boolean updateFont;
        private int borderGap;
        private Color currentLineForeground;
        private float digitAlignment;
        private int minimumDisplayDigits;

        //  Keep history information to reduce the number of times the component
        //  needs to be repainted

        private int lastDigits;
        private int lastHeight;
        private int lastLine;

        private HashMap<String, FontMetrics> fonts;

        /**
         *	Create a line number component for a text component. This minimum
         *  display width will be based on 3 digits.
         *
         *  @param component  the related text component
         */
        public TextLineNumerator(JTextComponent component)
        {
            this(component, 3);
        }

        /**
         *	Create a line number component for a text component.
         *
         *  @param component  the related text component
         *  @param minimumDisplayDigits  the number of digits used to calculate
         *                               the minimum width of the component
         */
        public TextLineNumerator(JTextComponent component, int minimumDisplayDigits)
        {
            this.component = component;

            setFont(component.getFont().deriveFont(Font.BOLD));

            setBorderGap(5);
            //setCurrentLineForeground( Color.RED );
            setDigitAlignment( RIGHT );
            setMinimumDisplayDigits( minimumDisplayDigits );

            component.getDocument().addDocumentListener(this);
            component.addCaretListener(this);
            component.addPropertyChangeListener("font", this);
        }

        /**
         *  Gets the update font property
         *
         *  @return the update font property
         */
        public boolean getUpdateFont()
        {
            return updateFont;
        }

        /**
         *  Set the update font property. Indicates whether this Font should be
         *  updated automatically when the Font of the related text component
         *  is changed.
         *
         *  @param updateFont  when true update the Font and repaint the line
         *                     numbers, otherwise just repaint the line numbers.
         */
        public void setUpdateFont(boolean updateFont)
        {
            this.updateFont = updateFont;
        }

        /**
         *  Gets the border gap
         *
         *  @return the border gap in pixels
         */
        public int getBorderGap()
        {
            return borderGap;
        }

        /**
         *  The border gap is used in calculating the left and right insets of the
         *  border. Default value is 5.
         *
         *  @param borderGap  the gap in pixels
         */
        public void setBorderGap(int borderGap)
        {
            this.borderGap = borderGap;
            Border inner = new EmptyBorder(0, borderGap, 0, borderGap);
            setBorder( new CompoundBorder(OUTER, inner) );
            lastDigits = 0;
            setPreferredWidth();
        }

        /**
         *  Gets the current line rendering Color
         *
         *  @return the Color used to render the current line number
         */
        public Color getCurrentLineForeground()
        {
            return currentLineForeground == null ? getForeground() : currentLineForeground;
        }

        /**
         *  The Color used to render the current line digits. Default is Color.RED.
         *
         *  @param currentLineForeground  the Color used to render the current line
         */
        public void setCurrentLineForeground(Color currentLineForeground)
        {
            this.currentLineForeground = currentLineForeground;
        }

        /**
         *  Gets the digit alignment
         *
         *  @return the alignment of the painted digits
         */
        public float getDigitAlignment()
        {
            return digitAlignment;
        }

        /**
         *  Specify the horizontal alignment of the digits within the component.
         *  Common values would be:
         *  <ul>
         *  <li>TextLineNumber.LEFT
         *  <li>TextLineNumber.CENTER
         *  <li>TextLineNumber.RIGHT (default)
         *	</ul>
         *  @param digitAlignment the Color used to render the current line
         */
        public void setDigitAlignment(float digitAlignment)
        {
            this.digitAlignment = digitAlignment > 1.0f ? 1.0f : digitAlignment < 0.0f ? -1.0f : digitAlignment;
        }

        /**
         *  Gets the minimum display digits
         *
         *  @return the minimum display digits
         */
        public int getMinimumDisplayDigits()
        {
            return minimumDisplayDigits;
        }

        /**
         *  Specify the minimum number of digits used to calculate the preferred
         *  width of the component. Default is 3.
         *
         *  @param minimumDisplayDigits  the number digits used in the preferred
         *                               width calculation
         */
        public void setMinimumDisplayDigits(int minimumDisplayDigits)
        {
            this.minimumDisplayDigits = minimumDisplayDigits;
            setPreferredWidth();
        }

        /**
         *  Calculate the width needed to display the maximum line number
         */
        private void setPreferredWidth()
        {
            Element root = component.getDocument().getDefaultRootElement();
            int lines = root.getElementCount();
            int digits = Math.max(String.valueOf(lines).length(), minimumDisplayDigits);

            //  Update sizes when number of digits in the line number changes

            if (lastDigits != digits)
            {
                lastDigits = digits;
                FontMetrics fontMetrics = getFontMetrics( getFont() );
                int width = fontMetrics.charWidth( '0' ) * digits;
                Insets insets = getInsets();
                int preferredWidth = insets.left + insets.right + width;

                Dimension d = getPreferredSize();
                d.setSize(preferredWidth, HEIGHT);
                setPreferredSize( d );
                setSize( d );
            }
        }

        /**
         *  Draw the line numbers
         */
        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);

            //	Determine the width of the space available to draw the line number

            FontMetrics fontMetrics = component.getFontMetrics( component.getFont() );
            Insets insets = getInsets();
            int availableWidth = getSize().width - insets.left - insets.right;

            //  Determine the rows to draw within the clipped bounds.

            Rectangle clip = g.getClipBounds();
            int rowStartOffset = component.viewToModel2D(new Point(0, clip.y));
            int endOffset = component.viewToModel2D(new Point(0, clip.y + clip.height));

            while (rowStartOffset <= endOffset)
            {
                try
                {
                    if (isCurrentLine(rowStartOffset))
                        g.setColor( getCurrentLineForeground() );
                    else
                        g.setColor( getForeground() );

                    //  Get the line number as a string and then determine the
                    //  "X" and "Y" offsets for drawing the string.

                    String lineNumber = getTextLineNumber(rowStartOffset);
                    int stringWidth = fontMetrics.stringWidth( lineNumber );
                    int x = getOffsetX(availableWidth, stringWidth) + insets.left;
                    int y = getOffsetY(rowStartOffset, fontMetrics);
                    g.drawString(lineNumber, x, y);

                    //  Move to the next row

                    rowStartOffset = Utilities.getRowEnd(component, rowStartOffset) + 1;
                }
                catch(Exception e) {break;}
            }
        }

        /*
         *  We need to know if the caret is currently positioned on the line we
         *  are about to paint so the line number can be highlighted.
         */
        private boolean isCurrentLine(int rowStartOffset)
        {
            int caretPosition = component.getCaretPosition();
            Element root = component.getDocument().getDefaultRootElement();

            return root.getElementIndex(rowStartOffset) == root.getElementIndex(caretPosition);
        }

        /*
         *	Get the line number to be drawn. The empty string will be returned
         *  when a line of text has wrapped.
         */
        protected String getTextLineNumber(int rowStartOffset)
        {
            Element root = component.getDocument().getDefaultRootElement();
            int index = root.getElementIndex( rowStartOffset );
            Element line = root.getElement( index );

            if (line.getStartOffset() == rowStartOffset)
                return String.valueOf(index + 1);
            else
                return "";
        }

        /*
         *  Determine the X offset to properly align the line number when drawn
         */
        private int getOffsetX(int availableWidth, int stringWidth)
        {
            return (int)((availableWidth - stringWidth) * digitAlignment);
        }

        /*
         *  Determine the Y offset for the current row
         */
        private int getOffsetY(int rowStartOffset, FontMetrics fontMetrics)
                throws BadLocationException
        {
            //  Get the bounding rectangle of the row

            Rectangle r = component.modelToView(rowStartOffset);
            int lineHeight = fontMetrics.getHeight();
            int y = r.y + r.height;
            int descent = 0;

            //  The text needs to be positioned above the bottom of the bounding
            //  rectangle based on the descent of the font(s) contained on the row.

            if (r.height == lineHeight)  // default font is being used
            {
                descent = fontMetrics.getDescent();
            }
            else  // We need to check all the attributes for font changes
            {
                if (fonts == null)
                    fonts = new HashMap<>();

                Element root = component.getDocument().getDefaultRootElement();
                int index = root.getElementIndex( rowStartOffset );
                Element line = root.getElement( index );

                for (int i = 0; i < line.getElementCount(); i++)
                {
                    Element child = line.getElement(i);
                    AttributeSet as = child.getAttributes();
                    String fontFamily = (String)as.getAttribute(StyleConstants.FontFamily);
                    Integer fontSize = (Integer)as.getAttribute(StyleConstants.FontSize);
                    String key = fontFamily + fontSize;

                    FontMetrics fm = fonts.get( key );

                    if (fm == null)
                    {
                        Font font = new Font(fontFamily, Font.PLAIN, fontSize);
                        fm = component.getFontMetrics( font );
                        fonts.put(key, fm);
                    }

                    descent = Math.max(descent, fm.getDescent());
                }
            }

            return y - descent;
        }

        //
//  Implement CaretListener interface
//
        @Override
        public void caretUpdate(CaretEvent e)
        {
            //  Get the line the caret is positioned on

            int caretPosition = component.getCaretPosition();
            Element root = component.getDocument().getDefaultRootElement();
            int currentLine = root.getElementIndex( caretPosition );

            //  Need to repaint so the correct line number can be highlighted

            if (lastLine != currentLine)
            {
//			repaint();
                getParent().repaint();
                lastLine = currentLine;
            }
        }

        //
//  Implement DocumentListener interface
//
        @Override
        public void changedUpdate(DocumentEvent e)
        {
            documentChanged();
        }

        @Override
        public void insertUpdate(DocumentEvent e)
        {
            documentChanged();
        }

        @Override
        public void removeUpdate(DocumentEvent e)
        {
            documentChanged();
        }

        /*
         *  A document change may affect the number of displayed lines of text.
         *  Therefore, the lines numbers will also change.
         */
        private void documentChanged()
        {
            //  View of the component has not been updated at the time
            //  the DocumentEvent is fired

            SwingUtilities.invokeLater(() -> {
                try
                {
                    int endPos = component.getDocument().getLength();
                    Rectangle rect = component.modelToView(endPos);

                    if (rect != null && rect.y != lastHeight)
                    {
                        setPreferredWidth();
//						repaint();
                        getParent().repaint();
                        lastHeight = rect.y;
                    }
                }
                catch (BadLocationException ex) { /* nothing to do */ }
            });
        }

        //
//  Implement PropertyChangeListener interface
//
        @Override
        public void propertyChange(PropertyChangeEvent evt)
        {
            if (evt.getNewValue() instanceof Font)
            {
                if (updateFont)
                {
                    Font newFont = (Font) evt.getNewValue();
                    setFont(newFont);
                    lastDigits = 0;
                    setPreferredWidth();
                }
                else
                {
//				repaint();
                    getParent().repaint();
                }
            }
        }
    }
}