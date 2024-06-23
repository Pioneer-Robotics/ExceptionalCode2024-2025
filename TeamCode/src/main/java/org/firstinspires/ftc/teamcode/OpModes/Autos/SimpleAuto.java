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

        // Move the robot in a square
        bot.led.lightsOn(RevBlinkinLedDriver.BlinkinPattern.RED);
        bot.pidController.moveToPosition(0, 50, -90, 1);
        bot.led.lightsOn(RevBlinkinLedDriver.BlinkinPattern.YELLOW);
        bot.pidController.moveToPosition(0, 0, 0, 1);
        bot.led.lightsOn(RevBlinkinLedDriver.BlinkinPattern.GREEN);
        bot.pidController.moveToPosition(0, -50, 90, 1);
        bot.led.lightsOn(RevBlinkinLedDriver.BlinkinPattern.BLUE);
        bot.pidController.moveToPosition(0, 0, 0, 1);
        bot.led.lightsOn(RevBlinkinLedDriver.BlinkinPattern.HOT_PINK);
    }
}
