package org.firstinspires.ftc.teamcode.OpModes.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

@Autonomous(name="Pre-Flight Check Auto", group="Autos")
public class PreFlightCheck extends LinearOpMode {
    // Possible states for the robot in auto
    enum State {
        INIT,
        TEST_DRIVE,
        TEST_SPECIMEN_ARM,
        TEST_INTAKE,
        TEST_TRANSFER,
        TEST_SLIDE,
        TEST_OCG_BOX,
        SET_FOR_MATCH
    }

    // Properties
    PreFlightCheck.State state;
    ElapsedTime timer;

    // Run Loop
    public void runOpMode() {
        initalize();

        // redundant
        // waitForStart();

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
        state = State.TEST_DRIVE;
    }
    private void handleTestDriveState() {
        // Add testing Movements HERE
        state = State.TEST_SPECIMEN_ARM;
    }
    private void handleTestSpecimenArmState() {
        // Add testing Movements HERE
        state = State.TEST_INTAKE;
    }
    private void handleTestIntakeState() {
        // Add testing Movements HERE
        state = State.TEST_TRANSFER;
    }
    private void handleTestTransferState() {
        // Add testing Movements HERE
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
