package org.firstinspires.ftc.teamcode.OpModes.Teleop.Drivers;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;

public class TeleopDriver1 {
    private final Gamepad gamepad;
    private final ElapsedTime timer = new ElapsedTime();
    // Enums for state machine
    private enum IntakeState {
        NONE,
        MID_WRIST,
        EXTEND
    }

    // Variables
    private double speed = 0.5;
    private boolean northMode = true;
    private IntakeState intakeState = IntakeState.NONE;

    // Toggles
    private final Toggle northModeToggle = new Toggle(true);
    private final Toggle incSpeedToggle = new Toggle(false);
    private final Toggle decSpeedToggle = new Toggle(false);
    private final Toggle resetIMUToggle = new Toggle(false);
    private final Toggle extendIntakeToggle = new Toggle(false);
    private final Toggle intakeClawToggle = new Toggle(false);
    private final Toggle intakeWristToggle = new Toggle(false);

    private TeleopDriver1(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    public static TeleopDriver1 createInstance(Gamepad gamepad) {
        return new TeleopDriver1(gamepad);
    }

    public void loopGamepad() {
        handleSpeedControls();
        handleNorthMode();
        handleIMUReset();
        driveRobot();
        updateIntakeState();
        driveIntake();
        toggleIntakeClaw();
        toggleIntakeWrist();
    }

    private void handleSpeedControls() {
        incSpeedToggle.toggle(gamepad.right_bumper);
        decSpeedToggle.toggle(gamepad.left_bumper);
        if (incSpeedToggle.justChanged() && incSpeedToggle.get()) {
            speed += 0.1;
        }
        if (decSpeedToggle.justChanged() && decSpeedToggle.get()) {
            speed -= 0.1;
        }
    }

    private void handleNorthMode() {
        northModeToggle.toggle(gamepad.share && gamepad.options);
        northMode = northModeToggle.get();
        Bot.mecanumBase.setNorthMode(northMode);
    }

    private void handleIMUReset() {
        resetIMUToggle.toggle(gamepad.x);
        if (resetIMUToggle.justChanged() && resetIMUToggle.get()) {
            Bot.pinpoint.resetIMU();
        }
    }

    private void driveRobot() {
        double px = Math.pow(gamepad.left_stick_x, 3);
        double py = -Math.pow(gamepad.left_stick_y, 3);
        double turn = gamepad.right_stick_x;
        Bot.mecanumBase.move(px, py, turn, speed);
    }

    private void updateIntakeState() {
        // Toggle prevents the state being set multiple times if the button is held
        extendIntakeToggle.toggle(gamepad.dpad_up);
        if (extendIntakeToggle.justChanged() && extendIntakeToggle.get()) {
            timer.reset();
            intakeState = IntakeState.MID_WRIST;
        } else if (gamepad.dpad_down) {
            intakeState = IntakeState.NONE;
            Bot.intake.misumiWristMid();
            Bot.intake.retractMisumiDrive();
            Bot.intakeClaw.clawUp();
            Bot.intakeClaw.clawPos0();
        }
    }

    private void driveIntake() {
        switch (intakeState) {
            case NONE:
                break;
            case MID_WRIST:
                Bot.intake.misumiWristMid();
                if (timer.milliseconds() > 250) {
                    intakeState = IntakeState.EXTEND;
                }
                break;
            case EXTEND:
                Bot.intake.extendMisumiDrive();
                Bot.intakeClaw.clawDown();
                intakeState = IntakeState.NONE;
        }
    }

    private void toggleIntakeClaw() {
        intakeClawToggle.toggle(gamepad.b);
        if (intakeClawToggle.justChanged() && intakeClawToggle.get()) {
            Bot.intakeClaw.openClaw();
        } else if (intakeClawToggle.justChanged() && !intakeClawToggle.get()) {
            Bot.intakeClaw.closeClaw();
        }
    }

    private void toggleIntakeWrist() {
        intakeWristToggle.toggle(gamepad.y);
        if (intakeWristToggle.justChanged() && intakeWristToggle.get()) {
            // Down state
            if (Bot.intake.isExtended()) {
                // If intake extended
                Bot.intake.misumiWristDown();
            } else {
                // If intake not extended
                Bot.intake.misumiWristMid();
            }
        } else if (intakeWristToggle.justChanged() && !intakeWristToggle.get()) {
            // Up state
            if (Bot.intake.isExtended()) {
                // If intake extended
                Bot.intake.misumiWristMid();
            } else {
                // If intake not extended
                Bot.intake.misumiWristUp();
                Bot.ocgBox.ocgPitchUp();
            }
        }
    }

    public boolean getNorthModeToggle() {
        return northMode;
    }

    public double getSpeed() {
        return speed;
    }
}
