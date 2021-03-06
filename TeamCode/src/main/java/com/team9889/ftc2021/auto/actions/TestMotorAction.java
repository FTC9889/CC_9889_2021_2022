package com.team9889.ftc2021.auto.actions;

import com.team9889.ftc2021.subsystems.Robot;
import com.team9889.lib.CruiseLib;
import com.team9889.lib.control.controllers.cruiseController;

/**
 * Created by Eric on 11/7/2019.
 */
public class TestMotorAction extends Action{
    private cruiseController cc;

    @Override
    public void start() {
        cc = new cruiseController(.05, .1);

        Robot.getInstance().fLDrive.resetEncoder();
    }

    @Override
    public void update() {
        Robot.getInstance().update();

        double motorPower = cc.update(Robot.getInstance().fLDrive.getPosition(), 6000);

        Robot.getInstance().fLDrive.setPower(motorPower);
    }

    @Override
    public boolean isFinished() {
        return CruiseLib.isBetween(Robot.getInstance().fLDrive.getPosition(), 5996, 6004);
    }

    @Override
    public void done() {
        Robot.getInstance().fLDrive.setPower(0);
    }
}
