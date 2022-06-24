package com.team9889.ftc2021.auto.actions.intake;

import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.auto.actions.ActionVariables;
import com.team9889.ftc2021.subsystems.Intake;
import com.team9889.ftc2021.subsystems.Robot;

/**
 * Created by Eric on 6/16/2022.
 */
public class StopDriveAfterIntake extends Action {

    @Override
    public void start() {

    }

    @Override
    public void update() {
    }

    @Override
    public boolean isFinished() {
        return Robot.getInstance().getIntake().loadState != Intake.LoadState.INTAKE;
    }

    @Override
    public void done() {
        ActionVariables.stopDriving = true;
    }
}
