/*
 * JINGLE
 *
 * Copyright (C) 2005-2008 Peter C. Tribble
 *
 * You may contact the author by email: peter.tribble@gmail.com
 */

package uk.co.petertribble.jingle;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import java.util.Map;
import java.util.HashMap;

/**
 * A Registry of JFrames. Allows an application to have multiple windows
 * open and for the user to close just the one window or exit the entire
 * application.
 *
 * @author Peter C. Tribble (peter.tribble@gmail.com)
 * @version 1.1
 */
public class JingleMultiFrame {

    private static Map <JFrame, JMenuItem> freg;

    static {
	freg = new HashMap <JFrame, JMenuItem> ();
    }

    /**
     * Inserts the given <code>JFrame</code> and <code>JMenuItem</code> into
     * the registry.
     */
    static public void register(JFrame f, JMenuItem jmi) {
	freg.put(f, jmi);
	setEnabled(freg.size() > 1);
    }

    /**
     * Unregister the given <code>JFrame</code>. If there are no remaining
     * <code>JFrame</code>s, exit the JVM. If there is only a single remaining
     * <code>JFrame</code>, disable its menu item.
     */
    static public void unregister(JFrame f) {
	f.dispose();
	freg.remove(f);
	if (freg.isEmpty()) {
	    System.exit(0);
	}
	setEnabled(freg.size() > 1);
    }

    private static void setEnabled(boolean b) {
	for (JMenuItem jmi : freg.values()) {
	    jmi.setEnabled(b);
	}
    }

}
