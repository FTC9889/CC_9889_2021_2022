package com.team9889.ftc2021.auto.actions.drive;

import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.subsystems.Robot;
import com.team9889.lib.CruiseLib;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import static java.lang.Math.abs;
import static java.lang.Math.toDegrees;

/**
 * Created by edm11 on 6/22/2022.
 */
public class Turn extends Action {
    double theta = 0, turnMultiplier = 0;

    public Turn (double theta, double turnSpeed) {
        this.theta = theta;
        this.turnMultiplier = turnSpeed;
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        double turnSpeed = CruiseLib.limitValue(-CruiseLib.angleWrap(
                theta - Robot.getInstance().getMecanumDrive().getAngle().getTheda(AngleUnit.DEGREES)) / 90.0,
                -0.1, -1, 0.1, 1) * turnMultiplier;

        Robot.getInstance().getMecanumDrive().setPower(0, 0, turnSpeed);
    }

    @Override
    public boolean isFinished() {
        return abs(CruiseLib.angleWrap(theta - Math.toDegrees(Robot.getInstance().rr.getPoseEstimate().getHeading()))) < 1;
    }

    @Override
    public void done() {

    }
}
