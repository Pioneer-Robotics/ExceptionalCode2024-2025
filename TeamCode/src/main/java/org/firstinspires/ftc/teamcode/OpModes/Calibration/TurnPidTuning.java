package org.firstinspires.ftc.teamcode.OpModes.Calibration;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.SplineCalc;

@Disabled
@Autonomous(name="TurnPidTuning", group="Calibration")
public class TurnPidTuning extends LinearOpMode {
    enum State {
        TURN_FORWARD,
        TURN_BACKWARD
    }

    State state = State.TURN_FORWARD;

    public void runOpMode() {
        Bot.init(this);

        waitForStart();

        double[][] drivePath = SplineCalc.nDegBez(new double[] {0,0}, new double[] {0,100}, 25);
        double[][] turnPath = SplineCalc.linearPath(new double[] {0,1}, new double[] {0,Math.PI}, 25);
        Bot.purePursuit.setTargetPath(drivePath);
        Bot.purePursuit.setTurnPath(turnPath);

        StringBuilder str = new StringBuilder();
        for (double[] point: turnPath) {
            str.append(point[1]);
            str.append(",");
        }
        Bot.dashboardTelemetry.addLine(str.toString());
        Bot.dashboardTelemetry.update();

        while(opModeIsActive()) {

            switch(state) {
                case TURN_FORWARD:
                    Bot.purePursuit.update();

                    if(Bot.purePursuit.reachedTarget()) {
                        drivePath = SplineCalc.nDegBez(new double[] {0,0}, new double[] {100,0}, 25);
                        turnPath = SplineCalc.linearPath(new double[] {0,1}, new double[] {Math.PI,0}, 25);
                        Bot.purePursuit.setTargetPath(drivePath);
                        Bot.purePursuit.setTurnPath(turnPath);
                        state = State.TURN_BACKWARD;
                    }
                    break;

                case TURN_BACKWARD:
                    Bot.purePursuit.update();

                    if(Bot.purePursuit.reachedTarget()) {
                        drivePath = SplineCalc.nDegBez(new double[] {0,0}, new double[] {0,100}, 25);
                        turnPath = SplineCalc.linearPath(new double[] {0,1}, new double[] {0,Math.PI}, 25);
                        Bot.purePursuit.setTargetPath(drivePath);
                        Bot.purePursuit.setTurnPath(turnPath);
                        state = State.TURN_FORWARD;
                    }
                    break;

            }

            Bot.dashboardTelemetry.addData("State", state);
            Bot.dashboardTelemetry.addData("Heading", Bot.pinpoint.getHeading());
            Bot.dashboardTelemetry.update();
        }
    }
}
