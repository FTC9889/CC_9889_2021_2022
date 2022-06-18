package com.team9889.ftc2021.auto.actions.intake;

import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.auto.actions.ActionVariables;
import com.team9889.ftc2021.subsystems.Intake;
import com.team9889.ftc2021.subsystems.Robot;

/**
 * Created by Eric on 6/16/2022.
 */
public class StopDriveAfterIntake extends Action {
    int counter = 0;

    @Override
    public void start() {

    }

    @Override
    public void update() {
        if (Robot.getInstance().getIntake().loadState != Intake.LoadState.INTAKE) {
            counter++;
            Robot.getInstance().getIntake().loadState = Intake.LoadState.INTAKE;
        } else {
            counter = 0;
        }
    }

    @Override
    public boolean isFinished() {
        return counter > 3;
    }

    @Override
    public void done() {
        ActionVariables.stopDriving = true;
    }
}
