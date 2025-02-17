package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.OpModes.Testing.BlockDetector;

@TeleOp(name="Teleop Blue")
public class TeleopBlue extends Teleop {
    @Override
    boolean useCameraCode() {
        return  true;
    }
    @Override
    BlockDetector.TeamColor teamColor() {
        return BlockDetector.TeamColor.BLUE;
    }
}
