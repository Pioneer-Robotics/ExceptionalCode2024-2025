package org.firstinspires.ftc.teamcode.OpModes.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Helpers.AutoPaths;

// 1+2 No Wrist Servo
@Autonomous(name="Specimen Auto", group="Autos")
public class SpecimenAuto extends LinearOpMode {

    // Possible states for the robot in auto
    enum State {
        INIT,
        SPECIMEN_HANG_UP,
        SPECIMEN_HANG_2,
        PUSH_SAMPLE_1,
        PUSH_SAMPLE_2,
        COLLECT_SPECIMEN_1,
        COLLECT_SPECIMEN_2,
        SPECIMEN_HANG_DOWN,
        PARK,
    }

    public void runOpMode() {
        Bot.init(this, Config.specimenStartX, Config.specimenStartY);
        ElapsedTime timer = new ElapsedTime();

        double offsetX = 0;
        boolean stop = false;
        boolean collect = false;
        State state = State.INIT;
        Bot.intake.openMisumiWrist();

        // Home specimen arm
        Bot.specimenArm.homeArm();

        waitForStart();
        while (opModeIsActive()) {

            switch (state) {

                // Set the initial path to hang preloaded specimen
                // --> SPECIMEN_HANG_UP
                case INIT:
                    AutoPaths.hangSpecimen(
                            Bot.pinpoint.getX(), // Current X
                            Bot.pinpoint.getY(), // Current Y
                            offsetX, // Hang offsetX X
                            2 // Offset Y
                    );
                    Bot.specimenArm.movePrepHangUp(0.5);
                    state = State.SPECIMEN_HANG_UP;
                    break;

                // Go to submersible, hang specimen
                // --> SPECIMEN_HANG_2
                case SPECIMEN_HANG_UP: // Hang specimen right side up
                    Bot.purePursuit.update(0.35, true);
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
                    if (timer.seconds() > 0.5) { // Wait for arm to go down
                        Bot.specimenArm.openClaw();
                        if (stop) {
                            // Set path to observation zone to park (PARK)
                            AutoPaths.park(
                                    Bot.pinpoint.getX(), // Current X
                                    Bot.pinpoint.getY() // Current Y
                            );
                            Bot.specimenArm.moveToIdle();
                            state = State.PARK;
                            break;
                        } else if (collect) {
                            // Set path to observation zone to grab specimen (COLLECT_SPECIMEN_1)
                            AutoPaths.collectSpecimen(
                                    Bot.pinpoint.getX(), // Current X
                                    Bot.pinpoint.getY(), // Current Y
                                    true // Coming from the submersible
                            );
                            Bot.specimenArm.moveToCollect(0.3);
                            state = State.COLLECT_SPECIMEN_1;
                            break;
                        } else {
                            // Set path to push first sample into observation zone (PUSH_SAMPLE_1)
                            AutoPaths.pushSample1(
                                    Bot.pinpoint.getX(), // Current X
                                    Bot.pinpoint.getY() // Current Y
                            );
                            timer.reset();
                            state = State.PUSH_SAMPLE_1;
                        }
                    }
                    // Move to next position
                    break;

                // Bring first sample into observation zone, set path to bring second sample to
                // observation zone
                // --> PUSH_SAMPLE_2
                case PUSH_SAMPLE_1:
                    Bot.purePursuit.update(0.75);
                    if (Bot.purePursuit.reachedTarget(4)) {
                        AutoPaths.pushSample2(
                                Bot.pinpoint.getX(), // Current X
                                Bot.pinpoint.getY() // Current Y
                        );
                        Bot.specimenArm.moveToCollect(0.5);
                        state = State.PUSH_SAMPLE_2;
                    }
                    break;

                // Bring second sample into observation zone, set path to collect specimen on fence
                // --> COLLECT_SPECIMEN_1
                case PUSH_SAMPLE_2:
                    Bot.purePursuit.update(0.75);
                    if (Bot.purePursuit.reachedTarget(4)) {
                        AutoPaths.collectSpecimen(
                                Bot.pinpoint.getX(), // Current X
                                Bot.pinpoint.getY(), // Current Y
                                false // Not coming from the submersible
                        );
                        state = State.COLLECT_SPECIMEN_1;
                    }
                    break;

                // Go to and collect specimen on fence
                // --> COLLECT_SPECIMEN_2
                case COLLECT_SPECIMEN_1:
                    Bot.purePursuit.update(0.35, true);
                    if (Bot.purePursuit.reachedTarget()) {
                        Bot.purePursuit.stop();
                        Bot.specimenArm.closeClaw();
                        timer.reset(); // Reset timer for next state
                        state = State.COLLECT_SPECIMEN_2;
                    }
                    break;

                // Wait to grab specimen from fence, set path to hang specimen with an offsetX
                // from the original
                // --> SPECIMEN_HANG_DOWN
                case COLLECT_SPECIMEN_2:
                    if (timer.seconds() > 0.5) { // Wait to grab the specimen
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
                    Bot.purePursuit.update(0.4, true);
                    if (Bot.purePursuit.reachedTarget()) { // || Bot.frontTouchSensor.getVoltage()<.4
                        Bot.purePursuit.stop();
                        Bot.specimenArm.movePostHang(1.0); // Move arm down
                        timer.reset();
                        if (collect) {
                            stop = true; // for 1+2
                        } else {
                            collect = true;
                        }
                        // Waits for arm to go down
                        // Decides what to do based on stop and collect booleans
                        state = State.SPECIMEN_HANG_2;
                    }
                    break;

                // Wait to reach observation zone to park
                // End of auto
                case PARK:
                    Bot.purePursuit.update(0.75, true);
                    if (Bot.purePursuit.reachedTarget(5)) {
                        terminateOpModeNow();
                    }
                    break;
            }

            Bot.pinpoint.update();
            telemetry.addData("State", state);
            telemetry.addData("X", Bot.pinpoint.getX());
            telemetry.addData("Y", Bot.pinpoint.getY());
            telemetry.addData("Theta", Bot.pinpoint.getHeading());
            telemetry.update();
        }
    }
}
