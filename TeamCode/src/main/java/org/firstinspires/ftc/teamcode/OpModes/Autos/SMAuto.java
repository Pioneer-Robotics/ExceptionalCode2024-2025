package org.firstinspires.ftc.teamcode.OpModes.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Bot;

@Autonomous(name="State Machine Auto", group="Autos")
public class SMAuto extends LinearOpMode {
    enum State {
        INIT,
        SPECIMIN_HANG_1,
        SPECIMIN_HANG_2,
    }

    State state = State.INIT;

    public void runOpMode() {
        Bot.init(this);

        while (opModeIsActive()) {

            switch (state) {
                case INIT:
                    telemetry.addLine("Inside Case INIT");

                    Bot.purePursuit.setTargetPath(new double[][]{{0, 0}, {-31.3, 63.3}});
                    Bot.specimenArm.moveToPos1(0.5);

                    state = State.SPECIMIN_HANG_1;

                case SPECIMIN_HANG_1:
                    telemetry.addLine("Inside Case SPECIMIN_HANG_1");
                    if (Bot.purePursuit.reachedTarget()) {
                        Bot.specimenArm.moveToPos2(0.5);
                        state = State.SPECIMIN_HANG_2;
                    }
            }
            telemetry.addData("State", state);
            telemetry.update();



        }
    }
}
