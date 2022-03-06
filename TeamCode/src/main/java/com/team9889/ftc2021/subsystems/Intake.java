package com.team9889.ftc2021.subsystems;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Eric on 8/19/2019.
 */

@Config
public class Intake extends Subsystem {
    public static double power = 1;

    int gateCounter = 0;

    double heightTimerOffset = 0, timerOffset = 0;

    public enum IntakeHeightState {
        UP, DOWN, NULL
    }
    public IntakeHeightState wantedIntakeHeightState = IntakeHeightState.NULL,
            currentIntakeHeightState = IntakeHeightState.NULL;
    IntakeHeightState lastIntakeHeightState = IntakeHeightState.NULL;

    public enum IntakeState {
        ON, OFF, OUT, FRONT_OUT
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
                SetIntake(power);
                SetPassThrough(1);

                if (!IntakeGateOpen()) {
                    intakeState = IntakeState.FRONT_OUT;
                }
                break;

            case OFF:
                SetIntake(0);
                SetPassThrough(0);
                break;

            case OUT:
                SetIntake(-0.5);
                SetPassThrough(-0.5);
                break;

            case FRONT_OUT:
                SetIntake(-0.7);
                break;
        }

        if (intakeState == IntakeState.FRONT_OUT) {
            Robot.getInstance().flag.setPosition(0.5);
        } else {
            Robot.getInstance().flag.setPosition(0);
        }
    }

    @Override
    public void stop() {
        intakeState = IntakeState.OFF;
    }

    public void SetIntake(double power){
        Robot.getInstance().intake.setPower(power);
    }

    public void SetPassThrough(double power){
        Robot.getInstance().passThrough.setPower(power);
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

    public boolean IntakeGateOpen() {
        return Robot.getInstance().intakeGate.isPressed();
    }
}