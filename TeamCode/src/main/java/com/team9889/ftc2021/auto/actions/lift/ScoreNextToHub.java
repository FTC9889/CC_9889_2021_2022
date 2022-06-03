package com.team9889.ftc2021.auto.actions.lift;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.auto.actions.ActionVariables;
import com.team9889.ftc2021.subsystems.Intake;
import com.team9889.ftc2021.subsystems.Lift;
import com.team9889.ftc2021.subsystems.Robot;

/**
 * Created by Eric on 6/2/2022.
 */
public class ScoreNextToHub extends Action {
    ElapsedTime timer = new ElapsedTime();

    @Override
    public void start() {
        ActionVariables.readyToScore = false;

        Robot.getInstance().getIntake().loadState = Intake.LoadState.OFF;

        Robot.getInstance().getLift().wantedLiftState = Lift.LiftState.NULL;
        Robot.getInstance().getLift().scoreState = Lift.ScoreState.NULL;
    }

    @Override
    public void update() {
//        if (timer.milliseconds() < 500) {
        Robot.getInstance().getLift().SetLiftLength(4);
        Lift.angle = 1;
        Robot.getInstance().getLift().wantedLiftState = Lift.LiftState.NULL;
        Robot.getInstance().getLift().scoreState = Lift.ScoreState.NULL;
//        }
    }

    @Override
    public boolean isFinished() {
        return ActionVariables.readyToScore;
    }

    @Override
    public void done() {

    }
}
