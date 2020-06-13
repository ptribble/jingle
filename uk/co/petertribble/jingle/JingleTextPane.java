/*
 * JINGLE
 *
 * Copyright (C) 2008  Peter C. Tribble
 *
 * You may contact the author by email: peter.tribble@gmail.com
 *
 * JingleTextPane - a panel containing text
 */

package uk.co.petertribble.jingle;

import javax.swing.JEditorPane;
import java.awt.Insets;

/**
 * A Scrollable panel containing Text
 *
 * @author Peter C. Tribble (peter.tribble@gmail.com)
 */
public class JingleTextPane extends JEditorPane {

    /**
     * Create a Scrollable panel containing Text
     */
    public JingleTextPane() {
	this("text/html");
    }

    /**
     * Create a Scrollable panel containing Text
     */
    public JingleTextPane(String content_type) {
	super();
	setContentType(content_type);
    }

    public void setText(String s) {
	super.setText(s);
	setMargin(new Insets(5, 5, 5, 5));
	setCaretPosition(0);
	setEditable(false);
    }
}
