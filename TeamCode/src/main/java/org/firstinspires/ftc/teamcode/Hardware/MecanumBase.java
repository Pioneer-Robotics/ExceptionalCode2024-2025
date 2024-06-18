package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Hardware.BotIMU;

public class MecanumBase {
    LinearOpMode opMode;
    DcMotorEx LF, LB, RF, RB;
    BotIMU imu;
    boolean northMode;
    public MecanumBase(LinearOpMode opMode) {
        // Contains hardwareMap and telemetry
        this.opMode = opMode;
        imu = new BotIMU(opMode);

        // Set up motors
        RF = opMode.hardwareMap.get(DcMotorEx.class, Config.motorRF);
        LF = opMode.hardwareMap.get(DcMotorEx.class, Config.motorLF);
        RB = opMode.hardwareMap.get(DcMotorEx.class, Config.motorRB);
        LB = opMode.hardwareMap.get(DcMotorEx.class, Config.motorLB);

        LF.setDirection(DcMotor.Direction.REVERSE);
        LB.setDirection(DcMotor.Direction.REVERSE);
        RF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        RF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


    }

    public void move(double speed, double angle, double turn) {

        double currentAngle = imu.getRadians();

        if(northMode) {angle -= currentAngle;}

        double power1 = (Math.sin(angle - (Math.PI / 4)) * speed);
        double power2 = (Math.sin(angle + (Math.PI / 4)) * speed);

        RF.setPower(power1 + turn);
        LF.setPower(power2 - turn);
        RB.setPower(power2 + turn);
        LB.setPower(power1 - turn);

        opMode.telemetry.addData("Angle", currentAngle);
        opMode.telemetry.addData("RF", RF.getPower());
        opMode.telemetry.addData("LF", LF.getPower());
        opMode.telemetry.addData("RB", RB.getPower());
        opMode.telemetry.addData("LB", LB.getPower());
        opMode.telemetry.addData("NorthMode", northMode);
    }

    public void setNorthMode(boolean newMode) {
        northMode = newMode;
        if(newMode){imu.resetYaw();opMode.telemetry.addLine("RESETING");}
    }

    public boolean getNorthMode() {return northMode;}


}
