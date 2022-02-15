package com.team9889.ftc2021.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Eric on 8/19/2019.
 */

@Config
public class Carousel extends Subsystem {
    public static double power = 0.7, wantedPower = 0, addedPower = 0.04, time = 1100;
    ElapsedTime timer = new ElapsedTime();

    boolean on = false;

    @Override
    public void init(boolean auto) {
    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) {
        telemetry.addData("Carousel Limit", GetLimit());
    }

    @Override
    public void update() {
        if (on) {
            if (timer.milliseconds() > time) {
                wantedPower = 1;
            }

            if (wantedPower < power) {
                wantedPower += addedPower;
            }

            if (Robot.getInstance().isRed) {
                SetWheelsPower(wantedPower);
            } else {
                SetWheelsPower(-wantedPower);
            }
        } else {
            timer.reset();
            SetWheelsPower(0);
            wantedPower = 0;
        }
    }

    @Override
    public void stop() {
        SetWheelsPower(0);
    }

    public void SetWheelsPower(double power){
        Robot.getInstance().carousel.setPower(power);
    }

    public void TurnOn() {
        on = true;
    }

    public void TurnOff() {
        on = false;
    }

    public boolean GetLimit() {
        if (Robot.getInstance().isRed) {
            return Robot.getInstance().redLimit.isPressed();
        } else {
            return Robot.getInstance().blueLimit.isPressed();
        }
    }
}