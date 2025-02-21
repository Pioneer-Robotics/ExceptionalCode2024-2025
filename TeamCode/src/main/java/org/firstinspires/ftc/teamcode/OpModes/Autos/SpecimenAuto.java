package org.firstinspires.ftc.teamcode.OpModes.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Helpers.AutoPaths;

import java.util.Timer;
import java.util.TimerTask;

@Autonomous(name="Specimen Auto", group="Autos")
public class SpecimenAuto extends LinearOpMode {

    private enum State {
        INIT,
        SPECIMEN_HANG,
        PUSH_SAMPLE_1,
        PUSH_SAMPLE_2,
        PUSH_SAMPLE_3,
        COLLECT_SPECIMEN,
        WAIT_FOR_CLAW,
        PARK,
    }

    private State state = State.INIT;
    private final ElapsedTime timer = new ElapsedTime();
    Timer armSchedule = new Timer();

    private final int totalSpecimens = 5;
    private int specimensLeft = totalSpecimens;
    private double offsetX = 0;

    public void runOpMode() {

        Bot.init(this, Config.specimenStartX, Config.specimenStartY);

        waitForStart();

        while (opModeIsActive()) {
            switch (state) {
                case INIT:
                    handleInitState();
                    break;
                case SPECIMEN_HANG:
                    handleHangState();
                    break;
                case PUSH_SAMPLE_1:
                    handlePushSampleOne();
                    break;
                case PUSH_SAMPLE_2:
                    handlePushSampleTwo();
                    break;
                case PUSH_SAMPLE_3:
                    handlePushSampleThree();
                    break;
                case COLLECT_SPECIMEN:
                    handleCollectSpecimen();
                    break;
                case WAIT_FOR_CLAW:
                    handleWaitForClaw();
                    break;
                case PARK:
                    handlePark();
                    break;
            }
            Bot.pinpoint.update();
            updateTelemetry();
        }
        Bot.currentThreads.stopThreads();
    }

    private void handleInitState() {
        // Set the initial path to hang preloaded specimen
        // --> SPECIMEN_HANG
        AutoPaths.hangPreloadSpecimen(
                Bot.pinpoint.getX(), // Current X
                Bot.pinpoint.getY(), // Current Y
                0, // Hang offsetX X
                0.25 // Offset Y
        );
        // Schedule specimen arm movement
        long delay = (long)(350 - Config.specimenAutoDelaySpcimenArmAdjustment);
        armSchedule.schedule(new TimerTask() {
            @Override
            public void run() {
                Bot.specimenArm.movePrepHang(1);
            }
        }, delay);
        state = State.SPECIMEN_HANG;
    }

    private void scheduleSpecimenArmCollect() {
        Timer armSchedule = new Timer();
        armSchedule.schedule(new TimerTask() {
            @Override
            public void run() {
                Bot.specimenArm.moveToCollect(0.75);
            }
        }, 750);
    }

    private void handleHangState() {
        // Release specimen at submersible, set next path (3 possibilities)
        // --> PUSH_SAMPLE_1, COLLECT_SPECIMEN, or PARK

        if (specimensLeft == totalSpecimens) {
            // Go slower on first hang
            Bot.purePursuit.update(0.4 + Config.specimenAutoPushSpeedAdjustment);
        } else {
            // Go faster on subsequent hangs
            Bot.purePursuit.update(0.65 + Config.specimenAutoPushSpeedAdjustment);
        }

        if (Bot.purePursuit.reachedTarget(2.5)) {
            Bot.specimenArm.openClaw();
            Bot.purePursuit.stop();

            if (specimensLeft == 0) {
                AutoPaths.park(
                        Bot.pinpoint.getX(), // Current X
                        Bot.pinpoint.getY() // Current Y
                );
                scheduleSpecimenArmCollect();
                state = State.PARK;
            } else if (specimensLeft < totalSpecimens) {
                AutoPaths.collectSpecimen(
                        Bot.pinpoint.getX(), // Current X
                        Bot.pinpoint.getY(), // Current Y
                        true // Coming from the submersible
                );
                scheduleSpecimenArmCollect();
                state = State.COLLECT_SPECIMEN;
            } else {
                AutoPaths.pushSample1(
                        Bot.pinpoint.getX(), // Current X
                        Bot.pinpoint.getY() // Current Y
                );
                scheduleSpecimenArmCollect();
                state = State.PUSH_SAMPLE_1;
            }
        }
    }

    private void handlePushSampleOne() {
        Bot.purePursuit.update(0.85 + Config.specimenAutoHandleSampleSpeedAdjustment);
        if (Bot.purePursuit.reachedTarget(5)) {
            AutoPaths.pushSample2(
                    Bot.pinpoint.getX(), // Current X
                    Bot.pinpoint.getY() // Current Y
            );
            state = State.PUSH_SAMPLE_2;
        }
    }

    private void handlePushSampleTwo() {
        Bot.purePursuit.update(0.8 + Config.specimenAutoHandleSampleSpeedAdjustment);
        if (Bot.purePursuit.reachedTarget(5)) {
            AutoPaths.pushSample3(
                    Bot.pinpoint.getX(), // Current X
                    Bot.pinpoint.getY() // Current Y
            );
            state = State.PUSH_SAMPLE_3;
        }
    }

    private void handlePushSampleThree() {
        Bot.purePursuit.update(0.75 + Config.specimenAutoHandleSampleSpeedAdjustment);
        if (Bot.purePursuit.reachedTarget(5)) {
            AutoPaths.collectSpecimen(
                    Bot.pinpoint.getX(), // Current X
                    Bot.pinpoint.getY(), // Current Y
                    false // Not coming from the submersible
            );
            state = State.COLLECT_SPECIMEN;
        }
    }

    private void handleCollectSpecimen() {
        Bot.purePursuit.update(0.525, true);
        if (Bot.purePursuit.reachedTargetXY(1.5, 1)) {
            Bot.purePursuit.stop();
            Bot.specimenArm.closeClaw();
            timer.reset();
            state = State.WAIT_FOR_CLAW;
        }
    }

    private void handleWaitForClaw() {
        if (timer.milliseconds() > 250) {
            offsetX += Config.hangOffset;
            AutoPaths.hangSpecimen(
                    Bot.pinpoint.getX(), // Current X
                    Bot.pinpoint.getY(), // Current Y
                    offsetX, // Hang offsetX X
                    0 // Offset Y
            );
            Bot.specimenArm.movePrepHang(0.5 + Config.specimenAutoPrepHangSpecimentSpeedAdjustment);
            specimensLeft--;
            state = State.SPECIMEN_HANG;
        }
    }

    private void handlePark() {
        Bot.purePursuit.update(1);
        if (Bot.purePursuit.reachedTarget(5)) {
            terminateOpModeNow();
        }
    }

    private void updateTelemetry() {
        telemetry.addData("State", state);
        telemetry.addData("X", Bot.pinpoint.getX());
        telemetry.addData("Y", Bot.pinpoint.getY());
        telemetry.addData("Theta", Bot.pinpoint.getHeading());
        telemetry.update();
    }
}
