package org.firstinspires.ftc.teamcode.OpModes.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Helpers.SplineCalc;

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
        PARK
    }

    private enum TransferState {
        NONE,
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
    ElapsedTime servoPosTimer, transferServoPosTimer, intakeServoPosTimer;
    int pickupSampleNumber = 1;

    public void runOpMode() {
        Bot.init(this, Config.sampleStartX, Config.sampleStartY,0);

        initialize();
        waitForStart();

        while (opModeIsActive()) {
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
                case PARK:
                    handlePARK();
                    break;
            }
            telemetry.addData("Pos X", Bot.pinpoint.getX());
            telemetry.addData("Pos Y", Bot.pinpoint.getY());
            telemetry.addData("Heading", Bot.pinpoint.getHeading());
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
        transferServoPosTimer = new ElapsedTime();
    }

    public void handleGOTO_BASKET() {
        double[] vector1 = {Bot.pinpoint.getX(), Bot.pinpoint.getY(), 0, 0};
        double[] vector2 = {Config.basketLoc[0], Config.basketLoc[1], 0, 0};
        double[][] path = SplineCalc.cubicHermite(vector1, vector2, 25);
        double[][] turnPath = SplineCalc.linearPath(new double[] {0, 1}, new double[] {Bot.pinpoint.getHeading(), Math.PI/4}, 25);
        Bot.purePursuit.setTargetPath(path);
        Bot.purePursuit.setTurnPath(turnPath);
        Bot.slideArm.moveUp(0.4);

        state = State.DROP_SAMPLE;
    }

    public void handleDROP_SAMPLE() {
        Bot.purePursuit.update();
        if (Bot.purePursuit.reachedTarget() && Bot.slideArm.isUp()) {
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
                state = State.PICKUP_SAMPLE1;
            } else if (pickupSampleNumber == 3) {
                state = State.PICKUP_SAMPLE1;
            }

        }
    }

    public void handlePICKUP_SAMPLE1() {
        //if (Bot.ocgBox.isPitchUp()) {
        if (servoPosTimer.milliseconds() > 1000) {
            double[] vector1 = {Bot.pinpoint.getX(), Bot.pinpoint.getY(), 0, 0};
            double[] vector2 = {Config.pickSample1[0], Config.pickSample1[1], 0, 0};
            double[][] path = SplineCalc.cubicHermite(vector1, vector2, 25);
            double[][] turnPath = SplineCalc.linearPath(new double[] {0, 1}, new double[] {Bot.pinpoint.getHeading(), Math.PI / 8}, 25);
            Bot.purePursuit.setTargetPath(path);
            Bot.purePursuit.setTurnPath(turnPath);

            Bot.slideArm.moveDown(0.4);
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

            Bot.slideArm.moveDown(0.4);
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

            Bot.slideArm.moveDown(0.4);
            triggerIntake();
            Bot.intakeClaw.openClaw();

            state = State.GRAB_SAMPLE;
        }
    }

    public void handleGRAB_SAMPLE() {
        handleIntake();
        Bot.purePursuit.update();
        if (Bot.purePursuit.reachedTarget() && intakeState == IntakeState.NONE && Bot.slideArm.isDown()) {
            Bot.intakeClaw.closeClaw();
            servoPosTimer.reset();
            pickupSampleNumber++;
            state = State.GOTO_BASKET_TRANSFER;
        }
    }

    public void handleGOTO_BASKET_TRANSFER() {
//        if (Bot.intakeClaw.isClawClosed()) {
        if (servoPosTimer.milliseconds() > 1000) {
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
            Bot.slideArm.moveUp(0.4);
            state = State.DROP_SAMPLE;
        }
    }

    public void handlePARK() {
        terminateOpModeNow();
    }



    public void triggerTransfer() {
        transferState = TransferState.WRIST_UP;
        transferServoPosTimer.reset();
    }
    public void cancelTransfer() {
        transferState = TransferState.NONE;
    }

    public void handleTransfer() {
        switch (transferState) {
            case NONE:
                break;
            case WRIST_UP:
                Bot.intake.misumiWristUp();
                //if (Bot.intake.isWristUp()) { transferState = TransferState.OCG_UP; }
                if (transferServoPosTimer.milliseconds() > 1000) {
                    transferState = TransferState.OCG_UP;
                    transferServoPosTimer.reset();
                }
                break;
            case OCG_UP:
                Bot.ocgBox.ocgPitchUp();
//                if (Bot.ocgBox.isPitchUp()) {
                if (transferServoPosTimer.milliseconds() > 1000) {
                    transferTimer.reset();
                    transferState = TransferState.DROP;
                }
                break;
            case DROP:
                if (transferTimer.milliseconds() > 500) {
                    Bot.intakeClaw.openClaw();
                    transferServoPosTimer.reset();
                }
//                if (Bot.intakeClaw.isClawOpen()) {
                if (transferServoPosTimer.milliseconds() > 1000) {
                    transferTimer.reset();
                    transferState = TransferState.OCG_IDLE;
                }
                break;
            case OCG_IDLE:
                if (transferTimer.milliseconds() > 3000) {
                    Bot.ocgBox.idle();
                    transferServoPosTimer.reset();
                }
//                if (Bot.ocgBox.isIdle()) {
                if (transferServoPosTimer.milliseconds() > 1000) {
                    transferState = TransferState.WRIST_DOWN;
                }
                break;
            case WRIST_DOWN:
                // As there is nothing after, the state is immediately set to NONE
                Bot.intake.misumiWristDown();
                Bot.intakeClaw.closeClaw();
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

    public void handleIntake() {
        switch(intakeState) {
            case NONE:
                break;
            case MID_WRIST:
                Bot.intake.midMisumiWrist();
//                if (Bot.intake.isWristMid()) {
                if (intakeServoPosTimer.milliseconds() > 1000) {
                    intakeState = IntakeState.EXTEND;
                }
                break;
            case EXTEND:
                Bot.intake.extendMisumiDrive();
                Bot.intakeClaw.clawDown();
                intakeState = IntakeState.NONE;
                break;
        }
    }


}
