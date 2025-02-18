package org.firstinspires.ftc.teamcode.OpModes.Testing;

import org.firstinspires.ftc.teamcode.OpModes.Teleop.AutomatedTeleop.AutoIntake;
import org.opencv.core.Point;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;

import java.util.List;

@TeleOp(name = "Sample Align", group = "testing")
public class SampleAlignTest extends LinearOpMode {
    public void runOpMode(){
        Bot.init(this);

        AutoIntake autoIntake = new AutoIntake();

//        List<ColorBlobLocatorProcessor.Blob> locatedBlobs;
//        ColorBlobLocatorProcessor.Blob largestBlob;
//
//        Point alignPoint;
//        double[] largestBlobInfo;

        waitForStart();
        while (opModeIsActive()){
//            locatedBlobs = Bot.locator.getBlobsList();
//            largestBlob = Bot.locator.getBlob(locatedBlobs,0);
//
//            alignPoint = Bot.locator.getAlignPoint(largestBlob);
//
//            largestBlobInfo = Bot.locator.getBlobInfo(largestBlob);

//            Bot.dashboardTelemetry.addData("Blob Theta", Bot.locator.getBlobInfo([2]));
            autoIntake.update();


        }
    }
}
