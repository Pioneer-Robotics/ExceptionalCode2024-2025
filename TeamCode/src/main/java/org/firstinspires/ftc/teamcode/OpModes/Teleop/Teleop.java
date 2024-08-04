package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;
import org.firstinspires.ftc.teamcode.Helpers.Utils;


@TeleOp(name="Teleop", group="Teleop")
public class Teleop extends LinearOpMode {
    public void runOpMode() {
        // Bot object
        Bot bot = new Bot(this);

        // Toggles
        Toggle northModeToggle = new Toggle(true);
        Toggle incSpeedToggle = new Toggle(false);
        Toggle decSpeedToggle = new Toggle(false);
        Toggle collectorToggle = new Toggle(false);

        // Initialize max speed
        double maxSpeed = 0.5;

        //Other
        boolean calmDown = false;

        waitForStart();

        if(!Config.competition) {
            try {
                bot.konami.konamiHard();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        while(opModeIsActive()) {
            // ---- GamePad 1 ----
            // Inputs for driving
            double px = gamepad1.left_stick_x;
            double py = -gamepad1.left_stick_y;
            double stickAngle = Math.atan2(py, px);
            double speed = Math.sqrt((px * px + py * py));

            // Move
            bot.mecanumBase.move(stickAngle, -gamepad1.right_stick_x*maxSpeed, speed*maxSpeed);

            // Toggle for field centric
            northModeToggle.toggle(gamepad1.a); // Toggle north mode
            bot.mecanumBase.setNorthMode(northModeToggle.get()); // Update north mode

            // Speed toggles
            incSpeedToggle.toggle(gamepad1.right_bumper);
            decSpeedToggle.toggle(gamepad1.left_bumper);
            if (incSpeedToggle.justChanged()) {
                maxSpeed = Utils.increment(maxSpeed, 0.1, 1);
            }
            if (decSpeedToggle.justChanged()) {
                maxSpeed = Utils.decrement(maxSpeed, 0.1, 0.2);
            }

            // Pixel drop
            bot.pixelDropLeft.selectBoolPos(Utils.floatToBool(gamepad1.left_trigger));
            bot.pixelDropRight.selectBoolPos(Utils.floatToBool(gamepad1.right_trigger));

            // Collector
            collectorToggle.toggle(gamepad1.dpad_down);
            bot.collector.setRunning(collectorToggle.get() && !gamepad1.dpad_up);

            if (gamepad1.dpad_left) {
                bot.collector.up();
            }
            if (gamepad1.dpad_right) {
                bot.collector.down();
            }
            if (gamepad1.dpad_up) {
                bot.collector.reverse();
            }

            //Other
            if (gamepad1.left_stick_button || gamepad1.right_stick_button){
                calmDown = true;
            }

            if (gamepad1.x) {
                bot.imu.resetYaw();
            }

            // ---- GamePad 2 ----
            // Slide motor
            if (gamepad2.dpad_right) {
                bot.commands.armMid();
            } else if (gamepad2.dpad_up) {
                bot.commands.armUp();
            } else if (gamepad2.dpad_down) {
                bot.commands.armDown();
            }

            // Slide servos
            if (gamepad2.b && bot.slide.getPosition() > 0.2) {
                bot.wrist.openServo();
            }
            if (gamepad2.a) {
                bot.wrist.closeServo();
            }

            if (gamepad2.right_bumper && bot.slide.getPosition() > 0.2) {
                bot.gripper.openServo();
            }
            if (gamepad2.left_bumper) {
                bot.gripper.closeServo();
            }

            // Get data for telemetry
            double[] pos = bot.pose.returnPose();

            double voltage = bot.voltageHandler.getVoltage();
            if (voltage < 10) {
                telemetry.addData("WARNING: Voltage Low", voltage);
            }

            // Telemetry and update
            telemetry.addData("Speed", maxSpeed);
            telemetry.addData("X", pos[0]);
            telemetry.addData("Y", pos[1]);
            telemetry.addData("Theta", pos[2]);
            telemetry.addData("Collector Toggle", collectorToggle.get());
            telemetry.addData("Voltage", voltage);
            if(calmDown){
                telemetry.addLine(Config.calmDownMessages[bot.utils.randNum(Config.calmDownMessages.length)]);

            }
            telemetry.update();
        }
    }
}