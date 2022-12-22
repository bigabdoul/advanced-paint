package edu.uopeople.cs1102;

import javafx.scene.paint.Color;

/**
 * A utility class to support color-specific operations.
 * @author https://github.com/bigabdoul.
 * @version 1.0
 */
public final class ColorUtil {
    
    /**
     * Private constructor for the 'static' class.
     */
    private ColorUtil() {
    }
    
    /**
     * The standard color palette.
     */
    public static final Color[] standardPalette = {
            Color.BLACK,
            Color.GRAY,
            
            Color.rgb(136, 0, 21), // Dark Red
            Color.RED, 
            
            Color.ORANGE,
            Color.rgb(255, 201, 14), // Gold
            
            Color.color(0.95, 0.9, 0), // Yellow
            Color.rgb(239, 228, 176), // Light Yellow

            Color.CORNSILK,
            Color.CORAL,
            
            Color.GREEN,
            Color.rgb(181, 230, 29), // Lime
           
            Color.rgb(0, 64, 64), // Darker Green
            Color.TEAL,
            
            Color.rgb(0, 162, 232), // Turquoise
            Color.rgb(153, 217, 234), // Light Turquoise
            
            Color.rgb(163, 73, 164), // Purple
            Color.rgb(200, 191, 231), // Lavender

            Color.CYAN, 
            Color.BLUE,
            
            Color.rgb(63, 72, 204), // Indigo
            Color.MAGENTA,

            Color.rgb(185, 99, 128), // Brown
            Color.rgb(255, 174, 201), // Rose
            
            Color.WHITE,
            Color.rgb(195, 195, 195), // Gray 25%
            
            Color.WHITE
    };

    /**
     * Converts the specified {@code Color} to its hexadecimal string representation.
     * @param c The {@code Color} object to convert.
     * @return A String representing the hexadecimal value of the specified color.
     */
    public static String toHexString(Color c) {
        return "#" + toHexString(c.getRed()) + toHexString(c.getGreen()) + toHexString(c.getBlue());
    }
    
    /**
     * Converts the specified {@code value} to its hexadecimal string representation.
     * @param value A double precision floating-point number between 0.0 and 1.0.
     * @return The hexadecimal string representation of the specified value.
     */
    public static String toHexString(double value) {
        String colorHex = Integer.toHexString((int)Math.round(255 * value));
        return (colorHex.length() == 1 ? "0" + colorHex : colorHex);
    }
    
    /**
     * Converts the specified hexadecimal string to its {@code Color} equivalent. 
     * @param s A hexadecimal string representing a color.
     * @return An initialized instance of {@code Color}.
     * @throws IllegalArgumentException The length of {@code s} (without the # symbol) is different from 6.
     */
    public static Color hexToColor(String s) throws IllegalArgumentException {
        // Expected format: s = "#00FF66"
        if (s.startsWith("#")) {
            s = s.substring(1); // remove the # symbol
        }
        if (s.length() != 6) {
            throw new IllegalArgumentException();
        }
        double r = Integer.valueOf(s.substring(0, 2), 16) / 255.0;
        double g = Integer.valueOf(s.substring(2, 4), 16) / 255.0;
        double b = Integer.valueOf(s.substring(4, 6), 16) / 255.0;
        return Color.color(r, g, b);
    }
}
