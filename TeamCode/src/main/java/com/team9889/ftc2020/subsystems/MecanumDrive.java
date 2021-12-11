package com.team9889.ftc2020.subsystems;

import android.util.Log;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2020.Constants;
import com.team9889.lib.control.math.cartesian.Rotation2d;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Created by Eric on 9/7/2019.
 */

@Config
public class MecanumDrive extends Subsystem {
    public double x, y, xSpeed, ySpeed, turnSpeed;

    public Rotation2d gyroAngle = new Rotation2d();
    public double angleFromAuton = 0;

    ElapsedTime timer = new ElapsedTime();

    private String filename = "gyro.txt";

    boolean auto;

    @Override
    public void init(boolean auto) {
        if(auto) {

        } else if (Constants.pose != null) {
            Robot.getInstance().rr.setPoseEstimate(Constants.pose);
            Robot.getInstance().rr.update();
            angleFromAuton = Constants.pose.getHeading();
        }

        this.auto = auto;

        timer.reset();
    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) {
        telemetry.addData("Left Front", Robot.getInstance().fLDrive.getPosition());
        telemetry.addData("Right Front", Robot.getInstance().fRDrive.getPosition());
        telemetry.addData("Left Back", Robot.getInstance().bLDrive.getPosition());
        telemetry.addData("Right Back", Robot.getInstance().bRDrive.getPosition());

        telemetry.addData("Gyro", gyroAngle.getTheda(AngleUnit.DEGREES) - angleFromAuton);
    }

    @Override
    public void update() {
        getAngle();

        if (!auto) {
            setFieldCentricPower(xSpeed, ySpeed, turnSpeed, Robot.getInstance().blue);
            xSpeed = 0;
            ySpeed = 0;
            turnSpeed = 0;
        }
    }

    @Override
    public void stop() {
        Robot.getInstance().fLDrive.setPower(0);
        Robot.getInstance().fRDrive.setPower(0);
        Robot.getInstance().bLDrive.setPower(0);
        Robot.getInstance().bRDrive.setPower(0);
    }

    public Rotation2d getAngle(){
        try {
            gyroAngle.setTheda(-Robot.getInstance().imu.getNormalHeading(), AngleUnit.DEGREES);
            return gyroAngle;
        } catch (Exception e){
            return new Rotation2d(0, AngleUnit.DEGREES);
        }
    }

    public void writeAngleToFile() {
        angleFromAuton = gyroAngle.getTheda(AngleUnit.RADIANS);
    }

    public void setFieldCentricPower(double x, double y, double rotation, boolean blue){
        double angle = gyroAngle.getTheda(AngleUnit.RADIANS);

        if (blue) {
            x = -x;
            y = -y;
        }

        double angleFromAuto = Robot.getInstance().getMecanumDrive().angleFromAuton;
        double xMod = x * Math.cos(angle - angleFromAuto) - y * Math.sin(angle - angleFromAuto);
        double yMod = x * Math.sin(angle - angleFromAuto) + y * Math.cos(angle - angleFromAuto);

        Log.v("Drive", "X:" + xMod + "Y:" + yMod + "Rotation:" + rotation);
        setPower(xMod, yMod, rotation);
    }

    public void setPower(double leftStickX, double leftStickY, double rightStickX){
        double r = Math.hypot(leftStickX, leftStickY);
        double robotAngle = Math.atan2(leftStickY, leftStickX) - Math.PI / 4;
        double rightX = rightStickX;
        final double v1 = r * Math.cos(robotAngle) + rightX;
        final double v2 = r * Math.sin(robotAngle) - rightX;
        final double v3 = r * Math.sin(robotAngle) + rightX;
        final double v4 = r * Math.cos(robotAngle) - rightX;

        Robot.getInstance().fLDrive.setPower(v1);
        Robot.getInstance().fRDrive.setPower(v2);
        Robot.getInstance().bLDrive.setPower(v3);
        Robot.getInstance().bRDrive.setPower(v4);
    }
}