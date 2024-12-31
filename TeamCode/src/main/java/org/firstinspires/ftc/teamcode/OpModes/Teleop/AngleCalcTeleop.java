package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import android.util.Size;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.SortOrder;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Hardware.Camera.LocatorClass;
import org.firstinspires.ftc.teamcode.SelfDrivingAuto.PID;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;

import java.sql.Blob;
import java.util.List;
import org.firstinspires.ftc.teamcode.Hardware.Camera.BlobClass;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ImageRegion;

@TeleOp(name = "Angle Calc Test")
public class AngleCalcTeleop extends LinearOpMode {
    public void runOpMode() {
        LocatorClass blueLocator = new LocatorClass(ColorRange.BLUE, this);

        List<ColorBlobLocatorProcessor.Blob> locatedBlobs;

        waitForStart();
        while (opModeIsActive()){

            telemetry.addData("Blobs Count", blueLocator.blobCount());
            telemetry.addData("Num Cont Points", blueLocator.numContPoints());
//            telemetry.addData("Straight Box Fit", blueLocator.getStraightBoxFit());
            telemetry.addData("Contours", blueLocator.getContourLine());
            telemetry.update();
        }

    }
}
