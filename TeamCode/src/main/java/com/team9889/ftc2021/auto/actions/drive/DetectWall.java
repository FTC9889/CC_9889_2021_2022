package com.team9889.ftc2021.auto.actions.drive;

import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.subsystems.Robot;

/**
 * Created by Eric on 3/2/2022.
 */
public class DetectWall extends Action {
    @Override
    public void start() {

    }

    @Override
    public void update() {

    }

    @Override
    public boolean isFinished() {
        if (Robot.getInstance().isRed) {
            return Robot.getInstance().bulkDataSlave.getAnalogInputValue(1) * 27.5 / 432 < 1.8;
        } else {
            return true;
        }
    }

    @Override
    public void done() {
        if (Robot.getInstance().rrCancelable) {
            Robot.getInstance().rr.breakFollowing();
        }
    }
}
