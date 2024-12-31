package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.SelfDrivingAuto.PID;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;

import java.util.List;

@TeleOp(name = "Vision Test")
public class visionTeleop extends LinearOpMode {
    public void runOpMode() {
        Bot.init(this);


        List<ColorBlobLocatorProcessor.Blob> locatedBlobs;

        PID xPID = new PID(Config.drivePID[0], Config.drivePID[1], Config.drivePID[2]);
        PID yPID = new PID(Config.drivePID[0], Config.drivePID[1], Config.drivePID[2]);
        PID turnPID = new PID(Config.turnPID[0], Config.turnPID[1], Config.turnPID[2]);

        waitForStart();
        while (opModeIsActive()){
            locatedBlobs = Bot.blueLocator.getBlobsList();


//            for(ColorBlobLocatorProcessor.Blob b : blobs)
//            {
//                RotatedRect boxFit = b.getBoxFit();
//                telemetry.addLine(String.format("%5d  %4.2f   %5.2f  (%3d,%3d)",
//                        b.getContourArea(), b.getDensity(), b.getAspectRatio(), (int) boxFit.center.x, (int) boxFit.center.y));
//            }

            double moveX = 0;
            double blobX = 0;


            if (!locatedBlobs.isEmpty()) {
                blobX = locatedBlobs.get(0).getBoxFit().center.x;
                telemetry.update();
                // Shift  40 pixels (center is 160) to line up with arm
                // Divide by 15 to approx convert from pixels to cm
                moveX = xPID.calculate(blobX/30, ((double) (160+40) / 30));
            }

            Bot.deadwheel_odom.calculate();
             double moveY = yPID.calculate(Bot.deadwheel_odom.getY(), gamepad1.left_stick_y*15.0);
//            double moveY = gamepad1.left_stick_y;
            double moveTheta = turnPID.calculate(-Bot.imu.getRadians(), 0);

            Bot.mecanumBase.move(moveX,moveY,moveTheta, 0.5);
            telemetry.addData("Blobs", locatedBlobs);
            telemetry.addData("Blox X", blobX);
            telemetry.addData("Move X", moveX);
            telemetry.update();
        }

    }
}
