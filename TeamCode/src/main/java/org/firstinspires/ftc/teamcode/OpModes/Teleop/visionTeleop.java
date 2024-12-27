package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import android.util.Size;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.SortOrder;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.SelfDrivingAuto.PID;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.opencv.core.RotatedRect;
import org.firstinspires.ftc.teamcode.Helpers.betterColorRange;

import org.firstinspires.ftc.teamcode.Hardware.Camera.SampleLocator;

import java.util.List;

@TeleOp(name = "Vision Test")
public class visionTeleop extends LinearOpMode {
    public void runOpMode() {

        ColorBlobLocatorProcessor blueLocator = new ColorBlobLocatorProcessor.Builder()
                .setTargetColorRange(ColorRange.BLUE)         // use a predefined color match
                .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)    // exclude blobs inside blobs
                .setRoi(ImageRegion.asUnityCenterCoordinates(-1, 1, 1, -1))  // search central 1/4 of camera view
                .setDrawContours(true)                        // Show contours on the Stream Preview
                .setBlurSize(5)                               // Smooth the transitions between different colors in image
                .build();

        VisionPortal portal = new VisionPortal.Builder()
                .addProcessor(blueLocator)
                .setCameraResolution(new Size(320, 240))
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .build();

        PID xPID = new PID(Config.drivePID[0], Config.drivePID[1], Config.drivePID[2]);
        PID yPID = new PID(Config.drivePID[0], Config.drivePID[1], Config.drivePID[2]);
        PID turnPID = new PID(Config.turnPID[0], Config.turnPID[1], Config.turnPID[2]);


        Bot.init(this);

        waitForStart();
        while (opModeIsActive()){

            List<ColorBlobLocatorProcessor.Blob> blobs = blueLocator.getBlobs();
            ColorBlobLocatorProcessor.Util.filterByArea(50, 20000, blobs);  // filter out very small blobs.
            ColorBlobLocatorProcessor.Util.sortByArea(SortOrder.DESCENDING, blobs); // Largest blob is first (hopefully correct one)



//            for(ColorBlobLocatorProcessor.Blob b : blobs)
//            {
//                RotatedRect boxFit = b.getBoxFit();
//                telemetry.addLine(String.format("%5d  %4.2f   %5.2f  (%3d,%3d)",
//                        b.getContourArea(), b.getDensity(), b.getAspectRatio(), (int) boxFit.center.x, (int) boxFit.center.y));
//            }

            double moveX = 0;
            double blobX = 0;

            if (!blobs.isEmpty()) {
                blobX = blobs.get(0).getBoxFit().center.x;
                telemetry.update();
                // Shift 40 pixels (center is 160) to line up with arm
                // Divide by 15 to approx convert from pixels to cm
                moveX = xPID.calculate(blobX/30, ((double) (160+40) / 30));
            }

            Bot.deadwheel_odom.calculate();
             double moveY = yPID.calculate(Bot.deadwheel_odom.getY(), gamepad1.left_stick_y*15.0);
//            double moveY = gamepad1.left_stick_y;
            double moveTheta = turnPID.calculate(-Bot.imu.getRadians(), 0);

            Bot.mecanumBase.move(moveX,moveY,moveTheta, 0.5);

            telemetry.addData("Blox X", blobX);
            telemetry.addData("Move X", moveX);
            telemetry.update();
        }

    }
}
