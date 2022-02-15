package com.team9889.ftc2021.auto.actions.drive;

import android.util.Log;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.subsystems.Robot;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Created by Eric on 1/21/2022.
 */
public class ResetRR extends Action {
    @Override
    public void start() {
        Robot.getInstance().getMecanumDrive().resetRR = true;
    }

    @Override
    public void update() {
//        Log.v("Robot Heading", "" + Robot.getInstance().rr.getPoseEstimate().getHeading());
        if (Math.abs(Robot.getInstance().rr.getPoseEstimate().getHeading()) < 5) {
            double yValue = Robot.getInstance().getMecanumDrive().getPositionRed().getY();
            Log.v("New Y Pos", "" + yValue);
            if (Math.abs(Robot.getInstance().rr.getPoseEstimate().getY() - yValue) < 5) {
                Log.v("Reset", "");
                Robot.getInstance().rr.setPoseEstimate(new Pose2d(Robot.getInstance().rr.getPoseEstimate().getX(),
                        yValue, Robot.getInstance().getMecanumDrive().getAngle().getTheda(AngleUnit.RADIANS) + Math.toRadians(90)));
            }
        }
    }

    @Override
    public boolean isFinished() {
        return !Robot.getInstance().getMecanumDrive().resetRR;
    }

    @Override
    public void done() {

    }
}
