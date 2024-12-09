package org.firstinspires.ftc.teamcode.OpModes.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

@Autonomous(name="PARK Basket", group="Autos")
public class ParkAutoBasket extends LinearOpMode {

    public void runOpMode() {
        Bot.init(this);

        boolean start = true;
        while(opModeIsActive()) {
            if (start) {
                Bot.purePursuit.setTargetPath(new double[][]{{Bot.deadwheel_odom.getX(), Bot.deadwheel_odom.getY()}, {Config.parkX, Config.parkY}});
                start = false;
            }
            Bot.purePursuit.update(0.4);

            if (Bot.purePursuit.reachedTarget(10)) {
                terminateOpModeNow();
            }
        }
    }
}
