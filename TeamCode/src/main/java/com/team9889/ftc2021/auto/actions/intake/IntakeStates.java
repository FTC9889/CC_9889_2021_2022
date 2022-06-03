package com.team9889.ftc2021.auto.actions.intake;

import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.subsystems.Intake.IntakeState;
import com.team9889.ftc2021.subsystems.Robot;

/**
 * Created by Eric on 11/22/2019.
 */
public class IntakeStates extends Action {
    IntakeState intake;

    public IntakeStates(IntakeState intake) {
        this.intake = intake;
    }

    @Override
    public void start() {
        Robot.getInstance().getIntake().intakeState = intake;
    }

    @Override
    public void update() {

    }

    @Override
    public boolean isFinished() {
        return !Robot.getInstance().getIntake().IntakeGateOpen();
    }

    @Override
    public void done() {
        if (Robot.getInstance().rrCancelable) {
            Robot.getInstance().rr.breakFollowing();
        }
    }
}
