/*
 * JINGLE
 *
 * Copyright (C) 2004  Peter C. Tribble
 *
 * You may contact the author by email: peter.tribble@gmail.com
 *
 * JingleHLabel - a html label with a tooltip
 */

package uk.co.petertribble.jingle;

import javax.swing.JLabel;
import java.awt.Color;

/**
 * A html label with a tooltip.
 *
 * @author Peter C. Tribble (peter.tribble@gmail.com)
 */
public class JingleHLabel extends JLabel {

    public JingleHLabel(String text) {
	this(text, (String) null, (Color) null);
    }

    public JingleHLabel(String text, String tip) {
	this(text, tip, (Color) null);
    }

    public JingleHLabel(String text, Color bgcolor) {
	this(text, (String) null, bgcolor);
    }

    public JingleHLabel(String text, String tip, Color bgcolor) {
	super("<html>" + text + "</html>");
	if (bgcolor != null) {
	    setOpaque(true);
	    setBackground(bgcolor);
	}
	if (tip != null) {
	    int ibeg = 0;
	    int iend = 50;
	    int stop;
	    StringBuilder sb = new StringBuilder("<html>");
	    while ((stop = tip.indexOf(' ', iend)) > 0) {
		sb.append(tip.substring(ibeg, stop)).append("&nbsp;<BR>");
		iend = stop + 50;
		ibeg = stop;
	    }
	    sb.append(tip.substring(ibeg)).append("</html>");
	    setToolTipText(sb.toString());
	}
    }

}
