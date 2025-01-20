package org.firstinspires.ftc.teamcode.OpModes.Calibration;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.SplineCalc;

@TeleOp(name = "Path Test")
public class PathTest extends LinearOpMode {
    public void runOpMode() {
        Bot.init(this);
        double[][] turnPath = SplineCalc.linearPath(new double[]{0, 0.25, 0.75, 1}, new double[]{Math.PI / 2, Math.PI / 2, 0, 0}, 25);
        Bot.purePursuit.setTurnPath(turnPath);

        waitForStart();
//        while (opModeIsActive()) {
//
//        }
    }
}
