package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;

@TeleOp(name="Teleop", group="Teleop")
public class Teleop extends LinearOpMode {
    public void runOpMode() {
        Bot bot = new Bot(this);
        Toggle northModeToggle = new Toggle(true);

        waitForStart();

        while(opModeIsActive()) {
            double px = gamepad1.left_stick_x;
            double py = -gamepad1.left_stick_y;
            double stickAngle = Math.atan2(py, px);
            double speed = Math.sqrt((px * px + py * py));
            double maxSpeed = 1;

            northModeToggle.toggle(gamepad1.a); // Toggle north mode
            bot.mecanumBase.setNorthMode(northModeToggle.get()); // Update north mode

            bot.mecanumBase.move(stickAngle, -gamepad1.right_stick_x*maxSpeed, speed*maxSpeed);

            // Get the pose in the teleop loop
            double[] pos = bot.pose.returnPose();

            double voltage = bot.voltageHandler.getVoltage();
            if (voltage < 10) {
                telemetry.addData("WARNING: Voltage Low: ", voltage);
            }

            // Telemetry in movement classes
            telemetry.addData("X", pos[0]);
            telemetry.addData("Y", pos[1]);
            telemetry.addData("Theta", pos[2]);
            telemetry.addData("Voltage", voltage);
            telemetry.update();
        }
    }
}