package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

public class OCGBox {
    private final ServoClass pitchServo, rollServo;

    public OCGBox() {
        this.pitchServo = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.pitchServo), Config.ocgBoxPitchUp, Config.ocgBoxPitchDown);
        this.rollServo = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.rollServo), Config.ocgBoxRollUp, Config.ocgBoxRollDown);
    }

    public void goToPitch(double pitch) { pitchServo.anyPos(pitch); }
    public void goToRoll(double roll) { rollServo.anyPos(roll); }



    // Getters
    public double getPitch() { return pitchServo.getPos(); }
    public double getRoll() { return rollServo.getPos(); }


}
