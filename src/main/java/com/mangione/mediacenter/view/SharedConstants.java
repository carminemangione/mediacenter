package com.mangione.mediacenter.view;

import java.awt.*;

public class SharedConstants {
    public final static Color DEFAULT_BACKGROUND_COLOR = Color.DARK_GRAY;
    public final static Color POPUP_BACKGROUND_COLOR =  new Color(100, 100, 100);
    public final static Color LIGHT_TEXT_COLOR = Color.LIGHT_GRAY;
    public final static Font BASE_FONT = new Font("ITC Garamond", Font.PLAIN, 12);
    public static final Font SYNOPSIS_FONT = SharedConstants.BASE_FONT.deriveFont(Font.PLAIN, 16);
    public static final Font GENRE_FONT = SharedConstants.BASE_FONT.deriveFont(Font.PLAIN, 20);
    public static final Font STATS_FONT = SharedConstants.BASE_FONT.deriveFont(Font.PLAIN, 36);

    public static final Dimension MOVIE_POSTER_SIZE = new Dimension(100, 150);


}
