package com.team9889.ftc2021.auto.actions.lift;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.subsystems.Dumper;
import com.team9889.ftc2021.subsystems.Lift;
import com.team9889.ftc2021.subsystems.Robot;
import com.team9889.lib.Pose;

import static java.lang.Math.abs;

/**
 * Created by Eric on 2/8/2022.
 */
public class ScoreInPosition extends Action {
    Lift.LiftState wantedState;
    Pose pose;
    int step = 0;
    ElapsedTime timer = new ElapsedTime();

    public ScoreInPosition(Lift.LiftState state, Pose pose) {
        this.wantedState = state;
        this.pose = pose;
    }

    @Override
    public void start() {
        timer.reset();
        Robot.getInstance().getLift().done = false;
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
                Pose error = Pose.getError(Pose.Pose2dToPose(Robot.getInstance().rr.getLocalizer().getPoseEstimate()), pose);
                if (abs(error.x) < 2 && abs(error.y) < 2 && abs(error.theta) < 3) {
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
