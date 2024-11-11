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
 * Copyright (C) 2005-2024 Peter C. Tribble
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
public final class JingleMultiFrame {

    private static Map<JFrame, JMenuItem> freg;

    static {
	freg = new HashMap<>();
    }

    private JingleMultiFrame() {
    }

    /**
     * Inserts the given <code>JFrame</code> and <code>JMenuItem</code> into
     * the registry.
     *
     * @param f the JFrame to add to the registry
     * @param jmi the JMenuItem used to exit the given JFrame
     */
    public static void register(JFrame f, JMenuItem jmi) {
	freg.put(f, jmi);
	setEnabled(freg.size() > 1);
    }

    /**
     * Unregister the given <code>JFrame</code>. If there are no remaining
     * <code>JFrame</code>s, exit the JVM. If there is only a single remaining
     * <code>JFrame</code>, disable its menu item.
     *
     * @param f the JFrame to remove from the registry
     */
    public static void unregister(JFrame f) {
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
