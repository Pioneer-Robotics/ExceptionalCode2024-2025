package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;

public class TeleopDriver2 {

    Gamepad gamepad2;
    Telemetry telemetry;

    Toggle clawToggle;
    Toggle ocgBoxToggle;
    Toggle ocgBoxToggleRight;

    private TeleopDriver2(Gamepad gamepad2, Telemetry telemetry) {
        this.gamepad2 = gamepad2;
        this.telemetry = telemetry;

        clawToggle = new Toggle(false);
        ocgBoxToggle = new Toggle(true);
        ocgBoxToggleRight = new Toggle(true);
    }

    // Factory method to create instances with variables
    public static TeleopDriver2 createInstance(Gamepad gamepad2, Telemetry telemetry) {
        return new TeleopDriver2(gamepad2, telemetry);
    }

    public void loopGameController() {
        presetSpecimenArmPosition();
        resetArm();
        handleSpecimenClawToggle();
        handleClawYaw();
        handleSlideArm();
        handleManualSlideArmControls();
        handleBoxMovements();
    }

    private void presetSpecimenArmPosition() {
        if (gamepad2.dpad_up) {
            Bot.specimenArm.movePostHang(1.0);
        } else if (gamepad2.dpad_down) {
            Bot.specimenArm.movePrepHang(0.4);
        } else if (gamepad2.dpad_left) {
            Bot.specimenArm.moveToCollect(0.4);
        }
    }

    private void  resetArm() {
        if (gamepad2.left_trigger > 0.1 || gamepad2.right_trigger > 0.1) {
            // Will get cast to an int anyways when incrementing the config
            int armAdjust = (int) (7.0 * gamepad2.right_trigger - 7.0 * gamepad2.left_trigger);
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
        clawToggle.toggle(gamepad2.b);
        if (clawToggle.justChanged()) {
            Bot.specimenArm.setClawPosBool(clawToggle.get());
        }
    }

    private void handleClawYaw() {
        //Claw Yaw
        if (gamepad2.left_stick_x < -0.1 && gamepad2.left_stick_x>-0.5){
            Bot.intakeClaw.clawNeg45();
        } else if (gamepad2.left_stick_x<-0.5){
            Bot.intakeClaw.clawNeg90();
        } else if (gamepad2.left_stick_x>0.1 && gamepad2.left_stick_x<0.5){
            Bot.intakeClaw.clawPos45();
        } else if (gamepad2.left_stick_x>0.5){
            Bot.intakeClaw.clawPos90();
        } else {
            Bot.intakeClaw.clawPos0();
        }
    }

    private void handleSlideArm() {
        // Slide Arm
        if (gamepad2.y) {
            Bot.intakeClaw.clawDown();
            Bot.intake.midMisumiWrist();
            Bot.slideArm.moveUp(0.8);
            Bot.ocgBox.idle();
        } else if (gamepad2.a) {
            Bot.intakeClaw.clawDown();
            Bot.intake.midMisumiWrist();
            Bot.slideArm.moveDown(0.8);
            Bot.ocgBox.idle();
        } else if (gamepad2.x) {
            Bot.slideArm.moveMid(0.8);
            Bot.ocgBox.idle();
        }
    }

    private void handleManualSlideArmControls() {
        // Manual slide arm controls
        if (gamepad2.right_stick_y > 0.5) {
            Bot.slideArm.move(0.1);
        } else if (gamepad2.right_stick_y < -0.5){
            Bot.slideArm.move(gamepad2.right_stick_y*0.3);
        } else if(Math.abs(gamepad2.right_stick_x)>0.5) {
            Bot.slideArm.motorOff();
            Bot.ocgBox.idle();
        }
    }

    private void handleBoxMovements() {
        handleBoxPitch();
        handleBoxRoll();
    }

    private void handleBoxPitch() {
        //Pitch
        ocgBoxToggle.toggle(gamepad2.left_bumper);
        if (ocgBoxToggle.justChanged()) {
            Bot.ocgBox.setPitchState(ocgBoxToggle.get());
        }
    }

    private void handleBoxRoll() {
        ocgBoxToggleRight.toggle(gamepad2.right_bumper);
        //Roll
        if (ocgBoxToggleRight.justChanged()) {
            Bot.ocgBox.setRollState(ocgBoxToggleRight.get());
        }
    }
}
