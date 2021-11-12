package com.team9889.ftc2020.subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Eric on 8/19/2019.
 */

public class Intake extends Subsystem {
    public enum IntakeHeightState {
        UP, DOWN, NULL
    }
    public IntakeHeightState intakeHeightState = IntakeHeightState.NULL;

    public boolean intake = false;
    public boolean outtake = false;

    @Override
    public void init(boolean auto) {
        if (auto) {

        }
    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) { }

    @Override
    public void update() {
        if (intake) {
            if (outtake)
                SetIntake(-1);
            else
                SetIntake(1);
        } else {
            SetIntake(0);
        }


        switch (intakeHeightState) {
            case UP:
                IntakeUp();
                break;

            case DOWN:
                IntakeDown();
                break;
        }
    }

    @Override
    public void stop() {
        SetIntake(0);
    }

    public void SetIntake(double power){
        Robot.getInstance().intake.setPower(power);
    }

    public void StartIntake() {
        intake = true;
        outtake = false;
    }

    public void StartOuttake() {
        intake = true;
        outtake = false;
    }

    public void StopIntake() {
        intake = false;
        outtake = false;
    }

    public void IntakeUp() {
        Robot.getInstance().intakeLift.setPosition(1);
    }

    public void IntakeDown() {
        Robot.getInstance().intakeLift.setPosition(0);
    }
}