package com.team9889.lib.roadrunner.drive.opmode;

import android.util.Log;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.team9889.ftc2020.DriverStation;
import com.team9889.ftc2020.subsystems.Robot;
import com.team9889.lib.roadrunner.drive.SampleMecanumDrive;

/*
 * This is an example of a more complex path to really test the tuning.
 */
@Autonomous(group = "drive")
public class SplineTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Robot.getInstance().init(hardwareMap, true, new DriverStation(gamepad1, gamepad2));
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        waitForStart();

        if (isStopRequested()) return;

        Trajectory traj = drive.trajectoryBuilder(new Pose2d())
                .splineTo(new Vector2d(30, 30), 0)
                .addTemporalMarker(1, () -> {
                    Log.v("Hello", "");
                    Robot.getInstance().lift.setPower(0.5);
                })
                .splineTo(new Vector2d(60, 0), Math.toRadians(180))
                .build();

        drive.followTrajectory(traj);

        sleep(2000);
    }
}
