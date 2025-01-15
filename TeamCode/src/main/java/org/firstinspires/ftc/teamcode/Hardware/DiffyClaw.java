package org.firstinspires.ftc.teamcode.Hardware;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

/**
 * Class for controlling a differential claw
 * Servos are assumed to have a range of 0 degrees to 270 degrees
 */
public class DiffyClaw {
    ServoClass diffyServo1, diffyServo2;

    public DiffyClaw() {
        this.diffyServo1 = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.diffyServo1), 0, 1);
        this.diffyServo2 = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.diffyServo2), 0, 1);
        goToPosition(0,0);
    }

    // Utility methods for forward and inverse kinematics
    public double calcSwing(double theta1, double theta2) { return((theta1 + theta2) / 2); }
    public double calcTwist(double theta1, double theta2) { return((theta1 - theta2) / 2); }
    public double calcTheta1(double swing, double twist) { return(swing + twist); }

    // Theta2 must be offset by 135 degrees to stay in the range of 0 to 270
    public double calcTheta2(double swing, double twist) { return(swing - twist + 135); }

    // Utility methods for converting between servo positions and degrees
    public double degreesToServoPos(double degrees) { return(degrees / 270.0); }
    public double servoPosToDegrees(double servoPos) { return(servoPos * 270.0); }

    /**
     * Goes to a swing and twist position
     * @param swing degrees of swing between 0 and 135
     * @param twist degrees of twist between 0 and 135
     */
    public void goToPosition(double swing, double twist) {
        double theta1 = degreesToServoPos(calcTheta1(swing, twist));
        double theta2 = degreesToServoPos(calcTheta2(swing, twist));
        diffyServo1.anyPos(theta1);
        diffyServo2.anyPos(theta2);
    }


    // Getters
    public double getSwing() {
        double theta1 = servoPosToDegrees(getServo1());
        double theta2 = servoPosToDegrees(getServo2());
        return(calcSwing(theta1, theta2));
    }

    public double getTwist() {
        double theta1 = servoPosToDegrees(getServo1());
        double theta2 = servoPosToDegrees(getServo2());
        return(calcTwist(theta1, theta2));
    }

    public double getServo1() { return(diffyServo1.getPos()); }
    public double getServo2() { return(diffyServo2.getPos()); }

}
