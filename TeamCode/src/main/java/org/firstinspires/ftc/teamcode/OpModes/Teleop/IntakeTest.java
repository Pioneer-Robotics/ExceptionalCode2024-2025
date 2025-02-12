package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Hardware.Intake;
import org.firstinspires.ftc.teamcode.Hardware.IntakeClaw;

@TeleOp(name = "Intake Test")
public class IntakeTest extends LinearOpMode {
    public void runOpMode() {
        Bot.opMode = this;
        IntakeClaw claw = new IntakeClaw(false);
        Intake intake = new Intake();

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.dpad_up) {
                // Extend intake
                intake.extendMisumiDrive();
                // Rotate misumi wrist down
                intake.misumiWristDown();
                // Rotate claw down
//                claw.clawDown();
            } else if (gamepad1.dpad_down) {
                // Retract intake
                intake.retractMisumiDrive();
                // Rotate misumi wrist up
                intake.misumiWristUp();
                // Rotate claw up
//                claw.clawUp();
            }

        }
    }
}
