package org.firstinspires.ftc.teamcode.OpModes.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Helpers.AutoPaths;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;

import java.util.Timer;
import java.util.TimerTask;

@Autonomous(name="Specimen Refactor Auto", group="Autos")
public class SpecimenRefactorAuto extends LinearOpMode {
    // Possible states for the robot in auto
    public enum State {
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

    // Properties
    public SpecimenRefactorAuto.State state;
    ElapsedTime timer;
    Timer armSchedule;
    TimerTask armTask;
    double offsetX;
    public double hang_number;
    double initial_hang_number;
    double yCollect;

    public Telemetry _telemetry = telemetry;
    public boolean unitTestIsActive = false;

    // Run Loop
    public void runOpMode() {
        initalize();

        // redundant
        // waitForStart();

        while (opModeIsActive() || unitTestIsActive) {
            switch (this.state) {
                case INIT:
                    handleInitState();
                    break;
                case SPECIMEN_HANG_2:
                    handleHang2State();
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
                case COLLECT_SPECIMEN_1:
                    handleCollectSpecimenOne();
                    break;
                case COLLECT_SPECIMEN_2:
                    handleSpecimenTwo();
                    break;
                case PARK:
                    handlePark();
                    break;
            }

            updateTelemetry();
        }
        Bot.currentThreads.stopThreads();
    }

    // Private Methods
    public void initalize() { // This method could be further refactored
        Bot.init(this, Config.specimenStartX, Config.specimenStartY);
        timer = new ElapsedTime();

        offsetX = 0;
        state = SpecimenRefactorAuto.State.INIT;

        Toggle preloadToggle = new Toggle(true);
        if (!Bot.isUnitTest) {
            while (!isStarted()) {
                preloadToggle.toggle(gamepad1.a);
                _telemetry.addData("Preload", preloadToggle.get());
                _telemetry.update();
            }
        }

        hang_number = 4;
        if (!preloadToggle.get()) {
            hang_number = 3;
        }
        initial_hang_number = hang_number;

        yCollect = 0;
    }

    // *** Future Issue to refactor - all handle State methods have at least 2 jobs and should only have 1
    // The move function should be different from the State Change function

    // Set the initial path to hang preloaded specimen
    // --> SPECIMEN_HANG_2
    public void handleInitState() {
        AutoPaths.hangSpecimenUp(
                Bot.pinpoint.getX(), // Current X
                Bot.pinpoint.getY(), // Current Y
                5, // Hang offsetX X
                0.25 // Offset Y
        );
        armSchedule = new Timer();
        armTask = new TimerTask() {
            @Override
            public void run() {
                Bot.specimenArm.movePrepHang(1);
            }
        };
        createTaskWithWait(armTask, armSchedule, 350);

        state = SpecimenRefactorAuto.State.SPECIMEN_HANG_2;
    }

    // Release specimen at submersible, set next path (3 possibilities)
    // stop --> PARK; collect --> COLLECT_SPECIMEN_1; else --> PUSH_SAMPLE_1
    public void handleHang2State() { // This methods should be further refactored ***
        if (hang_number == initial_hang_number) {
            Bot.purePursuit.update(0.4);
        } else {
            Bot.purePursuit.update(0.65);
        }
        if (Bot.purePursuit.reachedTarget(2.5)) {
            Bot.specimenArm.openClaw();
            if (hang_number == 0) {
                // Set path to observation zone to park (PARK)
                AutoPaths.park(
                        Bot.pinpoint.getX(), // Current X
                        Bot.pinpoint.getY() // Current Y
                );
                armSchedule = new Timer();
                armTask = new TimerTask() {
                    @Override
                    public void run() {
                        Bot.specimenArm.moveToCollect(0.75);
                    }
                };
                createTaskWithWait(armTask, armSchedule, 750);
                state = SpecimenRefactorAuto.State.PARK;
            } else if (hang_number < initial_hang_number) {
                // Set path to observation zone to grab specimen (COLLECT_SPECIMEN_1)
                AutoPaths.collectSpecimen(
                        Bot.pinpoint.getX(), // Current X
                        Bot.pinpoint.getY(), // Current Y
                        true // Coming from the submersible
                );
                armSchedule = new Timer();
                armTask = new TimerTask() {
                    @Override
                    public void run() {
                        Bot.specimenArm.moveToCollect(0.75);
                    }
                };
                createTaskWithWait(armTask, armSchedule, 750);
                state = SpecimenRefactorAuto.State.COLLECT_SPECIMEN_1;
            } else {
                // Set path to push first sample into observation zone (PUSH_SAMPLE_1)
                AutoPaths.pushSample1(
                        Bot.pinpoint.getX(), // Current X
                        Bot.pinpoint.getY() // Current Y
                );
                timer.reset();
                armSchedule = new Timer();
                armTask = new TimerTask() {
                    @Override
                    public void run() {
                        Bot.specimenArm.moveToCollect(0.4);
                    }
                };
                createTaskWithWait(armTask, armSchedule, 750);
                state = SpecimenRefactorAuto.State.PUSH_SAMPLE_1;
            }
        }
        // Move to next position
    }

    // Bring first sample into observation zone, set path to bring second sample to
    // observation zone
    // --> PUSH_SAMPLE_2
    public void handlePushSampleOne() {
        Bot.purePursuit.update(0.85);
        if (Bot.purePursuit.reachedTarget(5)) {
            AutoPaths.pushSample2(
                    Bot.pinpoint.getX(), // Current X
                    Bot.pinpoint.getY() // Current Y
            );
            state = SpecimenRefactorAuto.State.PUSH_SAMPLE_2;
        }
    }

    // Bring second sample into observation zone, set path to collect specimen on fence
    // --> PUSH_SAMPLE_3
    public void handlePushSampleTwo() {
        Bot.purePursuit.update(0.8);
        if (Bot.purePursuit.reachedTarget(5)) {
            AutoPaths.pushSample3(
                    Bot.pinpoint.getX(), // Current X
                    Bot.pinpoint.getY() // Current Y
            );
            state = SpecimenRefactorAuto.State.PUSH_SAMPLE_3;
        }
    }

    // Bring third sample into observation zone, set path to collect specimen on fence
    // --> COLLECT_SPECIMEN_1
    public void handlePushSampleThree() {
        Bot.purePursuit.update(0.75);
        if (Bot.purePursuit.reachedTarget(5)) {
            AutoPaths.collectSpecimen(
                    Bot.pinpoint.getX(), // Current X
                    Bot.pinpoint.getY(), // Current Y
                    false // Not coming from the submersible
            );
            state = SpecimenRefactorAuto.State.COLLECT_SPECIMEN_1;
        }
    }

    // Go to and collect specimen on fence
    // --> COLLECT_SPECIMEN_2
    public void handleCollectSpecimenOne() {
        if (hang_number == initial_hang_number-1) {
            Bot.purePursuit.update(0.475, true);
        } else {
            Bot.purePursuit.update(0.55, true);
        }
        if (Bot.purePursuit.reachedTargetXY(1.5, 1)) {
            Bot.purePursuit.stop();
            Bot.specimenArm.closeClaw();
            timer.reset(); // Reset timer for next state
            state = SpecimenRefactorAuto.State.COLLECT_SPECIMEN_2;
        }
    }

    // Wait to grab specimen from fence, set path to hang specimen with an offsetX
    // from the original
    // --> SPECIMEN_HANG_2
    public void handleSpecimenTwo() {
        if (timer.seconds() > 0.25 || unitTestIsActive) {
            yCollect = Bot.pinpoint.getY();
            offsetX += Config.hangOffset; // Adjust the hang offsetX
            AutoPaths.hangSpecimen(
                    Bot.pinpoint.getX(), // Current X
                    Bot.pinpoint.getY(), // Current Y
                    offsetX, // Hang offsetX X
                    0 // Offset Y
            );
            Bot.specimenArm.movePrepHang(0.5);
            hang_number--;
            state = SpecimenRefactorAuto.State.SPECIMEN_HANG_2;
        }
    }

    // Wait to reach observation zone to park
    // End of auto
    public void handlePark() {
        Bot.purePursuit.update(1);
        if (Bot.purePursuit.reachedTarget(5)) {
            if (!Bot.isUnitTest) {
                terminateOpModeNow();
            } else {
                unitTestIsActive = false;
            }
        }
    }

    private void createTaskWithWait(TimerTask task, Timer taskTimer, long delay) {
        if (Bot.isUnitTest) {
            task.run();
            return;
        }

        taskTimer = new Timer();
        taskTimer.schedule(task, delay);
    }

    private void updateTelemetry() {
        Bot.pinpoint.update();
        _telemetry.addData("State", state);
        _telemetry.addData("Y Collect", yCollect);
        _telemetry.addData("X", Bot.pinpoint.getX());
        _telemetry.addData("Y", Bot.pinpoint.getY());
        _telemetry.addData("Theta", Bot.pinpoint.getHeading());
        _telemetry.update();
        if (!Bot.isUnitTest) {
            Bot.dashboardTelemetry.addData("X", Bot.pinpoint.getX());
            Bot.dashboardTelemetry.addData("Y", Bot.pinpoint.getY());
            Bot.dashboardTelemetry.update();
        }
    }
}
