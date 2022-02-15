package com.team9889.ftc2021.auto.modes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2021.auto.AutoModeBase;
import com.team9889.ftc2021.auto.actions.lift.LiftGoToPos;
import com.team9889.ftc2021.auto.actions.utl.RobotUpdate;
import com.team9889.ftc2021.auto.actions.utl.Wait;
import com.team9889.ftc2021.subsystems.Dumper;
import com.team9889.ftc2021.subsystems.Lift;
import com.team9889.lib.roadrunner.drive.DriveConstants;
import com.team9889.lib.roadrunner.drive.SampleMecanumDrive;

/**
 * Created by Eric on 11/23/2021.
 */

@Autonomous(name = "\uD83D\uDD35 Blue Auto \uD83D\uDD35", preselectTeleOp = "Teleop")
public class BlueFull extends AutoModeBase {
    Trajectory traj;

    @Override
    public void run(StartPosition startPosition, Boxes box) {
        Robot.isRed = false;
        Robot.rr.getLocalizer().setPoseEstimate(new Pose2d(7, 63, Math.toRadians(90)));

        ThreadAction(new RobotUpdate(Robot));

        box = Robot.getCamera().getTSEPos();
        switch (box) {
            case LEFT:
                traj = Robot.rr.trajectoryBuilder(new Pose2d(7, 63, Math.toRadians(90)), true)
                        .splineTo(new Vector2d(-12, 35), Math.toRadians(-90))
                        .build();
                Robot.rr.followTrajectory(traj);

                Robot.getDumper().gateState = Dumper.GateState.OPEN;
                break;

            case MIDDLE:
                traj = Robot.rr.trajectoryBuilder(new Pose2d(7, 63, Math.toRadians(90)), true)
                        .splineTo(new Vector2d(0, 50), Math.toRadians(-105))
                        .build();
                Robot.rr.followTrajectory(traj);

                runAction(new LiftGoToPos(14, 0, 1500));
                Robot.getDumper().gateState = Dumper.GateState.OPEN;
                break;

            case RIGHT:
                traj = Robot.rr.trajectoryBuilder(new Pose2d(7, 63, Math.toRadians(90)), true)
                        .splineTo(new Vector2d(7, 55), Math.toRadians(-108))
                        .build();
                Robot.rr.followTrajectory(traj);

                runAction(new LiftGoToPos(21, 0, 1500));
                Robot.getDumper().gateState = Dumper.GateState.OPEN;
                break;
        }

        runAction(new Wait(300));

        Robot.getLift().wantedLiftState = Lift.LiftState.DOWN;
        Robot.getDumper().gateState = Dumper.GateState.CLOSED;

        traj = Robot.rr.trajectoryBuilder(traj.end())
                .splineTo(new Vector2d(15, 65), Math.toRadians(15),
                        SampleMecanumDrive.getVelocityConstraint(35, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
//                .splineTo(new Vector2d(20, -68), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(45, 68), Math.toRadians(-0))
                .splineToConstantHeading(new Vector2d(55, 68), Math.toRadians(-0),
                        SampleMecanumDrive.getVelocityConstraint(15, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker(15, () -> {
//                        ThreadAction(new ResetRR());
                })
                .addDisplacementMarker(30, () -> {
                    Robot.getLift().SetLiftPower(-.1);
                    Robot.getIntake().SetIntake(1);
                    Robot.getIntake().SetPassThrough(1);
                })
                .addDisplacementMarker(45, () -> {
                    Robot.getMecanumDrive().resetRR = false;
                })
                .build();
        Robot.rr.followTrajectory(traj);

        runAction(new Wait(100));

        traj = Robot.rr.trajectoryBuilder(traj.end(), true)
                .splineTo(new Vector2d(18, 68), Math.toRadians(-180))
                .splineTo(new Vector2d(7, 58), Math.toRadians(-110))
                .addTemporalMarker(.25, () -> {
                    Robot.getIntake().SetIntake(-.25);
                    Robot.getIntake().SetPassThrough(-1);
                })
                .addTemporalMarker(1.5, () -> {
                    Robot.getIntake().SetIntake(.1);
                })
                .build();
        Robot.rr.followTrajectory(traj);

        Robot.getDumper().gateState = Dumper.GateState.PARTIAL;
        Robot.getLift().wantedLiftState = Lift.LiftState.NULL;
        runAction(new LiftGoToPos(21, 0, 1500));
        Robot.getDumper().gateState = Dumper.GateState.OPEN;
        runAction(new Wait(300));





        Robot.getLift().wantedLiftState = Lift.LiftState.DOWN;
        Robot.getDumper().gateState = Dumper.GateState.CLOSED;

        traj = Robot.rr.trajectoryBuilder(traj.end())
                .splineTo(new Vector2d(20, 68), Math.toRadians(10))
                .splineToConstantHeading(new Vector2d(45, 68), Math.toRadians(-0))
                .splineToConstantHeading(new Vector2d(55, 68), Math.toRadians(-0),
                        SampleMecanumDrive.getVelocityConstraint(15, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker(15, () -> {
//                        ThreadAction(new ResetRR());
                })
                .addDisplacementMarker(30, () -> {
                    Robot.getLift().SetLiftPower(-.1);
                    Robot.getIntake().SetIntake(1);
                    Robot.getIntake().SetPassThrough(1);
                })
                .addDisplacementMarker(45, () -> {
                    Robot.getMecanumDrive().resetRR = false;
                })
                .build();
        Robot.rr.followTrajectory(traj);

        runAction(new Wait(100));

        traj = Robot.rr.trajectoryBuilder(traj.end(), true)
                .splineTo(new Vector2d(18, 68), Math.toRadians(-180))
                .splineTo(new Vector2d(7, 60), Math.toRadians(-113))
                .addTemporalMarker(.25, () -> {
                    Robot.getIntake().SetIntake(-.25);
                    Robot.getIntake().SetPassThrough(-1);
                })
                .addTemporalMarker(1.5, () -> {
                    Robot.getIntake().SetIntake(.1);
                })
                .build();
        Robot.rr.followTrajectory(traj);

        Robot.getDumper().gateState = Dumper.GateState.PARTIAL;
        Robot.getLift().wantedLiftState = Lift.LiftState.NULL;
        runAction(new LiftGoToPos(21, 0, 1500));
        Robot.getDumper().gateState = Dumper.GateState.OPEN;
        runAction(new Wait(500));





        Robot.getLift().wantedLiftState = Lift.LiftState.DOWN;
        Robot.getDumper().gateState = Dumper.GateState.CLOSED;

        traj = Robot.rr.trajectoryBuilder(traj.end())
                .splineTo(new Vector2d(20, 68), Math.toRadians(10))
                .splineToConstantHeading(new Vector2d(45, 68), Math.toRadians(-0))
                .splineToConstantHeading(new Vector2d(60, 68), Math.toRadians(-0),
                        SampleMecanumDrive.getVelocityConstraint(15, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker(15, () -> {
//                        ThreadAction(new ResetRR());
                })
                .addDisplacementMarker(30, () -> {
                    Robot.getLift().SetLiftPower(-.1);
                    Robot.getIntake().SetIntake(1);
                    Robot.getIntake().SetPassThrough(1);
                })
                .addDisplacementMarker(45, () -> {
                    Robot.getMecanumDrive().resetRR = false;
                })
                .build();
        Robot.rr.followTrajectory(traj);

        runAction(new Wait(100));

        traj = Robot.rr.trajectoryBuilder(traj.end(), true)
                .splineTo(new Vector2d(18, 70), Math.toRadians(-180))
                .splineTo(new Vector2d(7, 60), Math.toRadians(-105))
                .addTemporalMarker(.25, () -> {
                    Robot.getIntake().SetIntake(-.25);
                    Robot.getIntake().SetPassThrough(-1);
                })
                .addTemporalMarker(1.5, () -> {
                    Robot.getIntake().SetIntake(.1);
                })
                .build();
        Robot.rr.followTrajectory(traj);

        Robot.getDumper().gateState = Dumper.GateState.PARTIAL;
        Robot.getLift().wantedLiftState = Lift.LiftState.NULL;
        runAction(new LiftGoToPos(21, 0, 1500));
        Robot.getDumper().gateState = Dumper.GateState.OPEN;
        runAction(new Wait(300));





        Robot.getLift().wantedLiftState = Lift.LiftState.DOWN;
        Robot.getDumper().gateState = Dumper.GateState.CLOSED;

        traj = Robot.rr.trajectoryBuilder(traj.end())
                .splineTo(new Vector2d(20, 68), Math.toRadians(10))
                .splineTo(new Vector2d(52, 68), Math.toRadians(-0))
                .build();
        Robot.rr.followTrajectory(traj);
    }

    @Override
    public StartPosition side() {
        return null;
    }

    @Override
    public void initialize() {

    }
}
