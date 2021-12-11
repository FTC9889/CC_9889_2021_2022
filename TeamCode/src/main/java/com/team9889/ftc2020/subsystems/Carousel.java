package com.team9889.ftc2020.subsystems;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Eric on 8/19/2019.
 */

@Config
public class Carousel extends Subsystem {
    public static double power = 0.65;

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
        SetWheelsPower(0);
    }

    public void SetWheelsPower(double power){
        Robot.getInstance().carousel.setPower(power);
    }

    public void TurnOn() {
        if (Robot.getInstance().isRed) {
            SetWheelsPower(power);
        } else {
            SetWheelsPower(-power);
        }
    }

    public void TurnOff() {
        SetWheelsPower(0);
    }
}