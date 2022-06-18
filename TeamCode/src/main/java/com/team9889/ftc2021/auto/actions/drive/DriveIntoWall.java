package com.team9889.ftc2021.auto.actions.drive;

import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.subsystems.Robot;
import com.team9889.lib.CruiseLib;

import static java.lang.Math.toDegrees;

/**
 * Created by Eric on 6/17/2022.
 */
public class DriveIntoWall extends Action {
    @Override
    public void start() {

    }

    @Override
    public void update() {
        double turnSpeed = CruiseLib.limitValue(-CruiseLib.angleWrap(
                0 - toDegrees(Robot.getInstance().rr.getPoseEstimate().getHeading())) / 90.0,
                0, -0.7, 0, 0.7);
        double ySpeed = Robot.getInstance().isRed ? -1 : 1;

        Robot.getInstance().getMecanumDrive().setFieldCentricPowerAuto(0, ySpeed, turnSpeed);
    }

    @Override
    public boolean isFinished() {
        if (Robot.getInstance().isRed) {
            return Robot.getInstance().bulkDataSlave.getAnalogInputValue(1) * 24.0 / 380.0 < 5 && Math.abs(Robot.getInstance().rr.getPoseEstimate().getHeading()) < 4;
        } else {
            return Robot.getInstance().bulkDataMaster.getAnalogInputValue(0) * 24.0 / 380.0 < 5 && Math.abs(Robot.getInstance().rr.getPoseEstimate().getHeading()) < 4;
        }
    }

    @Override
    public void done() {
        Robot.getInstance().getMecanumDrive().setFieldCentricPowerAuto(0, 0, 0);
    }
}
