package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

public class SlideArm {
    private final DcMotorEx slideMotor;
    ServoClass ocgBox;

    public SlideArm() {
        slideMotor = Bot.opMode.hardwareMap.get(DcMotorEx.class, Config.slideMotor);
        slideMotor.setTargetPositionTolerance(5);
        slideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        ocgBox = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.ocgBox), Config.ocgBoxOpen, Config.ocgBoxClose);
    }

    /**
     * Move the linear slide to a specified position at a specified speed
     * @param position The position to move to, in the range [0, 1] where 0 is fully retracted and 1 is fully extended
     * @param speed The speed to move the slide at, in the range [0, 1]
     */
    public void moveToPosition(double position, double speed) {
        // Convert to ticks and limits slide to max range
        int positionTicks = Math.min(Math.max((int) (position * Config.maxSlideHeight), Config.maxSlideHeight), -Config.minSlideHeight);
        slideMotor.setTargetPosition(positionTicks);
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideMotor.setVelocity(Config.maxSlideTicksPerSecond * speed);
    }

    public void moveToPosition(double position) {
        moveToPosition(position, Config.defaultSlideSpeed);
    }

    public void moveDown(double speed) {
        moveToPosition(Config.slideDown, speed);
    }

    public void moveMid(double speed) {
        moveToPosition(Config.slideLowBasket, speed);
    }

    public void moveUp(double speed) {
        moveToPosition(Config.slideHighBasket, speed);
    }

    public void ocgDrop() {
        ocgBox.closeServo();
    }

    public void ocgUp() {
        ocgBox.openServo();
    }

    public int getArmPosition() {
        return(slideMotor.getCurrentPosition());
    }


}
