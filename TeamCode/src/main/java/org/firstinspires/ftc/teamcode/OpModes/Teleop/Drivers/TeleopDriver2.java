package org.firstinspires.ftc.teamcode.OpModes.Teleop.Drivers;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;

public class TeleopDriver2 {
    private final Gamepad gamepad;
    private final ElapsedTime timer = new ElapsedTime();

    // Enums for state machine
    private enum TransferState {
        NONE,
        OCG_UP,
        WRIST_UP,
        DROP,
        OCG_IDLE,
        WRIST_DOWN
    }

    // Toggles
    private final Toggle intakeTransferToggle = new Toggle(false);
    private final Toggle specimenClawToggle = new Toggle(false);
    private final Toggle ocgBoxToggle = new Toggle(false);

    // Variables
    private boolean manualSlideArmControl = false;
    private TransferState transferState = TransferState.NONE;

    private TeleopDriver2(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    public static TeleopDriver2 createInstance(Gamepad gamepad) {
        return new TeleopDriver2(gamepad);
    }

    public void loopGamepad() {
        runSpecimenArm();
        toggleSpecimenClaw();
        controlClawRotation();
        driveSlideArm();
        manualSlideArmControl();
        toggleOCGBox();
        handleResetSlideEncoders();
        updateTransferState();
        handleTransfer();
    }

    private void runSpecimenArm() {
        if (gamepad.dpad_down) {
            Bot.specimenArm.movePrepHang(0.65);
        } else if (gamepad.dpad_left) {
            Bot.specimenArm.moveToCollect(0.4);
        }
    }

    private void toggleSpecimenClaw() {
        specimenClawToggle.toggle(gamepad.circle);
        if (specimenClawToggle.justChanged() && specimenClawToggle.get()) {
            Bot.specimenArm.openClaw();
        } else if (specimenClawToggle.justChanged() && !specimenClawToggle.get()) {
            Bot.specimenArm.closeClaw();
        }
    }

    private void controlClawRotation() {
        if (gamepad.left_stick_x <= -0.5) {
            Bot.intakeClaw.clawNeg90();
        } else if (gamepad.left_stick_x > -0.5 && gamepad.left_stick_x < -0.1) {
            Bot.intakeClaw.clawNeg45();
        } else if (gamepad.left_stick_x >= 0.5) {
            Bot.intakeClaw.clawPos90();
        } else if (gamepad.left_stick_x < 0.5 && gamepad.left_stick_x > 0.1) {
            Bot.intakeClaw.clawPos45();
        } else {
            Bot.intakeClaw.clawPos0();
        }
    }

    private void driveSlideArm() {
        if (gamepad.y) {
            manualSlideArmControl = false;
            Bot.intake.misumiWristMid();
            Bot.slideArm.moveUp(0.65);
        } else if (gamepad.a) {
            manualSlideArmControl = false;
            Bot.intake.misumiWristMid();
            Bot.ocgBox.idle();
            Bot.slideArm.moveDown(0.65);
        } else if (gamepad.x) {
            manualSlideArmControl = false;
            Bot.intake.misumiWristMid();
            Bot.slideArm.moveMid(0.65);
        }
        if (!manualSlideArmControl) {
            Bot.slideArm.update();
        }
    }

    private void manualSlideArmControl() {
        float rightStickY = -gamepad.right_stick_y;
        if (rightStickY > 0.25) {
            manualSlideArmControl = true;
            Bot.intake.misumiWristMid();
            Bot.slideArm.move(rightStickY - 0.25);
        } else if (rightStickY < -0.25) {
            manualSlideArmControl = true;
            Bot.intake.misumiWristMid();
            Bot.slideArm.move(rightStickY + 0.25);
        } else if (manualSlideArmControl) {
            Bot.slideArm.move(0);
        }
    }

    private void toggleOCGBox() {
        ocgBoxToggle.toggle(gamepad.right_bumper);
        if (ocgBoxToggle.justChanged() && ocgBoxToggle.get()) {
            Bot.ocgBox.ocgPitchDrop();
        } else if (ocgBoxToggle.justChanged() && !ocgBoxToggle.get()) {
            Bot.ocgBox.idle();
        }
    }

    private void updateTransferState() {
        intakeTransferToggle.toggle(gamepad.left_bumper);
        if (intakeTransferToggle.justChanged() && intakeTransferToggle.get()) {
            transferState = TransferState.WRIST_UP;
        }
    }

    private void handleTransfer() {
        switch (transferState) {
            case NONE:
                break;

            case WRIST_UP:
                Bot.specimenArm.movePrepHang(0.6);
                Bot.intake.misumiWristUp();
                timer.reset();
                transferState = TransferState.OCG_UP;
                break;

            case OCG_UP:
                if (timer.milliseconds() > 300) {
                    Bot.ocgBox.ocgPitchUp();
                    timer.reset();
                    transferState = TransferState.DROP;
                }
                break;

            case DROP:
                if (timer.milliseconds() > 300) {
                    Bot.intakeClaw.openClaw();
                    timer.reset();
                    transferState = TransferState.OCG_IDLE;
                }
                break;

            case OCG_IDLE:
                if (timer.milliseconds() > 300)
                    transferState = TransferState.WRIST_DOWN;
                break;

            case WRIST_DOWN:
                // As there is nothing after, the state is immediately set to NONE
                Bot.intake.misumiWristDown();
                Bot.intakeClaw.closeClaw();
                Bot.ocgBox.idle();
                transferState = TransferState.NONE;
        }
    }

    private void handleResetSlideEncoders() {
        if (gamepad.share && gamepad.options) {
            Bot.slideArm.resetEncoders();
        }
    }
}
