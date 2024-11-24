package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import android.graphics.Color;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "Color Sensor Test")
public class colorTestTeleop extends LinearOpMode {
    RevColorSensorV3 colorSensor;
    int bitShiftBlue, bitShiftRed, bitShiftGreen, bitShiftAlpha;
    double distance;
    float[] hsvValues = {0F,0F,0F};
    NormalizedRGBA normColors;
    float normBlue, normRed, normGreen;

    public double gammaScale (double normColor, double gamma) {
        return Math.pow(normColor, gamma);
    }

    @Override
    public void runOpMode(){
        colorSensor = hardwareMap.get(RevColorSensorV3.class, "colorSensor");
        colorSensor.enableLed(false);


        waitForStart();
        while(opModeIsActive()){
            normColors = colorSensor.getNormalizedColors();
            normColors.toColor();
            //            normColors.toColor()
            distance = colorSensor.getDistance(DistanceUnit.MM);

            bitShiftRed = (colorSensor.red()>>8);
            bitShiftGreen = (colorSensor.green()>>16);
            bitShiftBlue = (colorSensor.blue()>>24);
            bitShiftAlpha = (colorSensor.alpha());

            colorSensor.getNormalizedColors();

            normBlue = normColors.blue;
            normGreen = normColors.green;
            normRed = normColors.red;
            Color.RGBToHSV((colorSensor.red() * 255),
                    (colorSensor.green() * 255),
                    (colorSensor.blue() * 255),
                    hsvValues);
            Color.blue(colorSensor.blue());

//            telemetry.addData("HSV", hsvValues);
//            telemetry.addData("Norm Colors", normColors);

            telemetry.addData("Percent Red", 100.0 * (normColors.red / normColors.alpha));
            telemetry.addData("Percent Green", 100.0 * (normColors.green / normColors.alpha));
            telemetry.addData("Percent Blue", 100.0 * (normColors.blue / normColors.alpha));


            telemetry.addData("Norm blue (mult 100)", normColors.blue * 100.0);
            telemetry.addData("Norm red (mult 100)", normColors.red * 100.0);
            telemetry.addData("Norm green (mult 100)", normColors.green * 100.0);
            telemetry.addLine("");

            telemetry.addData("Norm blue (gamma 0.2)", gammaScale(normColors.blue, 0.2));
            telemetry.addData("Norm red (gamma 0.2)", gammaScale(normColors.red, 0.2));
            telemetry.addData("Norm green (gamma 0.2)", gammaScale(normColors.green, 0.2));
            telemetry.addLine("");

            telemetry.addData("Norm blue", normColors.blue);
            telemetry.addData("Norm red", normColors.red);
            telemetry.addData("Norm green", normColors.green);


//            telemetry.addData("Shift Red", bitShiftRed);
//            telemetry.addData("Shift Green", bitShiftGreen);
//            telemetry.addData("Shift Blue", bitShiftBlue);
//
//            //Note: Color.blue() is the same as bit shifted blue
//
//            telemetry.addData("test blue", Color.blue(colorSensor.blue()));
//
//
////            telemetry.addData("1", colorSensor.getNormalizedColors());
////            telemetry.addData("ARGB", colorSensor.argb());
////            telemetry.addData("3", colorSensor;
//            telemetry.addData("R Raw", colorSensor.red());
//            telemetry.addData("G Raw", colorSensor.green());
//            telemetry.addData("Blue Raw", colorSensor.blue());
//            telemetry.addData("A Raw", colorSensor.alpha());
//
//            telemetry.addData("R div", (colorSensor.red()/(Math.pow(2,16)))*256);
//            telemetry.addData("G div", (colorSensor.green()/(Math.pow(2,16)))*256);
//            telemetry.addData("B div", (colorSensor.blue()/(Math.pow(2,16)))*256);
//            telemetry.addData("A div", (colorSensor.alpha()/(Math.pow(2,16)))*256);
//
//            telemetry.addData("HSV 1", hsvValues[0]);
//            telemetry.addData("HSV 2 (*100)", hsvValues[1]*100);
//            telemetry.addData("HSV 3", hsvValues[2]);
//
//            telemetry.addData("Bin R", Integer.toBinaryString(colorSensor.red()));

            telemetry.update();
        }
    }
}
