package org.firstinspires.ftc.teamcode.Helpers;

import static org.firstinspires.ftc.teamcode.Config.trackWidth;
import org.firstinspires.ftc.teamcode.Helpers.AngleUtils;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Config;
import org.opencv.core.Mat;

public class Pose{
//    LinearOpMode opMode;

    DcMotorEx odoLeft, odoRight, odoCenter;
    double x, y;
    double theta = 0;
    double normTheta = 0;
    double[] poseArr = new double[3];
    double deltaLeft, deltaRight, deltaCenter, deltaPerpPos, deltaMiddle;
    double curLeftTicks, curRightTicks, curCenterTicks, prevLeftTicks, prevRightTicks, prevCenterTicks, deltaX, deltaY, phi;

    public Pose(LinearOpMode opMode){
        // Set up odometers
        odoLeft = opMode.hardwareMap.get(DcMotorEx.class, Config.odoLeft);
        odoRight = opMode.hardwareMap.get(DcMotorEx.class, Config.odoRight);
        odoCenter = opMode.hardwareMap.get(DcMotorEx.class, Config.odoCenter);

        odoLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        odoRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        odoCenter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        double trackWidth = Config.trackWidth;

    }

    public void calculate(){
        // Odo readings
        curLeftTicks = -odoLeft.getCurrentPosition();
        curRightTicks = -odoRight.getCurrentPosition();
        curCenterTicks = -odoCenter.getCurrentPosition();

        deltaLeft = curLeftTicks - prevLeftTicks;
        deltaRight = curRightTicks - prevRightTicks;
        deltaCenter = curCenterTicks - prevCenterTicks;

        deltaPerpPos = deltaCenter - Config.forwardOffest*phi;
        deltaMiddle = (deltaLeft + deltaRight)/2;

        phi = ((deltaLeft-deltaRight)*Config.ticsToCM)/trackWidth;

        deltaX = deltaMiddle*Math.cos(normTheta) - deltaPerpPos*Math.sin(normTheta);
        deltaY = deltaMiddle* Math.sin(normTheta) + deltaPerpPos*Math.cos(normTheta);

        x += deltaX*Config.ticsToCM;
        y += deltaY*Config.ticsToCM;
        theta += phi;
        normTheta = AngleUtils.normalizeRadians(theta);

        prevLeftTicks = curLeftTicks;
        prevRightTicks = curRightTicks;
        prevCenterTicks = curCenterTicks;
    }

    public double getY(){return(x);}

    public double getX(){return(y);}

    public double getTheta(){return(normTheta);}

    public void DANCE(){

    }

    /**
     * 0 is X coord
     * 1 is y coord
     * 2 is theta
     */
    public double[] returnPose(){
        calculate();
        poseArr[0] = getX();
        poseArr[1] = getY();
        poseArr[2] = getTheta();
        return (poseArr);
    }

}
