package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

public class OCGBox {
    public final ServoClass pitchServo, rollServo;
    //True is in Up/Transfer position
    public boolean rollState, pitchState;

    public OCGBox() {
        this.pitchServo = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.ocgPitchServo), Config.ocgBoxPitchUp, Config.ocgBoxPitchDown);
        this.rollServo = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.ocgRollServo), Config.ocgBoxRollUp, Config.ocgBoxRollDown);
        rollState = true;
        pitchState = true;
        ocgRollUp();
        ocgPitchUp();
    }

    public void goToPitch(double pitch) { pitchServo.anyPos(pitch); }
    public void goToRoll(double roll) { rollServo.anyPos(roll); }

    public void ocgRollUp(){
        goToRoll(Config.ocgBoxRollUp);
        rollState = true;
    }

    public void ocgRollDown(){
        if (pitchState){
            goToRoll(Config.ocgBoxRollDown);
        }
        rollState = false;
    }

    public void ocgPitchUp(){
        goToPitch(Config.ocgBoxPitchUp);
        pitchState = true;
    }

    public void ocgPitchDrop(){
        if (rollState){
            goToPitch(Config.ocgBoxPitchDown);
        }
        pitchState = false;
    }

    public void setRollState(boolean state){
        if (!state){
            ocgRollDown();
        } else {
            ocgRollUp();
        }
    }

    public void setPitchState(boolean state){
        if (!state){
            ocgPitchDrop();
        } else {
            ocgPitchUp();
        }
    }

    // Getters
    public double getPitch() { return pitchServo.getPos(); }
    public double getRoll() { return rollServo.getPos(); }


}
