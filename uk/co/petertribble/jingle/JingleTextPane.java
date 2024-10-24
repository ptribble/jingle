/*
 * JINGLE
 *
 * Copyright (C) 2008-2024  Peter C. Tribble
 *
 * You may contact the author by email: peter.tribble@gmail.com
 *
 * JingleTextPane - a panel containing text
 */

package uk.co.petertribble.jingle;

import javax.swing.JEditorPane;
import java.awt.Insets;

/**
 * A Scrollable panel containing Text.
 *
 * @author Peter C. Tribble (peter.tribble@gmail.com)
 */
public class JingleTextPane extends JEditorPane {

    private static final long serialVersionUID = 1L;

    /**
     * Create a Scrollable panel containing Text.
     */
    public JingleTextPane() {
	this("text/html");
    }

    /**
     * Create a Scrollable panel containing Text.
     */
    public JingleTextPane(String contentType) {
	super();
	setContentType(contentType);
    }

    @Override
    public void setText(String s) {
	super.setText(s);
	setMargin(new Insets(5, 5, 5, 5));
	setCaretPosition(0);
	setEditable(false);
    }
}
