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
 * Copyright (C) 2004-2024  Peter C. Tribble
 */

package uk.co.petertribble.jingle;

import java.awt.Insets;
import javax.swing.JEditorPane;

/**
 * An uneditable JTextPane with a fixed border.
 *
 * @author Peter C. Tribble (peter.tribble@gmail.com)
 */
public class JingleHPane extends JEditorPane {

    private static final long serialVersionUID = 1L;

    /**
     * Create a new JingleHPane, which is a JEditorPane expecting text/html.
     *
     * @param text the String to display in the JingleHPane
     */
    public JingleHPane(String text) {
	this(text, "text/html");
    }

    /**
     * Create a new JingleHPane, which is a custom JEditorPane.
     *
     * @param text the String to display in the JingleHPane
     * @param type the mime type of the text
     */
    public JingleHPane(String text, String type) {
	super(type, text);
	setup();
    }

    private void setup() {
	setMargin(new Insets(5, 5, 5, 5));
	setCaretPosition(0);
	setEditable(false);
    }
}
