package org.firstinspires.ftc.teamcode.OpModes.Teleop.Drivers;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.OpModes.Testing.BlockDetector;

public class TeleopDriver1 {
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

    private enum IntakeState {
        NONE,
        MID_WRIST,
        EXTEND
    }

    private enum SeachingState {
        OUT,
        IN,
        NONE
    }

    // Variables
    private double speed = 0.5;
    private boolean northMode = true;
    private IntakeState intakeState = IntakeState.NONE;
    private TransferState transferState = TransferState.NONE;
    boolean useCameraCode;
    BlockDetector detector;
    SeachingState seachingState = SeachingState.NONE;

    // Toggles
    private final Toggle northModeToggle = new Toggle(true);
    private final Toggle incSpeedToggle = new Toggle(false);
    private final Toggle decSpeedToggle = new Toggle(false);
    private final Toggle resetIMUToggle = new Toggle(false);
    private final Toggle extendIntakeToggle = new Toggle(false);
    private final Toggle intakeClawToggle = new Toggle(false);
    private final Toggle intakeWristToggle = new Toggle(false);
    private final Toggle intakeTransferToggle = new Toggle(false);
    private final Toggle cameraToggle = new Toggle(false);

    private TeleopDriver1(Gamepad gamepad, boolean useCameraCode, BlockDetector detector) {
        this.gamepad = gamepad;
        this.useCameraCode = useCameraCode;
        this.detector = detector;
    }

    public static TeleopDriver1 createInstance(Gamepad gamepad, boolean useCameraCode, BlockDetector detector) {
        return new TeleopDriver1(gamepad, useCameraCode, detector);
    }

    public void loopGamepad() {
        handleSpeedControls();
        if (useCameraCode) {
            handleCamera();
        }
        handleNorthMode();
        handleIMUReset();
        driveRobot();
        updateIntakeState();
        driveIntake();
        toggleIntakeClaw();
        toggleIntakeWrist();
        updateTransferState();
        handleTransfer();
    }

    private void handleCamera() {
        updateCameraButtonState();
        updateRobotBasedOnCameraState();
    }

    private void updateCameraButtonState() {
        cameraToggle.toggle(gamepad.back);

        // Change state based on button
        if (cameraToggle.justChanged() && cameraToggle.get()) {
            boolean isArmOut = true; // TODO: -
            boolean isArmMid = false; // TODO: -

            if (isArmOut) {
                seachingState = SeachingState.IN;
            } else if (isArmMid) {
                seachingState = SeachingState.OUT;
            } else {
                // In the middle of out and in so do nothing
            }
        }
    }

    private void updateRobotBasedOnCameraState() {
        // update robot based on current state
        switch (seachingState) {
            case IN:
                if (Bot.intake.isWristMid()) {
                    // if at mid - change state to none
                    seachingState = SeachingState.NONE;
                } else {
                    if (detector.isTeamColorFound()) {
                        teamColorPieceFound();
                    } else {
                        // move in more
                        Bot.intake.decrementExtendMisumiDrive();
                    }
                }
            case OUT:
                if (Bot.intake.isExtended()) {
                    // if at end - change state to none
                    seachingState = SeachingState.NONE;
                } else {
                    if (detector.isTeamColorFound()) {
                        teamColorPieceFound();
                    } else {
                        // else move out more
                        Bot.intake.incrementExtendMisumiDrive();
                    }
                }
            case NONE:
                break;
        };
    }

    private void teamColorPieceFound() {
        // if found then haptic feedback
        gamepad.rumble(500);

        // stop state
        seachingState = SeachingState.NONE;

        // Move claw to angle
        double angleInDegrees = detector.getTeamColorAngle();
        double convertedAngle = angleInDegrees/360;
        Bot.intakeClaw.yawAnyPosAngle(convertedAngle);
    }

    private void handleSpeedControls() {
        incSpeedToggle.toggle(gamepad.right_bumper);
        decSpeedToggle.toggle(gamepad.left_bumper);
        if (incSpeedToggle.justChanged()) {
            speed += 0.1;
        }
        if (decSpeedToggle.justChanged()) {
            speed -= 0.1;
        }
    }

    private void handleNorthMode() {
        northModeToggle.toggle(gamepad.left_trigger > 0.8 && gamepad.right_trigger > 0.8);
        northMode = northModeToggle.get();
        Bot.mecanumBase.setNorthMode(northMode);
    }

    private void handleIMUReset() {
        resetIMUToggle.toggle(gamepad.x);
        if (resetIMUToggle.justChanged()) {
            Bot.pinpoint.resetIMU();
        }
    }

    private void driveRobot() {
        double px = Math.pow(gamepad.left_stick_x, 3);
        double py = -Math.pow(gamepad.left_stick_y, 3);
        double turn = gamepad.right_stick_x * (Bot.intake.isExtended() ? Config.EXTENSION_TURN_POWER_DEMULTIPLIER : 1);
        Bot.mecanumBase.move(px, py, turn, speed);
    }

    private void updateIntakeState() {
        // Toggle prevents the state being set multiple times if the button is held
        extendIntakeToggle.toggle(gamepad.dpad_up);
        if (extendIntakeToggle.justChanged()) {
            intakeState = IntakeState.MID_WRIST;
        } else if (gamepad.dpad_down) {
            intakeState = IntakeState.NONE;
            Bot.intake.midMisumiWrist();
            Bot.intake.retractMisumiDrive();
            Bot.intakeClaw.clawUp();
        }
    }

    private void driveIntake() {
        switch (intakeState) {
            case NONE:
                break;
            case MID_WRIST:
                Bot.intake.midMisumiWrist();
                if (Bot.intake.isWristMid()) {
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

        boolean isInDownState = intakeWristToggle.justChanged() && intakeWristToggle.get();
        boolean isInUpState = intakeWristToggle.justChanged() && !intakeWristToggle.get();
        boolean isIntakeExtended = Bot.intake.isExtended();

        if (isInDownState) {
            if (isIntakeExtended) {
                Bot.intake.misumiWristDown();
            } else {
                Bot.intake.midMisumiWrist();
            }
        } else if (isInUpState) {
            if (isIntakeExtended) {
                Bot.intake.midMisumiWrist();
            } else {
                Bot.intake.misumiWristUp();
                Bot.ocgBox.ocgPitchUp();
            }
        }
    }

    private void updateTransferState() {
        intakeTransferToggle.toggle(gamepad.dpad_right);
        if (intakeTransferToggle.justChanged()) {
            transferState = TransferState.WRIST_UP;
        }
    }

    private void handleTransfer() {
        switch (transferState) {
            case NONE:
                break;

            case WRIST_UP:
                Bot.intake.misumiWristUp();
                timer.reset();
                transferState = TransferState.OCG_UP;
                break;

            case OCG_UP:
                Bot.ocgBox.ocgPitchUp();
                if (timer.milliseconds() > 500) {
                    timer.reset();
                    Bot.intakeClaw.openClaw();
                    transferState = TransferState.DROP;
                }
                break;

            case DROP:
                if (timer.milliseconds() > 750) {
                    Bot.ocgBox.idle();
                    transferState = TransferState.OCG_IDLE;
                }
                break;

            case OCG_IDLE:
                transferState = TransferState.WRIST_DOWN;
                break;

            case WRIST_DOWN:
                // As there is nothing after, the state is immediately set to NONE
                Bot.intake.misumiWristDown();
                Bot.intakeClaw.closeClaw();
                transferState = TransferState.NONE;
        }
    }

    public boolean getNorthModeToggle() {
        return northMode;
    }

    public double getSpeed() {
        return speed;
    }
}
