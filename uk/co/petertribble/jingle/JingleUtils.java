/**
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
 * Copyright (C) 2004-2025 Peter C. Tribble
 */

package uk.co.petertribble.jingle;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * A collection of miscellaneous graphical functions used by applications.
 *
 * @author Peter Tribble
 * @version 1.1
 *
 */
public final class JingleUtils {

    /**
     * The size of the margin around the text in a panel.
     */
    private static final int IMARGIN = 5;

    /**
     * A fixed Insets so that defInsets() doesn't create a new one every time.
     */
    private static final Insets DEFINSET =
	new Insets(IMARGIN, IMARGIN, IMARGIN, IMARGIN);

    private JingleUtils() {
    }

    /**
     * Locate a window centrally on the screen.
     *
     * @param w The Window to be positioned
     */
    public static void centerWindow(Window w) {
	Dimension d = w.getToolkit().getScreenSize();
	w.setLocation(((int) d.getWidth() - w.getWidth()) / 2,
		((int) d.getHeight() - w.getHeight()) / 2);
    }

    /**
     * Gives a menubar that just does file-exit.
     *
     * @return the populated JMenuBar
     */
    public static JMenuBar exitMenuBar() {
	JMenuBar menuPanel = new JMenuBar();
	final JMenu fileMenu = new JMenu(
		JingleResources.getString("FILE.TEXT"));
	final JMenuItem fileMenuExit = new JMenuItem(
		JingleResources.getString("FILE.EXIT.TEXT"), KeyEvent.VK_E);
	fileMenuExit.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		System.exit(0);
	    }
	});
	fileMenu.add(fileMenuExit);
	menuPanel.add(fileMenu);
	return menuPanel;
    }

    /**
     * Gives a menubar that just does file-close.
     *
     * @param f the JFrame that the JMenuBar will be attached to
     *
     * @return the populated JMenuBar
     */
    public static JMenuBar closeMenuBar(final JFrame f) {
	JMenuBar menuPanel = new JMenuBar();
	final JMenu fileMenu = new JMenu(
		JingleResources.getString("FILE.TEXT"));
	final JMenuItem fileMenuExit = new JMenuItem(
		JingleResources.getString("FILE.CLOSE.TEXT"), KeyEvent.VK_C);
	fileMenuExit.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		f.dispose();
	    }
	});
	fileMenu.add(fileMenuExit);
	menuPanel.add(fileMenu);
	return menuPanel;
    }

    /**
     * Creates an informational popup displaying a message.
     *
     * @param f the JFrame that the popup will be related to
     * @param msg the text of the message to be displayed
     */
    public static void infoPopup(JFrame f, String msg) {
	JOptionPane.showMessageDialog(f, msg,
			JingleResources.getString("POPUP.MESSAGE.TEXT"),
			JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Creates a warning popup displaying a message.
     *
     * @param f the JFrame that the popup will be related to
     * @param msg the text of the message to be displayed
     */
    public static void warningPopup(JFrame f, String msg) {
	JOptionPane.showMessageDialog(f, msg,
			JingleResources.getString("POPUP.WARNING.TEXT"),
			JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Creates an error popup displaying a message.
     *
     * @param f the JFrame that the popup will be related to
     * @param msg the text of the message to be displayed
     */
    public static void errorPopup(JFrame f, String msg) {
	JOptionPane.showMessageDialog(f, msg,
			JingleResources.getString("POPUP.ERROR.TEXT"),
			JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Create a fixed margin around the content of a panel.
     * The width of the margin is a fixed 5 pixels.
     *
     * @return an Insets with 5 pixels on each side
     */
    public static Insets defInsets() {
	return DEFINSET;
    }
}
