package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;

public class RobotSpecimenArmSubSystem {

    // Properties
    Gamepad gamepad;
    Toggle clawToggle;

    // Initialization
    // Private constructor to enforce the use of the factory method
    private RobotSpecimenArmSubSystem(Gamepad gamepad) {
        this.gamepad = gamepad;
        clawToggle = new Toggle(false);
    }

    // Factory method to create instances with variables
    public static RobotSpecimenArmSubSystem createInstance(Gamepad gamepad) {
        return new RobotSpecimenArmSubSystem(gamepad);
    }

    // Run Loop
    public void doOneStateLoop() {
        presetSpecimenArmPosition();
        resetArm();
        handleSpecimenClawToggle();
    }

    private void presetSpecimenArmPosition() {
        if (gamepad.dpad_up) {
            Bot.specimenArm.movePostHang(1.0);
        } else if (gamepad.dpad_down) {
            Bot.specimenArm.movePrepHang(0.4);
        } else if (gamepad.dpad_left) {
            Bot.specimenArm.moveToCollect(0.4);
        }
    }

    private void  resetArm() {
        if (gamepad.left_trigger > 0.1 || gamepad.right_trigger > 0.1) {
            // Will get cast to an int anyways when incrementing the config
            int armAdjust = (int) (7.0 * gamepad.right_trigger - 7.0 * gamepad.left_trigger);
            if (Bot.specimenArm.getPosition() == 2) {
                Bot.specimenArm.moveToCollect(0.5);
            } else if (Bot.specimenArm.getPosition() == 1) {
                Bot.specimenArm.movePostHang(0.5);
            } else if (Bot.specimenArm.getPosition() == 0) {
                Bot.specimenArm.movePrepHang(0.5);
            }
        }
    }

    private void handleSpecimenClawToggle() {
        // Specimen Claw toggle
        clawToggle.toggle(gamepad.b);
        if (clawToggle.justChanged()) {
            Bot.specimenArm.setClawPosBool(clawToggle.get());
        }
    }
}
