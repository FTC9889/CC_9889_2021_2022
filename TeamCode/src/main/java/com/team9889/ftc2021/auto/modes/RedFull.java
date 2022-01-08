package com.team9889.ftc2021.auto.modes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2021.auto.AutoModeBase;
import com.team9889.ftc2021.auto.actions.utl.RobotUpdate;
import com.team9889.ftc2021.auto.actions.utl.Wait;
import com.team9889.ftc2021.subsystems.Dumper;
import com.team9889.ftc2021.subsystems.Lift;
import com.team9889.lib.roadrunner.drive.DriveConstants;
import com.team9889.lib.roadrunner.drive.SampleMecanumDrive;

/**
 * Created by Eric on 11/23/2021.
 */

@Autonomous(name = "\uD83D\uDFE5 Red Auto \uD83D\uDFE5", preselectTeleOp = "Teleop")
public class RedFull extends AutoModeBase {
    Trajectory traj;

    @Override
    public void run(StartPosition startPosition, Boxes box) {
        Robot.rr.getLocalizer().setPoseEstimate(new Pose2d(-33, -65, Math.toRadians(90)));

        ThreadAction(new RobotUpdate());


        traj = Robot.rr.trajectoryBuilder(new Pose2d(-33, -65, Math.toRadians(90)))
                .splineTo(new Vector2d(-33, -60), Math.toRadians(90))
                .build();
        Robot.rr.followTrajectory(traj);

        Robot.rr.turn(Math.toRadians(-180));
        runAction(new Wait(500));
        box = Robot.getCamera().getWormPos();

        switch (box) {
            case LEFT:
                Robot.getLift().wantedLiftState = Lift.LiftState.LAYER1;
                break;

            case MIDDLE:
                Robot.getLift().wantedLiftState = Lift.LiftState.LAYER2;
                break;

            case RIGHT:
                Robot.getLift().wantedLiftState = Lift.LiftState.LAYER3;
                break;
        }
        traj = Robot.rr.trajectoryBuilder(traj.end().plus(new Pose2d(0, 0, Math.toRadians(-180))), true)
//                .lineToSplineHeading(new Pose2d(-25, -55, Math.toRadians(90)))
                .lineTo(new Vector2d(-16, -36.5),
                SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        Robot.rr.followTrajectory(traj);

        Robot.getDumper().gateState = Dumper.GateState.OPEN;

        runAction(new Wait(500));

        Robot.rr.turn(Math.toRadians(-15));

        traj = Robot.rr.trajectoryBuilder(traj.end().plus(new Pose2d(0, 0, Math.toRadians(-15))))
                .splineToConstantHeading(new Vector2d(-25, -50), Math.toRadians(-90))
                .addDisplacementMarker(() -> {
                    Robot.getDumper().gateState = Dumper.GateState.CLOSED;
                    Robot.getLift().isDown = false;
                    Robot.getLift().wantedLiftState = Lift.LiftState.DOWN;
                })
                .splineToConstantHeading(new Vector2d(-68, -57), Math.toRadians(-90),
                        SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        Robot.rr.followTrajectory(traj);

        Robot.carousel.setPower(0.5);
        runAction(new Wait(3500));
        Robot.getCarousel().TurnOff();

        runAction(new Wait(timeToWait));

        traj = Robot.rr.trajectoryBuilder(traj.end(), true)
                .splineTo(new Vector2d(-60, -45), Math.toRadians(0))
                .build();
        Robot.rr.followTrajectory(traj);

        traj = Robot.rr.trajectoryBuilder(traj.end(), true)
                .lineTo(new Vector2d(55, -30))
                .build();
        Robot.rr.followTrajectory(traj);

        Robot.rr.turn(Math.toRadians(-90));
    }

    @Override
    public StartPosition side() {
        return null;
    }
}
