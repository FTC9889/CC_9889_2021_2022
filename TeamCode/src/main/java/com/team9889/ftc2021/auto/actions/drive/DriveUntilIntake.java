package com.team9889.ftc2021.auto.actions.drive;

import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.subsystems.Intake;
import com.team9889.ftc2021.subsystems.Robot;
import com.team9889.lib.CruiseLib;

import static java.lang.Math.toDegrees;

/**
 * Created by Eric on 6/17/2022.
 */
public class DriveUntilIntake extends Action {
    int counter = 0, speed = 0;

    @Override
    public void start() {

    }

    @Override
    public void update() {
        double turnSpeed = CruiseLib.limitValue(-CruiseLib.angleWrap(
                0 - toDegrees(Robot.getInstance().rr.getPoseEstimate().getHeading())) / 90.0,
                0, -0.7, 0, 0.7);
        double xSpeed = (-0.0133 * Robot.getInstance().rr.getPoseEstimate().getX()) + 0.75;
        xSpeed = CruiseLib.limitValue(xSpeed, speed, 0.25);
        Robot.getInstance().getMecanumDrive().setPower(0, xSpeed,turnSpeed);

        speed += 0.02;

        if (Robot.getInstance().getIntake().loadState != Intake.LoadState.INTAKE) {
            counter++;
            Robot.getInstance().getIntake().loadState = Intake.LoadState.INTAKE;
        } else {
            counter = 0;
        }
    }

    @Override
    public boolean isFinished() {
        return counter > 1;
    }

    @Override
    public void done() {
        Robot.getInstance().getMecanumDrive().setFieldCentricPowerAuto(0, 0, 0);
    }
}
