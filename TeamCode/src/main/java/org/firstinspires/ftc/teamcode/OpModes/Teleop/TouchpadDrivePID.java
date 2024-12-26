package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcontroller.external.samples.ConceptAprilTag;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.SelfDrivingAuto.PID;


@TeleOp(name="Touchpad Drive PID")
public class TouchpadDrivePID extends LinearOpMode {
    public void runOpMode() {
        Bot.init(this);

        PID xPID = new PID(Config.drivePID[0], Config.drivePID[1], Config.drivePID[2]);
        PID yPID = new PID(Config.drivePID[0], Config.drivePID[1], Config.drivePID[2]);
        PID turnPID = new PID(Config.turnPID[0], Config.turnPID[1], Config.turnPID[2]);

        waitForStart();

        while(opModeIsActive()) {
            double x = gamepad1.touchpad_finger_1_x;
            double y = gamepad1.touchpad_finger_1_y;

            Bot.pinpoint.update();
            double moveX = xPID.calculate(Bot.pinpoint.getX(), x*40);
            double moveY = yPID.calculate(Bot.pinpoint.getY(), y*40);
            double moveTheta = turnPID.calculate(Bot.pinpoint.getHeading(), 0);
            Bot.mecanumBase.move(moveX, moveY, moveTheta,0.25);

            telemetry.addData("x", x*40);
            telemetry.addData("y", y*40);
            telemetry.update();
        }
    }
}
