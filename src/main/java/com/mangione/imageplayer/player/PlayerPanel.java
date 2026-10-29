package com.mangione.imageplayer.player;

import javax.swing.*;

public interface PlayerPanel {
	JComponent getPanel();

	int getDurationMillis();

	default boolean hasAudio() {
		return false;
	}

	default void setMuted(boolean muted) {
	}

	default void dispose() {
	}
}
