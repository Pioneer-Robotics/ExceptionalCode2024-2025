package org.firstinspires.ftc.teamcode.OpModes.Autos;

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
        bot.pidController.moveToPosition(10, 0);
        bot.pidController.moveToPosition(10, 10);
        bot.pidController.moveToPosition(0, 10);
        bot.pidController.moveToPosition(0, 0);
    }
}
