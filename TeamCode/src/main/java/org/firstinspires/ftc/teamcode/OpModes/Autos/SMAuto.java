package org.firstinspires.ftc.teamcode.OpModes.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.BezierCalc;

@Autonomous(name="State Machine Auto", group="Autos")
public class SMAuto extends LinearOpMode {
    enum State {
        INIT,
        SPECIMEN_HANG_1,
        SPECIMEN_HANG_2,
        OBSERVATION_ZONE,
        OBSERVATION_ZONE_2,
        COLLECT_SPECIMEN
    }

    State state = State.INIT;

    public void runOpMode() {
        Bot.init(this);
        ElapsedTime timer = new ElapsedTime();

        waitForStart();
        while (opModeIsActive()) {

            switch (state) {
                case INIT:
                    Bot.purePursuit.setTargetPath(new double[][]{{0, 0}, {-25.0, 64}});
                    Bot.specimenArm.movePrepHang(0.5);
                    state = State.SPECIMEN_HANG_1;
                    break;

                case SPECIMEN_HANG_1:
                    Bot.purePursuit.update(0.4);
                    if (Bot.purePursuit.reachedTarget()) {
                        Bot.purePursuit.stop();
                        Bot.specimenArm.moveHangDown(1.0);
                        timer.reset();
                        state = State.SPECIMEN_HANG_2;
                    }
                    break;

                case SPECIMEN_HANG_2:
                    if (timer.seconds() > 0.5) {
                        Bot.specimenArm.openClaw();
                        double[] pointsX = {-28,115,50,85,85};
                        double[] pointsY = {64,20,150,170,25};
                        double[][] path = BezierCalc.nDegBez(pointsX, pointsY, 50);
                        Bot.purePursuit.setTargetPath(path);
                        timer.reset();
                        state = State.OBSERVATION_ZONE;
                    }
                    // Move to next position
                    break;

                case OBSERVATION_ZONE:
                    Bot.purePursuit.update(0.7);
                    if (timer.seconds() > 1) {
                        Bot.specimenArm.moveToCollect(0.4);
                    }
                    if (Bot.purePursuit.reachedTarget()) {
                        double[] pointsX = {85,46,105,110};
                        double[] pointsY = {25,130,190,25};
                        double[][] path = BezierCalc.nDegBez(pointsX, pointsY, 50);
                        Bot.purePursuit.setTargetPath(path);
                        state = State.OBSERVATION_ZONE_2;
                    }
                    break;

                case OBSERVATION_ZONE_2:
                    Bot.purePursuit.update(0.6);
                    if (Bot.purePursuit.reachedTarget()) {
                        double[] pointsX = {110,100,95};
                        double[] pointsY = {25,50,15};
                        double[][] path = BezierCalc.nDegBez(pointsX, pointsY, 50);
                        Bot.purePursuit.setTargetPath(path);
                    }
                    break;

                case COLLECT_SPECIMEN:
                    Bot.purePursuit.update(0.25);
                    if (Bot.purePursuit.reachedTarget()) {
                        Bot.purePursuit.stop();
                        Bot.specimenArm.closeClaw();
                    }
                }
            telemetry.addData("State", state);
            telemetry.addData("X", Bot.pose.getRawOTOS()[0]);
            telemetry.addData("Y", Bot.pose.getRawOTOS()[1]);
            telemetry.addData("Theta", Bot.pose.getRawOTOS()[2]);
            telemetry.update();
        }
    }
}
