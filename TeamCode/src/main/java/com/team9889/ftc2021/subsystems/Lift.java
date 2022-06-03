package com.team9889.ftc2021.subsystems;

import android.util.Log;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.lib.CruiseLib;
import com.team9889.lib.control.controllers.PID;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Eric on 8/19/2019.
 */

@Config
public class Lift extends Subsystem {
    public static double angle = 0, power = 1, offset1 = -0.02, offset2 = 0, offset3 = 0.01;

    public static double liftTolerance = 1.5;

    public boolean done = true;

    public enum LiftState {
        DOWN, LAYER1, LAYER2, LAYER3, LAYER3_CLOSE, SHARED_CLOSE, SHARED_FAR, SMART, NULL
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

    public static PID liftPID = new PID(0.09, 0, 0);
    public static double maxCurrent = 6500;
    double current = 0;
    ElapsedTime timer = new ElapsedTime();

    @Override
    public void init(boolean auto) {
        if (auto) {
            wantedLiftState = LiftState.NULL;
            angle = .66;
        } else {
            wantedLiftState = LiftState.DOWN;
            angle = 0;
        }
    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) {
        telemetry.addData("Lift Height", GetLiftLength());
        telemetry.addData("Lift Angle", angle);

//        telemetry.addData("Is Lift Down", IsDown());
//        telemetry.addData("Lift PID Power", liftPID.getOutput());
    }

    @Override
    public void update() {
        switch (wantedLiftState) {
            case DOWN:
                angle = .66;

                if (!IsDown()) {
                    SetLiftPower(-1);
                } else {
                    SetLiftPower(0);
                    done = true;
                }
                break;

            case LAYER1:
                done = SetLiftPos(25, 3);
                break;

            case LAYER2:
                done = SetLiftPos(24, 12);
                break;

            case LAYER3:
                done = SetLiftPos(31, 25);
                break;

            case LAYER3_CLOSE:
                done = SetLiftPos(22, 18);
                angle = 1;
                break;

            case SHARED_CLOSE:
                done = SetLiftPos(11, 8);
                break;

            case SHARED_FAR:
                done = SetLiftPos(20, 8);
                break;

            case SMART:
                if (Robot.getInstance().getIntake().block) {
                    done = SetLiftPos(29, 23);
                } else {
                    done = SetLiftPos(24, 14);
                }
                break;

            case NULL:
                break;
        }
        currentLiftState = wantedLiftState;

        switch (scoreState) {
            case RELEASE:
                if (Robot.getInstance().driverStation != null) {
                    Robot.getInstance().driverStation.dumperOpen = true;
                }
                Robot.getInstance().getDumper().gateState = Dumper.GateState.OPEN;

                scorePose = new Vector2d(Robot.getInstance().fLDrive.getPosition(), Robot.getInstance().fRDrive.getPosition());

                if ((Robot.getInstance().robotTimer.milliseconds() - scoreStateTime) >= 250) {
                    scoreState = ScoreState.DOWN;
                    timer.reset();
                    scoreStateTime = Robot.getInstance().robotTimer.milliseconds();
                }
                break;

            case DOWN:
                if (Math.abs(Robot.getInstance().fLDrive.getPosition() - scorePose.getX()) > 50 &&
                        Math.abs(Robot.getInstance().fRDrive.getPosition() - scorePose.getY()) > 50) {
                    if (Robot.getInstance().driverStation != null) {
                        Robot.getInstance().driverStation.dumperOpen = false;
                    }
                    Robot.getInstance().getDumper().gateState = Dumper.GateState.CLOSED;

                    if (Robot.getInstance().driverStation != null) {
                        Robot.getInstance().driverStation.liftDown = true;
                    }
                    wantedLiftState = LiftState.DOWN;

                    if (IsDown() || timer.milliseconds() > 1500) {
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


        angle = CruiseLib.limitValue(angle, 1, .21);
        SetAngleAdjust(angle);
    }

    @Override
    public void stop() {
        SetLiftPower(0);
    }

    public boolean SetLiftLength(double length) {
        if (Math.abs(GetLiftLength() - length) > liftTolerance) {
            SetLiftPower(liftPID.update(GetLiftLength(), length));
        } else {
            SetLiftPower(0);
            return true;
        }

        return false;
    }

    public void SetLiftPower(double power){
        Log.i("Power", "" + power);
//        power = CruiseLib.limitValue(power, 1, 0.2);

        if (IsDown()) {
            if (power > 0) {
                Robot.getInstance().lift.setPower(power);
            } else {
                Robot.getInstance().lift.setPower(0);
            }
        } else {
            if (Math.abs(power) > 0.05) {
                Robot.getInstance().lift.setPower(power);
            } else {
                Robot.getInstance().lift.setPower(0.07);
//                if (angle <= .5) {
//                    Robot.getInstance().lift.setPower(0.15);
//                } else if (angle >= 0.85) {
//                    Robot.getInstance().lift.setPower(0.3);
//                } else {
//                    Robot.getInstance().lift.setPower(0.2);
//                }
            }
        }
    }

    public void SetAngleAdjust(double pos) {
        pos = CruiseLib.limitValue(pos, 1, 0.21);
        Robot.getInstance().angleAdjust1.setPosition(pos + offset1);
        Robot.getInstance().angleAdjust2.setPosition(pos + offset2);
        Robot.getInstance().angleAdjust3.setPosition(pos + offset3);
    }

//  Units are Inches
    public double GetLiftLength() {
        double spoolRadius = 0.875, ticks = 145.1;
        double spoolCircumference = 2 * Math.PI * spoolRadius;
        return (Robot.getInstance().lift.getPosition() / ticks) * spoolCircumference;
    }

    public boolean IsDown() {
        return Robot.getInstance().downLimit.isPressed();
    }


//  Units are Inches and Degrees
    public Boolean SetLiftPos(double length, double height) {
        double midToEnd = 5.625, lengthToEnd = 10, liftArm = 11.75, dumperHeight = 4.25;
        double hypot = Math.sqrt(Math.pow(height - 6 + (6 - dumperHeight), 2) + Math.pow(length - midToEnd, 2));
        double theta = Math.atan2(height - 5.625, length + midToEnd) + (Math.toRadians(5.913585024279) / 2);
//        double theta = Math.toRadians(height);
//        double hypot = ((length - 7) / Math.cos(Math.toRadians(21.7265))) / 2;

//         - Math.toRadians(8.65)

        boolean done = SetLiftLength(hypot);


//        x = cos^(-1)((-4 d^3 + 2 sqrt(-16 d^4 + 4232 d^2 + 16095) - 49 d)/(68 (d^2 + 4)))
//        double motorAngle = Math.acos((-4 * Math.pow(hypot, 3) + 2 * Math.sqrt(-16 * Math.pow(hypot, 4)
//                + 4232 * Math.pow(hypot, 2) + 16095) - 49 * hypot)
//                / (68 * (Math.pow(hypot, 2) + 4)));


//        height = sin(theta) * lift_length
//        cos(B) = c^2 + a^2 - b^2 / 2ca' 3.25
        double servoBar = 4.375, liftBar = 2.8125;
//        double h = Math.sin(theta) * lengthToEnd;
        double h = Math.sqrt(liftArm*liftArm + lengthToEnd*lengthToEnd - (2 * liftArm * lengthToEnd * Math.cos(theta)));
//        Angle of servo to h
//        double heightAngle = Math.acos((h*h + servoBar*servoBar - liftBar*liftBar) / (2*h*servoBar));
//        Angle of servoBar to liftBar
        double barsAngle = Math.acos((servoBar*servoBar + liftBar*liftBar - h*h) / (2*servoBar*liftBar));
        barsAngle *= liftBar / servoBar;
//        Log.i("Lift", Math.toDegrees(theta) + ", " + h + ", " + Math.toDegrees(barsAngle));
//        System.out.println(Math.toDegrees(barsAngle));

        if (Double.isNaN(barsAngle)) {
            if (height < 10)
                barsAngle = 0;
            else
                barsAngle = 1;
        }

//        Servo 300 degrees
        angle = (Math.toDegrees(barsAngle / 270) / ((double) 20 / 48)) + .21;

        return done;
//        return false;
    }

//    public static void main(String[] args) {
//        for (int i = 0; i < 360; i++) {
//            SetLiftPos(0, i);
//        }
//    }
}