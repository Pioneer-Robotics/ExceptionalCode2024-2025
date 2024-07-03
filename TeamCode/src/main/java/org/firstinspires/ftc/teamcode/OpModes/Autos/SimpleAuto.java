package org.firstinspires.ftc.teamcode.OpModes.Autos;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Bot;

// Not tested
@Autonomous(name = "SimpleAuto", group = "Autos")
public class SimpleAuto extends LinearOpMode {
    public void runOpMode() {
        Bot bot = new Bot(this);

        waitForStart();

        while (opModeIsActive()) {
            // Move the robot in a square
            bot.led.lightsOn(RevBlinkinLedDriver.BlinkinPattern.RED);
            bot.pidController.moveToPosition(0, 60, 0, 0.6);
            bot.led.lightsOn(RevBlinkinLedDriver.BlinkinPattern.YELLOW);
            bot.pidController.moveToPosition(60, 0, 90, 0.6);
            bot.led.lightsOn(RevBlinkinLedDriver.BlinkinPattern.GREEN);
            bot.pidController.moveToPosition(0, -60, 180, 0.6);
            bot.led.lightsOn(RevBlinkinLedDriver.BlinkinPattern.BLUE);
            bot.pidController.moveToPosition(-60, 0, 270, 0.6);
            bot.led.lightsOn(RevBlinkinLedDriver.BlinkinPattern.HOT_PINK);
            bot.pidController.moveToPosition(0, 0, 0, 0.6);

            terminateOpModeNow();
        }
    }
}
