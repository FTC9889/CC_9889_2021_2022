package com.team9889.ftc2021.auto.modes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2021.auto.AutoModeBase;
import com.team9889.ftc2021.auto.actions.ActionVariables;
import com.team9889.ftc2021.auto.actions.drive.DriveIntoWall;
import com.team9889.ftc2021.auto.actions.drive.DriveUntilIntake;
import com.team9889.ftc2021.auto.actions.drive.PurePursuit;
import com.team9889.ftc2021.auto.actions.drive.PurePursuitRobotCentric;
import com.team9889.ftc2021.auto.actions.drive.SetUltrasonicPose;
import com.team9889.ftc2021.auto.actions.drive.Turn;
import com.team9889.ftc2021.auto.actions.intake.Outtake;
import com.team9889.ftc2021.auto.actions.intake.StopDriveAfterIntake;
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

        // Score
        Lift.LiftState level = Lift.LiftState.LAYER3;
        switch (box) {
            case RIGHT:
                break;
            case MIDDLE:
                level = Lift.LiftState.LAYER2;
                break;

            case LEFT:
                level = Lift.LiftState.LAYER1;
                break;
        }

        // Drive to Hub
        path.add(new Pose(5, -52, -90, 1, 0, 6));
        path.add(new Pose(5, -49, -60, 1, 0, 6));
        runAction(new PurePursuit(path));
        path.clear();

        ThreadAction(new Outtake(0, -1));

        Robot.getLift().cycleAuto = true;
        runAction(new Score(level, false));
        Robot.getLift().cycleAuto = false;

//        runAction(new Wait(400));
//        ActionVariables.readyToScore = true;

//        while (!ActionVariables.readyToScore && opModeIsActive()) {
//            Thread.yield();
//        }

        runAction(new Wait(300));

        Intake.down = 0.58;

        // Drive into Warehouse and intake
        for (int cycle = 1; cycle <= 3; cycle++) {
            Robot.getIntake().loadState = Intake.LoadState.INTAKE;

//            path.add(new Pose(15, -61, 0, 0.3, 0, 10));
//            path.add(new Pose(30, -61, 0, 0.3, 0, 10));
//            path.add(new Pose(45 + (cycle - 1) * 3, -60, 0, 0.3, 0, 10));
//            path.add(new Pose(70, -61, 0, .15, 0, 10));
//            runAction(new PurePursuitRobotCentric(path, new Pose(6, 6, 5), true));
//            path.clear();

            path.add(new Pose(10, -64, 0, 0.5, 0, 6));
            path.add(new Pose(35, -64, 0, 0.5, 0, 6));
            path.add(new Pose(60, -64, 0, 0.15, 0, 6));
            runAction(new PurePursuit(path, new Pose(6, 6, 5), true));
            path.clear();


            if (Robot.rr.getPoseEstimate().getX() > 20) {
                runAction(new Turn(5, 1));
                runAction(new SetUltrasonicPose(SetUltrasonicPose.Position.WH_CYCLE, 4));

                // Drive to Hub
                path.add(new Pose(30, -66, 5, 1, 0, 6));
                path.add(new Pose(15, -66, 5, 1, 0, 6));
            }
            path.add(new Pose(12, -49, -60, 1, 0, 12));
            runAction(new PurePursuit(path));
            path.clear();

            runAction(new Wait(200));

            runAction(new Score(Lift.LiftState.LAYER3, false));
        }

        runAction(new Wait(300));

        Robot.getIntake().loadState = Intake.LoadState.OFF;
        Intake.down = 0.7;

//        path.add(new Pose(15, -60, 0, 0.6, 0, 10));
//        path.add(new Pose(30, -60, 0, 0.6, 0, 10));
//        path.add(new Pose(55, -62, 0, 0.6, 0, 10));
//        runAction(new PurePursuitRobotCentric(path, new Pose(6, 6, 5)));
//        path.clear();

        path.add(new Pose(10, -64, 0, 0.5, 0, 6));
        path.add(new Pose(50, -64, 0, 0.5, 0, 6));
        runAction(new PurePursuit(path, new Pose(6, 6, 5)));
        path.clear();
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
