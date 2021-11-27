package com.team9889.ftc2020.subsystems;

import android.util.Log;

import com.acmerobotics.dashboard.config.Config;
import com.team9889.lib.CruiseLib;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Eric on 8/19/2019.
 */

@Config
public class Carousel extends Subsystem {
    public static double RPM = 160;
    boolean lastOn = false;

    @Override
    public void init(boolean auto) {
    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) { }

    @Override
    public void update() {
    }

    @Override
    public void stop() {
        SetWheelsRPM(0);
    }

    public void SetWheelsRPM(double RPM){
        double power = (RPM / 160) / 2;
        Log.i("POWER", "" + power);
        Robot.getInstance().carousel.setPower(-CruiseLib.limitValue(power, 0.5, -0.5));
//        Robot.getInstance().carousel.setPower(RPM);
    }

    public void TurnOn() {
        if (Robot.getInstance().isRed) {
            SetWheelsRPM(RPM);
        } else {
            SetWheelsRPM(-RPM);
        }
        lastOn = true;
    }

    public void TurnOff() {
        if (!lastOn) {
            SetWheelsRPM(.5);
        }else {
            SetWheelsRPM(0);
        }
        lastOn = false;
    }
}