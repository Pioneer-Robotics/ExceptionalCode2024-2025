package org.firstinspires.ftc.teamcode.Helpers;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Hardware.CurrentDetection;

import java.util.Timer;
import java.util.TimerTask;

public class CurrentUtils {
    LinearOpMode opMode;
    private CurrentDetection slideCurrent;
    private CurrentDetection specArmCurrent;
    private CurrentDetection LFCurrent, RFCurrent, LBCurrent, RBCurrent;
    Timer timer;
    TimerTask task;

    public CurrentUtils(@NonNull LinearOpMode opMode) {
        this.opMode = opMode;
        this.slideCurrent = new CurrentDetection(Bot.slideArm.getMotor());
        this.specArmCurrent = new CurrentDetection(Bot.specimenArm.getMotor());
        this.LFCurrent = new CurrentDetection(Bot.mecanumBase.getLF());
        this.RFCurrent = new CurrentDetection(Bot.mecanumBase.getRF());
        this.LBCurrent = new CurrentDetection(Bot.mecanumBase.getLB());
        this.RBCurrent = new CurrentDetection(Bot.mecanumBase.getRB());

        start();
    }

    public void checkAll() {
        slideCurrent.checkCurrent();
        specArmCurrent.checkCurrent();
        LFCurrent.checkCurrent();
        RFCurrent.checkCurrent();
        LBCurrent.checkCurrent();
        RBCurrent.checkCurrent();
    }

    public double getSlideCurrent() {return slideCurrent.getCurrent();}
    public double getSpecArmCurrent() {return specArmCurrent.getCurrent();}
    public double getLFCurrent() {return LFCurrent.getCurrent();}
    public double getRFCurrent() {return RFCurrent.getCurrent();}
    public double getLBCurrent() {return LBCurrent.getCurrent();}
    public double getRBCurrent() {return RBCurrent.getCurrent();}

    public double getTotalCurrent() {
        double total = getSlideCurrent()
                     + getSpecArmCurrent()
                     + getLFCurrent()
                     + getRFCurrent()
                     + getLBCurrent()
                     + getRBCurrent();
        return total;
    }

    public void stop() {
        timer.cancel();
    }

    public void start() {
        this.timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                checkAll();
            }
        };
        timer.schedule(task, 0, 50);
    }


}
