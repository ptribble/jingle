/*
 * SPDX-License-Identifier: CDDL-1.0
 *
 * This file and its contents are supplied under the terms of the
 * Common Development and Distribution License ("CDDL"), version 1.0.
 * You may only use this file in accordance with the terms of version
 * 1.0 of the CDDL.
 *
 * A full copy of the text of the CDDL should have accompanied this
 * source. A copy of the CDDL is also available via the Internet at
 * http://www.illumos.org/license/CDDL.
 *
 * Copyright 2005-2026 Peter C. Tribble
 */

package uk.co.petertribble.jingle;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import uk.co.petertribble.jumble.JumbleFile;

/**
 * A popup window suitable for about or help windows. The window displays
 * simple text, and has a button to close the window.
 */
public final class JingleInfoFrame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a window displaying the contents of the given <code>File</code>.
     *
     * @param f The <code>File</code> whose contents are to be displayed. The
     * file contents should be text.
     */
    public JingleInfoFrame(final File f) {
	this(JumbleFile.getStringContents(f), "text/plain");
    }

    /**
     * Creates a window displaying the contents of the given
     * <code>String</code>.
     *
     * @param s The <code>String</code> to be displayed.
     */
    public JingleInfoFrame(final String s) {
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
    public JingleInfoFrame(final File f, final String type) {
	this(JumbleFile.getStringContents(f), type);
    }

    /**
     * Creates a window displaying the contents of the given
     * <code>String</code>, interpreted as the given mime type.
     *
     * @param s The <code>String</code> to be displayed.
     * @param type The mime type the contents should be displayed as.
     */
    public JingleInfoFrame(final String s, final String type) {
	makeWindow(s, type);
    }

    /**
     * Creates a window displaying text from a named resource.
     *
     * @param cl The ClassLoader to search for the resource.
     * @param name The name of the resource to display.
     * @param type The mime type the contents should be displayed as.
     */
    public JingleInfoFrame(final ClassLoader cl, final String name,
			   final String type) {
	String text;
	try (InputStream is = cl.getResourceAsStream(name);
	    BufferedReader br = new BufferedReader(
			new InputStreamReader(is, StandardCharsets.UTF_8))) {
	    StringBuilder sb = new StringBuilder();
	    String line;
	    while ((line = br.readLine()) != null) {
		sb.append(line).append('\n');
	    }
	    text = sb.toString();
	} catch (IOException ioe) {
	    text = JingleResources.getString("INFO.ERROR.TEXT");
	}
	setTitle(name);
	makeWindow(text, type);
    }

    private void makeWindow(final String text, final String type) {
	JingleVPanel p = new JingleVPanel();
	p.setLayout(new BorderLayout());
	p.add(new JScrollPane(new JingleHPane(text, type)),
		BorderLayout.CENTER);
	JPanel buttonPanel = new JPanel();
	buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
	buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	buttonPanel.add(Box.createHorizontalGlue());
	JButton closeButton
	    = new JButton(JingleResources.getString("INFO.CLOSE.TEXT"));
	closeButton.addActionListener(this);
	buttonPanel.add(closeButton);
	p.add(buttonPanel, BorderLayout.SOUTH);
	getContentPane().add(p);
	setSize(600, 400);
	setVisible(true);
    }

    class WindowExit extends WindowAdapter {
	@Override
	public void windowClosing(final WindowEvent we) {
	    dispose();
	}
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
	setVisible(false);
	dispose();
    }
}
