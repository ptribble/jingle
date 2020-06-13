/*
 * JINGLE
 *
 * Copyright (C) 2005-2007 Peter C. Tribble
 *
 * You may contact the author by email: peter.tribble@gmail.com
 */

package uk.co.petertribble.jingle;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.*;
import uk.co.petertribble.jumble.JumbleFile;

/**
 * A popup window suitable for about or help windows. The window displays
 * simple text, and has a button to close the window.
 */
public class JingleInfoFrame extends JFrame implements ActionListener {

    private JButton closeButton;

    /**
     * Creates a window displaying the contents of the given <code>File</code>.
     *
     * @param f The <code>File</code> whose contents are to be displayed. The
     * file contents should be text.
     */
    public JingleInfoFrame(File f) {
	this(JumbleFile.getStringContents(f), "text/plain");
    }

    /**
     * Creates a window displaying the contents of the given
     * <code>String</code>.
     *
     * @param s The <code>String</code> to be displayed.
     */
    public JingleInfoFrame(String s) {
	this(s, "text/plain");
    }

    /**
     * Creates a window displaying the contents of the given <code>File</code>,
     * interpreted as the given mime type.
     *
     * @param f The <code>File</code> whose contents are to be displayed. The
     * file contents should be text.
     * @param type The mime type the contents should be displayed as.
     */
    public JingleInfoFrame(File f, String type) {
	this(JumbleFile.getStringContents(f), type);
    }

    /**
     * Creates a window displaying the contents of the given
     * <code>String</code>, interpreted as the given mime type.
     *
     * @param s The <code>String</code> to be displayed.
     * @param type The mime type the contents should be displayed as.
     */
    public JingleInfoFrame(String s, String type) {
	makeWindow(s, type);
    }

    /**
     * Creates a window displaying text from a named resource.
     *
     * @param cl The ClassLoader to search for the resource.
     * @param name The name of the resource to display.
     * @param type The mime type the contents should be displayed as.
     */
    public JingleInfoFrame(ClassLoader cl, String name, String type) {
	String text = "";
	InputStream is = null;
	try {
	    is = cl.getResourceAsStream(name);
	    BufferedReader br = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	    String line;
	    while ((line = br.readLine()) != null) {
		sb.append(line).append("\n");
	    }
	    text = sb.toString();
	} catch (IOException ioe) {
	    text = JingleResources.getString("INFO.ERROR.TEXT");
	} finally {
	    if (is != null) {
		try {
		    is.close();
		} catch (IOException ioe2) { }
	    }
	}
	setTitle(name);
	makeWindow(text, type);
    }

    private void makeWindow(String text, String type) {
	JingleVPanel p = new JingleVPanel();
	p.setLayout(new BorderLayout());
	p.add(new JScrollPane(new JingleHPane(text, type)),
		BorderLayout.CENTER);
	JPanel buttonPanel = new JPanel();
	buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
	buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	buttonPanel.add(Box.createHorizontalGlue());
	closeButton = new JButton(JingleResources.getString("INFO.CLOSE.TEXT"));
	closeButton.addActionListener(this);
	buttonPanel.add(closeButton);
	p.add(buttonPanel, BorderLayout.SOUTH);
	getContentPane().add(p);
	setSize(600, 400);
	setVisible(true);
    }

    class winExit extends WindowAdapter {
	public void windowClosing(WindowEvent we) {
	    dispose();
	}
    }

    public void actionPerformed(ActionEvent e) {
	setVisible(false);
	dispose();
    }

}
