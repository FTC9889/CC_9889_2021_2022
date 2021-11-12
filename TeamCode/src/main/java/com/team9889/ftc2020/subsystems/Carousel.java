package com.team9889.ftc2020.subsystems;

import com.team9889.lib.CruiseLib;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Eric on 8/19/2019.
 */

public class Carousel extends Subsystem {
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
        Robot.getInstance().carousel.setPower(CruiseLib.limitValue(power, 0.5, -0.5));
    }
}