package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Hardware.LocatorClass;
import org.firstinspires.ftc.teamcode.Hardware.ServoClass;
import org.firstinspires.ftc.vision.opencv.ColorRange;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;

import java.util.List;

import org.firstinspires.ftc.vision.opencv.ColorRange;

@TeleOp(name = "Vision Test", group = "Calibration")
public class VisionTest extends LinearOpMode {
    ServoClass testServo;
    public void runOpMode(){
        LocatorClass blueLocator = new LocatorClass(ColorRange.BLUE, this, LocatorClass.CameraOrientation.HORIZONTAL);
        List<ColorBlobLocatorProcessor.Blob> locatedBlobs;
        ColorBlobLocatorProcessor.Blob largestBlob;
        FtcDashboard dashboard = FtcDashboard.getInstance();
        double sampleTheta;

        testServo = new ServoClass(hardwareMap.get(Servo.class, "testServo"),0,1);

        dashboard.startCameraStream(blueLocator.getCameraStream(), 30);
        testServo.anyPos(0.5);
        waitForStart();
        while (opModeIsActive()){
            locatedBlobs = blueLocator.getBlobsList();
            largestBlob = blueLocator.getBlob(locatedBlobs,0);
            sampleTheta = blueLocator.getSampleTheta(largestBlob);

            if (sampleTheta < 0){
                yawAnyNegAngle(sampleTheta);
            } else if (sampleTheta > 0){
                yawAnyPosAngle(sampleTheta);
            } else {
                testServo.anyPos(0.5);
            }

            TelemetryPacket packet = new TelemetryPacket();
            packet.put("Sample Theta", sampleTheta);
            packet.put("Servo Pos", testServo.getPos());
            dashboard.sendTelemetryPacket(packet);
        }

    }

    public void yawAnyPosAngle(double angle){testServo.anyPos((1.5)/(90/(angle)));}
    public void yawAnyNegAngle(double angle){testServo.anyPos((0.5)/(90/(angle)));}
}
