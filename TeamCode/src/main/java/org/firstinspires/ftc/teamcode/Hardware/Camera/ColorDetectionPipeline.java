package org.firstinspires.ftc.teamcode.Hardware.Camera;


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

    Rect leftHalf = new Rect(1,1,(width/2),height-1);
    Rect rightHalf = new Rect((width/2)+1,1,width-2,height-1);

    Mat blurredImg = new Mat();
    Mat modImg = new Mat();
    Mat leftCrop = new Mat();
    Mat rightCrop = new Mat();


    double leftAvgFin;

    double rightAvgFin;


    @Override
    public Mat processFrame(Mat input){
        Imgproc.cvtColor(input, modImg, Imgproc.COLOR_RGB2YCrCb);

        Imgproc.rectangle(input,leftHalf,new Scalar(0, 255, 0),2);
        Imgproc.rectangle(input,rightHalf,new Scalar(0, 255, 0),2);

//        Imgproc.blur(modImg,blurredImg,new Size(15,15));

        leftCrop = input.submat(leftHalf);
        rightCrop = input.submat(rightHalf);

        Core.extractChannel(leftCrop, leftCrop,2);
        Core.extractChannel(leftCrop, leftCrop,2);

        Scalar leftAvg = Core.mean(leftCrop);
        Scalar rightAvg = Core.mean(rightCrop);

        leftAvgFin = leftAvg.val[0];
        rightAvgFin = rightAvg.val[0];
//        0 is left, 1 is right

        if(leftAvgFin > rightAvgFin){
            location = 0;
        } else if (rightAvgFin > leftAvgFin) {
            location = 1;
        }

        return(rightCrop);

    }


}
