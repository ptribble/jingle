/*
 * JINGLE
 *
 * Copyright (C) 2004-2009 Peter C. Tribble
 *
 * You may contact the author by email: peter.tribble@gmail.com
 */

package uk.co.petertribble.jingle;

import javax.swing.*;
import javax.swing.text.*;

/**
 * A JTextField that will accept only integers.
 */
public class JingleIntTextField extends JTextField {

    public JingleIntTextField(int cols) {
	super(cols);
    }

    public JingleIntTextField(int value, int cols) {
	super(Integer.toString(value), cols);
    }

    protected Document createDefaultModel() {
	return new IntegerDocument();
    }

    static class IntegerDocument extends PlainDocument {

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
