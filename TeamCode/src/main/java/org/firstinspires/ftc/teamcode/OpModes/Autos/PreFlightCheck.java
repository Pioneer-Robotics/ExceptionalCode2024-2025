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

    private enum IntakeState {
        ARM_OUT,
        CLAW_OPEN,
        DOWN,
        CLAW_CLOSE,
        UP
    }

    // Properties
    PreFlightCheck.State state;
    DriveTestState driveTestState;
    SpecimenArmTestState specimenArmTestState;
    IntakeState intakeState;
    ElapsedTime timer;
    RobotTransferSubSystem robotTransferSubSystem;

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
                    handleTestSlideState();
                    break;
                case TEST_OCG_BOX:
                    handleTestOcgBoxState();
                    break;
                case SET_FOR_MATCH:
                    handleTestSetForMatchState();
                    break;
            }

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
        robotTransferSubSystem = RobotTransferSubSystem.createInstance(gamepad1, telemetry);
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
        // TODO
        state = State.TEST_SLIDE;
    }
    private void handleTestSlideState() {
        // Add testing Movements HERE
        state = State.TEST_OCG_BOX;
    }
    private void handleTestOcgBoxState() {
        // Add testing Movements HERE
        state = State.SET_FOR_MATCH;
    }

    private void handleTestSetForMatchState() {
        // Add testing Movements HERE
        Bot.purePursuit.update(1);
        if (Bot.purePursuit.reachedTarget(5)) {
            terminateOpModeNow();
        }
    }

    private void updateTelemetry() {
        // Add telemetry statements HERE

        Bot.pinpoint.update();

        telemetry.update();
        Bot.dashboardTelemetry.addData("X", Bot.pinpoint.getX());
        Bot.dashboardTelemetry.addData("Y", Bot.pinpoint.getY());
        Bot.dashboardTelemetry.update();
    }

}
