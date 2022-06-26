package com.team9889.ftc2021.auto.actions.drive.duck;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.subsystems.Intake;
import com.team9889.ftc2021.subsystems.Robot;
import com.team9889.lib.CruiseLib;

import org.opencv.core.Point;

/**
 * Created by Eric on 5/27/2022.
 */

public class DriveToDuck extends Action {
    ElapsedTime timer = new ElapsedTime();

    int counter = 0;

    @Override
    public void start() {
        timer.reset();
    }

    @Override
    public void update() {
        if (counter < 5 && timer.milliseconds() < 3000) {
            Point point = Robot.getInstance().getCamera().scanForDuck.getPoint();

            double xSpeed = 0, turnSpeed;

            if (point.y >= 120 || point.x == 160) {
                turnSpeed = 0;
            } else {
                turnSpeed = (0.0015 * (point.x - 160)) + 0.0221;
            }

//            if ((point.y < 145 || Math.abs(Robot.getInstance().getCamera().scanForDuck.getPoint().x - 160) < 6)
//                    || point.y >= 160 || point.x == 160) {
                if (Robot.getInstance().isRed) {
                    if (Robot.getInstance().rr.getPoseEstimate().getY() > -59 && Robot.getInstance().rr.getPoseEstimate().getX() > -65) {
                        xSpeed = 0.3 * CruiseLib.limitValue(-0.0556 * Robot.getInstance().rr.getPoseEstimate().getY() + 3.2222, 1, 0.3);
                    }
                } else {
                    if (Robot.getInstance().rr.getPoseEstimate().getY() < 61 && Robot.getInstance().rr.getPoseEstimate().getX() > -65) {
                        xSpeed = 0.4 * CruiseLib.limitValue(-0.0556 * Robot.getInstance().rr.getPoseEstimate().getY() + 3.2222, 1, 0.4);
                    }
                }
//            }

            Robot.getInstance().getMecanumDrive().setPower(0, xSpeed, turnSpeed);

            if (Robot.getInstance().getIntake().loadState != Intake.LoadState.INTAKE) {
                counter++;
                Robot.getInstance().getIntake().loadState = Intake.LoadState.INTAKE;
            } else {
                counter = 0;
            }
        } else {
            Robot.getInstance().getMecanumDrive().setPower(0, -.2, 0);
        }
    }

    @Override
    public boolean isFinished() {
        return timer.milliseconds() > 4000;
    }

    @Override
    public void done() {
        Robot.getInstance().getIntake().loadState = Intake.LoadState.TRANSFER;

        Robot.getInstance().getIntake().overridePower = true;

        Robot.getInstance().getMecanumDrive().setPower(0, 0, 0);
    }
}
