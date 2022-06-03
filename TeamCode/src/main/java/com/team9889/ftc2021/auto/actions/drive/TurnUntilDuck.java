package com.team9889.ftc2021.auto.actions.drive;

import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.auto.actions.ActionVariables;
import com.team9889.ftc2021.subsystems.Robot;

/**
 * Created by Eric on 5/27/2022.
 */
public class TurnUntilDuck extends Action {
    @Override
    public void start() {
        Robot.getInstance().getMecanumDrive().setPower(0, 0, 0.1);
    }

    @Override
    public void update() {
        if (Robot.getInstance().getCamera().scanForDuck.getPoint().x != 160) {
            Robot.getInstance().getMecanumDrive().setPower(0, 0,
                    (0.0018 * (Robot.getInstance().getCamera().scanForDuck.getPoint().x - 160)) + 0.0221);
        }
    }

    @Override
    public boolean isFinished() {
        return (Robot.getInstance().getCamera().scanForDuck.getPoint().x != 160 &&
                Robot.getInstance().getCamera().scanForDuck.getPoint().x - 160 < 2.5) ||
                Math.toDegrees(Robot.getInstance().rr.getPoseEstimate().getHeading()) < -90;
    }

    @Override
    public void done() {
        Robot.getInstance().getMecanumDrive().setPower(0, 0, 0);

        if (Robot.getInstance().getCamera().scanForDuck.getPoint().x != 160 &&
                Robot.getInstance().getCamera().scanForDuck.getPoint().x - 160 < 2.5) {
            ActionVariables.grabbedDuck = true;
        } else {
            ActionVariables.grabbedDuck = false;
        }
    }
}
