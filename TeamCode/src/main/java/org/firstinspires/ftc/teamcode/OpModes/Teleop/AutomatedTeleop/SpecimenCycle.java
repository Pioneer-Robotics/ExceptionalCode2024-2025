package org.firstinspires.ftc.teamcode.OpModes.Teleop.AutomatedTeleop;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Helpers.AutoPaths;

import java.util.Timer;
import java.util.TimerTask;

public class SpecimenCycle {
    private enum State {
        HANG,
        COLLECT,
        WAIT_FOR_CLAW
    }

    private State state = State.HANG;
    private final ElapsedTime timer = new ElapsedTime();

    public static SpecimenCycle createInstance() {
        return new SpecimenCycle();
    }

    public void start() {
        Bot.pinpoint.setPose(Config.specCollectX, Config.specCollectY, 0);
        state = State.HANG;
    }

    public void update() {
        switch (state) {
            case HANG:
                handleHang();
                break;
            case COLLECT:
                handleCollect();
                break;
            case WAIT_FOR_CLAW:
                handleWaitForClaw();
                break;
        }
    }

    private void scheduleSpecimenArmCollect() {
        Timer armSchedule = new Timer();
        armSchedule.schedule(new TimerTask() {
            @Override
            public void run() {
                Bot.specimenArm.moveToCollect(0.75);
            }
        }, 750);
    }

    private void handleHang() {
        Bot.purePursuit.update(0.65);
        if (Bot.purePursuit.reachedTarget(2.5)) {
            AutoPaths.collectSpecimen(
                    Bot.pinpoint.getX(), // Current X
                    Bot.pinpoint.getY(), // Current Y
                    true // Coming from the submersible
            );
            scheduleSpecimenArmCollect();
            state = State.COLLECT;
        }
    }

    private void handleCollect() {
        Bot.purePursuit.update(0.525, true);
        if (Bot.purePursuit.reachedTargetXY(1.5, 1)) {
            Bot.purePursuit.stop();
            Bot.specimenArm.closeClaw();
            timer.reset();
            state = State.WAIT_FOR_CLAW;
        }
    }

    private void handleWaitForClaw() {
        if (timer.milliseconds() > 250) {
            // FIXME: Add sliding motion to hang specimens without offset
            AutoPaths.hangSpecimen(
                    Bot.pinpoint.getX(), // Current X
                    Bot.pinpoint.getY(), // Current Y
                    0, // Hang offsetX X
                    0 // Offset Y
            );
            Bot.specimenArm.movePrepHang(0.5);
            state = State.HANG;
        }
    }
}
