package org.firstinspires.ftc.teamcode.OpModes.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Helpers.AutoPaths;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;

import java.io.FileWriter;
import java.io.IOException;

// 1+2 No Wrist Servo
@Autonomous(name="Specimen Auto Old Do Not Use", group="Autos")
public class SpecimenAutoOldDoNotUse extends LinearOpMode {

    // Possible states for the robot in auto
    enum State {
        INIT,
        SPECIMEN_HANG_UP,
        SPECIMEN_HANG_2,
        PUSH_SAMPLE_1,
        PUSH_SAMPLE_2,
        PUSH_SAMPLE_3,
        COLLECT_SPECIMEN_1,
        COLLECT_SPECIMEN_2,
        SPECIMEN_HANG_DOWN,
        PARK,
    }

    public void runOpMode() {
        Bot.init(this, Config.specimenStartX, Config.specimenStartY);
        ElapsedTime timer = new ElapsedTime();
        ElapsedTime analyticsTimer = new ElapsedTime();
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter("/sdcard/SpecHangAnalytics.txt", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            fileWriter.append("---NEW AUTO---\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        double offsetX = 0;
        State state = State.INIT;

        Toggle preloadToggle = new Toggle(false);
        while (!isStarted()) {
            preloadToggle.toggle(gamepad1.a);
            telemetry.addData("Preload", preloadToggle.get());
            telemetry.update();
        }

        double hang_number = 4;
        if (!preloadToggle.get()) {
            hang_number = 3;
        }
        final double initial_hang_number = hang_number;

        double yCollect = 0;

        // redundant
        // waitForStart();

        while (opModeIsActive()) {

            switch (state) {

                // Set the initial path to hang preloaded specimen
                // --> SPECIMEN_HANG_UP
                case INIT:
                    AutoPaths.hangSpecimenUp(
                            Bot.pinpoint.getX(), // Current X
                            Bot.pinpoint.getY(), // Current Y
                            offsetX, // Hang offsetX X
                            0 // Offset Y
                    );
                    Bot.specimenArm.movePrepHangUp(0.6);
                    state = State.SPECIMEN_HANG_UP;
                    break;

                // Go to submersible, hang specimen
                // --> SPECIMEN_HANG_2
                case SPECIMEN_HANG_UP: // Hang specimen right side up
                    Bot.purePursuit.update(0.45);
                    if (Bot.purePursuit.reachedTarget()) { //  || Bot.frontTouchSensor.getVoltage()<.4
                        Bot.purePursuit.stop();
                        Bot.specimenArm.movePostHangUp(1.0); // Hang specimen
                        timer.reset(); // Reset timer for next state
                        state = State.SPECIMEN_HANG_2;
                    }
                    break;

                // Release specimen at submersible, set next path (3 possibilities)
                // stop --> PARK; collect --> COLLECT_SPECIMEN_1; else --> PUSH_SAMPLE_1
                case SPECIMEN_HANG_2:
                    if (timer.seconds() > 0.3) { // Wait for arm to go down
                        Bot.specimenArm.openClaw();
                        if (hang_number == 0) {
                            // Set path to observation zone to park (PARK)
                            AutoPaths.park(
                                    Bot.pinpoint.getX(), // Current X
                                    Bot.pinpoint.getY() // Current Y
                            );
                            Bot.specimenArm.moveToIdle();
                            state = State.PARK;
                            break;
                        } else if (hang_number < initial_hang_number) {
                            // Set path to observation zone to grab specimen (COLLECT_SPECIMEN_1)
                            AutoPaths.collectSpecimen(
                                    Bot.pinpoint.getX(), // Current X
                                    Bot.pinpoint.getY(), // Current Y
                                    true // Coming from the submersible
                            );
                            Bot.specimenArm.moveToCollect(0.4);
                            state = State.COLLECT_SPECIMEN_1;
                            break;
                        } else {
                            // Set path to push first sample into observation zone (PUSH_SAMPLE_1)
                            AutoPaths.pushSample1(
                                    Bot.pinpoint.getX(), // Current X
                                    Bot.pinpoint.getY() // Current Y
                            );
                            timer.reset();
                            Bot.specimenArm.moveToCollect(0.4);
                            state = State.PUSH_SAMPLE_1;
                        }
                    }
                    // Move to next position
                    break;

                // Bring first sample into observation zone, set path to bring second sample to
                // observation zone
                // --> PUSH_SAMPLE_2
                case PUSH_SAMPLE_1:
                    Bot.purePursuit.update(0.85);
                    if (Bot.purePursuit.reachedTarget(5)) {
                        AutoPaths.pushSample2(
                                Bot.pinpoint.getX(), // Current X
                                Bot.pinpoint.getY() // Current Y
                        );
                        state = State.PUSH_SAMPLE_2;
                    }
                    break;

                // Bring second sample into observation zone, set path to collect specimen on fence
                // --> COLLECT_SPECIMEN_1
                case PUSH_SAMPLE_2:
                    Bot.purePursuit.update(0.75);
                    if (Bot.purePursuit.reachedTarget(5)) {
                        AutoPaths.pushSample3(
                                Bot.pinpoint.getX(), // Current X
                                Bot.pinpoint.getY() // Current Y
                        );
                        state = State.PUSH_SAMPLE_3;
                    }
                    break;

                case PUSH_SAMPLE_3:
                    Bot.purePursuit.update(0.65);
                    if (Bot.purePursuit.reachedTarget(5)) {
                        AutoPaths.collectSpecimen(
                                Bot.pinpoint.getX(), // Current X
                                Bot.pinpoint.getY(), // Current Y
                                false // Not coming from the submersible
                        );
//                        double[][] turnPath = SplineCalc.linearPath(new double[]{0, 0.25, 0.75, 1}, new double[]{Math.PI / 2, Math.PI / 2, 0, 0}, 25);
//                        Bot.purePursuit.setTurnPath(turnPath);
//                        Bot.purePursuit.setTurnMultiplier(1.25);
                        state = State.COLLECT_SPECIMEN_1;
                    }
                    break;

                // Go to and collect specimen on fence
                // --> COLLECT_SPECIMEN_2
                case COLLECT_SPECIMEN_1:
                    Bot.purePursuit.update(0.5, true);
                    if (Bot.purePursuit.reachedTargetXY(1.5, 0.75)) {
                        Bot.purePursuit.stop();
                        Bot.specimenArm.closeClaw();
                        timer.reset(); // Reset timer for next state
                        // Get cycle time and reset timer
                        if (hang_number < 4) {
                            double cycleTime = analyticsTimer.seconds();
                            try {
                                fileWriter.append("Cycle ").append(String.valueOf((int) (4 - hang_number))).append(",").append(String.valueOf(cycleTime)).append("\n");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        analyticsTimer.reset();
                        state = State.COLLECT_SPECIMEN_2;
                    }
                    break;

                // Wait to grab specimen from fence, set path to hang specimen with an offsetX
                // from the original
                // --> SPECIMEN_HANG_DOWN
                case COLLECT_SPECIMEN_2:
                    if (timer.seconds() > 0.3) { // Wait to grab the specimen
                        yCollect = Bot.pinpoint.getY();
                        offsetX += Config.hangOffset; // Adjust the hang offsetX
                        AutoPaths.hangSpecimen(
                                Bot.pinpoint.getX(), // Current X
                                Bot.pinpoint.getY(), // Current Y
                                offsetX, // Hang offsetX X
                                0 // Offset Y
                        );
                        Bot.specimenArm.movePrepHang(0.5);
                        state = State.SPECIMEN_HANG_DOWN;
                    }
                    break;

                // Go to submersible and hang specimen
                // First time: Set collect to true. Second time: Set stop to true.
                // --> SPECIMEN_HANG_2 (Creates a loop)
                case SPECIMEN_HANG_DOWN: // Hang specimen upside down
                    Bot.purePursuit.update(0.525, true);
                    // TODO: get good tolerance
                    if (Bot.purePursuit.reachedTarget(1.0)) { // || Bot.frontTouchSensor.getVoltage()<.4
                        Bot.purePursuit.stop();
                        Bot.specimenArm.movePostHang(1.0); // Move arm down
                        timer.reset();
                        hang_number--;
                        // Waits for arm to go down
                        // Decides what to do based on stop and collect booleans
                        state = State.SPECIMEN_HANG_2;
                    }
                    break;

                // Wait to reach observation zone to park
                // End of auto
                case PARK:
                    Bot.purePursuit.update(1.5);
                    if (Bot.purePursuit.reachedTarget(5)) {
                        terminateOpModeNow();
                    }
                    break;
            }

            Bot.pinpoint.update();
            telemetry.addData("State", state);
            telemetry.addData("Y Collect", yCollect);
            telemetry.addData("X", Bot.pinpoint.getX());
            telemetry.addData("Y", Bot.pinpoint.getY());
            telemetry.addData("Theta", Bot.pinpoint.getHeading());
            telemetry.update();
            Bot.dashboardTelemetry.update();
        }
        try {
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Bot.currentThreads.stopThreads();
    }
}
