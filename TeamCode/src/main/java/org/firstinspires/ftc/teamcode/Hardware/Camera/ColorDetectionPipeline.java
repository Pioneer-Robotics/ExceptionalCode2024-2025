package org.firstinspires.ftc.teamcode.Hardware.Camera;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class ColorDetectionPipeline extends OpenCvPipeline {
    int location = -1;

//    Scalar lowerBlue = new Scalar(40.45, 108.54, 26.61);
//    Scalar upperBlue = new Scalar(130.85, 189.44, 132.09);
//    Scalar lowerRed = new Scalar(360, 62, 60);
//    Scalar upperRed = new Scalar(0, 100, 100);
    Scalar lowerRed = new Scalar(0, 108, 76); // Adjust as needed for light blue
    Scalar upperRed = new Scalar(180, 241, 178);

    Mat hsvImg = new Mat();
    Mat mask = new Mat();
    Mat leftCrop = new Mat();
    Mat centerCrop = new Mat();
    Mat rightCrop = new Mat();

    @Override
    public Mat processFrame(Mat input){
        Imgproc.cvtColor(input, hsvImg, Imgproc.COLOR_RGB2HSV);
        Core.inRange(hsvImg, lowerRed, upperRed, mask);
        return(mask);

    }

}
