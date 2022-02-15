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
 * Created by Eric on 11/13/2021.
 */

//@Disabled
@Autonomous(group = "Test")
public class Test extends AutoModeBase {
    TrajectorySequence preloaded, firstCycle, secondCycle, thirdCycle, fourthCycle, park;

    @Override
    public void initialize() {
        Pose2d startPos = new Pose2d(7, -63, Math.toRadians(-90));
        Robot.rr.setPoseEstimate(startPos);

        preloaded = Robot.rr.trajectorySequenceBuilder(startPos)
                // Drive to preloaded
                .setReversed(true)
                .lineToSplineHeading(new Pose2d(7, -55, Math.toRadians(-60)))
                .build();

        firstCycle = Robot.rr.trajectorySequenceBuilder(preloaded.end())
                // Drive to warehouse and Intake
                .setReversed(false)
                .lineToSplineHeading(new Pose2d(15, -64, Math.toRadians(0)))
                .lineToSplineHeading(new Pose2d(55, -64, Math.toRadians(0)))
                .addDisplacementMarker(30, () -> {
                    runAction(new IntakeStates(Intake.IntakeState.ON));
                })

                // Drive to hub
                .setReversed(true)
                .addDisplacementMarker(75, () -> {
                    runAction(new IntakeStates(Intake.IntakeState.OUT));
                })
                .splineTo(new Vector2d(15, -64), Math.toRadians(180))
                .splineTo(new Vector2d(7, -57), Math.toRadians(120))
                .addDisplacementMarker(85, () -> {
                    Robot.getDumper().gateState = Dumper.GateState.PARTIAL;
                    runAction(new IntakeStates(Intake.IntakeState.OFF));
                })
                .build();

        secondCycle = Robot.rr.trajectorySequenceBuilder(firstCycle.end())
                // Drive to warehouse and Intake
                .setReversed(false)
//                .splineToSplineHeading(new Pose2d(10, -65), Math.toRadians(0))
//                .splineToSplineHeading(new Pose2d(45, -65), Math.toRadians(0))
//                .splineToSplineHeading(new Pose2d(55, -65, Math.toRadians(0)), Math.toRadians(0))
                .lineToSplineHeading(new Pose2d(15, -66, Math.toRadians(0)))
                .lineToSplineHeading(new Pose2d(55, -66, Math.toRadians(0)))
                .addDisplacementMarker(30, () -> {
                    runAction(new IntakeStates(Intake.IntakeState.ON));
                })

                // Drive to hub
                .setReversed(true)
                .addDisplacementMarker(75, () -> {
                    runAction(new IntakeStates(Intake.IntakeState.OUT));
                })
                .splineTo(new Vector2d(15, -66), Math.toRadians(180))
                .splineTo(new Vector2d(-3, -59), Math.toRadians(100))
                .addDisplacementMarker(85, () -> {
                    Robot.getDumper().gateState = Dumper.GateState.PARTIAL;
                    runAction(new IntakeStates(Intake.IntakeState.OFF));
                })
                .build();

        thirdCycle = Robot.rr.trajectorySequenceBuilder(secondCycle.end())
                // Drive to warehouse and Intake
                .setReversed(false)
//                .splineToSplineHeading(new Pose2d(10, -65), Math.toRadians(0))
//                .splineToSplineHeading(new Pose2d(45, -65), Math.toRadians(0))
//                .splineToSplineHeading(new Pose2d(55, -65, Math.toRadians(0)), Math.toRadians(0))
                .lineToSplineHeading(new Pose2d(15, -64, Math.toRadians(0)))
                .lineToSplineHeading(new Pose2d(55, -64, Math.toRadians(0)))
                .addDisplacementMarker(30, () -> {
                    runAction(new IntakeStates(Intake.IntakeState.ON));
                })

                // Drive to hub
                .setReversed(true)
                .addDisplacementMarker(75, () -> {
                    runAction(new IntakeStates(Intake.IntakeState.OUT));
                })
                .splineTo(new Vector2d(15, -64), Math.toRadians(180))
                .splineTo(new Vector2d(-3, -55), Math.toRadians(100))
                .addDisplacementMarker(85, () -> {
                    Robot.getDumper().gateState = Dumper.GateState.PARTIAL;
                    runAction(new IntakeStates(Intake.IntakeState.OFF));
                })
                .build();

        fourthCycle = Robot.rr.trajectorySequenceBuilder(thirdCycle.end())
                // Drive to warehouse and Intake
                .setReversed(false)
//                .splineToSplineHeading(new Pose2d(10, -65), Math.toRadians(0))
//                .splineToSplineHeading(new Pose2d(45, -65), Math.toRadians(0))
//                .splineToSplineHeading(new Pose2d(55, -65, Math.toRadians(0)), Math.toRadians(0))
                .lineToSplineHeading(new Pose2d(15, -64, Math.toRadians(0)))
                .lineToSplineHeading(new Pose2d(55, -64, Math.toRadians(0)))
                .addDisplacementMarker(30, () -> {
                    runAction(new IntakeStates(Intake.IntakeState.ON));
                })

                // Drive to hub
                .setReversed(true)
                .addDisplacementMarker(75, () -> {
                    runAction(new IntakeStates(Intake.IntakeState.OUT));
                })
                .splineTo(new Vector2d(15, -64), Math.toRadians(180))
                .splineTo(new Vector2d(-3, -55), Math.toRadians(100))
                .addDisplacementMarker(85, () -> {
                    Robot.getDumper().gateState = Dumper.GateState.PARTIAL;
                    runAction(new IntakeStates(Intake.IntakeState.OFF));
                })
                .build();

        park = Robot.rr.trajectorySequenceBuilder(fourthCycle.end())
                // Drive to warehouse and Intake
                .setReversed(false)
                .splineTo(new Vector2d(45, -73), Math.toRadians(0))
                .build();
    }

    @Override
    public void run(StartPosition startPosition, Boxes box) {
        ThreadAction(new RobotUpdate(Robot));

        Robot.rr.followTrajectorySequence(preloaded);
        runAction(new Score(Lift.LiftState.LAYER3));
        Robot.rr.followTrajectorySequence(firstCycle);
        runAction(new Score(Lift.LiftState.LAYER3));
        Robot.rr.followTrajectorySequence(secondCycle);
        runAction(new Score(Lift.LiftState.LAYER3));
        Robot.rr.followTrajectorySequence(thirdCycle);
        runAction(new Score(Lift.LiftState.LAYER3));
        Robot.rr.followTrajectorySequence(fourthCycle);
        runAction(new Score(Lift.LiftState.LAYER3));
        Robot.rr.followTrajectorySequence(park);
    }

    @Override
    public StartPosition side() {
        return null;
    }
}
