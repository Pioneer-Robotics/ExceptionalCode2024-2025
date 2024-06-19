package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Hardware.MecanumBase;
import org.firstinspires.ftc.teamcode.Helpers.GamepadControls;

@TeleOp
public class Teleop extends LinearOpMode {
    public void runOpMode() {
        MecanumBase mecanumBase = new MecanumBase(this);
        GamepadControls pad = new GamepadControls(this);

        double maxSpeed = 0.5;
        waitForStart();

        while(opModeIsActive()) {
            double px = pad.lStickX1;
            double py = -pad.lStickY1;
            double turn = -pad.rStickX1;
            double stickAngle = Math.atan2(py, px);
            double speed = Math.hypot(px, py);

            if(pad.a1) {maxSpeed += 0.01;}
            if(pad.a1) {maxSpeed -= 0.01;}

            if(pad.x1) {mecanumBase.setNorthMode(true);}
            if(pad.y1) {mecanumBase.setNorthMode(false);}
            //Need to make a toggle function

            mecanumBase.move(speed*maxSpeed, stickAngle, turn*maxSpeed);

            // Telemetry in movement classes
            telemetry.update();
        }
    }
}
