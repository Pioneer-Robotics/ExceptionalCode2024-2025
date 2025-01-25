package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;


@TeleOp(name="Teleop")
public class Teleop extends LinearOpMode {
    public void runOpMode() {
        Bot.init(this);

        // Toggles
        Toggle northModeToggle = new Toggle(true);
        Toggle incSpeedToggle = new Toggle(false);
        Toggle decSpeedToggle = new Toggle(false);
        Toggle clawToggle = new Toggle(false);
        Toggle intakeClawToggle = new Toggle(false);
        Toggle intakeWristToggle = new Toggle(false);
        Toggle ocgBoxToggle = new Toggle(true);
        Toggle ocgBoxToggleRight = new Toggle(true);

        boolean bothTrigPressed;

        // Initialize max speed
        double maxSpeed = 0.5;

        ElapsedTime timer = new ElapsedTime();
        double prevMilliseconds = 0;

        waitForStart();

        while(opModeIsActive()) {
            /* ------------------
               -    GamePad 1   -
               ------------------ */

            // Inputs for driving
            double px = gamepad1.left_stick_x;
            double py = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;

            // Move
            Bot.mecanumBase.move(px, py, turn, maxSpeed);

            // Toggle for field centric

            bothTrigPressed = gamepad1.left_trigger > 0.8 && gamepad1.right_trigger > 0.8;
            northModeToggle.toggle(bothTrigPressed); // Toggle north mode
            Bot.mecanumBase.setNorthMode(northModeToggle.get()); // Update north mode

            if (gamepad1.x) {
                Bot.imu.resetYaw();
            }

            incSpeedToggle.toggle(gamepad1.right_bumper);
            decSpeedToggle.toggle(gamepad1.left_bumper);
            if (incSpeedToggle.justChanged()) {
                maxSpeed += 0.1;
            }
            if (decSpeedToggle.justChanged()) {
                maxSpeed -= 0.1;
            }

           if (gamepad1.dpad_up) {
               Bot.intake.midMisumiWrist();
               Bot.intake.extendMisumiDrive();
               Bot.intakeClaw.clawDown();
//               Bot.intake.openIntakeWrist();
               intakeWristToggle.set(true);
           } else if (gamepad1.dpad_down) {
               Bot.intake.midMisumiWrist();
//               Bot.intake.closeIntakeWrist();
               Bot.intake.retractMisumiDrive();
               Bot.intakeClaw.clawUp();
               intakeWristToggle.set(false);
           }

            intakeClawToggle.toggle(gamepad1.b);
            if (intakeClawToggle.justChanged() && intakeClawToggle.get()) {
                Bot.intakeClaw.openClaw();
            } else if (intakeClawToggle.justChanged() && !intakeClawToggle.get()) {
                Bot.intakeClaw.closeClaw();
            }

            intakeWristToggle.toggle(gamepad1.y);
            if (intakeWristToggle.justChanged() && intakeWristToggle.get()) {
                if (Bot.intake.isExtended()){
                    Bot.intake.misumiWristDown();
                } else {
                    Bot.intake.midMisumiWrist();
                }
            } else if (intakeWristToggle.justChanged() && !intakeWristToggle.get()) {
                if (Bot.intake.isExtended()) {
                    Bot.intake.midMisumiWrist();
                } else {
                    Bot.intake.misumiWristUp();
                }
            }

            /* ------------------
               -    GamePad 2   -
               ------------------ */

            // Preset specimen arm positions
            if (gamepad2.dpad_up) {
                Bot.specimenArm.movePostHang(1.0);
            } else if (gamepad2.dpad_down) {
                Bot.specimenArm.movePrepHang(0.5);
            } else if (gamepad2.dpad_left) {
                Bot.specimenArm.moveToCollect(0.5);
            }

            // Reset arm
            if (gamepad2.left_trigger > 0.1 || gamepad2.right_trigger > 0.1) {
                // Will get cast to an int anyways when incrementing the config
                int armAdjust = (int) (7.0 * gamepad2.right_trigger - 7.0 * gamepad2.left_trigger);
                Config.specimenArmPostHang += armAdjust;
                Config.specimenArmPrepHang += armAdjust;

                if (Bot.specimenArm.getPosition() == 2) {
                    Bot.specimenArm.moveToCollect(0.3);
                } else if (Bot.specimenArm.getPosition() == 1) {
                    Bot.specimenArm.movePostHang(0.4);
                } else if (Bot.specimenArm.getPosition() == 0) {
                    Bot.specimenArm.movePrepHang(0.4);
                }

            }

            // Specimen Claw toggle
            clawToggle.toggle(gamepad2.b);
            if (clawToggle.justChanged()) {
                Bot.specimenArm.setClawPosBool(clawToggle.get());
            }

            //Claw Yaw
            if (gamepad2.left_stick_x < -0.1 && gamepad2.left_stick_x>-0.5){
                Bot.intakeClaw.clawNeg45();
            } else if (gamepad2.left_stick_x<-0.5){
                Bot.intakeClaw.clawNeg90();
            } else if (gamepad2.left_stick_x>0.1 && gamepad2.left_stick_x<0.5){
                Bot.intakeClaw.clawPos45();
            } else if (gamepad2.left_stick_x>0.5){
                Bot.intakeClaw.clawPos90();
            } else {
                Bot.intakeClaw.clawPos0();
            }

            // Slide Arm
            if (gamepad2.y) {
                Bot.intakeClaw.clawDown();
                Bot.intake.midMisumiDrive();
                Bot.intake.midMisumiWrist();
//                Bot.intake.closeClaw();
                Bot.slideArm.moveUp(0.8);

            } else if (gamepad2.a) {
                Bot.intakeClaw.clawDown();
                Bot.intake.midMisumiDrive();
                Bot.intake.midMisumiWrist();
                Bot.slideArm.moveDown(0.8);
            } else if (gamepad2.x) {
                Bot.intakeClaw.clawDown();
                Bot.intake.midMisumiDrive();
                Bot.slideArm.moveMid(0.8);
//                Bot.intake.closeClaw();
            }

            // Manual slide arm controls
            if (gamepad2.right_stick_y > 0.5) {
                Bot.slideArm.move(-(gamepad2.right_stick_y - 0.5));
            } else if (gamepad2.right_stick_y < -0.5){
                Bot.slideArm.move(-(gamepad2.right_stick_y + 0.5));
            } else if(Math.abs(gamepad2.right_stick_x)>0.5) {
                Bot.slideArm.motorOff();
            }

            // Box state
            //Pitch
            ocgBoxToggle.toggle(gamepad2.left_bumper);
            if (ocgBoxToggle.justChanged()) {
//                Bot.slideArm.setOCGBox(ocgBoxToggle.get());
                Bot.ocgBox.setPitchState(ocgBoxToggle.get());
            }

            ocgBoxToggleRight.toggle(gamepad2.right_bumper);
            //Roll
            if (ocgBoxToggleRight.justChanged()) {
//                Bot.slideArm.setOCGBoxRight(ocgBoxToggleRight.get());
                Bot.ocgBox.setRollState(ocgBoxToggleRight.get());
            }

            // Get data for telemetry
            double voltage = Bot.voltageHandler.getVoltage();
            if (voltage < 10) {
                Bot.led.lightsOn(RevBlinkinLedDriver.BlinkinPattern.YELLOW);
                telemetry.addData("WARNING: Voltage Low", voltage);
            } else {
                Bot.led.lightsOn(RevBlinkinLedDriver.BlinkinPattern.GREEN);
            }

            // Telemetry and update
            Bot.pinpoint.update();
            telemetry.addData("Total Current", Bot.currentThreads.getTotalCurrent());
            telemetry.addData("dt", timer.milliseconds() - prevMilliseconds);
            telemetry.addData("Refresh Rate (Hz)", 1000 / (timer.milliseconds() - prevMilliseconds));
            telemetry.addData("Thread Current Slide 1", Bot.currentThreads.getSlideCurrent1());
            telemetry.addData("Thread Current Slide 2", Bot.currentThreads.getSlideCurrent2());
            telemetry.addData("Thread Current Spec", Bot.currentThreads.getSpecArmCurrent());
            telemetry.addData("North Mode", northModeToggle.get());
            telemetry.addData("Slide Arm Position", Bot.slideArm.getArmPosition());
            telemetry.addData("Specimen Arm Position", Bot.specimenArm.getPositionTicks());
            telemetry.addData("specimenArmPostHang", Config.specimenArmPostHang);
            telemetry.addData("specimenArmPrepHang", Config.specimenArmPrepHang);
            telemetry.addData("X", Bot.pinpoint.getX());
            telemetry.addData("Y", Bot.pinpoint.getY());
            telemetry.addData("Heading", Bot.pinpoint.getHeading());
            telemetry.addData("Speed", maxSpeed);
            telemetry.addData("Voltage", voltage);
            telemetry.update();
            prevMilliseconds = timer.milliseconds();
        }
        Bot.currentThreads.stopThreads();
    }
}
