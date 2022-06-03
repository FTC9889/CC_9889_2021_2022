package com.team9889.ftc2021.auto.actions.lift;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.auto.actions.ActionVariables;
import com.team9889.ftc2021.subsystems.Dumper;
import com.team9889.ftc2021.subsystems.Lift;
import com.team9889.ftc2021.subsystems.Robot;

/**
 * Created by Eric on 2/8/2022.
 */
public class Score extends Action {
    Lift.LiftState wantedState;
    int step = 0;
    ElapsedTime timer = new ElapsedTime();
    Boolean delay = false;

    public Score(Lift.LiftState state) {
        this.wantedState = state;
    }

    public Score(Lift.LiftState state, Boolean delay) {
        this.wantedState = state;
        this.delay = delay;
    }

    @Override
    public void start() {
        timer.reset();
        Robot.getInstance().getLift().done = false;
        ActionVariables.readyToScore = true;
    }

    @Override
    public void update() {
        switch (step) {
            case 0:
                Robot.getInstance().getLift().wantedLiftState = wantedState;

                if ((Robot.getInstance().getLift().done && timer.milliseconds() > 100) || timer.milliseconds() > 500) {
                    step = 1;
                    timer.reset();
                }
                break;

            case 1:
                if (timer.milliseconds() > 250 || !delay) {
                    step = 2;
                    timer.reset();
                }
                break;

            case 2:
                Robot.getInstance().getDumper().gateState = Dumper.GateState.OPEN;

                if (timer.milliseconds() > 450) {
                    step = 3;
                    timer.reset();
                    Robot.getInstance().getLift().done = false;

                    Robot.getInstance().getLift().wantedLiftState = Lift.LiftState.DOWN;
                    Robot.getInstance().getDumper().gateState = Dumper.GateState.CLOSED;
                }
                break;
        }
    }

    @Override
    public boolean isFinished() {
        return step == 3;
    }

    @Override
    public void done() {
    }
}