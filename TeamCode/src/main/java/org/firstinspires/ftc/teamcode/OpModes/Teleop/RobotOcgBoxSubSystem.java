package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;

public class RobotOcgBoxSubSystem {

    // Properties
    Gamepad gamepad;
    Toggle ocgBoxToggle;
    Toggle ocgBoxToggleRight;

    // Initialization
    // Private constructor to enforce the use of the factory method
    private RobotOcgBoxSubSystem(Gamepad gamepad) {
        this.gamepad = gamepad;

        ocgBoxToggle = new Toggle(true);
        ocgBoxToggleRight = new Toggle(true);
    }

    // Factory method to create instances with variables
    public static RobotOcgBoxSubSystem createInstance(Gamepad gamepad) {
        return new RobotOcgBoxSubSystem(gamepad);
    }

    // Run Loop
    public void doOneStateLoop() {
        handleBoxMovements();
    }

    private void handleBoxMovements() {
        handleBoxPitch();
        handleBoxRoll();
    }

    private void handleBoxPitch() {
        //Pitch
        ocgBoxToggle.toggle(gamepad.left_bumper);
        if (ocgBoxToggle.justChanged()) {
            Bot.ocgBox.setPitchState(ocgBoxToggle.get());
        }
    }

    private void handleBoxRoll() {
        ocgBoxToggleRight.toggle(gamepad.right_bumper);
        //Roll
        if (ocgBoxToggleRight.justChanged()) {
            Bot.ocgBox.setRollState(ocgBoxToggleRight.get());
        }
    }
}
