package org.firstinspires.ftc.teamcode.OpModes.SubSystems;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Bot;

public class TeleopDriver2 {

    // Properties
    Gamepad gamepad2;

    RobotSpecimenArmSubSystem specimenArmSubSystem;
    RobotOcgBoxSubSystem ocgBoxSubSystem;
    RobotSlideArmSubSystem slideArmSubSystem;

    // Initialization
    private TeleopDriver2(Gamepad gamepad2) {
        this.gamepad2 = gamepad2;

        specimenArmSubSystem = RobotSpecimenArmSubSystem.createInstance(gamepad2);
        ocgBoxSubSystem = RobotOcgBoxSubSystem.createInstance(gamepad2);
        slideArmSubSystem = RobotSlideArmSubSystem.createInstance(gamepad2);
    }

    // Factory method to create instances with variables
    public static TeleopDriver2 createInstance(Gamepad gamepad2) {
        return new TeleopDriver2(gamepad2);
    }

    // Run Loop
    public void doOneStateLoop() {
        specimenArmSubSystem.doOneStateLoop();
        ocgBoxSubSystem.doOneStateLoop();
        slideArmSubSystem.doOneStateLoop();

        handleClawYaw();
    }

    // SubSystem private methods
    private void handleClawYaw() {
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
    }
}
