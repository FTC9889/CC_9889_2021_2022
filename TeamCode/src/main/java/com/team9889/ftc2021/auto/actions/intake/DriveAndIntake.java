package com.team9889.ftc2021.auto.actions.intake;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.subsystems.Intake;
import com.team9889.ftc2021.subsystems.Robot;

/**
 * Created by Eric on 5/31/2022.
 */
public class DriveAndIntake extends Action {
    ElapsedTime timer = new ElapsedTime();

    @Override
    public void start() {
        Robot.getInstance().getIntake().loadState = Intake.LoadState.INTAKE;
    }

    @Override
    public void update() {
        if (timer.milliseconds() >= 250) {
            Robot.getInstance().getMecanumDrive().setPower(0, 0.15, 0);
        }
    }

    @Override
    public boolean isFinished() {
        return Robot.getInstance().getIntake().loadState != Intake.LoadState.INTAKE || timer.milliseconds() > 2000;
    }

    @Override
    public void done() {
        Robot.getInstance().getIntake().loadState = Intake.LoadState.TRANSFER;
        Robot.getInstance().getMecanumDrive().setPower(0, 0, 0);
    }
}
