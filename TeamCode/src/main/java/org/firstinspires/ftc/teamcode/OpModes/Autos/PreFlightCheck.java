package org.firstinspires.ftc.teamcode.OpModes.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Helpers.SplineCalc;
import org.firstinspires.ftc.teamcode.OpModes.SubSystems.RobotTransferSubSystem;

@Autonomous(name="Pre-Flight Check Auto", group="Autos")
public class PreFlightCheck extends LinearOpMode {
    // Possible states for the robot in auto
    private enum State {
        INIT,
        TEST_DRIVE,
        TEST_SPECIMEN_ARM,
        TEST_INTAKE,
        TEST_TRANSFER,
        TEST_SLIDE,
        TEST_OCG_BOX,
        SET_FOR_MATCH
    }

    private enum DriveTestState {
        FORWARDS,
        BACKWARDS
    }

    private enum SpecimenArmTestState {
        MOVE_TO_HANG,
        MOVE_TO_COLLECT
    }

    public enum TransferTestState {
        OCG_UP,
        WRIST_UP,
        DROP,
        OCG_IDLE,
        WRIST_DOWN
    }

    private enum IntakeState {
        ARM_OUT,
        CLAW_OPEN,
        DOWN,
        CLAW_CLOSE,
        UP
    }

    private enum SlideTestState {
        SLIDE_UP,
        SLIDE_DOWN
    }

    // Properties
    PreFlightCheck.State state;
    DriveTestState driveTestState;
    SpecimenArmTestState specimenArmTestState;
    IntakeState intakeState;
    ElapsedTime timer;
    TransferTestState transferTestState;
    SlideTestState slideTestState;

    // Run Loop
    public void runOpMode() {
        initalize();

        waitForStart();

        while (opModeIsActive()) {
            switch (state) {
                case INIT:
                    handleInitState();
                    break;
                case TEST_DRIVE:
                    handleTestDriveState();
                    break;
                case TEST_SPECIMEN_ARM:
                    handleTestSpecimenArmState();
                    break;
                case TEST_INTAKE:
                    handleTestIntakeState();
                    break;
                case TEST_TRANSFER:
                    handleTestTransferState();
                    break;
                case TEST_SLIDE:
                    // handleTestSlideState();
                    // FIXME: Temporary for testing without slide
                    state = State.TEST_OCG_BOX;
                    break;
                case TEST_OCG_BOX:
                    handleTestOcgBoxState();
                    break;
                case SET_FOR_MATCH:
                    handleTestSetForMatchState();
                    break;
            }
            Bot.pinpoint.update();
            updateTelemetry();
        }
        Bot.currentThreads.stopThreads();
    }

    // Private Methods
    private void initalize() { // This method could be further refactored
        Bot.init(this, Config.specimenStartX, Config.specimenStartY);
        timer = new ElapsedTime();
        state = State.INIT;
    }

    private void handleInitState() {
        // Add testing Movements
        driveTestState = DriveTestState.FORWARDS;
        specimenArmTestState = SpecimenArmTestState.MOVE_TO_HANG;
        intakeState = IntakeState.ARM_OUT;
        transferTestState = TransferTestState.OCG_UP;
        slideTestState = SlideTestState.SLIDE_UP;
        state = State.TEST_DRIVE;
    }
    private void handleTestDriveState() {
        switch (driveTestState) {
            case FORWARDS:
                double[][] forwardPath = SplineCalc.nDegBez(new double[] {0, 0}, new double[] {0, 100}, 10);
                testDrivePath(forwardPath);
                if (Bot.purePursuit.reachedTarget()) {
                    driveTestState = DriveTestState.BACKWARDS;
                }
                break;

            case BACKWARDS:
                double[][] backwardsPath = SplineCalc.nDegBez(new double[] {0, 0}, new double[] {0, 100}, 10);
                testDrivePath(backwardsPath);
                if (Bot.purePursuit.reachedTarget()) {
                    state = State.TEST_SPECIMEN_ARM;
                }
                break;
        }
        telemetry.addData("X", Bot.pinpoint.getX());
        telemetry.addData("Y", Bot.pinpoint.getY());
    }

    private void testDrivePath(double[][] path) {
        Bot.purePursuit.setTargetPath(path);
        Bot.purePursuit.update();
    }

    private void handleTestSpecimenArmState() {
        switch (specimenArmTestState) {
            case MOVE_TO_HANG:
                Bot.specimenArm.movePrepHang(0.5);
                // TODO: Specimen arm reached position with end stop switch
                if (Bot.specimenArm.reachedPosition()) {
                    specimenArmTestState = SpecimenArmTestState.MOVE_TO_COLLECT;
                }
                break;
            case MOVE_TO_COLLECT:
                Bot.specimenArm.moveToCollect(0.5);
                if (Bot.specimenArm.reachedPosition()) {
                    state = State.TEST_INTAKE;
                }
                break;
        }
    }
    private void handleTestIntakeState() {
        // Add testing Movements HERE
        switch (intakeState) {
            case ARM_OUT:
                Bot.intake.extendMisumiDrive();
                if (Bot.intake.isExtended()) {
                    intakeState = IntakeState.CLAW_OPEN;
                }
                break;
            case CLAW_OPEN:
                Bot.intakeClaw.openClaw();
                if (Bot.intakeClaw.isClawOpen()) {
                    intakeState = IntakeState.DOWN;
                }
                break;
            case DOWN:
                Bot.intake.misumiWristDown();
                if (Bot.intake.isWristDown()) {
                    intakeState = IntakeState.CLAW_CLOSE;
                }
                break;
            case CLAW_CLOSE:
                Bot.intakeClaw.closeClaw();
                if (Bot.intakeClaw.isClawClosed()) {
                    intakeState = IntakeState.UP;
                }
                break;
            case UP:
                Bot.intake.midMisumiWrist();
                if (Bot.intake.isWristMid()) {
                    state = State.TEST_TRANSFER;
                }
                break;
        }
    }
    private void handleTestTransferState() {
        switch (transferTestState) {
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
            transferTestState = TransferTestState.OCG_UP;
        }
    }

    private void updateOCGUpTransferState() {
        Bot.ocgBox.ocgPitchUp();
        if (Bot.ocgBox.isPitchUp()) {
            timer.reset();
            transferTestState = TransferTestState.DROP;
        }
    }

    private void updateDropTransferState() {
        if (timer.milliseconds() > 500) {
            Bot.intakeClaw.openClaw();
        }
        if (Bot.intakeClaw.isClawOpen()) {
            timer.reset();
            transferTestState = TransferTestState.OCG_IDLE;
        }
    }

    private void updateOCGIdleTransferState() {
        if (timer.milliseconds() > 3000) {
            Bot.ocgBox.idle();
        }
        if (Bot.ocgBox.isIdle()) {
            transferTestState = TransferTestState.WRIST_DOWN;
        }
    }

    private void updateWristDownTransferState() {
        // As there is nothing after, the state is immediately set to NONE
        Bot.intake.misumiWristDown();
        Bot.intakeClaw.closeClaw();
        state = State.TEST_SLIDE;
    }

    private void handleTestSlideState() {
        switch (slideTestState) {
            case SLIDE_UP:
                Bot.slideArm.moveUp(0.5);
                if (Bot.slideArm.reachedPosition()) {
                    slideTestState = SlideTestState.SLIDE_DOWN;
                }
                break;
            case SLIDE_DOWN:
                Bot.slideArm.moveDown(0.5);
                if (Bot.slideArm.reachedPosition()) {
                    state = State.TEST_OCG_BOX;
                }
                break;
        }
    }
    private void handleTestOcgBoxState() {
        // Add testing Movements HERE
        state = State.SET_FOR_MATCH;
    }

    private void handleTestSetForMatchState() {
        terminateOpModeNow();
    }

    private void updateTelemetry() {
        telemetry.update();
        Bot.dashboardTelemetry.update();
    }

}
