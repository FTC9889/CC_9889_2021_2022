package com.team9889.ftc2021.auto.actions.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.subsystems.MecanumDrive;
import com.team9889.ftc2021.subsystems.Robot;
import com.team9889.lib.control.math.cartesian.Vector2d;

/**
 * Created by Eric on 5/30/2022.
 */
public class SetUltrasonicPose extends Action {
    public enum Position {
        CAROUSEL, WH_AFTER_BARRIER, WH_INTAKE_SHARED_SIDE, DUCK_AUTO_SCORE
    }
    Position position;

    int times, step = 0;

    public SetUltrasonicPose(Position position, int times) {
        this.position = position;
        this.times = times;
    }

    Vector2d pose = new Vector2d();


    @Override
    public void start() {

    }

    @Override
    public void update() {
        Vector2d tempPose = new Vector2d();
        if (Robot.getInstance().isRed) {
            switch (position) {
                case CAROUSEL:
                    tempPose = Robot.getInstance().getMecanumDrive().getPosition
                            (MecanumDrive.Sensor.LEFT, MecanumDrive.Sensor.BACK, MecanumDrive.Corner.BOTTOM_RIGHT);
                    break;

                case WH_AFTER_BARRIER:
                    tempPose = Robot.getInstance().getMecanumDrive().getPosition
                            (MecanumDrive.Sensor.BACK, MecanumDrive.Sensor.LEFT, MecanumDrive.Corner.TOP_RIGHT);
                    break;

                case WH_INTAKE_SHARED_SIDE:
                    tempPose = Robot.getInstance().getMecanumDrive().getPosition
                            (MecanumDrive.Sensor.LEFT, MecanumDrive.Sensor.FRONT, MecanumDrive.Corner.TOP_RIGHT);
                    break;

                case DUCK_AUTO_SCORE:
                    tempPose = Robot.getInstance().getMecanumDrive().getPosition
                            (MecanumDrive.Sensor.BACK, MecanumDrive.Sensor.RIGHT, MecanumDrive.Corner.BOTTOM_RIGHT);
                    break;
            }
        } else {

        }

        pose.setX(((pose.getX() * step) + tempPose.getX()) / (step + 1));
        pose.setY(((pose.getY() * step) + tempPose.getY()) / (step + 1));


        step++;
    }

    @Override
    public boolean isFinished() {
        return step >= times;
    }

    @Override
    public void done() {
        Robot.getInstance().rr.setPoseEstimate(new Pose2d(pose.getX(), pose.getY(),
                Robot.getInstance().rr.getPoseEstimate().getHeading()));
    }
}
