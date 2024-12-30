package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibrationIdentity;
import org.firstinspires.ftc.teamcode.Helpers.VideoWritePipeline;
import org.firstinspires.ftc.vision.VisionPortal;

@TeleOp
public class RecordVid extends LinearOpMode {

    VideoWritePipeline pipeline;
    VisionPortal portal;
    CameraCalibration calibration;

    public void runOpMode() {

        int width = 640;
        int height = 480;

        pipeline = new VideoWritePipeline();

        portal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(pipeline)
                .setCameraResolution(new Size(width, height))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .build();

        ElapsedTime timer = new ElapsedTime();

        waitForStart();

//        if(true) {
//            throw new RuntimeException("Killed after start");
//        }
        timer.reset();

        while(opModeIsActive()) {
            if(timer.seconds() > 8) {
                //pipeline.videoWriter.release();
                requestOpModeStop();
            }

            telemetry.addLine("In the while loop");
            telemetry.update();
        }


    }
}
