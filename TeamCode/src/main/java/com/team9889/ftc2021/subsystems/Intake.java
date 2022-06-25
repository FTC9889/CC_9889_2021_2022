package com.team9889.ftc2021.subsystems;

import android.graphics.Color;
import android.util.Log;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.openftc.revextensions2.ExpansionHubEx;

/**
 * Created by Eric on 8/19/2019.
 */

@Config
public class Intake extends Subsystem {
    public static double power = 1, up = 0.45, down = 0.7;

    public boolean overridePower = false, passThroughOn = false;

    ElapsedTime liftTimer = new ElapsedTime();

    public enum IntakeHeightState {
        UP, DOWN, NULL
    }
    public IntakeHeightState wantedIntakeHeightState = IntakeHeightState.NULL;

    public enum IntakeState {
        ON, OFF, OUT, PARTIAL_OUT
    }
    public IntakeState intakeState = IntakeState.OFF, passThroughState = IntakeState.OFF;

    public enum LoadState {
        INTAKE, TRANSFER, OUTTAKE, OFF
    }
    public LoadState loadState = LoadState.OFF;

    ElapsedTime hopperTimer = new ElapsedTime();

    public boolean block = true;

    @Override
    public void init(boolean auto) {
//        if (auto) {
        intakeState = IntakeState.OFF;
        wantedIntakeHeightState = IntakeHeightState.UP;
//        }

        SetIntake(0);
        SetPassThrough(0);

        overridePower = false;
        passThroughOn = false;

        up = 0.45;

        Robot.getInstance().inLeft.setGain(2);
    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) {
//        telemetry.addData("Intake Left", Robot.getInstance().inLeft.getDistance(DistanceUnit.INCH));
//        telemetry.addData("Intake Right", Robot.getInstance().inRight.getDistance(DistanceUnit.INCH));
        telemetry.addData("Freight in Intake", GetFreightInIntake());
        telemetry.addData("Is Block", block);

        telemetry.addData("Intake Current", Robot.getInstance().intake.motor.getCurrentDraw(ExpansionHubEx.CurrentDrawUnits.MILLIAMPS));

        telemetry.addData("Power", power);
    }

    @Override
    public void update() {
        switch (loadState) {
            case INTAKE:
                if (!overridePower)
                    power = 0.75;
                IntakeOn();
                IntakeDown();
                if (passThroughOn)
                    SetPassThrough(0.5);
                else
                    passThroughState = IntakeState.OFF;

                if (GetFreightInIntake() && hopperTimer.milliseconds() > 500) {
                    loadState = LoadState.TRANSFER;

                    block = GetHue() <= 120;
                }
                break;

            case TRANSFER:
//                power = -0.4;
                if (overridePower) {
                    power = 0.2;
                    IntakeOn();
                } else {
                    IntakeOff();
                }
                IntakeUp();
                PassThroughOn();
                break;

            case OUTTAKE:
                IntakeOut();
                IntakeDown();
                PassThroughOut();
                break;

            case OFF:
                IntakeOff();
                IntakeUp();
                PassThroughOff();
                break;
        }

        if (loadState == LoadState.TRANSFER) {
            Robot.getInstance().getCapArm().intake = true;
        } else {
            Robot.getInstance().getCapArm().intake = false;
            liftTimer.reset();
        }

        if (loadState != LoadState.INTAKE) {
            hopperTimer.reset();
        }

        switch (intakeState) {
            case ON:
                SetIntake(power);
                break;

            case OFF:
                SetIntake(0);
                break;

            case OUT:
                SetIntake(-0.5);
                break;
        }

        switch (passThroughState) {
            case ON:
                SetPassThrough(1);
                break;

            case OFF:
                SetPassThrough(0);
                break;

            case OUT:
                SetPassThrough(-0.5);
                break;

            case PARTIAL_OUT:
                SetPassThrough(-0.6);
                break;
        }

        if (false) {
            Robot.getInstance().flag.setPosition(0.5);
        } else {
            if (Robot.getInstance().slowdown) {
                Robot.getInstance().flag.setPosition(1);
            } else {
                Robot.getInstance().flag.setPosition(0);
            }
        }

        Log.v("Loop Time I", "" + Robot.getInstance().loopTime.milliseconds());
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

    public void IntakeOn() {
        intakeState = IntakeState.ON;
    }

    public void IntakeOut() {
        intakeState = IntakeState.OUT;
    }

    public void IntakeOff() {
        intakeState = IntakeState.OFF;
    }

    public void PassThroughOn() {
        passThroughState = IntakeState.ON;
    }

    public void PassThroughOut() {
        passThroughState = IntakeState.OUT;
    }

    public void PassThroughOff() {
        passThroughState = IntakeState.OFF;
    }

    public void IntakeUp() {
        Robot.getInstance().intakeLift.setPosition(up);
    }

    public void IntakeDown() {
        Robot.getInstance().intakeLift.setPosition(down);
    }

    public boolean IntakeGateOpen() {
        return Robot.getInstance().intakeGate.isPressed();
    }

    public boolean GetFreightInIntake() {
        try {
            return Robot.getInstance().inLeft.getDistance(DistanceUnit.INCH) < 1.5 || Robot.getInstance().inRight.getDistance(DistanceUnit.INCH) < 3.4;
        }
        catch (Exception exception) {
            Log.e("Intake failed TryCatch", "" + exception);
            return false;
        }
    }

    public double GetHue() {
        final float[] hsvValues = new float[3];
        NormalizedRGBA colors = Robot.getInstance().inLeft.getNormalizedColors();
        Color.colorToHSV(colors.toColor(), hsvValues);
        return hsvValues[0];
    }
}