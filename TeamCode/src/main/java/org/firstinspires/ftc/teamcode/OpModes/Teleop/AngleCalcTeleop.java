package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.Camera.LocatorClass;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;

import java.util.List;

import org.firstinspires.ftc.vision.opencv.ColorRange;

@TeleOp(name = "Angle Calc Test")
public class AngleCalcTeleop extends LinearOpMode {
    public void runOpMode() {
        LocatorClass blueLocator = new LocatorClass(ColorRange.BLUE, this);

        List<ColorBlobLocatorProcessor.Blob> locatedBlobs;
        FtcDashboard dashboard = FtcDashboard.getInstance();
        dashboard.startCameraStream(blueLocator.getCameraStream(), 30);

        waitForStart();
        while (opModeIsActive()){

            TelemetryPacket packet = new TelemetryPacket(false);

            packet.put("Blob Count", blueLocator.blobCount());
            packet.put("Direction", blueLocator.determineRotDir());
            packet.put("Sample Theta", blueLocator.getSampleTheta());
            packet.put("Built In Theta", blueLocator.getBuiltInAngle());
            packet.put("Max Y Point", blueLocator.getMaxYPoint());
            packet.put("Min Y Point", blueLocator.getMinYPoint());
            packet.put("Box Pt 0", blueLocator.getBoxPoints()[0]);
            packet.put("Box Pt 1", blueLocator.getBoxPoints()[1]);
            packet.put("Box Pt 2", blueLocator.getBoxPoints()[2]);
            packet.put("Box Pt 3", blueLocator.getBoxPoints()[3]);

            dashboard.sendTelemetryPacket(packet);

            telemetry.addData("Blobs Count", blueLocator.blobCount());
            telemetry.addData("Sample Theta", blueLocator.getSampleTheta()
            );
//            telemetry.addData("Box Points", blueLocator.getPoints());
            telemetry.addData("Box Pt 1", blueLocator.getBoxPoints()[0]);
            telemetry.addData("Box Pt 2", blueLocator.getBoxPoints()[1]);
            telemetry.addData("Box Pt 3", blueLocator.getBoxPoints()[2]);
            telemetry.addData("Box Pt 4", blueLocator.getBoxPoints()[3]);

//            telemetry.addData("Straight Box Fit", blueLocator.getStraightBoxFit());
            telemetry.update();
        }

    }
}
