package com.team9889.ftc2020.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.team9889.lib.control.controllers.PID;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Eric on 8/19/2019.
 */

@Config
public class Lift extends Subsystem {
    public static double liftTolerance = 1;

    public enum LiftState {
        DOWN, LAYER1, LAYER2, LAYER3, SHARED, NULL
    }
    public LiftState wantedLiftState = LiftState.NULL, currentLiftState = LiftState.NULL;

    public static double layer1 = 5, layer2 = 10, layer3 = 15, shared = 5;

    public static PID liftPID = new PID(0, 0, 0);

    @Override
    public void init(boolean auto) {
        if (auto) {

        }
    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) {
        telemetry.addData("Is Lift Down", IsDown());
        telemetry.addData("Lift Height", GetLiftHeight());
    }

    @Override
    public void update() {
        switch (wantedLiftState) {
            case DOWN:
                if (!IsDown()) {
                    Robot.getInstance().lift.setPower(-0.5);
                } else {
                    Robot.getInstance().lift.setPower(0);
                }
                break;

            case LAYER1:
                if (Math.abs(GetLiftHeight() - layer1) > liftTolerance) {
                    SetLiftPower(liftPID.update(GetLiftHeight(), layer1));
                }
                break;

            case LAYER2:
                if (Math.abs(GetLiftHeight() - layer2) > liftTolerance) {
                    SetLiftPower(liftPID.update(GetLiftHeight(), layer2));
                }
                break;

            case LAYER3:
                if (Math.abs(GetLiftHeight() - layer3) > liftTolerance) {
                    SetLiftPower(liftPID.update(GetLiftHeight(), layer3));
                }
                break;

            case SHARED:
                if (Math.abs(GetLiftHeight() - shared) > liftTolerance) {
                    SetLiftPower(liftPID.update(GetLiftHeight(), shared));
                }
                break;

            case NULL:
                break;
        }

        if (IsDown()) {
            Robot.getInstance().lift.resetEncoder();
        }
    }

    @Override
    public void stop() {
        SetLiftPower(0);
    }

    public void SetLiftPower(double power){
        if (IsDown()) {
            if (power > 0) {
                Robot.getInstance().lift.setPower(power);
            }
        } else {
            Robot.getInstance().lift.setPower(power);
        }
    }

    public double GetLiftHeight () {
        double liftPos = Robot.getInstance().lift.getPosition(), ticksPerRev = 537.6,
                spoolCircumference = 8.93, stages = 2, angle = 20;
        return ((liftPos / ticksPerRev) * spoolCircumference * stages) * Math.sin(angle);
    }

    public boolean IsDown() {
        return Robot.getInstance().downLimit.isPressed();
    }
}