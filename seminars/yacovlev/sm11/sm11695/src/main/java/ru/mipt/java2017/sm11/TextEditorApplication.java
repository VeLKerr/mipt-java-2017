package ru.mipt.java2017.sm11;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.prefs.Preferences;

public class TextEditorApplication extends JFrame implements WindowListener {

    private JTextPane textPane = new JTextPane();
    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuFile = new JMenu("File");
    private Preferences preferences = Preferences.userNodeForPackage(TextEditorApplication.class);

    private Action fileNew = null;
    private Action fileExit = null;

    public TextEditorApplication() {
        super("Text editor");
        setLayout(new BorderLayout());
        add(textPane, BorderLayout.CENTER);
        add(menuBar, BorderLayout.NORTH);
        fileNew = new AbstractAction("New") {
            @Override
            public void actionPerformed(ActionEvent e) {
                textPane.setText("");
            }
        };
        fileExit = new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                preferences.putInt("w", getWidth());
                preferences.putInt("h", getHeight());
                preferences.putInt("x", getX());
                preferences.putInt("y", getY());
                preferences.put("text", textPane.getText());
                System.exit(0);
            }
        };
        setMinimumSize(new Dimension(400, 300));
        menuBar.add(menuFile);
        menuFile.add(fileNew);
        menuFile.addSeparator();
        menuFile.add(fileExit);
        int w = preferences.getInt("w", 400);
        int h = preferences.getInt("h", 300);
        int x = preferences.getInt("x", 0);
        int y = preferences.getInt("y", 0);
        setSize(w, h);
        setLocation(x, y);
        textPane.setText(preferences.get("text", "'"));
    }

    public static void main(String args[]) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        SwingUtilities.invokeLater(() -> {
            // This function placed to event queue
            new TextEditorApplication().setVisible(true);
        });
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        // WRONG!!!
//        preferences.putInt("w", getWidth());
//        preferences.putInt("h", getHeight());
//        preferences.putInt("x", getX());
//        preferences.putInt("y", getY());
//        preferences.put("text", textPane.getText());
//        System.exit(0);
        fileExit.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {
        System.gc();
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
