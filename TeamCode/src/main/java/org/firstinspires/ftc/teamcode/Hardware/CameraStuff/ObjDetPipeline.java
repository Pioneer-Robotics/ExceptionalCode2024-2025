package org.firstinspires.ftc.teamcode.Hardware.CameraStuff;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Size;
import org.openftc.easyopencv.OpenCvPipeline;

public class ObjDetPipeline extends OpenCvPipeline{

    Mat blurredImg = new Mat();
    Mat hsvImage = new Mat();
    Mat mask = new Mat();
    Mat morphOutput = new Mat();
    Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(24,24));
    Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(12,12));

    @Override
    public Mat processFrame(Mat input){
        Imgproc.blur(input, blurredImg, new Size(7,7));

        Imgproc.cvtColor(blurredImg, hsvImage, Imgproc.COLOR_BGR2HSV);

        Scalar minValues = new Scalar(42,74.7,68.2);
        Scalar maxValues = new Scalar(58,49.2,99.6);

        Core.inRange(hsvImage, minValues, maxValues, mask);

        return(input);

//        Imgproc.getStructuringElement()
    }

}