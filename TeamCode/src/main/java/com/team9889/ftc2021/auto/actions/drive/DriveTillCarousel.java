package com.team9889.ftc2021.auto.actions.drive;

import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.subsystems.Robot;

/**
 * Created by Eric on 5/27/2022.
 */
public class DriveTillCarousel extends Action {
    @Override
    public void start() {

    }

    @Override
    public void update() {
        Robot.getInstance().getMecanumDrive().setPower(0, -0.3, 0);
    }

    @Override
    public boolean isFinished() {
        if (Robot.getInstance().isRed) {
            return !Robot.getInstance().redLimit.isPressed();
        } else {
            return !Robot.getInstance().blueLimit.isPressed();
        }
    }

    @Override
    public void done() {
        Robot.getInstance().getMecanumDrive().setPower(0, 0, 0);
    }
}
