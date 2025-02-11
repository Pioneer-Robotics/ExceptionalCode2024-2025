package org.firstinspires.ftc.teamcode.OpModes.SubSystems;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bot;

public class RobotTransferSubSystem {

    // State Enum
    public enum TransferState {
        NONE,
        OCG_UP,
        WRIST_UP,
        DROP,
        OCG_IDLE,
        WRIST_DOWN
    }

    // Properties
    Gamepad gamepad;
    Telemetry telemetry;

    ElapsedTime transferTimer;

    RobotTransferSubSystem.TransferState transferState;

    // Initialization
    // Private constructor to enforce the use of the factory method
    private RobotTransferSubSystem(Gamepad gamepad, Telemetry telemetry) {
        this.gamepad = gamepad;
        this.telemetry = telemetry;

        transferTimer = new ElapsedTime();
    }

    // Factory method to create instances with variables
    public static RobotTransferSubSystem createInstance(Gamepad gamepad1, Telemetry telemetry) {
        return new RobotTransferSubSystem(gamepad1, telemetry);
    }

    // Run Loop
    public void doOneStateLoop() {
        resetTransferStateToNone();
        handleTransferEntryPointAndInterrupt();
        updateTransferState();
        updateTelemetry();
    }

    // SubSystem private methods
    private void resetTransferStateToNone() {
        transferState = RobotTransferSubSystem.TransferState.NONE;
    }

    private void handleTransferEntryPointAndInterrupt() {
        if (gamepad.dpad_right) {
            transferState = RobotTransferSubSystem.TransferState.WRIST_UP;
        }
        if (gamepad.touchpad) {
            transferState = RobotTransferSubSystem.TransferState.NONE;
        }
    }

    public void updateTransferState() {
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
            transferState = RobotTransferSubSystem.TransferState.OCG_UP;
        }
    }

    private void updateOCGUpTransferState() {
        Bot.ocgBox.ocgPitchUp();
        if (Bot.ocgBox.isPitchUp()) {
            transferTimer.reset();
            transferState = RobotTransferSubSystem.TransferState.DROP;
        }
    }

    private void updateDropTransferState() {
        if (transferTimer.milliseconds() > 500) {
            Bot.intakeClaw.openClaw();
        }
        if (Bot.intakeClaw.isClawOpen()) {
            transferTimer.reset();
            transferState = RobotTransferSubSystem.TransferState.OCG_IDLE;
        }
    }

    private void updateOCGIdleTransferState() {
        if (transferTimer.milliseconds() > 3000) {
            Bot.ocgBox.idle();
        }
        if (Bot.ocgBox.isIdle()) {
            transferState = RobotTransferSubSystem.TransferState.WRIST_DOWN;
        }
    }

    private void updateWristDownTransferState() {
        // As there is nothing after, the state is immediately set to NONE
        Bot.intake.misumiWristDown();
        Bot.intakeClaw.closeClaw();
        transferState = RobotTransferSubSystem.TransferState.NONE;
    }

    public void updateTelemetry() {
        telemetry.addData("TransferState", transferState);
        telemetry.update();
    }

    public TransferState getTransferState() {
        return transferState;
    }
}
