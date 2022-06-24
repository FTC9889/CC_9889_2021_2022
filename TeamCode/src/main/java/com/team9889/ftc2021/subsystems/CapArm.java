package com.team9889.ftc2021.subsystems;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Eric on 5/13/2022.
 */
public class CapArm extends Subsystem{
    public int step = 0;
    public double position = 0.15;
    public boolean manualControl = false;
    ElapsedTime timer = new ElapsedTime();

    public boolean intake = false;

    @Override
    public void init(boolean auto) {

    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) {
        telemetry.addData("Cap Arm Step", step);
    }

    @Override
    public void update() {
        if (!manualControl) {
            switch (step) {
                case 0:
                    // Arm Down
                    setCapArm(intake ? 0.3 : 0.17);
                    capClose();
                    break;

                case 1:
                    // Grab Cap
                    setCapArm(0.865);
                    capClose();
                    break;

                case 2:
                    // Arm Up
                    setCapArm(0.58);
                    capClose();
                    timer.reset();
                    break;

                case 3:
                    // Arm Down
                    capOpen();
                    if (timer.milliseconds() > 1000) {
                        step = 4;
                    }
                    break;

                case 4:
                    setCapArm(0.4);
                    break;

                case 5:
                    step = 1;
                    break;
            }
        } else {
            timer.reset();
        }

        if (step < 0) {
            step = 0;
        } else if (step > 5) {
            step = 5;
        }
    }

    @Override
    public void stop() {

    }

    public void setCapArm(double position) {
        Robot.getInstance().capArm.setPosition(position);
        this.position = position;
    }

//    public void setCapArm(double position, double time) {
//        Robot.getInstance().capArm.setPosition(position, time);
//    }

    public void capClose() {
        Robot.getInstance().capRelease.setPosition(1);
    }

    public void capOpen() {
        Robot.getInstance().capRelease.setPosition(0);
    }
}
