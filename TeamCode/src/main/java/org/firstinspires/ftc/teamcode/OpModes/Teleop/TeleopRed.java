package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.OpModes.Testing.BlockDetector;

@TeleOp(name="Teleop Red")
public class TeleopRed extends Teleop {
    @Override
    boolean useCameraCode() {
        return  true;
    }
    @Override
    BlockDetector.TeamColor teamColor() {
        return BlockDetector.TeamColor.RED;
    }
}
