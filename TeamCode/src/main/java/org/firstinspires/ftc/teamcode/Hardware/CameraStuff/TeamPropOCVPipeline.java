package org.firstinspires.ftc.teamcode.Hardware.CameraStuff;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class TeamPropOCVPipeline extends OpenCvPipeline{
    int location = -1;
    public int width = 1280;
    public int height = 720;
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

    Rect leftImg = new Rect(0, 0, width/3, height);
    Rect centerImg = new Rect(width/3, 0,  width/3,height);
    Rect rightImg = new Rect(2*width/3, 0, width/3, height);

    @Override
    public Mat processFrame(Mat input){
        Imgproc.cvtColor(input, hsvImg, Imgproc.COLOR_RGB2HSV);

        Imgproc.rectangle(mask, leftImg,new Scalar(0, 255, 0),2);
        Imgproc.rectangle(mask, centerImg,new Scalar(0,255,0),2);
        Imgproc.rectangle(mask, rightImg,new Scalar(0, 255, 0),2);

        Core.inRange(hsvImg, lowerRed, upperRed, mask);

        leftCrop = mask.submat(leftImg);
        centerCrop = mask.submat(centerImg);
        rightCrop = mask.submat(rightImg);

        return(mask);

    }

}