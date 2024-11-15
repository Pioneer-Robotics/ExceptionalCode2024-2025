package org.firstinspires.ftc.teamcode.OpModes.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.BezierCalc;

@Autonomous(name="Test Auto", group="Autos")
public class TestAuto extends LinearOpMode {
    public void runOpMode() {
        Bot.init(this);
        Bot.purePursuit.setTargetPath(new double[][]{{0, 0}, {0, 100}});

        waitForStart();
        while (opModeIsActive()) {
            Bot.purePursuit.update(0.5);

            telemetry.addData("X", Bot.pose.getRawOTOS()[0]);
            telemetry.addData("Y", Bot.pose.getRawOTOS()[1]);
            telemetry.addData("Theta", Bot.pose.getRawOTOS()[2]);
            telemetry.update();
        }
    }
}