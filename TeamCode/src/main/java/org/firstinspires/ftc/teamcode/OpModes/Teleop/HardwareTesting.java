package org.firstinspires.ftc.teamcode.OpModes.Teleop;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;

@TeleOp(name="Hardware testing")
public class HardwareTesting extends LinearOpMode {
    public void runOpMode() throws InterruptedException {
        Bot.init(this);

        waitForStart();
        while (opModeIsActive()) {


            telemetry.addData("Slide height", Bot.slideArm.getArmPosition());
        }
    }
}
