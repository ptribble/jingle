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
 * Copyright (C) 2008-2025  Peter C. Tribble
 */

package uk.co.petertribble.jingle;

import javax.swing.JEditorPane;

/**
 * A Scrollable panel containing Text.
 *
 * @author Peter C. Tribble (peter.tribble@gmail.com)
 */
public final class JingleTextPane extends JEditorPane {

    private static final long serialVersionUID = 1L;

    /**
     * Create a Scrollable panel containing Text, assumed to be of
     * type text/html.
     */
    public JingleTextPane() {
	this("text/html");
    }

    /**
     * Create a Scrollable panel containing Text.
     *
     * @param contentType the content type used to format the content
     */
    public JingleTextPane(String contentType) {
	super();
	setContentType(contentType);
    }

    @Override
    public void setText(String s) {
	super.setText(s);
	setMargin(JingleUtils.defInsets());
	setCaretPosition(0);
	setEditable(false);
    }
}
