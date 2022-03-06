package com.team9889.ftc2021.auto.actions.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.subsystems.Robot;
import com.team9889.lib.control.controllers.MotionProfileFollower;
import com.team9889.lib.control.motion.ProfileParameters;
import com.team9889.lib.control.motion.TrapezoidalMotionProfile;

/**
 * Created by Eric on 3/4/2022.
 */

public class DriveMotionProfile extends Action {
    MotionProfileFollower mpYProfile = new MotionProfileFollower(0.00002, 0,0.017, 0.0005);
    TrapezoidalMotionProfile yProfile = new TrapezoidalMotionProfile();

    MotionProfileFollower mpXProfile = new MotionProfileFollower(0.00002, 0, 0.048, 0.0005);
    TrapezoidalMotionProfile xProfile = new TrapezoidalMotionProfile();

    ElapsedTime timer = new ElapsedTime();

    ProfileParameters xParameters = new ProfileParameters(40, 36);
    ProfileParameters yParameters = new ProfileParameters(80, 65);

    Pose2d pose;

    public DriveMotionProfile(Pose2d pose) {
        this.pose = pose;
    }

    @Override
    public void start() {
        timer.reset();

        yProfile.calculate(pose.getY(), yParameters);
        mpYProfile.setProfile(yProfile);

        xProfile.calculate(pose.getX() / 2, xParameters);
        mpXProfile.setProfile(xProfile);

        mpXProfile.update(Robot.getInstance().rr.getPoseEstimate().getX(), timer.milliseconds() / 1000);
        mpYProfile.update(Robot.getInstance().rr.getPoseEstimate().getY(), timer.milliseconds() / 1000);
    }

    @Override
    public void update() {
        double xSpeed = mpXProfile.update(Robot.getInstance().rr.getPoseEstimate().getX(), timer.milliseconds() / 1000);
        double ySpeed = mpYProfile.update(Robot.getInstance().rr.getPoseEstimate().getY(), timer.milliseconds() / 1000);
        Robot.getInstance().getMecanumDrive().setPower(xSpeed, ySpeed, 0);
    }

    @Override
    public boolean isFinished() {
        return mpXProfile.profile.getVelocity() == 0 && mpYProfile.profile.getVelocity() == 0;
    }

    @Override
    public void done() {
        Robot.getInstance().getMecanumDrive().setPower(0, 0, 0);
    }
}
