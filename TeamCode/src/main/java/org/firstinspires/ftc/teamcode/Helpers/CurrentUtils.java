package org.firstinspires.ftc.teamcode.Helpers;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Hardware.CurrentDetection;

public class CurrentUtils {
    LinearOpMode opMode;
    private final CurrentDetection slideCurrent1, slideCurrent2;
    private final CurrentDetection specArmCurrent;
    private final CurrentDetection LFCurrent, RFCurrent, LBCurrent, RBCurrent;

    public CurrentUtils(@NonNull LinearOpMode opMode) {
        this.opMode = opMode;
        this.slideCurrent1 = new CurrentDetection(Bot.slideArm.getMotor1(), Config.defaultMaxCurrent, false, true);
        this.slideCurrent2 = new CurrentDetection(Bot.slideArm.getMotor2(), Config.defaultMaxCurrent, false, true);
        this.specArmCurrent = new CurrentDetection(Bot.specimenArm.getMotor(), 2500, true, false);
        this.LFCurrent = new CurrentDetection(Bot.mecanumBase.getLF());
        this.RFCurrent = new CurrentDetection(Bot.mecanumBase.getRF());
        this.LBCurrent = new CurrentDetection(Bot.mecanumBase.getLB());
        this.RBCurrent = new CurrentDetection(Bot.mecanumBase.getRB());

        startThreads();
    }

    public void startThreads() {
        slideCurrent1.start();
        slideCurrent2.start();
        specArmCurrent.start();
        LFCurrent.start();
        RFCurrent.start();
        LBCurrent.start();
        RBCurrent.start();
    }

    public void stopThreads() {
        slideCurrent1.stop();
        slideCurrent2.stop();
        specArmCurrent.stop();
        LFCurrent.stop();
        RFCurrent.stop();
        LBCurrent.stop();
        RBCurrent.stop();
    }

    public double getSlideCurrent1() {return slideCurrent1.getCurrent();}
    public double getSlideCurrent2() {return slideCurrent2.getCurrent();}
    public double getSpecArmCurrent() {return specArmCurrent.getCurrent();}
    public double getLFCurrent() {return LFCurrent.getCurrent();}
    public double getRFCurrent() {return RFCurrent.getCurrent();}
    public double getLBCurrent() {return LBCurrent.getCurrent();}
    public double getRBCurrent() {return RBCurrent.getCurrent();}

    public double getTotalCurrent() {
        double totalCurrent = getSlideCurrent1()
                            + getSlideCurrent2()
                            + getSpecArmCurrent()
                            + getLFCurrent()
                            + getRFCurrent()
                            + getLBCurrent()
                            + getRBCurrent();
        return totalCurrent;
    }


}
