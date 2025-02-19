package org.firstinspires.ftc.teamcode.OpModes.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Helpers.SplineCalc;
import org.firstinspires.ftc.teamcode.OpModes.Teleop.Drivers.TeleopDriver2;

@Autonomous(name = "Sample Auto WAITTTTT", group = "Autos")
public class SampleAutoWait extends LinearOpMode {
    private enum State {
        INIT,
        GOTO_BASKET,
        DROP_SAMPLE,
        OCG_UP,
        PICKUP_SAMPLE1,
        PICKUP_SAMPLE2,
        PICKUP_SAMPLE3,
        GRAB_SAMPLE,
        GOTO_BASKET_TRANSFER,
        SLIDE_UP,
        GOTO_SUB,
        PARK
    }

    private enum TransferState {
        NONE,
        INTAKE_IN,
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

    State state;
    TransferState transferState;
    IntakeState intakeState;
    ElapsedTime transferTimer;
    ElapsedTime servoPosTimer, intakeServoPosTimer;
    int pickupSampleNumber = 1;

    public void runOpMode() {
        Bot.init(this, Config.sampleStartX, Config.sampleStartY,0);

        initialize();
        waitForStart();

        while (opModeIsActive()) {
            Bot.slideArm.update();
            switch (state) {
                case GOTO_BASKET:
                    handleGOTO_BASKET();
                    break;
                case DROP_SAMPLE:
                    handleDROP_SAMPLE();
                    break;
                case OCG_UP:
                    handleOCG_UP();
                    break;
                case PICKUP_SAMPLE1:
                    handlePICKUP_SAMPLE1();
                    break;
                case PICKUP_SAMPLE2:
                    handlePICKUP_SAMPLE2();
                    break;
                case PICKUP_SAMPLE3:
                    handlePICKUP_SAMPLE3();
                    break;
                case GRAB_SAMPLE:
                    handleGRAB_SAMPLE();
                    break;
                case GOTO_BASKET_TRANSFER:
                    handleGOTO_BASKET_TRANSFER();
                    break;
                case SLIDE_UP:
                    handleSLIDE_UP();
                    break;
                case GOTO_SUB:
                    handleGOTO_SUB();
                    break;
                case PARK:
                    handlePARK();
                    break;
            }
            telemetry.addData("Pos X", Bot.pinpoint.getX());
            telemetry.addData("Pos Y", Bot.pinpoint.getY());
            telemetry.addData("Heading", Bot.pinpoint.getHeading());
            telemetry.addData("State", state);
            telemetry.addData("Intake State", intakeState);
            telemetry.addData("Transfer State", transferState);
            telemetry.update();
        }
    }

    public void initialize() {
        state = State.GOTO_BASKET;
        transferState = TransferState.NONE;
        intakeState = IntakeState.NONE;
        transferTimer = new ElapsedTime();
        servoPosTimer = new ElapsedTime();
        intakeServoPosTimer = new ElapsedTime();
    }

    public void handleGOTO_BASKET() {
        double[] vector1 = {Bot.pinpoint.getX(), Bot.pinpoint.getY(), 0, 0};
        double[] vector2 = {Config.basketLoc[0], Config.basketLoc[1], 0, 0};
        double[][] path = SplineCalc.cubicHermite(vector1, vector2, 25);
        double[][] turnPath = SplineCalc.linearPath(new double[] {0, 1}, new double[] {Bot.pinpoint.getHeading(), Math.PI/4}, 25);
        Bot.purePursuit.setTargetPath(path);
        Bot.purePursuit.setTurnPath(turnPath);
        Bot.intake.misumiWristMid();
        Bot.slideArm.moveUp(0.8);

        state = State.DROP_SAMPLE;
    }

    public void handleDROP_SAMPLE() {
        Bot.purePursuit.update();
        if (Bot.purePursuit.reachedTarget(1) && Bot.slideArm.isUp()) {
            Bot.purePursuit.stop();
            Bot.ocgBox.ocgPitchDrop();
            servoPosTimer.reset();
            state = State.OCG_UP;
        }
    }

    public void handleOCG_UP() {
       // if (Bot.ocgBox.isDrop()) {
        if (servoPosTimer.milliseconds() > 1000) {
            Bot.ocgBox.ocgPitchUp();
            servoPosTimer.reset();
            if (pickupSampleNumber == 1) {
                state = State.PICKUP_SAMPLE1;
            } else if (pickupSampleNumber == 2) {
                state = State.PICKUP_SAMPLE2;
            } else if (pickupSampleNumber == 3) {
                state = State.PICKUP_SAMPLE3;
            } else if (pickupSampleNumber == 4) {
                state = State.GOTO_SUB;
            }

        }
    }

    public void handlePICKUP_SAMPLE1() {
        //if (Bot.ocgBox.isPitchUp()) {
        if (servoPosTimer.milliseconds() > 1000) {
            double[] vector1 = {Bot.pinpoint.getX(), Bot.pinpoint.getY(), 0, 0};
            double[] vector2 = {Config.pickSample1[0], Config.pickSample1[1], 0, 0};
            double[][] path = SplineCalc.cubicHermite(vector1, vector2, 25);
            double[][] turnPath = SplineCalc.linearPath(new double[] {0, 1}, new double[] {Bot.pinpoint.getHeading(), (Math.PI / 16)}, 25);
            Bot.purePursuit.setTargetPath(path);
            Bot.purePursuit.setTurnPath(turnPath);

            triggerIntake();
            Bot.intakeClaw.openClaw();

            state = State.GRAB_SAMPLE;
        }
    }

    public void handlePICKUP_SAMPLE2() {
//        if (Bot.ocgBox.isPitchUp()) {
        if (servoPosTimer.milliseconds() > 1000) {
            double[] vector1 = {Bot.pinpoint.getX(), Bot.pinpoint.getY(), 0, 0};
            double[] vector2 = {Config.pickSample2[0], Config.pickSample2[1], 0, 0};
            double[][] path = SplineCalc.cubicHermite(vector1, vector2, 25);
            double[][] turnPath = SplineCalc.linearPath(new double[] {0, 1}, new double[] {Bot.pinpoint.getHeading(), 0}, 25);
            Bot.purePursuit.setTargetPath(path);
            Bot.purePursuit.setTurnPath(turnPath);

            Bot.slideArm.moveDown(0.8);
            triggerIntake();
            Bot.intakeClaw.openClaw();

            state = State.GRAB_SAMPLE;
        }
    }

    public void handlePICKUP_SAMPLE3() {
//        if (Bot.ocgBox.isPitchUp()) {
        if (servoPosTimer.milliseconds() > 1000) {
            double[] vector1 = {Bot.pinpoint.getX(), Bot.pinpoint.getY(), 0, 0};
            double[] vector2 = {Config.pickSample3[0], Config.pickSample3[1], 0, 0};
            double[][] path = SplineCalc.cubicHermite(vector1, vector2, 25);
            double[][] turnPath = SplineCalc.linearPath(new double[] {0, 1}, new double[] {Bot.pinpoint.getHeading(), -Math.PI/8}, 25);
            Bot.purePursuit.setTargetPath(path);
            Bot.purePursuit.setTurnPath(turnPath);

            Bot.slideArm.moveDown(0.8);
            triggerIntake();
            Bot.intakeClaw.openClaw();
            Bot.intakeClaw.clawPos45();

            state = State.GRAB_SAMPLE;
        }
    }

    public void handleGRAB_SAMPLE() {
        handleIntake();
        Bot.slideArm.moveDown(0.8);
        Bot.purePursuit.update();

        boolean didReachTarget = Bot.purePursuit.reachedTarget(1);
        boolean currentIntakeState = intakeState == IntakeState.NONE;
        boolean isSlideArmDown = Bot.slideArm.isDown();

        telemetry.addData("didReachTarget", didReachTarget);
        telemetry.addData("currentIntakeState", currentIntakeState);
        telemetry.addData("isSlideArmDown", isSlideArmDown);

        if (didReachTarget && currentIntakeState && isSlideArmDown) {
            Bot.purePursuit.stop();
            Bot.intake.misumiWristDown();
            servoPosTimer.reset();
            pickupSampleNumber++;
            state = State.GOTO_BASKET_TRANSFER;
        }
    }

    public void handleGOTO_BASKET_TRANSFER() {
//        if (Bot.intakeClaw.isClawClosed()) {
        if (servoPosTimer.milliseconds() > 250) {
            Bot.intakeClaw.closeClaw();
        }
        if (servoPosTimer.milliseconds() > 500) {
            triggerTransfer();

            double[] vector1 = {Bot.pinpoint.getX(), Bot.pinpoint.getY(), 0, 0};
            double[] vector2 = {Config.basketLoc[0], Config.basketLoc[1], 0, 0};
            double[][] path = SplineCalc.cubicHermite(vector1, vector2, 25);
            double[][] turnPath = SplineCalc.linearPath(new double[] {0, 1}, new double[] {Bot.pinpoint.getHeading(), Math.PI/4}, 25);
            Bot.purePursuit.setTargetPath(path);
            Bot.purePursuit.setTurnPath(turnPath);

            state = State.SLIDE_UP;
        }
    }

    public void handleSLIDE_UP() {
        handleTransfer();
        Bot.purePursuit.update();
        if (transferState == TransferState.NONE) {
            Bot.slideArm.moveUp(0.8);
            state = State.DROP_SAMPLE;
        }
    }

    public void handleGOTO_SUB() {
//        if (Bot.ocgBox.isPitchUp()) {
        if (servoPosTimer.milliseconds() > 1000) {
            double[] vector1 = {Bot.pinpoint.getX(), Bot.pinpoint.getY(), 0, 0};
            double[] vector2 = {Config.pickSample2[0], Config.pickSample2[1], 275, -25};
            double[][] path = SplineCalc.cubicHermite(vector1, vector2, 25);
            double[][] turnPath = SplineCalc.linearPath(new double[] {0, 0.5, 1}, new double[] {Bot.pinpoint.getHeading(), 0, Math.PI/2}, 25);
            Bot.purePursuit.setTargetPath(path);
            Bot.purePursuit.setTurnPath(turnPath);

            Bot.specimenArm.movePrepHang(0.4);
        }
    }

    public void handlePARK() {
        Bot.purePursuit.update(0.4);
        // Stop the robot early and let momentum drift into the submersible to not damage arm
        if (Bot.purePursuit.reachedTarget(20)) {
            Bot.mecanumBase.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            terminateOpModeNow();
        }
    }


    /* -------------------------
       - Finite State Machines -
       ------------------------- */

    public void triggerTransfer() {
        transferState = TransferState.INTAKE_IN;
        transferTimer.reset();
    }

    public void cancelTransfer() {
        transferState = TransferState.NONE;
    }

    private void handleTransfer() {
        switch (transferState) {
            case NONE:
                break;

            case INTAKE_IN:
                Bot.intake.retractMisumiDrive();
                Bot.intakeClaw.clawUp();
                transferTimer.reset();
                transferState = TransferState.WRIST_UP;
                break;

            case WRIST_UP:
                if (transferTimer.milliseconds() > 750) {
                    Bot.intake.misumiWristUp();
                    transferTimer.reset();
                    transferState = TransferState.OCG_UP;
                }
                break;

            case OCG_UP:
                if (transferTimer.milliseconds() > 300) {
                    Bot.ocgBox.ocgPitchUp();
                    transferTimer.reset();
                    transferState = TransferState.DROP;
                }
                break;

            case DROP:
                if (transferTimer.milliseconds() > 300) {
                    Bot.intakeClaw.openClaw();
                    transferTimer.reset();
                    transferState = TransferState.OCG_IDLE;
                }
                break;

            case OCG_IDLE:
                if (transferTimer.milliseconds() > 300)
                    transferState = TransferState.WRIST_DOWN;
                break;

            case WRIST_DOWN:
                // As there is nothing after, the state is immediately set to NONE
                Bot.intake.misumiWristDown();
                Bot.intakeClaw.closeClaw();
                Bot.ocgBox.idle();
                transferState = TransferState.NONE;
        }
    }


    public void triggerIntake() {
        intakeState = IntakeState.MID_WRIST;
        intakeServoPosTimer.reset();
    }
    public void cancelIntake() {
        intakeState = IntakeState.NONE;
    }

    private void handleIntake() {
        switch (intakeState) {
            case NONE:
                break;
            case MID_WRIST:
                Bot.intake.misumiWristMid();
                if (intakeServoPosTimer.milliseconds() > 250) {
                    intakeState = IntakeState.EXTEND;
                }
                break;
            case EXTEND:
                Bot.intake.midMisumiDrive();
                Bot.intakeClaw.clawDown();
                intakeState = IntakeState.NONE;
        }
    }


}
