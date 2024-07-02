package org.firstinspires.ftc.teamcode.Hardware.Camera;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class ColorDetectionPipeline extends OpenCvPipeline{
    //Streaming in 1280x720

    public int width = 1280;
    public int height = 720;
    public int location = -1;

    Scalar lowerRed = new Scalar(108,40,40); // Adjust as needed for light blue
    Scalar upperRed = new Scalar(255,0,0); // Adjust as needed for dark blue

//    Rect leftHalf = new Rect(1,1,(width/2)-1,height-1);
//    Rect rightHalf = new Rect((width/2)+1,1,(width/2),height-1);
    Rect leftImg = new Rect(0, 0, width/3, height);
    Rect centerImg = new Rect(width/3, 0,  width/3,height);
    Rect rightImg = new Rect(2*width/3, 0, width/3, height);

    Mat blurredImg = new Mat();
    Mat modImg = new Mat();
    Mat leftCrop = new Mat();
    Mat centerCrop = new Mat();
    Mat rightCrop = new Mat();


    double leftAvgFin;
    double centerAvgFin;
    double rightAvgFin;
    double Max = 0;
    double test = 0;


    @Override
    public Mat processFrame(Mat input){
//        Imgproc.cvtColor(input, modImg, Imgproc.COLOR_RGB2YCrCb);
        modImg = input;

        Imgproc.rectangle(modImg, leftImg,new Scalar(0, 255, 0),2);
        Imgproc.rectangle(modImg, centerImg,new Scalar(0,255,0),2);
        Imgproc.rectangle(modImg, rightImg,new Scalar(0, 255, 0),2);

//        Imgproc.blur(modImg,blurredImg,new Size(15,15));

        leftCrop = modImg.submat(leftImg);
        centerCrop = modImg.submat(centerImg);
        rightCrop = modImg.submat(rightImg);

        Core.extractChannel(leftCrop, leftCrop,0);
        Core.extractChannel(centerCrop, centerCrop,0);
        Core.extractChannel(rightCrop, rightCrop,0);


        Scalar leftAvg = Core.mean(leftCrop);
        Scalar centerAvg = Core.mean(centerCrop);
        Scalar rightAvg = Core.mean(rightCrop);

        leftAvgFin = leftAvg.val[0];
        centerAvgFin = centerAvg.val[0];
        rightAvgFin = rightAvg.val[0];

        Max = Math.max(rightAvgFin, Math.max(leftAvgFin, centerAvgFin));

        if(Max == leftAvgFin){location = 0;}
        if(Max == centerAvgFin){location = 1;}
        if(Max == rightAvgFin){location = 2;}
        test += 1;

        return(modImg);

    }

}
