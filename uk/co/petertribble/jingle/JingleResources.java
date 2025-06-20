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
 * Copyright (C) 2024  Peter C. Tribble
 */

package uk.co.petertribble.jingle;

import java.util.ResourceBundle;

/**
 * Provide textual resources for Jingle classes.
 */
public final class JingleResources {

    private static final ResourceBundle JINGLERES =
	ResourceBundle.getBundle("properties/jingle");

    /*
     * This class cannot be instantiated.
     */
    private JingleResources() {
    }

    /**
     * Get the value of the resource for the given key.
     *
     * @param key the String naming the desired key
     *
     * @return a String containing the value for the given key
     */
    public static String getString(String key) {
	return JINGLERES.getString(key);
    }
}
