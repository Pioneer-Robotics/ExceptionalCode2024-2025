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
    }

    State state = State.INIT;

    public void runOpMode() {
        Bot.init(this);
        ElapsedTime timer = new ElapsedTime();

        waitForStart();
        while (opModeIsActive()) {

            switch (state) {
                case INIT:
                    Bot.purePursuit.setTargetPath(new double[][]{{0, 0}, {-30.0, 58.0}});
                    Bot.specimenArm.moveToPos1(0.5);
                    state = State.SPECIMEN_HANG_1;
                    break;

                case SPECIMEN_HANG_1:
                    Bot.purePursuit.update();
                    if (Bot.purePursuit.reachedTarget()) {
                        Bot.purePursuit.stop();
                        Bot.specimenArm.moveToPos2(1.0);
                        timer.reset();
                        state = State.SPECIMEN_HANG_2;
                    }
                    break;

                case SPECIMEN_HANG_2:
                    if (timer.seconds() > 0.5) {
                        Bot.specimenArm.openClaw();
//                        double[] pointsX = {0,140,106,145,147};
//                        double[] pointsY = {0,-42,102,116,-67};
//                        double[][] path = BezierCalc.nDegBez(pointsX, pointsY, 50);
//                        Bot.purePursuit.setTargetPath(path);
//                        state = State.OBSERVATION_ZONE;
                    }
                    // Move to next position
                    break;

//                case OBSERVATION_ZONE:
//                    if (Bot.purePursuit.reachedTarget()) {
//                        telemetry.addLine("We are stoppeeeeeeedddd haiiii this is not goooooood");
//                        Bot.purePursuit.stop();
//                    }
//                    break;
            }
            telemetry.addData("State", state);
            telemetry.addData("X", Bot.pose.getRawOTOS()[0]);
            telemetry.addData("Y", Bot.pose.getRawOTOS()[1]);
            telemetry.addData("Theta", Bot.pose.getRawOTOS()[2]);
            telemetry.update();
        }
    }
}
