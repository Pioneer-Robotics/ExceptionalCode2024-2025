package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;

public class RobotIntakeSubSystem {

    // State Enum
    private enum IntakeState {
        NONE,
        MID_WRIST,
        EXTEND
    }

    // Properties
    Gamepad gamepad;

    RobotIntakeSubSystem.IntakeState intakeState;

    Toggle intakeClawToggle;
    Toggle intakeWristToggle;

    // Initialization
    // Private constructor to enforce the use of the factory method
    private RobotIntakeSubSystem(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    // Factory method to create instances with variables
    public static RobotIntakeSubSystem createInstance(Gamepad gamepad1) {
        return new RobotIntakeSubSystem(gamepad1);
    }

    // Run Loop
    public void doOneStateLoop() {
        resetIntakeStateToNone();
        setupToggles();
        handleDpadUpOrDown();
        updateIntakeState();
        updateMidWristIntakeState();
        handleIntakeClawToggle();
        handleIntakeWristToggle();
    }

    // SubSystem private methods
    private void resetIntakeStateToNone() {
        intakeState = RobotIntakeSubSystem.IntakeState.NONE;
    }
    private void setupToggles() {
        intakeClawToggle = new Toggle(false);
        intakeWristToggle = new Toggle(false);
    }

    // Intake
    private void handleDpadUpOrDown() {
        if (gamepad.dpad_up) {
            intakeState = RobotIntakeSubSystem.IntakeState.MID_WRIST;
            intakeWristToggle.set(true);
        } else if (gamepad.dpad_down) {
            Bot.intake.midMisumiWrist();
            Bot.intake.retractMisumiDrive();
            Bot.intakeClaw.clawUp();
            intakeWristToggle.set(false);
        }
    }

    private void updateIntakeState() {
        switch (intakeState) {
            case NONE:
                break;
            case MID_WRIST:
                updateMidWristIntakeState();
                break;
            case EXTEND:
                updateExtendIntakeState();
                break;
        }
    }

    private void updateMidWristIntakeState() {
        Bot.intake.midMisumiWrist();
        if (Bot.intake.isWristMid()) {
            intakeState = RobotIntakeSubSystem.IntakeState.EXTEND;
        }
    }

    private void updateExtendIntakeState() {
        Bot.intake.extendMisumiDrive();
        Bot.intakeClaw.clawDown();
        intakeState = RobotIntakeSubSystem.IntakeState.NONE;
    }

    private void handleIntakeClawToggle() {
        intakeClawToggle.toggle(gamepad.b);
        if (intakeClawToggle.justChanged() && intakeClawToggle.get()) {
            Bot.intakeClaw.openClaw();
        } else if (intakeClawToggle.justChanged() && !intakeClawToggle.get()) {
            Bot.intakeClaw.closeClaw();
        }
    }

    private void handleIntakeWristToggle() {
        intakeWristToggle.toggle(gamepad.y);
        if (intakeWristToggle.justChanged() && intakeWristToggle.get()) {
            // Down state
            if (Bot.intake.isExtended()) {
                // If intake extended
                Bot.intake.misumiWristDown();
            } else {
                // If intake not extended
                Bot.intake.midMisumiWrist();
            }
        } else if (intakeWristToggle.justChanged() && !intakeWristToggle.get()) {
            // Up state
            if (Bot.intake.isExtended()) {
                // If intake extended
                Bot.intake.midMisumiWrist();
            } else {
                // If intake not extended
                Bot.intake.misumiWristUp();
                Bot.ocgBox.ocgPitchUp();
            }
        }
    }
}
