package org.firstinspires.ftc.teamcode.Helpers;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Hardware.CurrentDetection;

public class CurrentUtils {
    LinearOpMode opMode;
    private final CurrentDetection slideCurrent;
    private final CurrentDetection specArmCurrent;
    private final CurrentDetection LFCurrent, RFCurrent, LBCurrent, RBCurrent;

    public CurrentUtils(@NonNull LinearOpMode opMode) {
        this.opMode = opMode;
        this.slideCurrent = new CurrentDetection(Bot.slideArm.getMotor());
        this.specArmCurrent = new CurrentDetection(Bot.specimenArm.getMotor(), 4000);
        this.LFCurrent = new CurrentDetection(Bot.mecanumBase.getLF());
        this.RFCurrent = new CurrentDetection(Bot.mecanumBase.getRF());
        this.LBCurrent = new CurrentDetection(Bot.mecanumBase.getLB());
        this.RBCurrent = new CurrentDetection(Bot.mecanumBase.getRB());

        startThreads();
    }

    public void startThreads() {
        slideCurrent.start(true);
        specArmCurrent.start(false);
        LFCurrent.start(false);
        RFCurrent.start(false);
        LBCurrent.start(false);
        RBCurrent.start(false);
    }

    public void stopThreads() {
        slideCurrent.stop();
        specArmCurrent.stop();
        LFCurrent.stop();
        RFCurrent.stop();
        LBCurrent.stop();
        RBCurrent.stop();
    }

    public double getSlideCurrent() {return slideCurrent.getCurrent();}
    public double getSpecArmCurrent() {return specArmCurrent.getCurrent();}
    public double getLFCurrent() {return LFCurrent.getCurrent();}
    public double getRFCurrent() {return RFCurrent.getCurrent();}
    public double getLBCurrent() {return LBCurrent.getCurrent();}
    public double getRBCurrent() {return RBCurrent.getCurrent();}

    public double getTotalCurrent() {
        double totalCurrent = getSlideCurrent()
                            + getSpecArmCurrent()
                            + getLFCurrent()
                            + getRFCurrent()
                            + getLBCurrent()
                            + getRBCurrent();
        return totalCurrent;
    }


}
