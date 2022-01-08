package com.team9889.ftc2021.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.lib.control.controllers.PID;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Eric on 8/19/2019.
 */

@Config
public class Lift extends Subsystem {
    public static double angle = 0, power = 0.1;

    public static double liftTolerance = 8;

    public enum LiftState {
        DOWN, LAYER1, LAYER2, LAYER3, SHARED, NULL
    }
    public LiftState wantedLiftState = LiftState.NULL, currentLiftState = LiftState.NULL,
            defaultLiftState = LiftState.LAYER3;

    public enum ScoreState {
        RELEASE, DOWN, NULL
    }
    public ScoreState scoreState = ScoreState.NULL;
    double scoreStateTime = 0;
    Vector2d scorePose = new Vector2d(), sinceScorePos = new Vector2d();

    public static double layer1 = 3, layer2 = 8, layer3 = 13.5, shared = 11;

    public static PID liftPID = new PID(0.6, 0, 0);

    public boolean isDown = true;
    public static double maxCurrent = 6500;
    double current = 0;
    ElapsedTime timer = new ElapsedTime();

    @Override
    public void init(boolean auto) {
        if (auto) {
            wantedLiftState = LiftState.DOWN;
        }
    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) {
        telemetry.addData("Is Lift Down", IsDown());
        telemetry.addData("Lift Height", GetLiftHeight());
        telemetry.addData("Default Lift State", defaultLiftState);
        telemetry.addData("Lift State", currentLiftState);
    }

    @Override
    public void update() {
        if (wantedLiftState == LiftState.DOWN && currentLiftState != LiftState.DOWN) {
            timer.reset();
        }
        switch (wantedLiftState) {
            case DOWN:
                if (!IsDown()) {
                    SetLiftPower(-0.5);
                    if (timer.milliseconds() > 800) {
                        isDown = true;
                    }
                } else {
                    SetLiftPower(0);
                }
                break;

            case LAYER1:
                if (Math.abs(GetLiftHeight() - layer1) > liftTolerance) {
                    SetLiftPower(liftPID.update(GetLiftHeight(), layer1));
                } else {
                    SetLiftPower(0);
                }
                break;

            case LAYER2:
                if (Math.abs(GetLiftHeight() - layer2) > liftTolerance) {
                    SetLiftPower(liftPID.update(GetLiftHeight(), layer2));
                } else {
                    SetLiftPower(0);
                }
                break;

            case LAYER3:
                if (Math.abs(GetLiftHeight() - layer3) > liftTolerance) {
                    SetLiftPower(liftPID.update(GetLiftHeight(), layer3));
                } else {
                    SetLiftPower(0);
                }
                break;

            case SHARED:
                if (Math.abs(GetLiftHeight() - shared) > liftTolerance) {
                    SetLiftPower(liftPID.update(GetLiftHeight(), shared));
                } else {
                    SetLiftPower(0);
                }
                break;

            case NULL:
                break;
        }
        currentLiftState = wantedLiftState;

        switch (scoreState) {
            case RELEASE:
                Robot.getInstance().driverStation.dumperOpen = true;
                Robot.getInstance().getDumper().gateState = Dumper.GateState.OPEN;

                scorePose = new Vector2d(Robot.getInstance().fLDrive.getPosition(), Robot.getInstance().fRDrive.getPosition());

                if ((Robot.getInstance().robotTimer.milliseconds() - scoreStateTime) >= 500) {
                    scoreState = ScoreState.DOWN;
                    scoreStateTime = Robot.getInstance().robotTimer.milliseconds();
                }
                break;

            case DOWN:
                if (Math.abs(Robot.getInstance().fLDrive.getPosition() - scorePose.getX()) > 250 &&
                        Math.abs(Robot.getInstance().fRDrive.getPosition() - scorePose.getY()) > 250) {
                    Robot.getInstance().driverStation.dumperOpen = false;
                    Robot.getInstance().getDumper().gateState = Dumper.GateState.CLOSED;

                    Robot.getInstance().driverStation.liftDown = true;
                    wantedLiftState = LiftState.DOWN;

                    if (IsDown()) {
                        scoreState = ScoreState.NULL;
                    }
                }
                break;

            case NULL:
                scoreStateTime = Robot.getInstance().robotTimer.milliseconds();
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

    public void SetLiftAngle(double angle) {
        double liftAngle = (Robot.getInstance().lift.getPosition() / 537.6) * 360;
        if (Math.abs(liftAngle - angle) > liftTolerance) {
//            SetLiftPower(liftPID.update(liftAngle, angle));
            SetLiftPower(power * ((liftAngle - angle) / Math.abs(liftAngle - angle)));
        } else {
            SetLiftPower(0);
        }
    }

    public void SetLiftPower(double power){
//        power = CruiseLib.limitValue(power, -0.3, -1, 0.3, 1);

//        if (power < 0 && current > maxCurrent) {
//            isDown = true;
//            Robot.getInstance().lift.setPower(0);
//        }

        if (isDown) {
            if (power > 0) {
                Robot.getInstance().lift.setPower(power);
                isDown = false;
            } else {
                Robot.getInstance().lift.setPower(0);
            }
        } else {
            Robot.getInstance().lift.setPower(power);
        }
    }

    public void SetAngleAdjust(double pos) {
        Robot.getInstance().angleAdjust1.setPosition(pos);
        Robot.getInstance().angleAdjust2.setPosition(pos);
    }

    public double GetLiftHeight () {
        double liftPos = Robot.getInstance().lift.getPosition(), ticksPerRev = 537.6,
                spoolCircumference = 8.93, stages = 2, angle = 20;
        return ((liftPos / ticksPerRev) * spoolCircumference * stages) * Math.sin(angle);
    }

    public boolean IsDown() {
//        return Robot.getInstance().downLimit.isPressed();
        return isDown;
    }


    public Vector2d SetLiftPos (double length, double height) {
        double hypot = Math.sqrt(Math.pow(height, 2) + Math.pow(length, 2)) / 2;
        double theta = Math.atan2(height, length) - Math.toRadians(0);

//        x = cos^(-1)((-4 d^3 + 2 sqrt(-16 d^4 + 4232 d^2 + 16095) - 49 d)/(68 (d^2 + 4)))
        double motorAngle = Math.acos((-4 * Math.pow(hypot, 3) + 2 * Math.sqrt(-16 * Math.pow(hypot, 4)
                + 4232 * Math.pow(hypot, 2) + 16095) - 49 * hypot)
                / (68 * (Math.pow(hypot, 2) + 4)));

//        height = sin(theta) * lift_length
//        cos(B) = c^2 + a^2 - b^2 / 2ca
        double h = Math.sin(theta) * 11.87;
        double heightAngle = Math.asin((Math.pow(h, 2) + Math.pow(3.78, 2) - Math.pow(3.78, 2)) / (2 * h * 3.78));
        if (Double.isNaN(heightAngle)) {
            heightAngle = 0;
        }

//        Servo 300 degrees
//        Motor 1680 ticks per rev
        SetLiftAngle(motorAngle);
        SetAngleAdjust(heightAngle / 300);
        angle = heightAngle;

        return new Vector2d(motorAngle, heightAngle);
    }

//    public static void main(String[] args) {
//        Vector2d lift = SetLiftPos(15, 10);
//        System.out.println(Math.toDegrees(lift.getX()) + ", " + Math.toDegrees(lift.getY()));
//    }
}