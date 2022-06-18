package com.team9889.ftc2021.auto.modes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2021.auto.AutoModeBase;
import com.team9889.ftc2021.auto.actions.ActionVariables;
import com.team9889.ftc2021.auto.actions.carousel.SpinDuck;
import com.team9889.ftc2021.auto.actions.drive.DriveTillCarousel;
import com.team9889.ftc2021.auto.actions.drive.duck.DriveToDuck;
import com.team9889.ftc2021.auto.actions.drive.PurePursuit;
import com.team9889.ftc2021.auto.actions.drive.SetUltrasonicPose;
import com.team9889.ftc2021.auto.actions.drive.duck.TurnUntilDuck;
import com.team9889.ftc2021.auto.actions.intake.DriveAndIntake;
import com.team9889.ftc2021.auto.actions.lift.Score;
import com.team9889.ftc2021.auto.actions.lift.ScoreNextToHub;
import com.team9889.ftc2021.auto.actions.utl.Wait;
import com.team9889.ftc2021.subsystems.Intake;
import com.team9889.ftc2021.subsystems.Lift;
import com.team9889.lib.Pose;

import java.util.ArrayList;

/**
 * Created by Eric on 5/27/2022.
 */

@Autonomous(name = "\uD83D\uDFE5 \uD83E\uDD86 ♻ Red Duck & Cycle ♻ \uD83E\uDD86 \uD83D\uDFE5", preselectTeleOp = "Teleop")
public class RedDuckCycle extends AutoModeBase {
    @Override
    public void run(StartPosition startPosition, Boxes box) {
        ArrayList<Pose> path = new ArrayList<>();

        path.add(new Pose(-38, -55, -90, 1, 0, 6));
        path.add(new Pose(-32, -49, -127, 1, 0, 12));
        runAction(new PurePursuit(path));
        path.clear();

        runAction(new Score(Lift.LiftState.LAYER3, false));

        path.add(new Pose(-55, -47, -127, 1, 0, 12));
        path.add(new Pose(-69, -47, 90, 1, 0, 12));
        runAction(new PurePursuit(path, new Pose(5, 5, 3)));
        path.clear();

        runAction(new Wait(250));
        runAction(new DriveTillCarousel());
//        runAction(new SetUltrasonicPose(SetUltrasonicPose.Position.CAROUSEL));
        runAction(new Wait(250));
        runAction(new SpinDuck());

        path.add(new Pose(-65, -40, 90, 1, 0, 6));
        path.add(new Pose(-60, -40, -60, 1, 0, 12));
        runAction(new PurePursuit(path));
        path.clear();

        Robot.getIntake().overridePower = true;
        Intake.power = 0.5;
        Robot.getIntake().loadState = Intake.LoadState.INTAKE;
        runAction(new TurnUntilDuck());

        if (ActionVariables.grabbedDuck) {
            runAction(new DriveToDuck());

            path.add(new Pose(-32, -52, -127, 1, 0, 12));
            runAction(new PurePursuit(path, new Pose(1, 1, 2)));
            path.clear();

            runAction(new Score(Lift.LiftState.LAYER3, false));
        }
        Robot.getIntake().loadState = Intake.LoadState.OFF;
        Robot.getIntake().overridePower = false;

        path.add(new Pose(-40, -24, -90, 1, -1, 6));
        path.add(new Pose(-24, -5, -180, 1, -1, 6));
        path.add(new Pose(5, -5, -180, 1, -1, 6));
        path.add(new Pose(12, -48, -180, 1, 0, 6));
        path.add(new Pose(70, -48, -180, 1, 0, 6));
        runAction(new PurePursuit(path, new Pose(5, 5, 5)));
        path.clear();

//        runAction(new PurePursuit(path));
//        path.clear();

//        runAction(new Wait(400));

//        runAction(new SetUltrasonicPose(SetUltrasonicPose.Position.WH_AFTER_BARRIER, 8));
//        Robot.rr.setPoseEstimate(new Pose2d(Robot.rr.getPoseEstimate().getX(), -48, Robot.rr.getPoseEstimate().getHeading()));

        path.add(new Pose(65, -44, -90, 1, 0, 6));
        runAction(new PurePursuit(path, new Pose(3, 3, 3)));
        path.clear();

//        Robot.rr.setPoseEstimate(new Pose2d(64, Robot.rr.getPoseEstimate().getY(), Robot.rr.getPoseEstimate().getHeading()));

//        runAction(new Wait(400));
        runAction(new SetUltrasonicPose(SetUltrasonicPose.Position.WH_INTAKE_SHARED_SIDE, 10));

        Robot.getIntake().loadState = Intake.LoadState.INTAKE;

        path.add(new Pose(65, -44, -90, 1, 0, 6));
        runAction(new PurePursuit(path, new Pose(3, 3, 3)));
        path.clear();

        runAction(new DriveAndIntake());

        path.add(new Pose(60, -35, -90, 1, 0, 10));
        path.add(new Pose(40, -35, 0, 1, 0, 6));
        path.add(new Pose(-5, -35, 0, 1, 0, 6));
        runAction(new PurePursuit(path));
        path.clear();

        ThreadAction(new ScoreNextToHub());
//        runAction(new Wait(200));

//        runAction(new SetUltrasonicPose(SetUltrasonicPose.Position.DUCK_AUTO_SCORE, 2));
//        Robot.rr.setPoseEstimate(new Pose2d(Robot.rr.getPoseEstimate().getX(), -40, Robot.rr.getPoseEstimate().getHeading()));

        path.add(new Pose(5, -18, 0, 1, 0, 6));
        runAction(new PurePursuit(path));
        path.clear();

        runAction(new Score(Lift.LiftState.LAYER3_CLOSE, false));

        path.add(new Pose(5, -32, 0, 1, 0, 6));
        path.add(new Pose(60, -32, 0, 1, 0, 6));
        runAction(new PurePursuit(path));
        path.clear();
    }

    @Override
    public StartPosition side() {
        return null;
    }

    @Override
    public void initialize() {
        Robot.isRed = true;
        Pose2d startPos = new Pose2d(-42, -61.375, Math.toRadians(-90));
        Robot.rr.setPoseEstimate(startPos);
    }
}
