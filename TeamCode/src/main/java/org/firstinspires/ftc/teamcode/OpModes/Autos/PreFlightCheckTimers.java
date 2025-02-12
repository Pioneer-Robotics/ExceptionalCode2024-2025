package org.firstinspires.ftc.teamcode.OpModes.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Helpers.SplineCalc;

import java.util.Timer;
import java.util.TimerTask;

@Autonomous(name="Pre-Flight Check Auto Timers", group="Autos")
public class PreFlightCheckTimers extends LinearOpMode {
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
    PreFlightCheckTimers.State state;
    PreFlightCheckTimers.DriveTestState driveTestState;
    PreFlightCheckTimers.SpecimenArmTestState specimenArmTestState;
    PreFlightCheckTimers.IntakeState intakeState;
    ElapsedTime timer;
    PreFlightCheckTimers.TransferTestState transferTestState;
    PreFlightCheckTimers.SlideTestState slideTestState;

    Timer taskTimer;
    TimerTask task;

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
                    state = PreFlightCheckTimers.State.TEST_OCG_BOX;
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
        state = PreFlightCheckTimers.State.INIT;
    }

    private void handleInitState() {
        // Add testing Movements
        driveTestState = PreFlightCheckTimers.DriveTestState.FORWARDS;
        specimenArmTestState = PreFlightCheckTimers.SpecimenArmTestState.MOVE_TO_HANG;
        intakeState = PreFlightCheckTimers.IntakeState.ARM_OUT;
        transferTestState = PreFlightCheckTimers.TransferTestState.OCG_UP;
        slideTestState = PreFlightCheckTimers.SlideTestState.SLIDE_UP;
        state = PreFlightCheckTimers.State.TEST_DRIVE;
    }
    private void handleTestDriveState() {
        switch (driveTestState) {
            case FORWARDS:
                double[][] forwardPath = SplineCalc.nDegBez(new double[] {0, 0}, new double[] {0, 100}, 10);
                testDrivePath(forwardPath);

                TimerTask forwardTask = new TimerTask() {
                    @Override public void run() {
                        driveTestState = PreFlightCheckTimers.DriveTestState.BACKWARDS;
                    }
                };
                createTaskWithWait(forwardTask);

                break;
            case BACKWARDS:
                double[][] backwardsPath = SplineCalc.nDegBez(new double[] {0, 0}, new double[] {0, 100}, 10);
                testDrivePath(backwardsPath);

                TimerTask backwardsTask = new TimerTask() {
                    @Override public void run() {
                        state = PreFlightCheckTimers.State.TEST_SPECIMEN_ARM;
                    }
                };
                createTaskWithWait(backwardsTask);

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

                TimerTask moveToHangTask = new TimerTask() {
                    @Override public void run() {
                        specimenArmTestState = PreFlightCheckTimers.SpecimenArmTestState.MOVE_TO_COLLECT;
                    }
                };
                createTaskWithWait(moveToHangTask);

                break;
            case MOVE_TO_COLLECT:
                Bot.specimenArm.moveToCollect(0.5);

                TimerTask moveToCollectTask = new TimerTask() {
                    @Override public void run() {
                        state = PreFlightCheckTimers.State.TEST_INTAKE;
                    }
                };
                createTaskWithWait(moveToCollectTask);

                break;
        }
    }
    private void handleTestIntakeState() {
        // Add testing Movements HERE
        switch (intakeState) {
            case ARM_OUT:
                Bot.intake.extendMisumiDrive();

                TimerTask armOutTask = new TimerTask() {
                    @Override public void run() {
                        intakeState = PreFlightCheckTimers.IntakeState.CLAW_OPEN;
                    }
                };
                createTaskWithWait(armOutTask);

                break;
            case CLAW_OPEN:
                Bot.intakeClaw.openClaw();

                TimerTask clawOpenTask = new TimerTask() {
                    @Override public void run() {
                        intakeState = PreFlightCheckTimers.IntakeState.DOWN;
                    }
                };
                createTaskWithWait(clawOpenTask);

                break;
            case DOWN:
                Bot.intake.misumiWristDown();

                TimerTask downTask = new TimerTask() {
                    @Override public void run() {
                        intakeState = PreFlightCheckTimers.IntakeState.CLAW_CLOSE;
                    }
                };
                createTaskWithWait(downTask);

                break;
            case CLAW_CLOSE:
                Bot.intakeClaw.closeClaw();

                TimerTask clawCloseTask = new TimerTask() {
                    @Override public void run() {
                        intakeState = PreFlightCheckTimers.IntakeState.UP;
                    }
                };
                createTaskWithWait(clawCloseTask);

                break;
            case UP:
                Bot.intake.midMisumiWrist();
                if (Bot.intake.isWristMid()) {
                    state = PreFlightCheckTimers.State.TEST_TRANSFER;
                }

                TimerTask upTask = new TimerTask() {
                    @Override public void run() {
                        state = PreFlightCheckTimers.State.TEST_TRANSFER;
                    }
                };
                createTaskWithWait(upTask);

                break;
        }
    }
    private void handleTestTransferState() {
        // TODO: - consider converting this to Timers or not
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
        TimerTask upTask = new TimerTask() {
            @Override public void run() {
                transferTestState = PreFlightCheckTimers.TransferTestState.OCG_UP;
            }
        };
        createTaskWithWait(upTask);
    }

    private void updateOCGUpTransferState() {
        Bot.ocgBox.ocgPitchUp();
        TimerTask upTask = new TimerTask() {
            @Override public void run() {
                transferTestState = PreFlightCheckTimers.TransferTestState.DROP;
            }
        };
        createTaskWithWait(upTask);
    }

    private void updateDropTransferState() {
        if (timer.milliseconds() > 500) {
            Bot.intakeClaw.openClaw();
        }
        if (Bot.intakeClaw.isClawOpen()) {
            timer.reset();
            transferTestState = PreFlightCheckTimers.TransferTestState.OCG_IDLE;
        }
    }

    private void updateOCGIdleTransferState() {
        if (timer.milliseconds() > 3000) {
            Bot.ocgBox.idle();
        }
        if (Bot.ocgBox.isIdle()) {
            transferTestState = PreFlightCheckTimers.TransferTestState.WRIST_DOWN;
        }
    }

    private void updateWristDownTransferState() {
        // As there is nothing after, the state is immediately set to NONE
        Bot.intake.misumiWristDown();
        Bot.intakeClaw.closeClaw();
        TimerTask upTask = new TimerTask() {
            @Override public void run() {
                state = PreFlightCheckTimers.State.TEST_SLIDE;
            }
        };
        createTaskWithWait(upTask);
    }

    private void handleTestSlideState() {
        switch (slideTestState) {
            case SLIDE_UP:
                Bot.slideArm.moveUp(0.5);

                TimerTask upTask = new TimerTask() {
                    @Override public void run() {
                        slideTestState = PreFlightCheckTimers.SlideTestState.SLIDE_DOWN;
                    }
                };
                createTaskWithWait(upTask);

                break;
            case SLIDE_DOWN:
                Bot.slideArm.moveDown(0.5);

                TimerTask downTask = new TimerTask() {
                    @Override public void run() {
                        state = PreFlightCheckTimers.State.TEST_OCG_BOX;
                    }
                };
                createTaskWithWait(downTask);

                break;
        }
    }
    private void handleTestOcgBoxState() {
        // TODO: - Add testing Movements HERE
        state = PreFlightCheckTimers.State.SET_FOR_MATCH;
    }

    private void handleTestSetForMatchState() {
        terminateOpModeNow();
    }

    private void createTaskWithWait(TimerTask task) {
        timer.reset();
        taskTimer = new Timer();
        this.task = task;
        taskTimer.schedule(task, 750);
    }

    private void updateTelemetry() {
        telemetry.update();
        Bot.dashboardTelemetry.update();
    }

}
