package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Hardware.MecanumBase;

@TeleOp
public class Teleop extends LinearOpMode {
    public void runOpMode() {
        MecanumBase mecanumBase = new MecanumBase(this);

        waitForStart();

        while(opModeIsActive()) {
            double px = gamepad1.left_stick_x;
            double py = -gamepad1.left_stick_y;
            double turn = -gamepad1.right_stick_x;
            double stickAngle = Math.atan2(py, px);
            double speed = Math.sqrt((px * px + py * py));
            boolean aButton = gamepad1.a;
            boolean bButton = gamepad1.b;
            boolean xButton = gamepad1.x;
            boolean yButton = gamepad1.y;
            double maxSpeed = 0.5;

            if(xButton) {mecanumBase.setNorthMode(true);}
            if(yButton) {mecanumBase.setNorthMode(false);}
            //Need to make a toggle function

            mecanumBase.move(stickAngle, turn*maxSpeed, speed*maxSpeed);

            // Telemetry in movement classes
            telemetry.update();
        }
    }
}
