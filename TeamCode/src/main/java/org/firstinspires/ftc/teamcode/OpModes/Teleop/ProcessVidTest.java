package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.opencv.core.Size;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.VideoCapture;
@Disabled
@TeleOp
public class ProcessVidTest extends LinearOpMode {
    VideoCapture videoCapture;
    VideoWriter videoWriter;

    public void runOpMode() {
        // videoCapture = new VideoCapture("/sdcard/compressed Screen Recording 2024-12-20 at 7.13.36 PM.mov");
        //videoCapture = new VideoCapture(0);
        videoWriter = new VideoWriter("asdf.mp4", VideoWriter.fourcc('h','2','6','4'), 20, new Size(640, 380));
        //videoWriter = new VideoWriter();
        videoWriter.open("asdf.mp4", VideoWriter.fourcc('h','2','6','4'), 20, new Size(640, 380), true);
        //videoWriter.open();


        if(videoWriter.isOpened()) {
            throw new RuntimeException("It say it open");
        }

        if(!videoWriter.isOpened()) {
            throw new RuntimeException("It say it CLOSED");
        }

        if(!videoWriter.open("asdf.mp4", -1, 20, new Size(640, 380), true)) {
            throw new RuntimeException("WHY IT NO OPEN");
        }
        if(videoWriter.open("asdf.mp4", -1, 20, new Size(640, 380), true)) {
            throw new RuntimeException("IT OPEN??????");
        }

        waitForStart();

        while (opModeIsActive()) {
            requestOpModeStop();
        }
    }
}
