package com.team9889.ftc2021.auto.actions.drive;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.subsystems.Intake;
import com.team9889.ftc2021.subsystems.Robot;

import org.opencv.core.Point;

/**
 * Created by Eric on 5/27/2022.
 */

public class DriveToDuck extends Action {
    ElapsedTime timer = new ElapsedTime();

    Point lastPoint = new Point(0, 0);

    @Override
    public void start() {
        timer.reset();
    }

    @Override
    public void update() {
        Point point = Robot.getInstance().getCamera().scanForDuck.getPoint();

        if (point.x == 160) {
            point = lastPoint;
        } else {
            lastPoint = point;
        }

        double xSpeed = 0, turnSpeed = (0.0014 * (point.x - 160)) + 0.0221;

        if (Robot.getInstance().isRed) {
            if (Robot.getInstance().rr.getPoseEstimate().getY() > -58 && Robot.getInstance().rr.getPoseEstimate().getX() > -65) {
                xSpeed = 0.5;
            }
        } else {
            if (Robot.getInstance().rr.getPoseEstimate().getY() < 58 && Robot.getInstance().rr.getPoseEstimate().getX() > -65) {
                xSpeed = 0.5;
            }
        }

        Robot.getInstance().getMecanumDrive().setPower(0, xSpeed, turnSpeed);
    }

    @Override
    public boolean isFinished() {
        return Robot.getInstance().getIntake().loadState != Intake.LoadState.INTAKE || timer.milliseconds() > 2000;
    }

    @Override
    public void done() {
        Robot.getInstance().getIntake().loadState = Intake.LoadState.TRANSFER;
        Robot.getInstance().getMecanumDrive().setPower(0, 0, 0);
    }
}
