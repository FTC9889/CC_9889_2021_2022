package com.team9889.ftc2021.auto.actions.drive.duck;

import com.acmerobotics.dashboard.config.Config;
import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.auto.actions.ActionVariables;
import com.team9889.ftc2021.subsystems.Robot;
import com.team9889.lib.CruiseLib;

/**
 * Created by Eric on 5/27/2022.
 */

public class TurnUntilDuck extends Action {
    @Override
    public void start() {
        Robot.getInstance().getMecanumDrive().setPower(0, 0, 0.1 * (Robot.getInstance().isRed ? 1 : -1));
    }

    @Override
    public void update() {
        if (Robot.getInstance().getCamera().scanForDuck.getPoint().x != 160) {
            Robot.getInstance().getMecanumDrive().setPower(0, 0,
                    CruiseLib.limitValue(0.003 * (Robot.getInstance().getCamera().scanForDuck.getPoint().x - 160), -0.07, -0.07, 0.07, 0.07));
        } else {
            Robot.getInstance().getMecanumDrive().setPower(0, 0, 0.1 * (Robot.getInstance().isRed ? 1 : -1));
        }
    }

    @Override
    public boolean isFinished() {
        boolean turnToFar;
        if (Robot.getInstance().isRed)
            turnToFar = CruiseLib.angleWrap(Math.toDegrees(Robot.getInstance().rr.getPoseEstimate().getHeading())) < -120;
        else
            turnToFar = Math.toDegrees(Robot.getInstance().rr.getPoseEstimate().getHeading()) > 120;

        return (Robot.getInstance().getCamera().scanForDuck.getPoint().x != 160 &&
                Math.abs(Robot.getInstance().getCamera().scanForDuck.getPoint().x - 160) < 4) ||
                turnToFar;
    }

    @Override
    public void done() {
        Robot.getInstance().getMecanumDrive().setPower(0, 0, 0);

        if (Robot.getInstance().getCamera().scanForDuck.getPoint().x != 160 &&
                Math.abs(Robot.getInstance().getCamera().scanForDuck.getPoint().x - 160) < 8) {
            ActionVariables.grabbedDuck = true;
        } else {
            ActionVariables.grabbedDuck = false;
        }
    }
}
