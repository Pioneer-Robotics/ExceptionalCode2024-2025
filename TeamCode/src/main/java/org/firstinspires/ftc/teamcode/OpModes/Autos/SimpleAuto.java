package org.firstinspires.ftc.teamcode.OpModes.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.Utils;

// Not tested
@Autonomous(name = "SimpleAuto", group = "Autos")
public class SimpleAuto extends LinearOpMode {
    public void runOpMode() {
        Bot bot = new Bot(this);

        waitForStart();

        while (opModeIsActive()) {
            bot.pidController.moveToPosition(-60, 75, -90);
            bot.pixelDropLeft.openServo();
            Utils.sleep(1, this);
            bot.pixelDropLeft.closeServo();
            bot.pidController.moveToPosition(0, 70, -90);
            bot.pixelDropRight.openServo();
            Utils.sleep(1, this);
            bot.pixelDropRight.closeServo();
            bot.pidController.moveToPosition(0, 0, 0);
            terminateOpModeNow();
        }
    }
}
