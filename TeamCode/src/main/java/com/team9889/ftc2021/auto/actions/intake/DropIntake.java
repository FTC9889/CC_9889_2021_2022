package com.team9889.ftc2021.auto.actions.intake;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.subsystems.Robot;

/**
 * Created by edm11 on 6/24/2022.
 */
public class DropIntake extends Action {
    ElapsedTime timer = new ElapsedTime();
    double timeOut, power = -0.5, lastTime = 0;

    public DropIntake (double timeOut) {
        this.timeOut = timeOut;
    }

    @Override
    public void start() {
        timer.reset();
    }

    @Override
    public void update() {
        if (timer.milliseconds() > timeOut) {
            Robot.getInstance().getIntake().SetIntake(power);
        }
    }

    @Override
    public boolean isFinished() {
        return timer.milliseconds() > timeOut + 100;
    }

    @Override
    public void done() {
    }
}
