package com.team9889.ftc2020.subsystems;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Eric on 8/19/2019.
 */

@Config
public class Intake extends Subsystem {
    public static double power = 1;

    double heightTimerOffset = 0, timerOffset = 0;

    public enum IntakeHeightState {
        UP, DOWN, NULL
    }
    public IntakeHeightState wantedIntakeHeightState = IntakeHeightState.NULL,
            currentIntakeHeightState = IntakeHeightState.NULL;
    IntakeHeightState lastIntakeHeightState = IntakeHeightState.NULL;

    public enum IntakeState {
        ON, OFF, OUT
    }
    public IntakeState intakeState = IntakeState.OFF;

    @Override
    public void init(boolean auto) {
        if (auto) {
            intakeState = IntakeState.OFF;
            wantedIntakeHeightState = IntakeHeightState.UP;
        }
    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) { }

    @Override
    public void update() {
        if (intakeState != IntakeState.OFF) {
            timerOffset = Robot.getInstance().robotTimer.milliseconds();
        }

        switch (intakeState) {
            case ON:
                if (wantedIntakeHeightState != IntakeHeightState.DOWN) {
                    currentIntakeHeightState = wantedIntakeHeightState;
                }

                if (currentIntakeHeightState != IntakeHeightState.DOWN) {
                    wantedIntakeHeightState = IntakeHeightState.DOWN;
                    Robot.getInstance().driverStation.intakeDown = true;
                } else {
                    SetIntake(power);
                }
                break;

            case OFF:
                if (Robot.getInstance().robotTimer.milliseconds() - timerOffset < 300) {
                    SetIntake(0);
                } else {
                    wantedIntakeHeightState = IntakeHeightState.UP;
                    Robot.getInstance().driverStation.intakeDown = false;
                }
                break;

            case OUT:
                if (wantedIntakeHeightState != IntakeHeightState.DOWN) {
                    currentIntakeHeightState = wantedIntakeHeightState;
                }

                if (currentIntakeHeightState != IntakeHeightState.DOWN) {
                    wantedIntakeHeightState = IntakeHeightState.DOWN;
                    Robot.getInstance().driverStation.intakeDown = true;
                } else {
                    SetIntake(-1);
                }
                break;
        }

        if (wantedIntakeHeightState != lastIntakeHeightState) {
            heightTimerOffset = Robot.getInstance().robotTimer.milliseconds();
        }

        switch (wantedIntakeHeightState) {
            case UP:
                IntakeUp();
                break;

            case DOWN:
                IntakeDown();
                break;
        }

        if (Robot.getInstance().robotTimer.milliseconds() - heightTimerOffset > 250) {
            currentIntakeHeightState = wantedIntakeHeightState;
        }
        lastIntakeHeightState = wantedIntakeHeightState;
    }

    @Override
    public void stop() {
        intakeState = IntakeState.OFF;
    }

    public void SetIntake(double power){
        Robot.getInstance().intake.setPower(power);
    }

    public void StartIntake() {
        intakeState = IntakeState.ON;
    }

    public void StartOuttake() {
        intakeState = IntakeState.OUT;
    }

    public void StopIntake() {
        intakeState = IntakeState.OFF;
    }

    public void IntakeUp() {
        Robot.getInstance().intakeLift.setPosition(0.1);
    }

    public void IntakeDown() {
        Robot.getInstance().intakeLift.setPosition(0.5);
    }
}