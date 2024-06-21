package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.MecanumBase;
import org.firstinspires.ftc.teamcode.Helpers.GamepadControls;
import org.firstinspires.ftc.teamcode.Helpers.Ticker;
import org.firstinspires.ftc.teamcode.Helpers.LightPattern;

/**
 * Main class for running the robot with user input
 */
@TeleOp
public class Teleop extends LinearOpMode {
    public void runOpMode() {
        MecanumBase mecanumBase = new MecanumBase(this);
        GamepadControls pad1 = new GamepadControls(gamepad1);
        Ticker tick = new Ticker(0);
        LightPattern lightPattern = new LightPattern(100000, this);

        double maxSpeed = 0.5;

        waitForStart();

        while(opModeIsActive()) {
            pad1.getControls();
            double px = pad1.lStickX;
            double py = -pad1.lStickY;
            double turn = -pad1.rStickX;
            double stickAngle = Math.atan2(py, px);
            double speed = Math.hypot(px, py);

            if(pad1.a) {maxSpeed += 0.01;}
            if(pad1.b) {maxSpeed -= 0.01;}

            if(pad1.x) {mecanumBase.setNorthMode(true);}
            if(pad1.y) {mecanumBase.setNorthMode(false);}
            //Need to make a toggle function

            mecanumBase.move(speed*maxSpeed, stickAngle, turn*maxSpeed);
            lightPattern.lights(tick.getTicks());

            // Telemetry in movement classes
            telemetry.update();
            tick.tick();
        }
    }
}
