package org.firstinspires.ftc.teamcode.OpModes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Bot;

@Disabled
@TeleOp(name = "Thread Test")
public class ThreadTest extends LinearOpMode {
    public void runOpMode() {
        Bot.init(this);

        double dt = 0.0;
        double prevTime = 0.0;

        ElapsedTime timer = new ElapsedTime();
        timer.reset();

        waitForStart();
        while (opModeIsActive()) {
            dt = timer.milliseconds() - prevTime;
            prevTime = timer.milliseconds();

            telemetry.addData("dt", dt);
            telemetry.addData("Refresh Rate (Hz)", 1000 / dt);
            telemetry.update();
        }
    }
}
