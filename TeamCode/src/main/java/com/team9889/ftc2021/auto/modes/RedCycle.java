package com.team9889.ftc2021.auto.modes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2021.auto.AutoModeBase;
import com.team9889.ftc2021.auto.actions.drive.DriveIntoWall;
import com.team9889.ftc2021.auto.actions.drive.DriveUntilIntake;
import com.team9889.ftc2021.auto.actions.drive.PurePursuit;
import com.team9889.ftc2021.auto.actions.drive.SetUltrasonicPose;
import com.team9889.ftc2021.auto.actions.lift.Score;
import com.team9889.ftc2021.auto.actions.utl.Wait;
import com.team9889.ftc2021.subsystems.Intake;
import com.team9889.ftc2021.subsystems.Lift;
import com.team9889.lib.Pose;

import java.util.ArrayList;

/**
 * Created by Eric on 5/27/2022.
 */

@Autonomous(name = "\uD83D\uDFE5 ♻ Red Cycle ♻ \uD83D\uDFE5", preselectTeleOp = "Teleop")
public class RedCycle extends AutoModeBase {
    @Override
    public void run(StartPosition startPosition, Boxes box) {
        ArrayList<Pose> path = new ArrayList<>();

        runAction(new Wait(timeToWait));

        // Drive to Hub
        path.add(new Pose(5, -52, -90, 1, 0, 6));
        path.add(new Pose(6, -52, -60, 1, 0, 6));
        runAction(new PurePursuit(path));
        path.clear();

        // Score
        switch (box) {
            case RIGHT:
                runAction(new Score(Lift.LiftState.LAYER3, false));
                break;

            case MIDDLE:
                runAction(new Score(Lift.LiftState.LAYER2, false));
                break;

            case LEFT:
                runAction(new Score(Lift.LiftState.LAYER1, false));
                break;
        }

        // Drive into Warehouse and intake
        Robot.getIntake().loadState = Intake.LoadState.OUTTAKE;

        path.add(new Pose(8, -57, 0, .4, 0, 6));
        path.add(new Pose(10, -60, 0, .4, 0, 6));
        path.add(new Pose(12, -63, 0, .4, 0, 6));
        path.add(new Pose(14, -66, 0, .4, 0, 6));
        runAction(new PurePursuit(path));
        path.clear();

//        runAction(new DriveIntoWall());
//        Robot.rr.setPoseEstimate(new Pose2d(8, -66, Robot.rr.getPoseEstimate().getHeading()));

        Robot.getIntake().loadState = Intake.LoadState.INTAKE;

        runAction(new DriveUntilIntake());
        runAction(new SetUltrasonicPose(SetUltrasonicPose.Position.WH_CYCLE, 4));

        // Drive to Hub
        path.add(new Pose(10, -66, 0, 1, 0, 6));
        path.add(new Pose(8, -55, -60, 1, 0, 12));
        runAction(new PurePursuit(path));
        path.clear();

        runAction(new Score(Lift.LiftState.LAYER3, false));

        runAction(new Wait(1000));
    }

    @Override
    public StartPosition side() {
        return StartPosition.RED_CYCLE;
    }

    @Override
    public void initialize() {
        Pose2d startPos = new Pose2d(5, -61.375, Math.toRadians(-90));
        Robot.rr.setPoseEstimate(startPos);
    }
}
