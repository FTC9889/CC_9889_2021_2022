package com.team9889.ftc2021.auto.actions.lift;

import android.util.Log;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.subsystems.Lift;
import com.team9889.ftc2021.subsystems.Robot;

/**
 * Created by Eric on 11/20/2021.
 */
public class LiftGoToHeight extends Action {
    Lift.LiftState liftState;
    ElapsedTime timer = new ElapsedTime();

    public LiftGoToHeight (Lift.LiftState liftState) {
        this.liftState = liftState;
    }

    @Override
    public void start() {
        Robot.getInstance().getLift().wantedLiftState = liftState;
        Robot.getInstance().getLift().done = false;

        Log.i("Lift Start", "");
        timer.reset();
    }

    @Override
    public void update() {
    }

    @Override
    public boolean isFinished() {
        Log.i("Lift Done", "" + Robot.getInstance().getLift().done);

        return (Robot.getInstance().getLift().done && timer.milliseconds() > 400);
//         || timer.milliseconds() > 1000
    }

    @Override
    public void done() {

    }
}
