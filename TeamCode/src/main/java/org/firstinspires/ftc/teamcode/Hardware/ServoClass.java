package org.firstinspires.ftc.teamcode.Hardware;

/**
 * Servo class with presets
 */

import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.Config;

public class ServoClass {
    private final Servo servo;
    private double openPos, closePos, pos1, pos2, pos3, pos4;

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
     */
    public void anyPos(double pos){servo.setPosition(pos);}
    /**
     * Set the servo to a preset
     */
    public void selectPos(int pos){
        if(pos == 1){servo.setPosition(pos1);}
        else if(pos == 2){servo.setPosition(pos2);}
        else if(pos == 3){servo.setPosition(pos3);}
        else if(pos == 4){servo.setPosition(pos4);}
    }
}
