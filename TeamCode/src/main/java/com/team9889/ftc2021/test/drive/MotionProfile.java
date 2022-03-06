package com.team9889.ftc2021.test.drive;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2021.Team9889Linear;
import com.team9889.lib.control.controllers.MotionProfileFollower;
import com.team9889.lib.control.motion.ProfileParameters;
import com.team9889.lib.control.motion.TrapezoidalMotionProfile;

/**
 * Created by Eric on 3/4/2022.
 */

@Config
@Autonomous
public class MotionProfile extends Team9889Linear {
    @Override
    public void initialize() {

    }

    MotionProfileFollower mpYProfile = new MotionProfileFollower(0.00002, 0,0.017, 0.0005);
    TrapezoidalMotionProfile yProfile = new TrapezoidalMotionProfile();

    MotionProfileFollower mpXProfile = new MotionProfileFollower(0.00002, 0, 0.048, 0.0005);
    TrapezoidalMotionProfile xProfile = new TrapezoidalMotionProfile();

    ElapsedTime timer = new ElapsedTime();

    ProfileParameters xParameters = new ProfileParameters(40, 36);
    ProfileParameters yParameters = new ProfileParameters(80, 65);

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart(true);
        timer.reset();

        yProfile.calculate(0, yParameters);
        mpYProfile.setProfile(yProfile);

        xProfile.calculate(-50.0 / 2, xParameters);
        mpXProfile.setProfile(xProfile);

        while(opModeIsActive()) {

            double xSpeed = mpXProfile.update(Robot.rr.getPoseEstimate().getX(), timer.milliseconds() / 1000);
            double ySpeed = mpYProfile.update(Robot.rr.getPoseEstimate().getY(), timer.milliseconds() / 1000);
            Robot.getMecanumDrive().setPower(xSpeed, ySpeed, 0);

//            telemetry.addData("Pose", Robot.rr.getPoseEstimate());
//
//            telemetry.addData("Wanted Speed", mpXProfile.profile.getVelocity());
//            telemetry.addData("Current Speed",
//                    ((Robot.fLDrive.getVelocity() / 537.6) * 1.25 * (3.77953 * Math.PI)) / 2);
//            telemetry.update();

            Robot.update();

//            if (mpYProfile.profile.getVelocity() == 0 && mpXProfile.profile.getVelocity() == 0) {
//                if (step == 0) {
//                    yProfile.calculate(0, new ProfileParameters(maxV, maxA));
//                    mpYProfile = new MotionProfileFollower(p, d, v, a);
//                    mpYProfile.setProfile(yProfile);
//
//                    xProfile.calculate(50.0 / 2, new ProfileParameters(maxV, maxA));
//                    mpXProfile = new MotionProfileFollower(p, d, v, a);
//                    mpXProfile.setProfile(xProfile);
//
//                    step = 1;
//                } else if (step == 1) {
//                    yProfile.calculate(0, new ProfileParameters(maxV, maxA));
//                    mpYProfile = new MotionProfileFollower(p, d, v, a);
//                    mpYProfile.setProfile(yProfile);
//
//                    xProfile.calculate(-50.0 / 2, new ProfileParameters(maxV, maxA));
//                    mpXProfile = new MotionProfileFollower(p, d, v, a);
//                    mpXProfile.setProfile(xProfile);
//
//                    step = 0;
//                } else {
//                    yProfile.calculate(0, new ProfileParameters(maxV, maxA));
//                    mpYProfile = new MotionProfileFollower(p, d, v, a);
//                    mpYProfile.setProfile(yProfile);
//
//                    xProfile.calculate(0, new ProfileParameters(maxV, maxA));
//                    mpXProfile = new MotionProfileFollower(p, d, v, a);
//                    mpXProfile.setProfile(xProfile);
//                }
//                timer.reset();
//            }
        }
    }
}
