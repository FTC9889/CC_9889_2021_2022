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
    TrajectorySequence preloaded, intake, intake2, intake3, intake4, wall, score, score2, park;

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
                .lineToSplineHeading(new Pose2d(4.875, -53, Math.toRadians(-63.5)))
                .build();

        wall = Robot.rr.trajectorySequenceBuilder(preloaded.end())
                .addDisplacementMarker(() -> {
                    ThreadAction(new DetectWall());
                    Robot.rrCancelable = true;
                })
                .setReversed(false)
                .splineToLinearHeading(new Pose2d(18, -67, Math.toRadians(-0)), Math.toRadians(-0))
                .addTemporalMarker(100, () -> {
                    Robot.rr.setPoseEstimate(new Pose2d(Robot.rr.getPoseEstimate().getX(), -63, Robot.rr.getPoseEstimate().getHeading()));
                    Robot.rrCancelable = false;
                })
                .build();

        intake = Robot.rr.trajectorySequenceBuilder(wall.end())
                // Drive to warehouse and Intake
                .addDisplacementMarker(() -> {
                    Robot.rrCancelable = true;
                })
//                .splineToConstantHeading(new Vector2d(24, -67), Math.toRadians(-0))
                .splineToConstantHeading(new Vector2d(57, -67), Math.toRadians(-0),
                        SampleMecanumDrive.getVelocityConstraint(22, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addTemporalMarker(100, () -> {
                    Robot.rrCancelable = false;
                })
                .build();

        intake2 = Robot.rr.trajectorySequenceBuilder(wall.end())
                // Drive to warehouse and Intake
                .addDisplacementMarker(() -> {
                    Robot.rrCancelable = true;
                })
//                .splineToConstantHeading(new Vector2d(24, -67), Math.toRadians(-0))
                .splineToConstantHeading(new Vector2d(56, -67), Math.toRadians(-0),
                        SampleMecanumDrive.getVelocityConstraint(22, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addTemporalMarker(100, () -> {
                    Robot.rrCancelable = false;
                })
                .build();

        intake3 = Robot.rr.trajectorySequenceBuilder(wall.end())
                // Drive to warehouse and Intake
                .addDisplacementMarker(() -> {
                    ThreadAction(new IntakeStates(Intake.IntakeState.ON));
                    Robot.rrCancelable = true;
                })
                .splineToConstantHeading(new Vector2d(46, -56), Math.toRadians(-0))
                .addTemporalMarker(100, () -> {
                    Robot.rrCancelable = false;
                })
                .build();

        intake4 = Robot.rr.trajectorySequenceBuilder(wall.end())
                // Drive to warehouse and Intake
                .addDisplacementMarker(() -> {
                    ThreadAction(new IntakeStates(Intake.IntakeState.ON));
                    Robot.rrCancelable = true;
                })
                .splineToConstantHeading(new Vector2d(48, -56), Math.toRadians(-0))
                .addTemporalMarker(100, () -> {
                    Robot.rrCancelable = false;
                })
                .build();

        score = Robot.rr.trajectorySequenceBuilder(intake.end().plus(new Pose2d(-30)))
                // Drive to hub
                .setReversed(true)
//                .splineTo(new Vector2d(35, -67), Math.toRadians(180))
                .splineTo(new Vector2d(18, -67), Math.toRadians(180))
                .splineTo(new Vector2d(7, -58), Math.toRadians(126))
                .addDisplacementMarker(30, () -> {
                    Robot.getIntake().intakeState = Intake.IntakeState.OFF;
                    Robot.getDumper().gateState = Dumper.GateState.PARTIAL;
                })
                .build();

        score2 = Robot.rr.trajectorySequenceBuilder(intake.end())
                // Drive to hub
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(35, -71), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(18, -71), Math.toRadians(180))
                .splineTo(new Vector2d(5, -60), Math.toRadians(126))
                .addDisplacementMarker(30, () -> {
                    Robot.getIntake().intakeState = Intake.IntakeState.OFF;
                    Robot.getDumper().gateState = Dumper.GateState.PARTIAL;
                })
                .build();

        park = Robot.rr.trajectorySequenceBuilder(wall.end())
                .splineToConstantHeading(new Vector2d(40, -66), Math.toRadians(-0))
                .build();
    }

    @Override
    public void run(StartPosition startPosition, Boxes box) {
        ThreadAction(new RobotUpdate(Robot));
        box = Robot.getCamera().getTSEPos();

        Robot.rr.followTrajectorySequence(preloaded);
        switch (box) {
            case LEFT:
                runAction(new Score(Lift.LiftState.LAYER1, true));
                break;
            case MIDDLE:
                runAction(new Score(Lift.LiftState.LAYER2, true));
                break;
            case RIGHT:
                runAction(new Score(Lift.LiftState.LAYER3, true));
                break;
        }

        Robot.rr.followTrajectorySequence(wall);
        ThreadAction(new IntakeStates(Intake.IntakeState.ON));
        Robot.rr.followTrajectorySequence(intake);

        score = Robot.rr.trajectorySequenceBuilder(Robot.rr.getPoseEstimate())
                // Drive to hub
                .setReversed(true)
                .splineTo(new Vector2d(18, -67), Math.toRadians(180))
                .splineTo(new Vector2d(7, -58), Math.toRadians(122))
                .addDisplacementMarker(30, () -> {
                    Robot.getIntake().intakeState = Intake.IntakeState.OFF;
                    Robot.getDumper().gateState = Dumper.GateState.PARTIAL;
                })
                .build();

        Robot.rr.followTrajectorySequence(score);
        Robot.rr.setPoseEstimate(new Pose2d(3.5, -51, Robot.rr.getPoseEstimate().getHeading()));
        runAction(new Score(Lift.LiftState.LAYER3));

        Robot.rr.followTrajectorySequence(wall);
        ThreadAction(new IntakeStates(Intake.IntakeState.ON));
        Robot.rr.followTrajectorySequence(intake2);

        score = Robot.rr.trajectorySequenceBuilder(Robot.rr.getPoseEstimate())
                // Drive to hub
                .setReversed(true)
                .splineTo(new Vector2d(18, -67), Math.toRadians(180))
                .splineTo(new Vector2d(7, -58), Math.toRadians(122))
                .addDisplacementMarker(30, () -> {
                    Robot.getIntake().intakeState = Intake.IntakeState.OFF;
                    Robot.getDumper().gateState = Dumper.GateState.PARTIAL;
                })
                .build();

        Robot.rr.followTrajectorySequence(score);
        Robot.rr.setPoseEstimate(new Pose2d(3.5, -51, Robot.rr.getPoseEstimate().getHeading()));
        runAction(new Score(Lift.LiftState.LAYER3));

        Robot.rr.followTrajectorySequence(wall);
        Robot.rr.followTrajectorySequence(intake3);

        score = Robot.rr.trajectorySequenceBuilder(Robot.rr.getPoseEstimate())
                // Drive to hub
                .setReversed(true)
                .splineTo(new Vector2d(18, -67), Math.toRadians(180))
                .splineTo(new Vector2d(7, -58), Math.toRadians(122))
                .addDisplacementMarker(30, () -> {
                    Robot.getIntake().intakeState = Intake.IntakeState.OFF;
                    Robot.getDumper().gateState = Dumper.GateState.PARTIAL;
                })
                .build();

        Robot.rr.followTrajectorySequence(score2);
        Robot.rr.setPoseEstimate(new Pose2d(3.5, -51, Robot.rr.getPoseEstimate().getHeading()));
        runAction(new Score(Lift.LiftState.LAYER3));

        Robot.rr.followTrajectorySequence(wall);
        Robot.rr.followTrajectorySequence(intake4);

        score = Robot.rr.trajectorySequenceBuilder(Robot.rr.getPoseEstimate())
                // Drive to hub
                .setReversed(true)
                .splineTo(new Vector2d(18, -67), Math.toRadians(180))
                .splineTo(new Vector2d(7, -58), Math.toRadians(122))
                .addDisplacementMarker(30, () -> {
                    Robot.getIntake().intakeState = Intake.IntakeState.OFF;
                    Robot.getDumper().gateState = Dumper.GateState.PARTIAL;
                })
                .build();

        Robot.rr.followTrajectorySequence(score2);
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
