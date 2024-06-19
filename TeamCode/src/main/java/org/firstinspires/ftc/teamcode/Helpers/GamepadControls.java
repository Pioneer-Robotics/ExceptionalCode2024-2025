package org.firstinspires.ftc.teamcode.Helpers;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/*
    Easier access to controller buttons
    Here for cleanliness and **completeness!!**
*/
public class GamepadControls {
    public double lStickX1, lStickY1, rStickX1, rStickY1;
    public double lStickX2, lStickY2, rStickX2, rStickY2;

    public boolean a1, b1, a2, b2;
    public boolean x1, y1, x2, y2;

    public boolean up1, right1, down1, left1;
    public boolean up2, right2, down2, left2;

    public boolean lStickButton1, rStickButton1, lStickButton2, rStickButton2;

    public boolean rBumper1, lBumper1, rBumper2, lBumper2;

    public double rTrigger1, lTrigger1, rTrigger2, lTrigger2;

    public GamepadControls(LinearOpMode opMode) {
        // Sticks
        lStickX1 = opMode.gamepad1.left_stick_x; lStickY1 = opMode.gamepad1.left_stick_y;
        rStickX1 = opMode.gamepad1.right_stick_x; rStickY1 = opMode.gamepad1.right_stick_y;
        lStickX2 = opMode.gamepad2.left_stick_x; lStickY2 = opMode.gamepad2.left_stick_y;
        rStickX2 = opMode.gamepad2.right_stick_x; rStickY2 = opMode.gamepad2.right_stick_y;

        // Buttons
        a1 = opMode.gamepad1.a; b1 = opMode.gamepad1.b;
        a2 = opMode.gamepad2.a; b2 = opMode.gamepad2.b;
        x1 = opMode.gamepad1.x; y1 = opMode.gamepad1.y;
        x2 = opMode.gamepad2.x; y2 = opMode.gamepad2.y;

        // Directional pad (D-pad)
        up1 = opMode.gamepad1.dpad_up; right1 = opMode.gamepad1.dpad_right;
        down1 = opMode.gamepad1.dpad_down; left1 = opMode.gamepad1.dpad_left;
        up2 = opMode.gamepad2.dpad_up; right2 = opMode.gamepad2.dpad_right;
        down2 = opMode.gamepad2.dpad_down; left2 = opMode.gamepad2.dpad_left;

        // Stick buttons
        lStickButton1 = opMode.gamepad1.left_stick_button; rStickButton1 = opMode.gamepad1.right_stick_button;
        lStickButton2 = opMode.gamepad2.left_stick_button; rStickButton2 = opMode.gamepad2.right_stick_button;

        // Bumpers
        rBumper1 = opMode.gamepad1.right_bumper; lBumper1 = opMode.gamepad1.left_bumper;
        rBumper2 = opMode.gamepad2.right_bumper; lBumper2 = opMode.gamepad2.left_bumper;

        // Triggers
        rTrigger1 = opMode.gamepad1.right_trigger; lTrigger1 = opMode.gamepad1.left_trigger;
        rTrigger2 = opMode.gamepad2.right_trigger; lTrigger2 = opMode.gamepad2.left_trigger;
    }
}
