package org.firstinspires.ftc.teamcode.OpModes.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.Utils;

// Not tested
@Autonomous(name = "SimpleAuto", group = "Autos")
public class SimpleAuto extends LinearOpMode {
    public void runOpMode() {

        waitForStart();

        while (opModeIsActive()) {
            Bot.pidController.moveToPosition(-60, 75, -90);
            Bot.pixelDropLeft.openServo();
            Utils.sleep(1);
            Bot.pixelDropLeft.closeServo();
            Bot.pidController.moveToPosition(0, 70, -90);
            Bot.pixelDropRight.openServo();
            Utils.sleep(1);
            Bot.pixelDropRight.closeServo();
            Bot.pidController.moveToPosition(0, 0, 0);
            terminateOpModeNow();
        }
    }
}
