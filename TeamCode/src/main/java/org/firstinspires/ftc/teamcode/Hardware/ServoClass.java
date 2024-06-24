package org.firstinspires.ftc.teamcode.Hardware;

/**
 * Servo class with presets
 */

import com.qualcomm.robotcore.hardware.Servo;

public class ServoClass {
    private final Servo servo;
    private final double closePos, openPos;
    private double pos1;
    private double pos2;
    private double pos3;
    private double pos4;

    /**
     * Constructor for ServoClass
     *
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

    public void closeServo(){servo.setPosition(closePos);}

    public void openServo(){servo.setPosition(openPos);}

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
}
