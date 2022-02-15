package com.team9889.ftc2021.auto.modes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2021.auto.AutoModeBase;
import com.team9889.ftc2021.auto.actions.intake.IntakeStates;
import com.team9889.ftc2021.auto.actions.lift.Score;
import com.team9889.ftc2021.auto.actions.utl.RobotUpdate;
import com.team9889.ftc2021.subsystems.Dumper;
import com.team9889.ftc2021.subsystems.Intake;
import com.team9889.ftc2021.subsystems.Lift;
import com.team9889.lib.roadrunner.trajectorysequence.TrajectorySequence;

/**
 * Created by Eric on 11/23/2021.
 */

@Autonomous(name = "\uD83D\uDFE5 Red Auto \uD83D\uDFE5", preselectTeleOp = "Teleop")
public class RedFull extends AutoModeBase {
    TrajectorySequence preloaded, firstCycle, secondCycle, thirdCycle, fourthCycle, park;

    @Override
    public void initialize() {
        Pose2d startPos = new Pose2d(7, -61.5, Math.toRadians(-90));
        Robot.rr.setPoseEstimate(startPos);

        preloaded = Robot.rr.trajectorySequenceBuilder(startPos)
                //-----------------
                //|   Preloaded   |
                //-----------------
                // Drive to preloaded
                .setReversed(true)
                .lineToSplineHeading(new Pose2d(-5, -59, Math.toRadians(-80)))
                .build();

        firstCycle = Robot.rr.trajectorySequenceBuilder(preloaded.end())
                //-----------------
                //|   1st Cycle   |
                //-----------------
                // Drive to warehouse and Intake
                .setReversed(false)
                .splineTo(new Vector2d(0, -60), Math.toRadians(-10))
                .splineToConstantHeading(new Vector2d(18, -64), Math.toRadians(-0))
                .splineToSplineHeading(new Pose2d(48, -64), Math.toRadians(-0))
                .addDisplacementMarker(40, () -> {
                    runAction(new IntakeStates(Intake.IntakeState.ON));
                })
                .addDisplacementMarker(70, () -> {
                    runAction(new IntakeStates(Intake.IntakeState.OUT));
                })
                .addDisplacementMarker(75, () -> {
                    runAction(new IntakeStates(Intake.IntakeState.OFF));
                })
                .addDisplacementMarker(80, () -> {
                    runAction(new IntakeStates(Intake.IntakeState.OUT));
                })
                .addDisplacementMarker(100, () -> {
                    runAction(new IntakeStates(Intake.IntakeState.OFF));
                    Robot.getDumper().gateState = Dumper.GateState.PARTIAL;
                })

                // Drive to hub
                .setReversed(true)
                .splineTo(new Vector2d(15, -64), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(5, -60), Math.toRadians(180))
                .splineTo(new Vector2d(-3, -57), Math.toRadians(100))
                .build();

        secondCycle = Robot.rr.trajectorySequenceBuilder(firstCycle.end())
                //-----------------
                //|   2nd Cycle   |
                //-----------------
                // Drive to warehouse and Intake
                .setReversed(false)
                .splineTo(new Vector2d(0, -60), Math.toRadians(-0))
                .splineToConstantHeading(new Vector2d(18, -64), Math.toRadians(-0))
                .splineToSplineHeading(new Pose2d(52, -64), Math.toRadians(-0))
                .addDisplacementMarker(40, () -> {
                    runAction(new IntakeStates(Intake.IntakeState.ON));
                })
                .addDisplacementMarker(70, () -> {
                    runAction(new IntakeStates(Intake.IntakeState.OUT));
                })
                .addDisplacementMarker(75, () -> {
                    runAction(new IntakeStates(Intake.IntakeState.OFF));
                })
                .addDisplacementMarker(80, () -> {
                    runAction(new IntakeStates(Intake.IntakeState.OUT));
                })
                .addDisplacementMarker(100, () -> {
                    runAction(new IntakeStates(Intake.IntakeState.OFF));
                    Robot.getDumper().gateState = Dumper.GateState.PARTIAL;
                })

                // Drive to hub
                .setReversed(true)
                .splineTo(new Vector2d(15, -64), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(5, -60), Math.toRadians(180))
                .splineTo(new Vector2d(-3, -57), Math.toRadians(100))
                .build();

        thirdCycle = Robot.rr.trajectorySequenceBuilder(secondCycle.end())
                //-----------------
                //|   3rd Cycle   |
                //-----------------
                // Drive to warehouse and Intake
                .setReversed(false)
                .splineTo(new Vector2d(0, -60), Math.toRadians(-0))
                .splineToConstantHeading(new Vector2d(18, -64), Math.toRadians(-0))
                .splineToSplineHeading(new Pose2d(60, -64), Math.toRadians(-0))
                .addDisplacementMarker(40, () -> {
                    runAction(new IntakeStates(Intake.IntakeState.ON));
                })
                .addDisplacementMarker(70, () -> {
                    runAction(new IntakeStates(Intake.IntakeState.OUT));
                })
                .addDisplacementMarker(75, () -> {
                    runAction(new IntakeStates(Intake.IntakeState.OFF));
                })
                .addDisplacementMarker(80, () -> {
                    runAction(new IntakeStates(Intake.IntakeState.OUT));
                })
                .addDisplacementMarker(100, () -> {
                    runAction(new IntakeStates(Intake.IntakeState.OFF));
                    Robot.getDumper().gateState = Dumper.GateState.PARTIAL;
                })

                // Drive to hub
                .setReversed(true)
                .splineTo(new Vector2d(15, -64), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(5, -60), Math.toRadians(180))
                .splineTo(new Vector2d(-3, -57), Math.toRadians(100))
                .build();

        park = Robot.rr.trajectorySequenceBuilder(thirdCycle.end())
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

        //4.5 56
        Robot.rr.followTrajectorySequence(preloaded);
        Robot.rr.setPoseEstimate(new Pose2d(-4.5, -56, Robot.rr.getPoseEstimate().getHeading()));
        runAction(new Score(Lift.LiftState.LAYER3));
        Robot.rr.followTrajectorySequence(firstCycle);
        Robot.rr.setPoseEstimate(new Pose2d(-8, -58, Robot.rr.getPoseEstimate().getHeading()));
        runAction(new Score(Lift.LiftState.LAYER3));
        Robot.rr.followTrajectorySequence(secondCycle);
        Robot.rr.setPoseEstimate(new Pose2d(-8, -58, Robot.rr.getPoseEstimate().getHeading()));
        runAction(new Score(Lift.LiftState.LAYER3));
        Robot.rr.followTrajectorySequence(thirdCycle);
        Robot.rr.setPoseEstimate(new Pose2d(-8, -58, Robot.rr.getPoseEstimate().getHeading()));
        runAction(new Score(Lift.LiftState.LAYER3));
        Robot.rr.followTrajectorySequence(park);
    }

    @Override
    public StartPosition side() {
        return null;
    }
}
