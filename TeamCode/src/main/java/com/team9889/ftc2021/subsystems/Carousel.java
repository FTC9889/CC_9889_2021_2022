package com.team9889.ftc2021.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Eric on 8/19/2019.
 */

@Config
public class Carousel extends Subsystem {
    public static double power = 0.6, wantedPower = 0, addedPower = 0.04, time = 1450;
    ElapsedTime timer = new ElapsedTime();

    public boolean on = false, override = false;

    @Override
    public void init(boolean auto) {
        power = 0.6;
        on = false;
        Robot.getInstance().carousel.setRPM(0);
        Robot.getInstance().carousel.setPower(0);
    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) {
//        telemetry.addData("Carousel Power", power);

        telemetry.addData("Carousel Velocity", Robot.getInstance().carousel.getVelocity());
        telemetry.addData("Carousel Encoder", Robot.getInstance().carousel.getPosition());

//        telemetry.addData("Carousel Limits", "  Red:" + Robot.getInstance().redLimit.isPressed() +
//                ", Blue:" + Robot.getInstance().blueLimit.isPressed());
    }

    @Override
    public void update() {
        if (!override) {
            if (on) {
                if (timer.milliseconds() > time) {
                    wantedPower = 1;
                }

                if (wantedPower < power) {
//                    wantedPower += addedPower;
                    Robot.getInstance().carousel.setRPM(170 * (Robot.getInstance().isRed ? 1 : -1));
                }

                if (Robot.getInstance().isRed) {
                    SetWheelsPower(wantedPower);
                } else {
                    SetWheelsPower(-wantedPower);
                }
            } else {
                timer.reset();
                SetWheelsPower(0);
                Robot.getInstance().carousel.setRPM(0);
                wantedPower = 0;
            }
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