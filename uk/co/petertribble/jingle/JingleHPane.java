/*
 * JINGLE
 *
 * Copyright (C) 2004-2024  Peter C. Tribble
 *
 * You may contact the author by email: peter.tribble@gmail.com
 *
 * JingleHPane - a html JTextPane
 */

package uk.co.petertribble.jingle;

import java.awt.Insets;
import javax.swing.JTabbedPane;
import javax.swing.JEditorPane;
import java.net.URL;
import javax.swing.event.*;

/**
 * A html JTextPane.
 *
 * @author Peter C. Tribble (peter.tribble@gmail.com)
 */
public class JingleHPane extends JEditorPane implements HyperlinkListener {

    private static final long serialVersionUID = 1L;

    private JTabbedPane jtp;

    public JingleHPane(String text) {
	this(text, "text/html");
    }

    public JingleHPane(String text, String type) {
	super(type, text);
	setup();
    }

    public JingleHPane(URL url, JTabbedPane jtp) {
	super();
	this.jtp = jtp;
	try {
	    setPage(url);
	} catch (java.io.IOException ioe) {}
	setup();
	addHyperlinkListener(this);
    }

    private void setup() {
	setMargin(new Insets(5, 5, 5, 5));
	setCaretPosition(0);
	setEditable(false);
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent ev) {
	if (ev.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
	    String desc = ev.getDescription();
	    if ("./".equals(desc)) {
		desc = "index.html";
	    }
	    // System.out.println("Link: " + desc);
	    if (jtp.indexOfTab(desc) > -1) {
		jtp.setSelectedIndex(jtp.indexOfTab(desc));
	    }
	}
    }

}
