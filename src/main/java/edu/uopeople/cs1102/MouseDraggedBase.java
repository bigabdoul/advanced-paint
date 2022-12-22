package edu.uopeople.cs1102;

import javafx.scene.input.MouseEvent;

/**
 * Represents the base type for classes that support mouse drag operations.
 *
 */
public abstract class MouseDraggedBase {
    // The current location of the mouse, when the user presses the mouse button.
    private int currentX, currentY;
    
    /**
     * The previous location of the mouse, when the user is drawing by dragging the mouse.
     */
    protected double prevX, prevY;
    
    /**
     * This is set to true while the user is dragging with the mouse.
     */
    protected boolean dragging;
    
    /**
     * The handler for a MousePressed event.
     * @param evt The MousePressed event data.
     */
    protected void mousePressed(MouseEvent evt) {
        onMousePressed(evt.getX(), evt.getY());
    }
    
    /**
     * Handles the MousePressed event. This method should be overwritten and called by subclasses.
     * @param x The x-coordinate of the mouse.
     * @param y The y-coordinate of the mouse.
     * @return true if the user is already dragging with the mouse; otherwise, false.
     */
    protected boolean onMousePressed(double x, double y) {
        if (dragging)       // Ignore mouse presses that occur when user is already dragging.
            return true;    // (This can happen if the user presses two mouse buttons at the same time.)

        currentX = (int)x;  // x-coordinate where the user clicked.
        currentY = (int)y;  // y-coordinate where the user clicked.
        
        return false;
    }
    
    /**
     * Called whenever the user moves the mouse while a mouse button is held down.
     * This method is intended to be used as a handler for the MousePressed event.
     * What should happen during this event must be implemented in the 
     * onMouseDragged(double, double) method.
     * @param evt The mouse event.
     */
    protected void mouseDragged(MouseEvent evt) {
       onMouseDragged(evt.getX(), evt.getY());
    }
    
    /**
     * The real handler for the MouseDragged event. This method should be
     * overwritten and called by subclasses that intend to handle this event.
     * @param x The current x-coordinate of the mouse.
     * @param y The current y-coordinate of the mouse.
     * @return false if no dragging is happening; otherwise, true.
     */
    protected boolean onMouseDragged(double x, double y) {
        if (!dragging)
        return false;  // Nothing to do because the user isn't dragging.

        currentX = (int)x;   // x-coordinate of mouse.
        currentY = (int)y;   // y-coordinate of mouse.
        
        return true; // dragging
    }

    /**
     * Called whenever the user releases the mouse button. Just sets
     * dragging to false.
     */
    protected void mouseReleased(MouseEvent evt) {
        dragging = false;
    }
    
    /**
     * Indicates whether a drag operation is in progress.
     * @return true if the user is dragging with the mouse; otherwise, false.
     */
    public boolean isDragging() {
        return dragging;
    }

    /**
     * Gets the current x-coordinate of the mouse as an integer.
     * @return An integer that represents the mouse's current x-coordinate.
     */
    public int getCurrentX() {
        return currentX;
    }

    /**
     * Gets the current y-coordinate of the mouse as an integer.
     * @return An integer that represents the mouse's current y-coordinate.
     */
    public int getCurrentY() {
        return currentY;
    }
    
    /**
     * Gets the previous x-coordinate of the mouse.
     * @return A double precision floating-point number that represents the mouse's previous x-coordinate.
     */
    public double getPrevX() {
        return prevX;
    }

    /**
     * Gets the previous y-coordinate of the mouse.
     * @return A double precision floating-point number that represents the mouse's previous y-coordinate.
     */
    public double getPrevY() {
        return prevY;
    }
}
