package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Threads.IncThread;
import org.firstinspires.ftc.teamcode.Threads.LEDThread;


@TeleOp(name = "MultithreadTest")
public class MultithreadTest extends LinearOpMode {

    public void runOpMode() throws InterruptedException {
        Bot.init(this);

        IncThread incrementer = new IncThread();
        LEDThread leds = new LEDThread(1, Bot.led);

        Thread incThread = new Thread(incrementer);
        Thread ledThread = new Thread(leds);

        waitForStart();
        incThread.start();
        ledThread.start();

        while(opModeIsActive()) {
            telemetry.addLine("Running");
            telemetry.addData("Counter", incrementer.getCounter());
            telemetry.update();
        }
    }
}

