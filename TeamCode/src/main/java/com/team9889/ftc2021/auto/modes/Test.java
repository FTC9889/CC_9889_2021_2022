package com.team9889.ftc2021.auto.modes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2021.auto.AutoModeBase;
import com.team9889.ftc2021.auto.actions.intake.IntakeStates;
import com.team9889.ftc2021.auto.actions.utl.RobotUpdate;
import com.team9889.ftc2021.subsystems.Intake;
import com.team9889.lib.roadrunner.trajectorysequence.TrajectorySequence;

/**
 * Created by Eric on 11/13/2021.
 */

//@Disabled
@Autonomous(group = "Test")
public class Test extends AutoModeBase {
    TrajectorySequence test, test2, secondCycle, thirdCycle, fourthCycle, park;

    @Override
    public void initialize() {
        Robot.isRed = true;
        Pose2d startPos = new Pose2d(7, -61.5, Math.toRadians(90));
        Robot.rr.setPoseEstimate(startPos);

        test = Robot.rr.trajectorySequenceBuilder(startPos)
                //-----------------
                //|   Preloaded   |
                //-----------------
                // Drive to preloaded
                .lineToSplineHeading(new Pose2d(7, -10, Math.toRadians(90)))
                .build();

        test2 = Robot.rr.trajectorySequenceBuilder(startPos)
                //-----------------
                //|   Preloaded   |
                //-----------------
                // Drive to preloaded
                .setReversed(true)
                .lineToSplineHeading(new Pose2d(7, -60, Math.toRadians(90)))
                .build();
    }

    @Override
    public void run(StartPosition startPosition, Boxes box) {
        ThreadAction(new RobotUpdate(Robot));
        ThreadAction(new IntakeStates(Intake.IntakeState.ON));

        Robot.rr.followTrajectorySequence(test);
        Robot.rr.followTrajectorySequence(test2);
    }

    @Override
    public StartPosition side() {
        return null;
    }
}
