package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Hardware.MecanumBase;
import org.firstinspires.ftc.teamcode.Helpers.Pose;

@TeleOp
public class Teleop extends LinearOpMode {
    public void runOpMode() {
        MecanumBase mecanumBase = new MecanumBase(this);
        Pose pose = new Pose(this);

        waitForStart();

        while(opModeIsActive()) {
            double px = gamepad1.left_stick_x;
            double py = -gamepad1.left_stick_y;
            double stickAngle = Math.atan2(py, px);
            double speed = Math.sqrt((px * px + py * py));
            double maxSpeed = 1;

            if(gamepad1.x) {mecanumBase.setNorthMode(true);}
            if(gamepad1.y) {mecanumBase.setNorthMode(false);}
            //Need to make a toggle function

            mecanumBase.move(stickAngle, -gamepad1.right_stick_x*maxSpeed, speed*maxSpeed);

            // Get the pose in the teleop loop
            double[] pos = pose.returnPose();

            // Telemetry in movement classes
            telemetry.addData("X", pos[0]);
            telemetry.addData("Y", pos[1]);
            telemetry.addData("Theta", pos[2]);
            telemetry.update();
        }
    }
}
