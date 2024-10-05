package org.firstinspires.ftc.teamcode.Helpers;


/**
 * This class is a wrapper class for booleans, but it allows you to negate
 * the boolean by pressing a button. The intended use case is that there is some boolean
 * value used in an OpMode, and you want it to change every time you press a specific button
 * on the gamepad. Make that boolean a {@code Toggle} and call {@code toggle(boolean)}
 * every cycle using the desired button as the input to {@code toggle()}
 */
public class Toggle {
    private boolean state; // The actual thing you're tracking
    private boolean prevState; // The state of the boolean during the previous cycle
    // Used to know if the state changed from false to true last cycle
    private boolean justChangedFlag; // True if the state was changed last cycle

    public Toggle(boolean startState){
        state = startState;
        prevState = false;
    }

    /**
     * Changes the state of the boolean if it hasn't been changed in the previous cycle
     * {@code state = !state}
     */
    public void toggle(boolean button) {
        justChangedFlag = false;
        // If the button is pressed and it wasn't pressed last cycle, change the state
        if (button && !prevState) {
            state = !state;
            justChangedFlag = true;
        }
        prevState = button;
    }

    /**
     * Sets the value of {@code state} on this object
     * @param bool the value you want {@code state} to have
     */
    public void set(boolean bool) {
        if (bool != state) {
            state = bool;
            justChangedFlag = true;
        } else { // This means state is already set to what it should be
            justChangedFlag = false;
        }
    }

    /**
     * Gets the {@code state} value of this {@code toggle} object
     * @return {@code this.state}
     */
    public boolean get() {
        return state;
    }

    /**
     * Gets the {@code justChangedFlag} value of this {@code toggle} object
     * @return {@code this.justChangedFlag}
     */
    public boolean justChanged() {
        return justChangedFlag;
    }
}
