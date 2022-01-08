package com.team9889.ftc2021.auto.actions.intake;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.subsystems.Intake.IntakeState;
import com.team9889.ftc2021.subsystems.Robot;

/**
 * Created by Eric on 11/22/2019.
 */
public class Intake extends Action {
    private ElapsedTime timer = new ElapsedTime();

    IntakeState intake;

    public Intake(IntakeState intake) {
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
        return true;
    }

    @Override
    public void done() {}
}
