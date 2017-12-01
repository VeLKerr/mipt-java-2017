package ru.mipt.java2017.sm11;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.prefs.Preferences;

public class TextEditor extends JFrame implements WindowListener {

    private JTextArea textArea = new JTextArea();
    private JMenuBar menuBar = new JMenuBar();
    private Action fileNew;
    private Action fileExit;
    private Preferences preferences = Preferences.userNodeForPackage(TextEditor.class);

    public TextEditor() {
        super("Text editor");
        setLayout(new BorderLayout());
        add(textArea, BorderLayout.CENTER);
        add(menuBar, BorderLayout.NORTH);
        fileNew = new AbstractAction("New") {
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
            }
        };
        fileExit = new AbstractAction("Exit") {
            public void actionPerformed(ActionEvent e) {
                savePreferences();
                System.exit(0);
            }
        };
        JMenu menuFile = new JMenu("File");
        menuFile.add(fileNew);
        menuFile.addSeparator();
        menuFile.add(fileExit);
        menuBar.add(menuFile);
        setMinimumSize(new Dimension(400, 300));
        addWindowListener(this);
        loadPreferences();
    }

    private void savePreferences() {
        preferences.put("text", textArea.getText());
        preferences.putInt("w", getWidth());
        preferences.putInt("h", getHeight());
        preferences.putInt("x", getX());
        preferences.putInt("y", getY());
    }

    private void loadPreferences() {
        int x = preferences.getInt("x", 0);
        int y = preferences.getInt("y", 0);
        int w = preferences.getInt("w", 400);
        int h = preferences.getInt("h", 300);
        String text = preferences.get("text", "");
        setSize(w, h);
        setLocation(x, y);
        textArea.setText(text);
    }

    public static void main(String args[]) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        // Wrong !!!
//        TextEditor editor = new TextEditor();
//        editor.setVisible(true);
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        SwingUtilities.invokeLater(() -> {
            TextEditor editor = new TextEditor();
            editor.setVisible(true);
        });
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
//        savePreferences();
//        System.exit(0);
        fileExit.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
