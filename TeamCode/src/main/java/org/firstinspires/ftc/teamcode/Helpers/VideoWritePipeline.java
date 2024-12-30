package org.firstinspires.ftc.teamcode.Helpers;

import android.graphics.Canvas;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoWriter;

public class VideoWritePipeline implements VisionProcessor {
    VideoWriter videoWriter;

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
//        String fileName = "VideoTest.mp4";
//        int fourcc = VideoWriter.fourcc('h','2','6','4');
//        //int fourcc = -1;
//        double fps = 30;
//        Size size = new Size(width,height);
//
//        this.videoWriter = new VideoWriter(fileName, fourcc, fps, size, true);
        //this.videoWriter = new VideoWriter();

//        if(true) {
//            throw new RuntimeException("inited videowriter");
//        }

//        if(!videoWriter.open(fileName, fourcc, fps, size, true)) {
//            throw new RuntimeException("WHY IT NO OPEN");
//        }
//        if(videoWriter.open(fileName, fourcc, fps, size, true)) {
//            throw new RuntimeException("IT OPEN??????");
//        }
    }

    @Override
    public Object processFrame(Mat input, long captureTimeNanos) {
        //videoWriter.write(input);
        return input;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

    }
}
