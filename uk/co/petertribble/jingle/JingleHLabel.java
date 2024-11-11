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

import javax.swing.JLabel;
import java.awt.Color;

/**
 * A html label with a tooltip.
 *
 * @author Peter C. Tribble (peter.tribble@gmail.com)
 */
public class JingleHLabel extends JLabel {

    private static final long serialVersionUID = 1L;

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
	    StringBuilder sb = new StringBuilder(32);
	    sb.append("<html>");
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
