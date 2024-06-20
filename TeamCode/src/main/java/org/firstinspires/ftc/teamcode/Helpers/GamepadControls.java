package org.firstinspires.ftc.teamcode.Helpers;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

/*
    Easier access to controller buttons
    Here for cleanliness and **completeness!!**
*/
public class GamepadControls {
    Gamepad gamepad;

    public double lStickX, lStickY, rStickX, rStickY;

    public boolean a, b, x, y;

    public boolean up, right, down, left;

    public boolean lStickButton, rStickButton;

    public boolean rBumper, lBumper;

    public double rTrigger, lTrigger;

    public GamepadControls(Gamepad gamepad) {
        this.gamepad = gamepad;
        getControls();
    }

    public void getControls() {
        // Sticks
        lStickX = gamepad.left_stick_x; lStickY = gamepad.left_stick_y;
        rStickX = gamepad.right_stick_x; rStickY = gamepad.right_stick_y;

        // Buttons
        a = gamepad.a; b = gamepad.b;
        x = gamepad.x; y = gamepad.y;

        // Directional pad (D-pad)
        up = gamepad.dpad_up; right = gamepad.dpad_right;
        down = gamepad.dpad_down; left = gamepad.dpad_left;

        // Stick buttons
        lStickButton = gamepad.left_stick_button; rStickButton = gamepad.right_stick_button;

        // Bumpers
        rBumper = gamepad.right_bumper; lBumper = gamepad.left_bumper;

        // Triggers
        rTrigger = gamepad.right_trigger; lTrigger = gamepad.left_trigger;
    }
}
