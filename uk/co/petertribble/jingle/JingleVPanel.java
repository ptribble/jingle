/*
 * JINGLE
 *
 * Copyright (C) 2004-2007  Peter C. Tribble
 *
 * You may contact the author by email: peter.tribble@gmail.com
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

    public Dimension getPreferredScrollableViewportSize() {
	return getPreferredSize();
    }

    public int getScrollableBlockIncrement(Rectangle r, int o, int d) {
	return r.height;
    }

    public int getScrollableUnitIncrement(Rectangle r, int o, int d) {
	return 24;
    }

    public boolean getScrollableTracksViewportWidth() {
	return true;
    }

    public boolean getScrollableTracksViewportHeight() {
	return false;
    }

}
