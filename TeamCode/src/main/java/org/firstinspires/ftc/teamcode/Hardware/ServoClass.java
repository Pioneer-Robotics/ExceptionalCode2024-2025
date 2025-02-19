package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

/**
 * Servo class with presets
 */
public class ServoClass {
    private final Servo servo;
    private final double closePos, openPos;
    private double pos1;
    private double pos2;
    private double pos3;
    private double pos4;

    /**
     * Constructor for ServoClass
     * @param servo    Servo object
     * @param openPos  double open position in range [0, 1]
     * @param closePos double close position in range [0, 1]
     */
    public ServoClass(Servo servo, double openPos, double closePos){
        this.servo = servo;
        this.openPos = openPos;
        this.closePos = closePos;

        //These positions are customizable
        pos1 = openPos + 0.04;
        pos2 = pos1 + 0.04;
        pos3 = pos2 + 0.04;
        pos4 = pos3 + 0.04;
    }

    /**
     * Constructor for ServoClass
     *
     * @param servo    Servo object
     * @param openPos  double open position in range [0, 1]
     * @param closePos double close position in range [0, 1]
     * @param startPos double start position in range [0, 1]
     */
    public ServoClass(Servo servo, double openPos, double closePos, double startPos) {
        this.servo = servo;
        this.openPos = openPos;
        this.closePos = closePos;

        //These positions are customizable
        pos1 = openPos + 0.04;
        pos2 = pos1 + 0.04;
        pos3 = pos2 + 0.04;
        pos4 = pos3 + 0.04;

        this.servo.setPosition(startPos);
    }

    public void closeServo(){servo.setPosition(closePos);}

    public void openServo() {
        servo.setPosition(openPos);
    }

    /**
     * Closes/opens servo based on boolean parameter
     *
     * @param position boolean position (false is closed and true is open(
     */
    public void selectBoolPos(boolean position) {
        double newPos = position ? openPos : closePos;
        if (servo.getPosition() != newPos) {
            servo.setPosition(newPos);
        }
    }

    /**
     * Set the servo to any value
     * @param pos double position in range [0, 1]
     */
    public void anyPos(double pos){servo.setPosition(pos);}
    /**
     * Set the servo to a preset
     * @param pos int position in range [1, 4]
     */
    public void selectPos(int pos){
        if(pos == 1){servo.setPosition(pos1);}
        else if(pos == 2){servo.setPosition(pos2);}
        else if(pos == 3){servo.setPosition(pos3);}
        else if(pos == 4){servo.setPosition(pos4);
        }
    }

    /**
     * Set servo preset position 1
     *
     * @param pos double position in range [0, 1]
     */
    public void setPos1(double pos) {
        pos1 = pos;
    }

    /**
     * Set servo preset position 2
     *
     * @param pos double position in range [0, 1]
     */
    public void setPos2(double pos) {
        pos2 = pos;
    }

    /**
     * Set servo preset position 3
     *
     * @param pos double position in range [0, 1]
     */
    public void setPos3(double pos) {
        pos3 = pos;
    }

    /**
     * Set servo preset position 4
     *
     * @param pos double position in range [0, 1]
     */
    public void setPos4(double pos) {
        pos4 = pos;}

    public double getPos() {
        return servo.getPosition();
    }
}
