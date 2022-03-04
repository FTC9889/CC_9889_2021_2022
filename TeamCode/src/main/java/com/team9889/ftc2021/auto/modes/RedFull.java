package com.team9889.ftc2021.auto.modes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2021.auto.AutoModeBase;
import com.team9889.ftc2021.auto.actions.drive.DetectWall;
import com.team9889.ftc2021.auto.actions.intake.IntakeStates;
import com.team9889.ftc2021.auto.actions.lift.Score;
import com.team9889.ftc2021.auto.actions.utl.RobotUpdate;
import com.team9889.ftc2021.subsystems.Dumper;
import com.team9889.ftc2021.subsystems.Intake;
import com.team9889.ftc2021.subsystems.Lift;
import com.team9889.lib.roadrunner.drive.DriveConstants;
import com.team9889.lib.roadrunner.drive.SampleMecanumDrive;
import com.team9889.lib.roadrunner.trajectorysequence.TrajectorySequence;

/**
 * Created by Eric on 11/23/2021.
 */

@Autonomous(name = "\uD83D\uDFE5 Red Auto \uD83D\uDFE5", preselectTeleOp = "Teleop")
public class RedFull extends AutoModeBase {
    TrajectorySequence preloaded, intake, intake2, wall, score, park;

    @Override
    public void initialize() {
        Robot.isRed = true;
        Pose2d startPos = new Pose2d(4.5, -61.5, Math.toRadians(-90));
        Robot.rr.setPoseEstimate(startPos);

        preloaded = Robot.rr.trajectorySequenceBuilder(startPos)
                //-----------------
                //|   Preloaded   |
                //-----------------
                // Drive to preloaded
                .setReversed(true)
                .lineToSplineHeading(new Pose2d(8, -57, Math.toRadians(-58)))
                .build();

        wall = Robot.rr.trajectorySequenceBuilder(preloaded.end())
                .addDisplacementMarker(() -> {
                    ThreadAction(new DetectWall());
                    Robot.rrCancelable = true;
                })
                .setReversed(false)
                .lineToSplineHeading(new Pose2d(8, -66, Math.toRadians(-0)))
                .addTemporalMarker(100, () -> {
                    Robot.rr.setPoseEstimate(new Pose2d(Robot.rr.getPoseEstimate().getX(), -63, Robot.rr.getPoseEstimate().getHeading()));
                    Robot.rrCancelable = false;
                })
                .build();

        intake = Robot.rr.trajectorySequenceBuilder(wall.end())
                // Drive to warehouse and Intake
                .addDisplacementMarker(() -> {
                    ThreadAction(new IntakeStates(Intake.IntakeState.ON));
                    Robot.rrCancelable = true;
                })
                .splineToConstantHeading(new Vector2d(18, -66), Math.toRadians(-0))
                .splineToConstantHeading(new Vector2d(50, -66), Math.toRadians(-0),
                        SampleMecanumDrive.getVelocityConstraint(50, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addTemporalMarker(100, () -> {
                    Robot.rrCancelable = false;
                })
                .build();

        intake2 = Robot.rr.trajectorySequenceBuilder(wall.end())
                // Drive to warehouse and Intake
                .addDisplacementMarker(() -> {
                    ThreadAction(new IntakeStates(Intake.IntakeState.ON));
                    Robot.rrCancelable = true;
                })
                .splineToConstantHeading(new Vector2d(18, -66), Math.toRadians(-0))
                .splineToConstantHeading(new Vector2d(48, -60), Math.toRadians(-0),
                        SampleMecanumDrive.getVelocityConstraint(50, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addTemporalMarker(100, () -> {
                    Robot.rrCancelable = false;
                })
                .build();

        score = Robot.rr.trajectorySequenceBuilder(intake.end())
                // Drive to hub
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(35, -70), Math.toRadians(180))
                .splineTo(new Vector2d(7, -70), Math.toRadians(180))
                .splineTo(new Vector2d(0, -62), Math.toRadians(122))
                .addDisplacementMarker(30, () -> {
                    Robot.getIntake().intakeState = Intake.IntakeState.OFF;
                    Robot.getDumper().gateState = Dumper.GateState.PARTIAL;
                })
                .build();

        park = Robot.rr.trajectorySequenceBuilder(score.end())
                //-----------------
                //|     Park      |
                //-----------------
                // Drive to warehouse and Intake
                .setReversed(false)
                .splineTo(new Vector2d(0, -60), Math.toRadians(-0))
                .splineToConstantHeading(new Vector2d(18, -64), Math.toRadians(-0))
                .splineToSplineHeading(new Pose2d(45, -64), Math.toRadians(-0))
                .build();
    }

    @Override
    public void run(StartPosition startPosition, Boxes box) {
        ThreadAction(new RobotUpdate(Robot));
        box = Robot.getCamera().getTSEPos();

        Robot.rr.followTrajectorySequence(preloaded);
        Robot.rr.setPoseEstimate(new Pose2d(0, -52, Robot.rr.getPoseEstimate().getHeading()));

        switch (box) {
            case LEFT:
                runAction(new Score(Lift.LiftState.LAYER1));
                break;
            case MIDDLE:
                runAction(new Score(Lift.LiftState.LAYER2));
                break;
            case RIGHT:
                runAction(new Score(Lift.LiftState.LAYER3));
                break;
        }

        Robot.rr.followTrajectorySequence(wall);
        Robot.rr.followTrajectorySequence(intake);
        Robot.rr.followTrajectorySequence(score);
        Robot.rr.setPoseEstimate(new Pose2d(0, -52, Robot.rr.getPoseEstimate().getHeading()));
        runAction(new Score(Lift.LiftState.LAYER3));
        Robot.rr.followTrajectorySequence(wall);
        Robot.rr.followTrajectorySequence(intake);
        Robot.rr.followTrajectorySequence(score);
        Robot.rr.setPoseEstimate(new Pose2d(0, -52, Robot.rr.getPoseEstimate().getHeading()));
        runAction(new Score(Lift.LiftState.LAYER3));
        Robot.rr.followTrajectorySequence(wall);
        Robot.rr.followTrajectorySequence(intake2);
        Robot.rr.followTrajectorySequence(score);
        Robot.rr.setPoseEstimate(new Pose2d(0, -52, Robot.rr.getPoseEstimate().getHeading()));
        runAction(new Score(Lift.LiftState.LAYER3));
        Robot.rr.followTrajectorySequence(wall);
        Robot.rr.followTrajectorySequence(intake2);
        Robot.rr.followTrajectorySequence(score);
        Robot.rr.setPoseEstimate(new Pose2d(0, -52, Robot.rr.getPoseEstimate().getHeading()));
        runAction(new Score(Lift.LiftState.LAYER3));
        Robot.rr.followTrajectorySequence(wall);
        Robot.rr.followTrajectorySequence(intake);
    }

    @Override
    public StartPosition side() {
        return null;
    }
}
