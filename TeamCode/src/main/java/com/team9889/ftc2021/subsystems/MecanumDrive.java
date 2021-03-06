package com.team9889.ftc2021.subsystems;

import android.util.Log;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2021.Constants;
import com.team9889.lib.CruiseLib;
import com.team9889.lib.control.controllers.PID;
import com.team9889.lib.control.math.cartesian.Rotation2d;
import com.team9889.lib.control.math.cartesian.Vector2d;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import static java.lang.Math.PI;

/**
 * Created by Eric on 9/7/2019.
 */

@Config
public class MecanumDrive extends Subsystem {
    public static double tolerance = 3;
    public static PID xPID = new PID(0, 0, 0), yPID = new PID(0, 0, 0),
            thetaPid = new PID(0, 0, 0);

    public double xSpeed, ySpeed, turnSpeed;

    public Rotation2d gyroAngle = new Rotation2d();
    public double angleFromAuton = 0;

    ElapsedTime timer = new ElapsedTime();

    public boolean resetRR = false, rrControl = false;

    private String filename = "gyro.txt";

    boolean auto;

    public enum Corner {
        TOP_RIGHT, BOTTOM_RIGHT, TOP_LEFT, BOTTOM_LEFT
    }
    public static Corner corner = Corner.TOP_RIGHT;
    public enum Sensor {
        FRONT, LEFT, BACK, RIGHT
    }
    public static Sensor xSensor = Sensor.FRONT, ySensor = Sensor.RIGHT;

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
        telemetry.addData("Robot Position", Robot.getInstance().rr.getPoseEstimate());
//
//        telemetry.addData("Left Front", Robot.getInstance().fLDrive.getPosition());
//        telemetry.addData("Right Front", Robot.getInstance().fRDrive.getPosition());
//        telemetry.addData("Left Back", Robot.getInstance().bLDrive.getPosition());
//        telemetry.addData("Right Back", Robot.getInstance().bRDrive.getPosition());

//        telemetry.addData("Gyro", gyroAngle.getTheda(AngleUnit.DEGREES) - angleFromAuton);

//        telemetry.addData("Sensor 0", (Robot.getInstance().bulkDataMaster.getAnalogInputValue(0) * 24 / 380) + 7);
//        telemetry.addData("Sensor 1", (Robot.getInstance().bulkDataMaster.getAnalogInputValue(1) * 24 / 380) + 7);
//        telemetry.addData("Sensor 3", (Robot.getInstance().bulkDataSlave.getAnalogInputValue(0) * 24 / 380) + 7);
//        telemetry.addData("Sensor 4", (Robot.getInstance().bulkDataSlave.getAnalogInputValue(1) * 24 / 380) + 7);
    }

    @Override
    public void update() {
        if (!auto && !rrControl) {
            setFieldCentricPower(xSpeed, ySpeed, turnSpeed, !Robot.getInstance().isRed);
            xSpeed = 0;
            ySpeed = 0;
            turnSpeed = 0;
        }

        Log.v("Loop Time M", "" + Robot.getInstance().loopTime.milliseconds());
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
//        Robot.getInstance().writer.write("Angle = " + gyroAngle.getTheda(AngleUnit.RADIANS));
        angleFromAuton = gyroAngle.getTheda(AngleUnit.RADIANS);
        Robot.getInstance().rr.setPoseEstimate(new Pose2d(Robot.getInstance().rr.getPoseEstimate().getX(),
                Robot.getInstance().rr.getPoseEstimate().getY(), 0));
    }

    public void setFieldCentricPower(double x, double y, double rotation, boolean blue) {
//        double angle = gyroAngle.getTheda(AngleUnit.RADIANS);
//
//        if (blue) {
//            x = -x;
//            y = -y;
//        }
//
//        double angleFromAuto = Robot.getInstance().getMecanumDrive().angleFromAuton;
//        double xMod = x * Math.cos(angle - angleFromAuto) - y * Math.sin(angle - angleFromAuto);
//        double yMod = x * Math.sin(angle - angleFromAuto) + y * Math.cos(angle - angleFromAuto);
//
//        Log.v("Drive", "X:" + xMod + "Y:" + yMod + "Rotation:" + rotation);
//        setPower(xMod, yMod, rotation);

        // Read pose
        Pose2d poseEstimate = Robot.getInstance().rr.getPoseEstimate();

        // Create a vector from the gamepad x/y inputs
        // Then, rotate that vector by the inverse of that heading
        com.acmerobotics.roadrunner.geometry.Vector2d input;
        if (Robot.getInstance().isRed) {
            input = new com.acmerobotics.roadrunner.geometry.Vector2d(y, -x).rotated(-poseEstimate.getHeading() - Math.toRadians(-90));
        } else {
            input = new com.acmerobotics.roadrunner.geometry.Vector2d(y, -x).rotated(-poseEstimate.getHeading() + Math.toRadians(-90));
        }

        // Pass in the rotated input + right stick value for rotation
        // Rotation is not part of the rotated input thus must be passed in separately
        Robot.getInstance().rr.setWeightedDrivePower(
                new Pose2d(
                        input.getX(),
                        input.getY(),
                        -rotation
                )
        );
    }

    public void setFieldCentricPowerAuto(double x, double y, double rotation) {
        // Read pose
        Pose2d poseEstimate = Robot.getInstance().rr.getPoseEstimate();

        // Create a vector from the gamepad x/y inputs
        // Then, rotate that vector by the inverse of that heading
        com.acmerobotics.roadrunner.geometry.Vector2d input;
        input = new com.acmerobotics.roadrunner.geometry.Vector2d(x, y).rotated(-poseEstimate.getHeading());

        // Pass in the rotated input + right stick value for rotation
        // Rotation is not part of the rotated input thus must be passed in separately
        Robot.getInstance().rr.setWeightedDrivePower(
                new Pose2d(
                        input.getX(),
                        input.getY(),
                        -rotation
                )
        );
    }

    public void setPower(double leftStickX, double leftStickY, double rightStickX){
        double r = Math.hypot(leftStickX, leftStickY);
        double robotAngle = Math.atan2(leftStickY, leftStickX) - PI / 4;
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

    public void driveToPose(Pose2d pose) {
        xSpeed = xPID.update(Robot.getInstance().rr.getPoseEstimate().getX(), pose.getX());
        ySpeed = yPID.update(Robot.getInstance().rr.getPoseEstimate().getY(), pose.getY());
        turnSpeed = thetaPid.update(Robot.getInstance().rr.getPoseEstimate().getHeading(), pose.getHeading());
    }

    public void alignToPoint(double angle) {
        double speed = thetaPid.update(getAngle().getTheda(AngleUnit.DEGREES), angle);

        if (Math.abs(getAngle().getTheda(AngleUnit.DEGREES) - angle) > tolerance) {
            turnSpeed += speed * CruiseLib.getSign(getAngle().getTheda(AngleUnit.DEGREES) - angle);
        }
    }


//  Constraints:
//      Robot must be parallel to the field walls
//      Assume Robot is always facing back wall

    //bulkDataMaster 0: Left (5.5 inches from robot center)
    //bulkDataMaster 1 Back (5.75 inches from robot center)
    //bulkDataSlave 0: Front (7 inches from robot center)
    //bulkDataSlave 1: Right (5.5 inches from robot center)
    public Vector2d getPosition (Sensor xSensor, Sensor ySensor, Corner corner) {
        double angle = Robot.getInstance().rr.getPoseEstimate().getHeading(), xDist = 0, yDist = 0;

        switch (xSensor) {
            case FRONT:
                xDist = (Robot.getInstance().bulkDataSlave.getAnalogInputValue(0) * 24.0 / 380.0) + 7;
                break;

            case LEFT:
                xDist = (Robot.getInstance().bulkDataMaster.getAnalogInputValue(0) * 24.0 / 380.0) + 5.5;
                break;

            case BACK:
                xDist = (Robot.getInstance().bulkDataMaster.getAnalogInputValue(1) * 24.0 / 380.0) + 5.75;
                break;

            case RIGHT:
                xDist = (Robot.getInstance().bulkDataSlave.getAnalogInputValue(1) * 24.0 / 380.0) + 5.5;
                break;
        }

        switch (ySensor) {
            case FRONT:
                yDist = (Robot.getInstance().bulkDataSlave.getAnalogInputValue(0) * 24.0 / 380.0) + 7;
                break;

            case LEFT:
                yDist = (Robot.getInstance().bulkDataMaster.getAnalogInputValue(0) * 24.0 / 380.0) + 5.5;
                break;

            case BACK:
                yDist = (Robot.getInstance().bulkDataMaster.getAnalogInputValue(1) * 24.0 / 380.0) + 5.75;
                break;

            case RIGHT:
                yDist = (Robot.getInstance().bulkDataSlave.getAnalogInputValue(1) * 24.0 / 380.0) + 5.5;
                break;
        }

        Vector2d robotPos = new Vector2d(), globalPos = new Vector2d();

        double adjustedAngle = angle;

        if (angle > PI / 4.0){
            adjustedAngle -= PI / 2.0;
        }else if (angle < -PI / 4.0){
            adjustedAngle += PI / 2.0;
        }

        if (angle > PI / 4.0){
            adjustedAngle -= PI / 2.0;
        }else if (angle < -PI / 4.0){
            adjustedAngle += PI / 2.0;
        }

        Log.v("Adjusted Angle", angle + ", " + adjustedAngle);

        robotPos.setX((xDist * Math.cos(0)) + (yDist * Math.sin(0)));
        robotPos.setY((yDist * Math.cos(0)) + (xDist * Math.sin(0)));

        switch (corner) {
            case TOP_RIGHT:
                globalPos = new Vector2d(72,-72);
                robotPos.setX(-robotPos.getX());
                break;

            case BOTTOM_RIGHT:
                globalPos = new Vector2d(-72,-72);
                break;

            case TOP_LEFT:
                globalPos = new Vector2d(72,72);
                robotPos.setX(-robotPos.getX());
                robotPos.setY(-robotPos.getY());
                break;

            case BOTTOM_LEFT:
                globalPos = new Vector2d(-72,72);
                robotPos.setY(-robotPos.getY());
                break;
        }

        globalPos = globalPos.add(robotPos);

        Robot.getInstance().rr.setPoseEstimate(new Pose2d(globalPos.getX(), globalPos.getY(), angle));

        Log.v("New Position", globalPos.getX() + ", " + globalPos.getY());

        return globalPos;
    }

    public Vector2d getPositionRed () {
        double dist0 = (double) Robot.getInstance().bulkDataSlave.getAnalogInputValue(0) * 24 / 380;
        double dist1 = (double) Robot.getInstance().bulkDataSlave.getAnalogInputValue(1) * 24 / 380;
        double angle = Robot.getInstance().rr.getPoseEstimate().getHeading();
        Vector2d frontDistToRobot = new Vector2d(5.75, 7), sideDistToRobot = new Vector2d(7, 0);
        Vector2d robotPos = new Vector2d(), globalPos = new Vector2d(72,-72);

//        angle = angle - Math.PI/2;

        double xDistance = dist0 + (frontDistToRobot.getX() * Math.sin(angle)) + frontDistToRobot.getY(),
            yDistance = dist1 + (sideDistToRobot.getY() * Math.sin(angle)) + sideDistToRobot.getX();

        robotPos.setX((xDistance * Math.cos(angle)) + (yDistance * Math.sin(angle)));
        robotPos.setY((yDistance * Math.cos(angle)) + (xDistance * Math.sin(angle)));

        globalPos = globalPos.add(robotPos);

        Robot.getInstance().rr.setPoseEstimate(new Pose2d(globalPos.getX(), globalPos.getY(), angle));

        return globalPos;
    }

    public Vector2d getPositionBlue () {
        double dist0 = (double) Robot.getInstance().bulkDataSlave.getAnalogInputValue(0) * 23 / 370;
        double dist1 = (double) Robot.getInstance().bulkDataMaster.getAnalogInputValue(0) * 16 / 250;
        double angle = getAngle().getTheda(AngleUnit.RADIANS);
        Vector2d frontDistToRobot = new Vector2d(-4, 7), sideDistToRobot = new Vector2d(-5, 8);
        Vector2d robotPos = new Vector2d(), globalPos = new Vector2d(72,72);

        angle = angle + PI/2;

        robotPos.setX((dist0 + (sideDistToRobot.getX() * Math.sin(angle)) + sideDistToRobot.getY())
                * Math.cos(angle));
        robotPos.setY((dist1 + (frontDistToRobot.getX() * Math.sin(angle)) + frontDistToRobot.getY())
                * Math.cos(angle));

        globalPos = globalPos.add(robotPos);

        Robot.getInstance().rr.setPoseEstimate(new Pose2d(globalPos.getX(), globalPos.getY(), getAngle().getTheda(AngleUnit.RADIANS)));

        return globalPos;
    }
}