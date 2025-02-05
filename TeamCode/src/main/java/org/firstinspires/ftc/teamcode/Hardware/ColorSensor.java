//package org.firstinspires.ftc.teamcode.Hardware;
//
//import com.qualcomm.hardware.rev.RevColorSensorV3;
//
//import org.firstinspires.ftc.teamcode.Bot;
//import org.firstinspires.ftc.teamcode.Config;
//
//public class ColorSensor {
//    RevColorSensorV3 colorSensor;
//    public ColorSensor() {
//        colorSensor = Bot.opMode.hardwareMap.get(RevColorSensorV3.class, Config.colorSensor);
//        colorSensor.enableLed(true);
//    }
//
//    /**
//     * Returns raw values from color sensor
//     * @return int[] {red, green, blue, alpha}
//     */
//    public int[] getRawColor() {
//        return new int[] {colorSensor.red(), colorSensor.green(), colorSensor.blue(), colorSensor.alpha()};
//    }
//
//    public double getBlueDistance() {
//        return Math.sqrt(
//                Math.pow(Config.colorBlue[0]-colorSensor.red(), 2) +
//                Math.pow(Config.colorBlue[1]-colorSensor.green(), 2) +
//                Math.pow(Config.colorBlue[2]-colorSensor.blue(), 2)
//        );
//    }
//
//    public double getYellowDistance() {
//        return Math.sqrt(
//                Math.pow(Config.colorYellow[0]-colorSensor.red(), 2) +
//                Math.pow(Config.colorYellow[1]-colorSensor.green(), 2) +
//                Math.pow(Config.colorYellow[2]-colorSensor.blue(), 2)
//        );
//    }
//
//    public double getRedDistance() {
//        return Math.sqrt(
//                Math.pow(Config.colorRed[0]-colorSensor.red(), 2) +
//                Math.pow(Config.colorRed[1]-colorSensor.green(), 2) +
//                Math.pow(Config.colorRed[2]-colorSensor.blue(), 2)
//        );
//    }
//
//    /**
//     * Certainty that it is seeing a blue sample
//     * @return in range [0,1]
//     */
//    public double getBluePercent() {
//        return (getBlueDistance())/(getBlueDistance()+getRedDistance()+getYellowDistance());
//    }
//
//    /**
//     * Certainty that it is seeing a yellow sample
//     * @return in range [0,1]
//     */
//    public double getYellowPercent() {
//        return (getYellowDistance())/(getBlueDistance()+getRedDistance()+getYellowDistance());
//    }
//
//    /**
//     * Certainty that it is seeing a red sample
//     * @return in range [0,1]
//     */
//    public double getRedPercent() {
//        return (getRedDistance())/(getBlueDistance()+getRedDistance()+getYellowDistance());
//    }
//
//    public double getMinDistance() {
//        return Math.min(Math.min(getRedDistance(), getBlueDistance()), getYellowDistance());
//    }
//
//    /**
//     * Returns the current color seen by the sensor
//     * Based on minimum euclidean distance
//     * @return "red", "blue", "yellow", or "none"
//     */
//    public String getColor() {
//        if (getRawColor()[3] < Config.alphaTolerance) {
//            // Alpha is not above tolerance, no sample
//            return "none";
//        }
//        if (getMinDistance() == getRedDistance()) {
//            return "red";
//        }
//        if (getMinDistance() == getYellowDistance()) {
//            return "yellow";
//        }
//        return "blue";
//    }
//}
