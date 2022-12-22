package edu.uopeople.cs1102;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * A more advanced program where the user can sketch curves in a variety of
 * colors. A color palette is shown along an edge (left, top, right or bottom) of 
 * the canvas. The user can select a drawing color by clicking on a color in the
 * palette. Under the colors is a "Clear button" that the user can click to clear 
 * the sketch.  The user draws by clicking and dragging in a large white area 
 * that occupies the entire canvas.
 * <p>
 * Program adapted from David J. Eck's SimplePaint JavaFX application 
 * (https://math.hws.edu/eck/cs124/javanotes8/source/chapter6/SimplePaint.java)
 * to make it more reusable and less complex.
 * </p> 
 * @author https://github.com/bigabdoul
 * @version 1.0
 */
public final class Program extends Application {

    /**
     * This main routine allows this class to be run as a program.
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * The start() method creates the GUI and shows the window on the screen.
     */
    @Override
    public void start(Stage stage) {
        
        final int width = 600;  // width of the canvas
        final int height = 400; // height of the canvas
        final int paletteWidth = 56;

        // Creating an AdvancedPaint object with a GridPane
        // handles the boilerplate code required to get started.
        AdvancedPaint paint = AdvancedPaint.createWithGridPane(width, height, PalettePos.Left);
        
        /* Configure the GUI and show the window. */
        Scene scene = new Scene(paint.getGridPane(), width + paletteWidth, height);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Advanced Paint");
        stage.show();
    }
}
