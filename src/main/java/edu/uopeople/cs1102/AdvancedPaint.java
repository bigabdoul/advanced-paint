package edu.uopeople.cs1102;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * This class allows drawing with a mouse on a canvas.
 * <p>
 * Instead of drawing fake "buttons" onto the canvas, this class uses real 
 * {@code Button} objects to generate a color palette outside the canvas.
 * </p>
 * 
 * @author https://github.com/bigabdoul.
 * @version 1.0
 */
public class AdvancedPaint extends MouseDraggedBase {
    private final int LINE_WIDTH_2 = 2;
    private final int LINE_WIDTH_3 = 3;

    private final Canvas canvas;
    private final GridPane grid;
    private final GraphicsContext g;

    // The currently selected drawing color, coded as an index into the above array
    private int currentColorNum = 0; 

    /*
     * Array of colors corresponding to available colors in the palette.
     * (The last color is a slightly darker version of yellow for
     * better visibility on a white background.)
     */
    private static final Color[] palette = ColorUtil.standardPalette;
    
    /**
     * Array of buttons to change the drawing color.
     */
    private final Button[] paletteButtons = new Button[palette.length];
    
    private boolean paletteButtonsCreated;
    
    // This style removes the rounded borders from the button 
    // and sets the same color for a button's text and background.
    private static final String BUTTON_STYLE_FORMAT = 
        "-fx-background-radius: 0; -fx-text-fill: %s; -fx-background-color: %s; -fx-border-width: 1px; -fx-border-color: gray;";
    
    private static final String BUTTON__HIGHLIGHT_STYLE_FORMAT = 
        "-fx-background-radius: 0; -fx-text-fill: %s; -fx-background-color: %s; -fx-border-width: 2px; -fx-border-color: white;";

    /**
     * Initializes a new instance of the {@code AdvancedPaint}
     * class using the specified {@code Canvas} object.
     * @param canvas The canvas on which to draw graphics.
     */
    public AdvancedPaint(Canvas canvas) {
        this(canvas, null);
    }
    
    /**
     * Initializes a new instance of the {@code AdvancedPaint} class
     * using the specified {@code Canvas} and {@code GridPane} objects.
     * @param canvas The canvas on which to draw graphics. Cannot be null.
     * @param grid A {@code GridPane} object. Can be null.
     */
    public AdvancedPaint(Canvas canvas, GridPane grid) {
        this.canvas = canvas;
        this.grid =grid;
        
        g = canvas.getGraphicsContext2D();
        
        /* Draw the canvas's content for the first time. */
        clearAndDrawPalette();
        
        /* Respond to mouse events on the canvas, by calling methods in this class. */
        canvas.setOnMousePressed(this::mousePressed);
        canvas.setOnMouseDragged(this::mouseDragged);
        canvas.setOnMouseReleased(this::mouseReleased);
    }
    
    /**
     * This is called when the user presses the mouse anywhere in the canvas.  
     * There are three possible responses, depending on where the user clicked:  
     * Change the current color, clear the drawing, or start drawing a curve.  
     * (Or do nothing if user clicks on the border.)
     */
    @Override
    protected boolean onMousePressed(double x, double y) {

        if (super.onMousePressed(x, y)) // Ignore mouse presses that occur
            return true; // when user is already drawing a curve.
                         // (This can happen if the user presses
                         // two mouse buttons at the same time.)
        
        // The user has clicked on the white drawing area.
        // Start drawing a curve from the point (x,y).
        prevX =  getCurrentX();
        prevY = getCurrentY();
        dragging = true;
        g.setLineWidth(LINE_WIDTH_2);  // Use a 2-pixel-wide line for drawing.
        g.setStroke( palette[currentColorNum] );
        
        return false;
    }

    /**
     * Called whenever the user moves the mouse while a mouse button is held down.  
     * If the user is drawing, draw a line segment from the previous mouse location 
     * to the current mouse location, and set up prevX and prevY for the next call.  
     * Note that in case the user drags outside of the drawing area, the values of
     * x and y are "clamped" to lie within this area.  This avoids drawing on the color 
     * palette or clear button.
     */
    @Override
    protected boolean onMouseDragged(double x, double y) {

        if (!super.onMouseDragged(x, y))
            return false;  // Nothing to do because the user isn't drawing.

        if (x < 3) // Adjust the value of x, to make sure it's in the drawing area.
            x = 3;
        
        if (x > canvas.getWidth() - 4)
            x = getCanvasWidth() - 4;

        if (y < 3) // Adjust the value of y, to make sure it's in the drawing area.
            y = 3;
        
        if (y > canvas.getHeight() - 4)   
            y = canvas.getHeight() - 4;

        g.strokeLine(prevX, prevY, x, y);  // Draw the line.

        prevX = x;  // Get ready for the next line segment in the curve.
        prevY = y;

        return true;
    }

    /**
     * Fills the canvas with white and draws the color palette and (simulated)
     * "Clear" button on the right edge of the canvas.  This method is called when
     * the canvas is created and when the user clicks "Clear."
     */
    public void clearAndDrawPalette() {
        clearCanvas();
        createPalette();
    }

    /**
     * Clears the drawing on the canvas.
     */
    public void clearCanvas() {
        int width = getCanvasWidth();    // Width of the canvas.
        int height = getCanvasHeight();  // Height of the canvas.

        g.setFill(Color.WHITE);
        g.fillRect(0,0,width,height);

        /* Draw a 3-pixel border around the canvas in gray.  This has to be
            done by drawing three rectangles of different sizes. */

        g.setStroke(Color.GRAY);
        g.setLineWidth(LINE_WIDTH_3);
        g.strokeRect(1.5, 1.5, width-LINE_WIDTH_3, height-LINE_WIDTH_3);
        
        currentColorNum = 0; // reset the stroke
    }

    /**
     * Gets (or creates and returns) the color palette buttons. 
     * @return An initialized array of {@code Button} objects.
     */
    public Button[] getPaletteButtons() {
        if (paletteButtonsCreated) {
            return paletteButtons;
        }
        
        createPalette();
        paletteButtonsCreated = true;
        return paletteButtons;
    }
    
    /**
     * Creates the color palette and wires up the appropriate event handlers.
     * @param buttons An initialized array of {@code Button} objects.
     * @param palette An initialized array of {@code Color} objects.
     */
    protected void createPalette() {
        Button btn = null;
        
        // create buttons
        for (int i = 0; i < paletteButtons.length; i++) {
            // convert each Color to its hexadecimal representation
            String colorHex = ColorUtil.toHexString(palette[i]);
            
            // Set the button's text to the index pointing to the 
            // Color in the 'palette' array.
            String colorIndexStr = Integer.toString(i);

            // We'll make sure that the color of the text matches 
            // the background color, resulting in an invisible text;
            // we'll use this text later to determine which color
            // is selected when the button is clicked.        	
            btn = new Button(colorIndexStr);
            
            // set the CSS style
            btn.setStyle(String.format(BUTTON_STYLE_FORMAT, colorHex, colorHex));
            
            // set width and height of the buttons
            btn.setPrefWidth(28);
            btn.setPrefHeight(28);
            
            // set the event handler for the button;
            // the event source is a Button
            btn.setOnMouseClicked(e -> changeColor((Button)e.getSource()));
            
            paletteButtons[i] = btn;
        }
        
        // set the text for the last created button
        btn.setText("CLEAR");
        
        // revert the button's text to black (otherwise, we wouldn't 
        // be able to read white text on a white background)
        unsetHightLight(btn);
    }
    
    /**
     * Changes the color used to draw on the canvas.
     * @param btn The button that triggered the action.
     */
    protected void changeColor(Button btn) {
        Button current = getCurrentButton();
        
        if (current == btn) {
            // do nothing if the color selection doesn't change
            // System.out.println("Same button clicked!");
            return;
        }
        
        // remove highlight from the previously clicked button
        unsetHightLight(current);
        
        // the button's text is the color, except for "CLEAR", which should clear the canvas
        String buttonText = btn.getText();
        
        if (buttonText.equals("CLEAR")) {
            clearCanvas();
        } else {
            // parse the color index
            currentColorNum = Integer.parseInt(buttonText);
        }
        
        // highlight the newly selected button
        // String color =
        setHighLight(btn);
        // System.out.println("Color changed to " + color);
    }

    /**
     * Gets the last-selected color.
     * @return A {@code Color} object, or null.
     */
    protected Color getCurrentColor() {
        if (currentColorNum > -1 && currentColorNum < paletteButtons.length)
            return palette[currentColorNum];
        else
            return null;
    }
    
    /**
     * Gets the last-selected button.
     * @return A {@code Button} object, or null.
     */
    protected Button getCurrentButton() {
        if (currentColorNum > -1 && currentColorNum < paletteButtons.length)
            return paletteButtons[currentColorNum];
        else
            return null;
    }
    
    /**
     * Checks whether the specified button refers to the "CLEAR" button.
     * @param btn The button object to compare against.
     * @return true if the specified button is the "CLEAR" button, otherwise, false.
     */
    protected boolean isClearButton(Button btn) {
        // "CLEAR" is the last button in the palette;
        // we could also compare the button's text to "CLEAR".
        return btn == paletteButtons[paletteButtons.length - 1];
    }

    /**
     * Highlights the specified button.
     * @param btn The button to highlight.
     * @return A string that represents the hexadecimal value of the stroke color.
     */
    protected String setHighLight(Button btn) {
        return setHighLightCore(btn, BUTTON__HIGHLIGHT_STYLE_FORMAT);
    }
    
    /**
     * Removes the highlight from the specified button.
     * @param btn The button from which to remove the highlight.
     * @return A string that represents the hexadecimal value of the stroke color.
     */
    protected String unsetHightLight(Button btn) {
        return setHighLightCore(btn, BUTTON_STYLE_FORMAT);
    }
    
    /**
     * Applies the specified style to the given button.
     * @param btn The button to which the style is applied.
     * @param style The JavaFX-specific CSS style to apply. The value must contain
     * two string format place holders for the button's text and background colors.
     * @return A string that represents the hexadecimal value of the stroke color.
     */
    protected String setHighLightCore(Button btn, String style) {

        if (btn == null) return null;

        String textcolor, bgcolor;

        if (isClearButton(btn)) {
            // black text on white background
            textcolor = "#000000"; // black
            bgcolor = "white";
        } else {
            // Get the hexadecimal string representation of the current color.
            textcolor = ColorUtil.toHexString(getCurrentColor());
            
            // Same color for the button's text and background,
            // effectively hiding the text.
            bgcolor = textcolor;
        }

        btn.setStyle(String.format(style, textcolor, bgcolor));
        
        return textcolor;
    }

    /**
     * Returns a reference to the underlying {@code GridPane} object if this
     * {@code AdvancedPaint} instance has been created by invoking the
     * {@code createWithGridPane()} method.
     * @return A reference to the underlying {@code GridPane} object, or null.
     */
    public GridPane getGridPane() {
        return grid;
    }
    
    /**
     * Gets a reference to the underlying {@code Canvas} object.
     * @return A reference to the underlying {@code Canvas} object.
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * Gets the width of the canvas as an integer.
     * @return An integer that represents the width of the canvas.
     */
    protected int getCanvasWidth() {
        return (int)canvas.getWidth();
    }

    /**
     * Gets the height of the canvas as an integer.
     * @return An integer that represents the height of the canvas.
     */
    protected int getCanvasHeight() {
        return (int)canvas.getHeight();
    }

    /**
     * Factory method that initializes and returns a 
     * new instance of the {@code AdvancedPaint} class.
     * @param canvas The canvas used to draw graphics.
     * @return An initialized instance of the {@code AdvancedPaint} class.
     */
    public static AdvancedPaint create(Canvas canvas) {
        return new AdvancedPaint(canvas);
    }
    
    /**
     * Creates and initializes an instance of the {@code AdvancedPaint} class
     * using the specified parameters by positioning the color palette to the left
     * of the canvas.
     * <p>
     * This factory method creates a {@code Canvas} object with the specified
     * dimensions and a {@code GridPane} object. The caller may retrieve the grid 
     * pane by invoking the {@code getGridPane()} method. Likewise, the canvas
     * object may be retrieved with the {@code getCanvas()} method.
     * </p>
     * @param canvasWidth The width of the internal {@code Canvas} object to create.
     * @param canvasHeight The height of the internal {@code Canvas} object to create.
     * @return An initialized instance of the {@code AdvancedPaint} class.
     */
    public static AdvancedPaint createWithGridPane(int canvasWidth, int canvasHeight) {
        return createWithGridPane(canvasWidth, canvasHeight, PalettePos.Left);
    }
    
    /**
     * Creates and initializes an instance of the {@code AdvancedPaint} class using
     * the specified parameters by placing the color palette at the specified position.
     * <p>
     * This factory method creates a {@code Canvas} object with the specified
     * dimensions and a {@code GridPane} object. The caller may retrieve the grid 
     * pane by invoking the {@code getGridPane()} method. Likewise, the canvas
     * object may be retrieved with the {@code getCanvas()} method.
     * </p>
     * @param canvasWidth The width of the internal {@code Canvas} object to create.
     * @param canvasHeight The height of the internal {@code Canvas} object to create.
     * @param position The position of the color palette relative to the canvas.
     * @return An initialized instance of the {@code AdvancedPaint} class.
     */
    public static AdvancedPaint createWithGridPane(int canvasWidth, int canvasHeight, PalettePos position) {

        // Create a Canvas object on which a user draws.
        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        
        // Create a grid with 3 columns and a number of rows equal 
        // to about half the number of buttons in the color palette.
        // The color palette occupies 2 columns and the canvas 1 column.
        
        GridPane grid = new GridPane();
        
        AdvancedPaint paint = new AdvancedPaint(canvas, grid);
        Button[] buttons = paint.getPaletteButtons();
        int rowCount = buttons.length;
        
        // if the palette is right to the canvas, columnIndex=0 (first column); otherwise, 2 (third column).
        boolean isright = position == PalettePos.Right;
        int canvasColIndex = isright ? 0 : 2;
        int buttonColIndex = isright ? 1 : 0;
        
        // Add the canvas to the grid in the second column (index=1) and row (index=0);
        // the canvas spans over 1 column and 'buttonLen' rows
        grid.add(canvas,
            canvasColIndex, 
            /*rowIndex*/ 0, 
            /*colspan*/ 1,
            /*rowspan*/ rowCount);
        
        // add all but the last (CLEAR) button
        for (int row = 0; row < rowCount - 1; row++) {
            // Add two buttons per row
            grid.add(buttons[row], buttonColIndex, row);
            
            // make sure the next button is available
            if (++row >= rowCount) break;
            
            grid.add(buttons[row], buttonColIndex + 1, row - 1);
        }
        
        // add the last (CLEAR) button spanning over 2 columns and 1 row
        int colspan = 2, rowspan = 1;
        
        Button clearButton = buttons[rowCount - 1];
        clearButton.setPrefWidth(56); // twice the width of the other buttons
        clearButton.setPrefHeight(34);
        
        grid.add(clearButton, buttonColIndex, rowCount - 1, colspan, rowspan);
        
        return paint;
    }
}
