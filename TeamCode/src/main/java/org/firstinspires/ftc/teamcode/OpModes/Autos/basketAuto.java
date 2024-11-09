package org.firstinspires.ftc.teamcode.OpModes.Autos;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Bot;

public class basketAuto extends LinearOpMode {

    public void runOpMode() {

        Bot.init(this);

        waitForStart();

        while (opModeIsActive()) {
            Bot.pidController.moveToPosition(74, 64);

        }
    }
}
