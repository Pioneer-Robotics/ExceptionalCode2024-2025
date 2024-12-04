package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.ConfigNew;

public class SlideArm {
    private final DcMotorEx slideMotor;
    ServoClass ocgBox;

    public SlideArm() {
        slideMotor = Bot.opMode.hardwareMap.get(DcMotorEx.class, ConfigNew.slideMotor);
        slideMotor.setTargetPositionTolerance(5);
        slideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
//        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        ocgBox = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, ConfigNew.ocgBox), ConfigNew.ocgBoxOpen, ConfigNew.ocgBoxClose);
    }

    /**
     * Move the linear slide to a specified position at a specified speed
     * @param position The position to move to, in the range [0, 1] where 0 is fully retracted and 1 is fully extended
     * @param speed The speed to move the slide at, in the range [0, 1]
     */
    public void moveToPosition(double position, double speed) {
        // Convert to ticks and limits slide to max range
        int positionTicks = Math.min(Math.max((int) (position * ConfigNew.maxSlideHeight), ConfigNew.maxSlideHeight), -ConfigNew.minSlideHeight);
        slideMotor.setTargetPosition(positionTicks);
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideMotor.setVelocity(ConfigNew.maxSlideTicksPerSecond * speed);
    }

    public void moveToPosition(double position) {
        moveToPosition(position, ConfigNew.defaultSlideSpeed);
    }


    public int getArmPosition() {
        return(slideMotor.getCurrentPosition());
    }


}
