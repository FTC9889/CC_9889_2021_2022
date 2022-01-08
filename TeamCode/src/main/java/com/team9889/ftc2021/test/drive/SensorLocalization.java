package com.team9889.ftc2021.test.drive;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2021.Team9889Linear;
import com.team9889.lib.control.math.cartesian.Rotation2d;

/**
 * Created by Eric on 12/27/2021.
 */

@TeleOp
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

            telemetry.addData("Position", Robot.getMecanumDrive().getPositionRed());
            telemetry.update();
        }
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
