/**
 * JINGLE
 *
 * Copyright (C) 2004-2024 Peter C. Tribble
 *
 * You may contact the author by email: peter.tribble@gmail.com
 */

package uk.co.petertribble.jingle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A collection of miscellaneous graphical functions used by applications
 *
 * @author Peter Tribble
 * @version 1.1
 *
 */
public class JingleUtils {

    /**
     * Locate a window centrally on the screen
     *
     * @param w The Window to be positioned
     */
    public static void Center(Window w) {
	Dimension d = w.getToolkit().getScreenSize();
	w.setLocation(((int)d.getWidth()-w.getWidth())/2,
		((int)d.getHeight()-w.getHeight())/2);
    }

    /**
     * Gives a menubar that just does file-exit
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
     * Gives a menubar that just does file-close
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
     * Creates an informational popup.
     */
    public static void infoPopup(JFrame f, String msg) {
	JOptionPane.showMessageDialog(f, msg,
			JingleResources.getString("POPUP.MESSAGE.TEXT"),
			JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Creates a warning popup.
     */
    public static void warningPopup(JFrame f, String msg) {
	JOptionPane.showMessageDialog(f, msg,
			JingleResources.getString("POPUP.WARNING.TEXT"),
			JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Creates an error popup.
     */
    public static void errorPopup(JFrame f, String msg) {
	JOptionPane.showMessageDialog(f, msg,
			JingleResources.getString("POPUP.ERROR.TEXT"),
			JOptionPane.ERROR_MESSAGE);
    }

}
