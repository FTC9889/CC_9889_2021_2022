package com.team9889.ftc2021.test.drive;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2021.Team9889Linear;
import com.team9889.lib.control.math.cartesian.Rotation2d;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by Eric on 12/27/2021.
 */

@TeleOp(group = "Test")
public class SensorLocalization extends Team9889Linear {
    public double sameWallTolerance = 20; // Inch

    Rotation2d angle = new Rotation2d();
    double D1 = 0, D2 = 0, D3 = 0, D4 = 0;
    double F1 = 0, F2 = 0, F3 = 0, F4 = 0;


    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart(false);

        while (opModeIsActive()) {
            Robot.update();

//            telemetry.addData("raw ultrasonic", Robot.rangeSensor.rawUltrasonic());
//            telemetry.addData("raw optical", Robot.rangeSensor.rawOptical());
//            telemetry.addData("cm optical", "%.2f cm", Robot.rangeSensor.cmOptical());
            telemetry.addData("cm", "%.2f cm", Robot.rangeSensor.getDistance(DistanceUnit.CM));
            telemetry.addData("in", "%.2f in", Robot.rangeSensor.getDistance(DistanceUnit.INCH));

            telemetry.addData("Red Side Ultrasonic Sensor", (double) Robot.bulkDataSlave.getAnalogInputValue(1) * 27.5 / 432);
            telemetry.addData("Red Front Ultrasonic Sensor", (double) Robot.bulkDataMaster.getAnalogInputValue(1) * 29.75 / 459);
            telemetry.addData("Blue Side Ultrasonic Sensor", (double) Robot.bulkDataMaster.getAnalogInputValue(0) * 29.25 / 112);
            telemetry.addData("Blue Front Ultrasonic Sensor", (double) Robot.bulkDataSlave.getAnalogInputValue(0) * 29.75 / 257);
            telemetry.addData("Red Position", Robot.getMecanumDrive().getPositionRed());
            telemetry.addData("Blue Position", Robot.getMecanumDrive().getPositionBlue());

            Robot.outputToTelemetry(telemetry);
            telemetry.update();
        }
    }

    @Override
    public void initialize() {

    }
}

//  --- = 1 foot
//  ------------------------------------
//  |               F1                 |
//  |                                  |
//  |                                  |
//  |                                  |
//  |                                  |
//  |F2                              F4|
//  |                                  |
//  |                                  |
//  |                                  |
//  |                                  |
//  |               F3                 |
//  ------------------------------------
