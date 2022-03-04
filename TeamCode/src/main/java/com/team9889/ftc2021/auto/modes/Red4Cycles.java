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
 * Created by Eric on 3/4/2022.
 */

@Autonomous(name = "\uD83D\uDFE5 Red 4 Cycles \uD83D\uDFE5", preselectTeleOp = "Teleop")
public class Red4Cycles extends AutoModeBase {
    TrajectorySequence preloaded, intake, intake2, wall, score, park;

    @Override
    public void initialize() {
        Robot.isRed = true;
        Pose2d startPos = new Pose2d(5.125, -61.375, Math.toRadians(-90));
        Robot.rr.setPoseEstimate(startPos);

        preloaded = Robot.rr.trajectorySequenceBuilder(startPos)
                //-----------------
                //|   Preloaded   |
                //-----------------
                .setReversed(true)
                .lineToSplineHeading(new Pose2d(4.875, -53, Math.toRadians(-58)))
                .build();

        wall = Robot.rr.trajectorySequenceBuilder(preloaded.end())
                .addDisplacementMarker(() -> {
                    ThreadAction(new DetectWall());
                    Robot.rrCancelable = true;
                })
                .setReversed(false)
                .splineToLinearHeading(new Pose2d(18, -65, Math.toRadians(-0)), Math.toRadians(-0))
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
                .splineToConstantHeading(new Vector2d(60, -65), Math.toRadians(-0),
                        SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
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
//                .splineToConstantHeading(new Vector2d(18, -65), Math.toRadians(-0))
                .splineToConstantHeading(new Vector2d(60, -56), Math.toRadians(-0))
                .addTemporalMarker(100, () -> {
                    Robot.rrCancelable = false;
                })
                .build();

        score = Robot.rr.trajectorySequenceBuilder(intake.end())
                // Drive to hub
                .setReversed(true)
                .splineTo(new Vector2d(35, -65), Math.toRadians(180))
                .splineTo(new Vector2d(18, -65), Math.toRadians(180))
                .splineTo(new Vector2d(6, -55), Math.toRadians(122))
                .addDisplacementMarker(30, () -> {
                    Robot.getIntake().intakeState = Intake.IntakeState.OFF;
                    Robot.getDumper().gateState = Dumper.GateState.PARTIAL;
                })
                .build();

        park = Robot.rr.trajectorySequenceBuilder(wall.end())
                .splineToConstantHeading(new Vector2d(40, -65), Math.toRadians(-0))
                .build();
    }

    @Override
    public void run(StartPosition startPosition, Boxes box) {
        ThreadAction(new RobotUpdate(Robot));
        box = Robot.getCamera().getTSEPos();

        Robot.rr.followTrajectorySequence(preloaded);
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
        Robot.rr.setPoseEstimate(new Pose2d(3.5, -51, Robot.rr.getPoseEstimate().getHeading()));
        runAction(new Score(Lift.LiftState.LAYER3));

        Robot.rr.followTrajectorySequence(wall);
        Robot.rr.followTrajectorySequence(intake);
        Robot.rr.followTrajectorySequence(score);
        Robot.rr.setPoseEstimate(new Pose2d(3.5, -51, Robot.rr.getPoseEstimate().getHeading()));
        runAction(new Score(Lift.LiftState.LAYER3));

        Robot.rr.followTrajectorySequence(wall);
        Robot.rr.followTrajectorySequence(intake2);
        Robot.rr.followTrajectorySequence(score);
        Robot.rr.setPoseEstimate(new Pose2d(3.5, -51, Robot.rr.getPoseEstimate().getHeading()));
        runAction(new Score(Lift.LiftState.LAYER3));

        Robot.rr.followTrajectorySequence(wall);
        Robot.rr.followTrajectorySequence(intake2);
        Robot.rr.followTrajectorySequence(score);
        Robot.rr.setPoseEstimate(new Pose2d(3.5, -51, Robot.rr.getPoseEstimate().getHeading()));
        runAction(new Score(Lift.LiftState.LAYER3));

        Robot.rr.followTrajectorySequence(wall);
        Robot.rr.followTrajectorySequence(park);
    }

    @Override
    public StartPosition side() {
        return null;
    }
}
