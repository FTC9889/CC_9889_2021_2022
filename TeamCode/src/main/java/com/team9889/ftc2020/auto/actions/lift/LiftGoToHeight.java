package com.team9889.ftc2020.auto.actions.lift;

import com.team9889.ftc2020.auto.actions.Action;
import com.team9889.ftc2020.subsystems.Lift;
import com.team9889.ftc2020.subsystems.Robot;

/**
 * Created by Eric on 11/20/2021.
 */
public class LiftGoToHeight extends Action {
    Lift.LiftState liftState;
    double height = 1000;

    public LiftGoToHeight (Lift.LiftState liftState) {
        this.liftState = liftState;
    }

    public LiftGoToHeight (double height) {
        this.height = height;
    }

    @Override
    public void start() {
        if (height == 1000) {
            Robot.getInstance().getLift().wantedLiftState = liftState;
        } else {

        }
    }

    @Override
    public void update() {

    }

    @Override
    public boolean isFinished() {
        return Robot.getInstance().getLift().currentLiftState ==
                Robot.getInstance().getLift().wantedLiftState;
    }

    @Override
    public void done() {

    }
}
