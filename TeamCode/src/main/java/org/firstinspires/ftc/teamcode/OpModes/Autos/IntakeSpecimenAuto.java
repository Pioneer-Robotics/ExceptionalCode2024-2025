package org.firstinspires.ftc.teamcode.OpModes.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Helpers.AutoPaths;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;

@Autonomous(name="Intake Specimen Auto", group="Autos")
public class IntakeSpecimenAuto extends LinearOpMode {
    // Possible states for the robot in auto
    enum State {
        INIT,
        INIT_SPECIMEN_HANG,
        GOTO_MOVE_SAMPLE,
        GRAB_SAMPLE,
        MOVE_SAMPLE_RIGHT,
        MOVE_SAMPLE_LEFT,
        COLLECT_SPECIMEN_1,
        COLLECT_SPECIMEN_2,
        COLLECT_SPECIMEN_3,
        HANG_SPECIMEN,
        PARK,
    }

    public void runOpMode() {
        Bot.init(this, Config.specimenStartX, Config.specimenStartY);
        ElapsedTime timer = new ElapsedTime();

        double offsetX = 0;
        State state = State.INIT;

        Toggle preloadToggle = new Toggle(false);
        while (!isStarted()) {
            preloadToggle.toggle(gamepad1.a);
            telemetry.addData("Preload", preloadToggle.get());
            telemetry.addData("pinpointX", Bot.pinpoint.getX());
            telemetry.addData("pinpointY", Bot.pinpoint.getY());
            telemetry.update();
        }

        double hang_number = 4;
        if (!preloadToggle.get()) {
            hang_number = 3;
        }
        final double initial_hang_number = hang_number;

        double yCollect = 0;
        int collectSampleNum = 1;

        waitForStart();

        timer.reset();
        while(opModeIsActive()) {

            switch(state) {
                // Set the initial path to hang preloaded specimen
                // --> INIT_SPECIMEN_HANG
                case INIT:
                    AutoPaths.hangInitSpecimenRam(
                            Bot.pinpoint.getX(), // Current X
                            Bot.pinpoint.getY(), // Current Y
                            offsetX, // Hang offsetX X
                            0 // Offset Y
                    );
//                    Bot.specimenArm.movePrepHangUp(0.6);
                    state = State.INIT_SPECIMEN_HANG;
                    break;

                // Go to submersible, hang specimen
                // --> GOTO_MOVE_SAMPLE
                case INIT_SPECIMEN_HANG:
                    Bot.purePursuit.update(0.2);
                    if (Bot.purePursuit.reachedTarget()) { //  || Bot.frontTouchSensor.getVoltage()<.4
                        Bot.purePursuit.stop();
//                        Bot.specimenArm.movePostHangUp(1.0); // Hang specimen
                        timer.reset(); // Reset timer for next state
                        state = State.GOTO_MOVE_SAMPLE;
                    }
                    break;

                // Release specimen and set path to sample position
                // --> GRAB_SAMPLE
                case GOTO_MOVE_SAMPLE:
                    if (timer.seconds() > 0.3) { // Wait for arm to go down
//                        Bot.specimenArm.openClaw();
                        AutoPaths.goToPickupPos(
                                Bot.pinpoint.getX(),
                                Bot.pinpoint.getY(),
                                Bot.pinpoint.getHeading(),
                                8 * (collectSampleNum - 1)
                        );
                        state = State.GRAB_SAMPLE;
                    }
                    break;

                // Complete path to sample position, extend slide, and when extended grab sample
                // --> MOVE_SAMPLE_RIGHT
                case GRAB_SAMPLE:
                    Bot.purePursuit.update(0.2);
                    if (Bot.purePursuit.reachedTarget(1)) {
                        Bot.intakeClaw.clawNeg45();
                        Bot.intakeClaw.clawDown();
                        Bot.intake.extendMisumiDrive();

                        if (Bot.intake.isExtended()) {
                            Bot.intakeClaw.closeClaw();
                            state = State.MOVE_SAMPLE_RIGHT;
                            timer.reset();
                        }
                    }
                    break;

                // Set path to move sample to observation zone
                // --> MOVE_SAMPLE_LEFT
                case MOVE_SAMPLE_RIGHT:
                    if (timer.seconds() > 0.3) {
                        Bot.intakeClaw.clawUp();
                        AutoPaths.moveSampleRight(
                                Bot.pinpoint.getX(),
                                Bot.pinpoint.getY(),
                                Bot.pinpoint.getHeading()
                        );
                        state = State.MOVE_SAMPLE_LEFT;
                    }
                    break;

                // Complete path to observation zone and release specimen
                // If there are samples left --> GOTO_MOVE_SAMPLE, no more samples --> COLLECT_SPECIMEN_1
                case MOVE_SAMPLE_LEFT:
                        Bot.purePursuit.update(0.2);
                        if (Bot.purePursuit.reachedTarget()) {
                            Bot.intakeClaw.openClaw();
                            collectSampleNum++;

                            if (collectSampleNum > 3) {
                                state = State.COLLECT_SPECIMEN_1;
                            } else {
                                state = State.GOTO_MOVE_SAMPLE;
                            }
                        }
                    break;

                // Set path to collect position
                // Everything is hung --> PARK, else --> COLLECT_SPECIMEN_2
                case COLLECT_SPECIMEN_1:
                    boolean fromSub;
                    fromSub = initial_hang_number != hang_number;
                    if (hang_number == 0) {
                        state = State.PARK;
                    } else {
                        AutoPaths.goToCollect(
                                Bot.pinpoint.getX(),
                                Bot.pinpoint.getY(),
                                Bot.pinpoint.getHeading(),
                                fromSub
                        );
                        state = State.COLLECT_SPECIMEN_2;
                    }
                    break;

                // Complete path to specimen and close claw
                // --> COLLECT_SPECIMEN_3
                case COLLECT_SPECIMEN_2:
                    Bot.purePursuit.update(0.2);
                    if (Bot.purePursuit.reachedTargetXY(1.5, 0.75)) {
                        Bot.purePursuit.stop();
//                        Bot.specimenArm.closeClaw();
                        timer.reset();
                        state = State.COLLECT_SPECIMEN_3;
                    }
                    break;

                // Wait and set path to submersible
                // --> HANG_SPECIMEN
                case COLLECT_SPECIMEN_3:
                    if (timer.seconds() > 0.3) {
                        offsetX += Config.hangOffset;
                        AutoPaths.hangSpecimenRam(
                                Bot.pinpoint.getX(),
                                Bot.pinpoint.getY(),
                                offsetX, // Hang offsetX X
                                0 // Offset Y

                        );
//                        Bot.specimenArm.movePrepHang(0.5);
                        state = State.HANG_SPECIMEN;
                    }
                    break;

                // Complete path to submersible and hang specimen
                // --> COLLECT_SPECIMEN_1
                case HANG_SPECIMEN:
                    Bot.purePursuit.update(0.2, true);
                    if (Bot.purePursuit.reachedTarget(1.0)) {
                        Bot.purePursuit.stop();
//                        Bot.specimenArm.movePostHang(1.0); // Move arm down
                        timer.reset();
                        hang_number--;
                        // Waits for arm to go down
                        // Decides what to do based on stop and collect booleans
                        state = State.COLLECT_SPECIMEN_1;
                    }
                    break;

                // Park
                // End of auto
                case PARK:
                    Bot.purePursuit.update(0.2);
                    if (Bot.purePursuit.reachedTarget(5)) {
                        terminateOpModeNow();
                    }
                    break;
            }
            telemetry.addData("State", state);
            telemetry.addData("xPos", Bot.pinpoint.getX());
            telemetry.addData("yPos", Bot.pinpoint.getY());
            telemetry.addData("Heading", Bot.pinpoint.getHeading());
            telemetry.update();
        }
    }
}