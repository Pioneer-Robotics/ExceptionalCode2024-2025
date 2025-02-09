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

    Toggle incSpeedToggle;
    Toggle decSpeedToggle;

    Toggle intakeClawToggle;
    Toggle intakeWristToggle;


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
        resetTransferAndIntakeStateToNone();
        setupProperties();
        moveRobot();
        handleToggleForFieldCentric();
        resetIMU();
        handleToggleSpeed();
        handleDpadUpOrDown();
        updateIntakeState();
        handleIntakeClawToggle();
        handleIntakeWristToggle();
        handleTransferEntryPointAndInterrupt();
        updateTransferState();
        updateTelemetry();
    }

    private void resetTransferAndIntakeStateToNone() {
        transferState = TeleopDriver1.TransferState.NONE;
        intakeState = TeleopDriver1.IntakeState.NONE;
    }
    private void setupProperties() {
        incSpeedToggle = new Toggle(false);
        decSpeedToggle = new Toggle(false);
        intakeClawToggle = new Toggle(false);
        intakeWristToggle = new Toggle(false);
    }

    private void moveRobot() {
        // Inputs for driving
        double px = gamepad1.left_stick_x;
        double py = -gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;

        // Move
        Bot.mecanumBase.move(px, py, turn, maxSpeed);
    }

    private void handleToggleForFieldCentric() {
        bothTrigPressed = gamepad1.left_trigger > 0.8 && gamepad1.right_trigger > 0.8;
        northModeToggle.toggle(bothTrigPressed); // Toggle north mode
        Bot.mecanumBase.setNorthMode(northModeToggle.get()); // Update north mode
    }

    private void resetIMU() {
        if (gamepad1.x) {
            Bot.pinpoint.resetIMU();
        }
    }

    private void handleToggleSpeed() {
        incSpeedToggle.toggle(gamepad1.right_bumper);
        decSpeedToggle.toggle(gamepad1.left_bumper);
        if (incSpeedToggle.justChanged()) {
            maxSpeed += 0.1;
        }
        if (decSpeedToggle.justChanged()) {
            maxSpeed -= 0.1;
        }
    }

    private void handleDpadUpOrDown() {
        if (gamepad1.dpad_up) {
            intakeState = TeleopDriver1.IntakeState.MID_WRIST;
            intakeWristToggle.set(true);
        } else if (gamepad1.dpad_down) {
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
            intakeState = TeleopDriver1.IntakeState.EXTEND;
        }
    }

    private void updateExtendIntakeState() {
        Bot.intake.extendMisumiDrive();
        Bot.intakeClaw.clawDown();
        intakeState = TeleopDriver1.IntakeState.NONE;
    }

    private void handleIntakeClawToggle() {
        intakeClawToggle.toggle(gamepad1.b);
        if (intakeClawToggle.justChanged() && intakeClawToggle.get()) {
            Bot.intakeClaw.openClaw();
        } else if (intakeClawToggle.justChanged() && !intakeClawToggle.get()) {
            Bot.intakeClaw.closeClaw();
        }
    }

    private void handleIntakeWristToggle() {
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
    }

    private void handleTransferEntryPointAndInterrupt() {
        if (gamepad1.dpad_right) {
            transferState = TeleopDriver1.TransferState.WRIST_UP;
        }
        if (gamepad1.touchpad) {
            transferState = TeleopDriver1.TransferState.NONE;
        }
    }

        private void updateTransferState() {
        // Handles a full transfer without spawning a thread or interrupting opMode
        // Waits for each action to be done before moving on to the next state
        switch (transferState) {
            case NONE:
                break;
            case WRIST_UP:
                updateWristUpTransferState();
                break;
            case OCG_UP:
                updateOCGUpTransferState();
                break;
            case DROP:
                updateDropTransferState();
                break;
            case OCG_IDLE:
                updateOCGIdleTransferState();
                break;
            case WRIST_DOWN:
                updateWristDownTransferState();
                break;
        }
    }

    private void updateWristUpTransferState() {
        Bot.intake.misumiWristUp();
        if (Bot.intake.isWristUp()) {
            transferState = TeleopDriver1.TransferState.OCG_UP;
        }
    }

    private void updateOCGUpTransferState() {
        Bot.ocgBox.ocgPitchUp();
        if (Bot.ocgBox.isPitchUp()) {
            transferTimer.reset();
            transferState = TeleopDriver1.TransferState.DROP;
        }
    }

    private void updateDropTransferState() {
        if (transferTimer.milliseconds() > 500) {
            Bot.intakeClaw.openClaw();
        }
        if (Bot.intakeClaw.isClawOpen()) {
            transferTimer.reset();
            transferState = TeleopDriver1.TransferState.OCG_IDLE;
        }
    }

    private void updateOCGIdleTransferState() {
        if (transferTimer.milliseconds() > 3000) {
            Bot.ocgBox.idle();
        }
        if (Bot.ocgBox.isIdle()) {
            transferState = TeleopDriver1.TransferState.WRIST_DOWN;
        }
    }

    private void updateWristDownTransferState() {
        // As there is nothing after, the state is immediately set to NONE
        Bot.intake.misumiWristDown();
        Bot.intakeClaw.closeClaw();
        transferState = TeleopDriver1.TransferState.NONE;
    }

    public void updateTelemetry() {
        telemetry.addData("TransferState", transferState);
        telemetry.addData("North Mode", northModeToggle.get());
        telemetry.addData("Speed", maxSpeed);
        telemetry.update();
    }
}
