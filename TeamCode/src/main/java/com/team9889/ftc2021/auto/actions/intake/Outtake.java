package com.team9889.ftc2021.auto.actions.intake;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.subsystems.Intake;
import com.team9889.ftc2021.subsystems.Robot;

/**
 * Created by Eric on 2/6/2022.
 */
public class Outtake extends Action {
    ElapsedTime timer = new ElapsedTime();
    double timeOut, power = -0.5;

    public Outtake (double timeOut) {
        this.timeOut = timeOut;
    }

    public Outtake (double timeOut, double power) {
        this.timeOut = timeOut;
        this.power = power;
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
        return timer.milliseconds() > timeOut + 200;
    }

    @Override
    public void done() {
    }
}
