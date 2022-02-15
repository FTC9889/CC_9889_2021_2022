package com.team9889.ftc2021.auto.actions.intake;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.subsystems.Robot;

/**
 * Created by Eric on 2/6/2022.
 */
public class Outtake extends Action {
    ElapsedTime timer = new ElapsedTime(), finishedTimer = new ElapsedTime();
    double timeOut;

    public Outtake (double timeOut) {
        this.timeOut = timeOut;
    }

    @Override
    public void start() {
        timer.reset();
        finishedTimer.reset();
    }

    @Override
    public void update() {
//        if (timer.milliseconds() < 100) {
//            Robot.getInstance().getIntake().SetPassThrough(-1);
//            Robot.getInstance().getIntake().SetIntake(-1);
//        } else if (timer.milliseconds() > 100 && timer.milliseconds() < 300) {
//            Robot.getInstance().getIntake().SetPassThrough(0);
//            Robot.getInstance().getIntake().SetIntake(0);
//        } else {
//            timer.reset();
//        }

        Robot.getInstance().getIntake().SetPassThrough(-1);
        Robot.getInstance().getIntake().SetIntake(-1);
    }

    @Override
    public boolean isFinished() {
        return finishedTimer.milliseconds() > timeOut;
    }

    @Override
    public void done() {
        Robot.getInstance().getIntake().SetPassThrough(0);
        Robot.getInstance().getIntake().SetIntake(0);
    }
}
