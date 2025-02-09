package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;

public class TeleopDriver1 {

    public enum TransferState {
        NONE,
        OCG_UP,
        WRIST_UP,
        DROP,
        OCG_IDLE,
        WRIST_DOWN
    }

    public enum IntakeState {
        NONE,
        MID_WRIST,
        EXTEND
    }

    Gamepad gamepad1;
    Telemetry telemetry;
    double maxSpeed;
    boolean bothTrigPressed;
    Toggle northModeToggle;
    ElapsedTime transferTimer;

    TeleopDriver1.TransferState transferState;
    TeleopDriver1.IntakeState intakeState;

    // Private constructor to enforce the use of the factory method
    private TeleopDriver1(Gamepad gamepad1, Telemetry telemetry) {
        this.gamepad1 = gamepad1;
        this.telemetry = telemetry;

        // Initialize max speed
        maxSpeed = 0.5;
        bothTrigPressed = false;
        northModeToggle = new Toggle(true);
        transferTimer = new ElapsedTime();
    }

    // Factory method to create instances with variables
    public static TeleopDriver1 createInstance(Gamepad gamepad1, Telemetry telemetry) {
        return new TeleopDriver1(gamepad1, telemetry);
    }


    public void loopGameController() {
        transferState = TeleopDriver1.TransferState.NONE;
        intakeState = TeleopDriver1.IntakeState.NONE;

        Toggle incSpeedToggle = new Toggle(false);
        Toggle decSpeedToggle = new Toggle(false);

        Toggle intakeClawToggle = new Toggle(false);
        Toggle intakeWristToggle = new Toggle(false);

        // Inputs for driving
        double px = gamepad1.left_stick_x;
        double py = -gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;

        // Move
        Bot.mecanumBase.move(px, py, turn, maxSpeed);

        // Toggle for field centric
        bothTrigPressed = gamepad1.left_trigger > 0.8 && gamepad1.right_trigger > 0.8;
        northModeToggle.toggle(bothTrigPressed); // Toggle north mode
        Bot.mecanumBase.setNorthMode(northModeToggle.get()); // Update north mode

        if (gamepad1.x) {
            Bot.pinpoint.resetIMU();
        }

        incSpeedToggle.toggle(gamepad1.right_bumper);
        decSpeedToggle.toggle(gamepad1.left_bumper);
        if (incSpeedToggle.justChanged()) {
            maxSpeed += 0.1;
        }
        if (decSpeedToggle.justChanged()) {
            maxSpeed -= 0.1;
        }

        if (gamepad1.dpad_up) {
            intakeState = TeleopDriver1.IntakeState.MID_WRIST;
            intakeWristToggle.set(true);
        } else if (gamepad1.dpad_down) {
            Bot.intake.midMisumiWrist();
            Bot.intake.retractMisumiDrive();
            Bot.intakeClaw.clawUp();
            intakeWristToggle.set(false);
        }

        switch (intakeState) {
            case NONE:
                break;
            case MID_WRIST:
                Bot.intake.midMisumiWrist();
                if (Bot.intake.isWristMid()) {
                    intakeState = TeleopDriver1.IntakeState.EXTEND;
                }
                break;
            case EXTEND:
                Bot.intake.extendMisumiDrive();
                Bot.intakeClaw.clawDown();
                intakeState = TeleopDriver1.IntakeState.NONE;
                break;
        }

        intakeClawToggle.toggle(gamepad1.b);
        if (intakeClawToggle.justChanged() && intakeClawToggle.get()) {
            Bot.intakeClaw.openClaw();
        } else if (intakeClawToggle.justChanged() && !intakeClawToggle.get()) {
            Bot.intakeClaw.closeClaw();
        }

        intakeWristToggle.toggle(gamepad1.y);
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

        // Transfer entry point and interrupt
        if (gamepad1.dpad_right) {
            transferState = TeleopDriver1.TransferState.WRIST_UP;
        }
        if (gamepad1.touchpad) {
            transferState = TeleopDriver1.TransferState.NONE;
        }

        // Handles a full transfer without spawning a thread or interrupting opMode
        // Waits for each action to be done before moving on to the next state
        switch (transferState) {
            case NONE:
                break;
            case WRIST_UP:
                Bot.intake.misumiWristUp();
                if (Bot.intake.isWristUp()) {
                    transferState = TeleopDriver1.TransferState.OCG_UP;
                }
                break;
            case OCG_UP:
                Bot.ocgBox.ocgPitchUp();
                if (Bot.ocgBox.isPitchUp()) {
                    transferTimer.reset();
                    transferState = TeleopDriver1.TransferState.DROP;
                }
                break;
            case DROP:
                if (transferTimer.milliseconds() > 500) {
                    Bot.intakeClaw.openClaw();
                }
                if (Bot.intakeClaw.isClawOpen()) {
                    transferTimer.reset();
                    transferState = TeleopDriver1.TransferState.OCG_IDLE;
                }
                break;
            case OCG_IDLE:
                if (transferTimer.milliseconds() > 3000) {
                    Bot.ocgBox.idle();
                }
                if (Bot.ocgBox.isIdle()) {
                    transferState = TeleopDriver1.TransferState.WRIST_DOWN;
                }
                break;
            case WRIST_DOWN:
                // As there is nothing after, the state is immediately set to NONE
                Bot.intake.misumiWristDown();
                Bot.intakeClaw.closeClaw();
                transferState = TeleopDriver1.TransferState.NONE;
        }

        updateTelemetry();
    }

    public void updateTelemetry() {
        telemetry.addData("TransferState", transferState);
        telemetry.addData("North Mode", northModeToggle.get());
        telemetry.addData("Speed", maxSpeed);
        telemetry.update();
    }
}
