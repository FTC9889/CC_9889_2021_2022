package com.team9889.ftc2021.auto.actions.lift;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.subsystems.Robot;
import com.team9889.lib.control.math.cartesian.Vector2d;

/**
 * Created by Eric on 1/19/2022.
 */
public class LiftGoToPos extends Action {
    Vector2d pos = new Vector2d();
    Boolean done = false;
    double timeout = 10000;
    ElapsedTime timer = new ElapsedTime();

    public LiftGoToPos(double x, double y) {
        pos.setX(x);
        pos.setY(y);
    }

    public LiftGoToPos(double x, double y, double timeout) {
        pos.setX(x);
        pos.setY(y);
        this.timeout = timeout;
    }

    @Override
    public void start() {
        timer.reset();
    }

    @Override
    public void update() {
        done = Robot.getInstance().getLift().SetLiftPos(pos.getX(), pos.getY());
    }

    @Override
    public boolean isFinished() {
        return done || timeout < timer.milliseconds();
    }

    @Override
    public void done() {
        Robot.getInstance().getLift().SetLiftPower(0);
    }
}
