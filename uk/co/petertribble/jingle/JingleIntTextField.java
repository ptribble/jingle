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
 * Copyright (C) 2004-2024 Peter C. Tribble
 */

package uk.co.petertribble.jingle;

import javax.swing.*;
import javax.swing.text.*;

/**
 * A JTextField that will accept only integers.
 */
public class JingleIntTextField extends JTextField {

    private static final long serialVersionUID = 1L;

    public JingleIntTextField(int cols) {
	super(cols);
    }

    public JingleIntTextField(int value, int cols) {
	super(Integer.toString(value), cols);
    }

    @Override
    protected Document createDefaultModel() {
	return new IntegerDocument();
    }

    static class IntegerDocument extends PlainDocument {
	private static final long serialVersionUID = 1L;

	@Override
	public void insertString(int offs, String str, AttributeSet a)
            throws BadLocationException {
		if (str == null) {
		    return;
		}
		try {
		    Integer.parseInt(str);
		    super.insertString(offs, str, a);
		} catch (NumberFormatException nfe) {}
	}
    }

    public int getInt() {
	return Integer.parseInt(getText());
    }
}
