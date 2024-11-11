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

import javax.swing.JPanel;
import javax.swing.Scrollable;
import java.awt.Dimension;
import java.awt.Rectangle;

/**
 * A JPanel that scrolls vertically but will squeeze horizontally.
 *
 * @author Peter C. Tribble (peter.tribble@gmail.com)
 * @version 1.0
 */
public class JingleVPanel extends JPanel implements Scrollable {

    private static final long serialVersionUID = 1L;

    @Override
    public Dimension getPreferredScrollableViewportSize() {
	return getPreferredSize();
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle r, int o, int d) {
	return r.height;
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle r, int o, int d) {
	return 24;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
	return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
	return false;
    }

}
