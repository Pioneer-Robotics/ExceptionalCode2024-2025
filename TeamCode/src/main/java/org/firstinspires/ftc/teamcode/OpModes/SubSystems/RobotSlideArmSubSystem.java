package org.firstinspires.ftc.teamcode.OpModes.SubSystems;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Bot;

public class RobotSlideArmSubSystem {

    // Properties
    Gamepad gamepad;

    // Initialization
    // Private constructor to enforce the use of the factory method
    private RobotSlideArmSubSystem(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    // Factory method to create instances with variables
    public static RobotSlideArmSubSystem createInstance(Gamepad gamepad) {
        return new RobotSlideArmSubSystem(gamepad);
    }

    // Run Loop
    public void doOneStateLoop() {
        handleSlideArm();
        handleManualSlideArmControls();
    }

    // Slide for box and hanging
    private void handleSlideArm() {
        // Slide Arm
        if (gamepad.y) {
            Bot.intakeClaw.clawDown();
            Bot.intake.midMisumiWrist();
            Bot.slideArm.moveUp(0.8);
            Bot.ocgBox.idle();
        } else if (gamepad.a) {
            Bot.intakeClaw.clawDown();
            Bot.intake.midMisumiWrist();
            Bot.slideArm.moveDown(0.8);
            Bot.ocgBox.idle();
        } else if (gamepad.x) {
            Bot.slideArm.moveMid(0.8);
            Bot.ocgBox.idle();
        }
    }

    private void handleManualSlideArmControls() {
        // Manual slide arm controls
        if (gamepad.right_stick_y > 0.5) {
            Bot.slideArm.move(0.1);
        } else if (gamepad.right_stick_y < -0.5){
            Bot.slideArm.move(gamepad.right_stick_y*0.3);
        } else if(Math.abs(gamepad.right_stick_x)>0.5) {
            Bot.slideArm.motorOff();
            Bot.ocgBox.idle();
        }
    }
}
