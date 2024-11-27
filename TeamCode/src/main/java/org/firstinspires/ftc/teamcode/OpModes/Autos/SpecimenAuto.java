package org.firstinspires.ftc.teamcode.OpModes.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.BezierCalc;

// 1+1 No Wrist Servo
@Autonomous(name="Specimen Auto", group="Autos")
public class SpecimenAuto extends LinearOpMode {
    enum State {
        INIT,
        SPECIMEN_HANG_1,
        SPECIMEN_HANG_2,
        OBSERVATION_ZONE,
        OBSERVATION_ZONE_2,
        COLLECT_SPECIMEN_1,
        COLLECT_SPECIMEN_2,
        SPECIMEN_HANG_DOWN,
        PARK,
    }

    State state = State.INIT;

    public void runOpMode() {
        Bot.init(this);
        ElapsedTime timer = new ElapsedTime();

        double offset = 0;
        boolean stop = false;
        boolean collect = false;

        waitForStart();
        while (opModeIsActive()) {

            switch (state) {
                case INIT:
                    Bot.purePursuit.setTargetPath(new double[][]{Bot.deadwheel_odom.getPose(), {-10, 80}});
                    Bot.specimenArm.movePrepHangUp(0.5);
                    state = State.SPECIMEN_HANG_1;
                    break;

                case SPECIMEN_HANG_1:
                    Bot.purePursuit.update(0.4);
                    if (Bot.purePursuit.reachedTarget() || (Bot.purePursuit.reachedTarget(5) && Bot.optical_odom.getAbsoluteVelocity() < 4)) {
                        Bot.purePursuit.stop();
                        Bot.specimenArm.movePostHangUp(1.0);
                        timer.reset();
                        state = State.SPECIMEN_HANG_2;
                    }
                    break;

                case SPECIMEN_HANG_2:
                    if (timer.seconds() > 0.5) {
                        Bot.specimenArm.openClaw();
                        if (stop) {
                            double[][] path = {{Bot.deadwheel_odom.getX(), Bot.deadwheel_odom.getY()}, {100, 10}};
                            Bot.purePursuit.setTargetPath(path);
                            state = State.PARK;
                            break;
                        } else if (collect) {
                            double[] pointsX = {Bot.deadwheel_odom.getX(),30,70};
                            double[] pointsY = {Bot.deadwheel_odom.getY(),0,0};
                            double[][] path = BezierCalc.nDegBez(pointsX, pointsY, 25);
                            Bot.purePursuit.setTargetPath(path);
                            state = State.COLLECT_SPECIMEN_1;
                            break;
                        }
                        double[] pointsX = {-15,25,110,60,100,70};
                        double[] pointsY = {77,-14,0,315,145,22};
                        double[][] path = BezierCalc.nDegBez(pointsX, pointsY, 25);
                        Bot.purePursuit.setTargetPath(path);
                        timer.reset();
                        state = State.OBSERVATION_ZONE;
                    }
                    // Move to next position
                    break;

                case OBSERVATION_ZONE:
                    Bot.purePursuit.update(0.5);
                    if (Bot.purePursuit.reachedTarget(4)) {
                        double[] pointsX = {85,60,120,120};
                        double[] pointsY = {22,130,190,25};
                        double[][] path = BezierCalc.nDegBez(pointsX, pointsY, 25);
                        Bot.purePursuit.setTargetPath(path);
                        state = State.OBSERVATION_ZONE_2;
                    }
                    break;

                case OBSERVATION_ZONE_2:
                    Bot.purePursuit.update(0.5);
                    if (Bot.purePursuit.reachedTarget(4)) {
                        double[] pointsX = {120,75,25,70};
                        double[] pointsY = {25,55,25,0};
                        double[][] path = BezierCalc.nDegBez(pointsX, pointsY, 25);
                        Bot.purePursuit.setTargetPath(path);
                        state = State.COLLECT_SPECIMEN_1;
                    }
                    break;

                case COLLECT_SPECIMEN_1:
                    Bot.purePursuit.update(0.35);
                    Bot.specimenArm.moveToCollect(0.5);
                    if (Bot.purePursuit.reachedTarget()) {
                        Bot.purePursuit.stop();
                        Bot.specimenArm.closeClaw();
                        timer.reset();
                        state = State.COLLECT_SPECIMEN_2;
                    }
                    break;

                case COLLECT_SPECIMEN_2:
                    if (timer.seconds() > 1) {
                        offset -= 10;
                        double[] pointsX = {Bot.deadwheel_odom.getX(), 0, -10+offset};
                        double[] pointsY = {Bot.deadwheel_odom.getY(), 0, 80};
                        double[][] path = BezierCalc.nDegBez(pointsX, pointsY, 25);
                        Bot.purePursuit.setTargetPath(path);
                        Bot.specimenArm.movePrepHang(0.5);
                        state = State.SPECIMEN_HANG_DOWN;
                    }
                    break;

                case SPECIMEN_HANG_DOWN:
                    Bot.purePursuit.update(0.4);
                    if (Bot.purePursuit.reachedTarget() || (Bot.purePursuit.reachedTarget(10) && Bot.optical_odom.getAbsoluteVelocity() < 4)) {
                        Bot.purePursuit.stop();
                        Bot.specimenArm.movePostHang(1.0);
                        timer.reset();
                        if (collect) {
                            stop = true;
                        } else {
                            collect = true;
                        }
                        state = State.SPECIMEN_HANG_2;
                    }
                    break;

                case PARK:
                    Bot.purePursuit.update(1);
                    if (Bot.purePursuit.reachedTarget(5)) {
                        terminateOpModeNow();
                    }
                    break;
                }
            telemetry.addData("State", state);
            telemetry.addData("X", Bot.deadwheel_odom.getX());
            telemetry.addData("Y", Bot.deadwheel_odom.getY());
            telemetry.addData("Theta", Bot.optical_odom.getHeading());
            telemetry.addData("Absolute Velocity", Bot.optical_odom.getAbsoluteVelocity());
            telemetry.update();
        }
    }
}
