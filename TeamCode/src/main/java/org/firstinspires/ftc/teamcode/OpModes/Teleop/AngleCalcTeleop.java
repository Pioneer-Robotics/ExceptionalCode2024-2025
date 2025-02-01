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
        ColorBlobLocatorProcessor.Blob largestBlob;
        FtcDashboard dashboard = FtcDashboard.getInstance();
        dashboard.startCameraStream(blueLocator.getCameraStream(), 30);

        waitForStart();
        while (opModeIsActive()){
            locatedBlobs = blueLocator.getBlobsList();
            largestBlob = blueLocator.getBlob(locatedBlobs, 0);

            TelemetryPacket packet = new TelemetryPacket(false);
//            packet.put("Blob Position", blueLocator.getBlobPos());
//            packet.put("Blob Count", blueLocator.blobCount());
            packet.put("Direction", blueLocator.rotationDirection(largestBlob));
            packet.put("Sample Theta", blueLocator.getSampleTheta(largestBlob));
//            packet.put("Biggest Blob Info", blueLocator.getBlobInfo(largestBlob));
            packet.put("Density", blueLocator.percentScreen(largestBlob));
//            packet.put("All Info", blueLocator.getAllBlobsInfo(locatedBlobs));
//            packet.put("Built In Theta", blueLocator.getBuiltInAngle(largestBlob));
//            packet.put("Box Pt 0", blueLocator.getBoxPoints(largestBlob)[0]);
//            packet.put("Box Pt 1", blueLocator.getBoxPoints(largestBlob)[1]);
//            packet.put("Box Pt 2", blueLocator.getBoxPoints(largestBlob)[2]);
//            packet.put("Box Pt 3", blueLocator.getBoxPoints(largestBlob)[3]);

            dashboard.sendTelemetryPacket(packet);
        }

    }
}
