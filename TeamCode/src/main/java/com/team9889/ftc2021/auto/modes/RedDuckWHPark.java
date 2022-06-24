package com.team9889.ftc2021.auto.modes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2021.auto.AutoModeBase;
import com.team9889.ftc2021.auto.actions.carousel.SpinDuck;
import com.team9889.ftc2021.auto.actions.drive.DriveTillCarousel;
import com.team9889.ftc2021.auto.actions.drive.PurePursuit;
import com.team9889.ftc2021.auto.actions.drive.PurePursuitRobotCentric;
import com.team9889.ftc2021.auto.actions.drive.duck.DriveToDuck;
import com.team9889.ftc2021.auto.actions.drive.duck.TurnUntilDuck;
import com.team9889.ftc2021.auto.actions.intake.Outtake;
import com.team9889.ftc2021.auto.actions.lift.Score;
import com.team9889.ftc2021.auto.actions.utl.Wait;
import com.team9889.ftc2021.subsystems.Intake;
import com.team9889.ftc2021.subsystems.Lift;
import com.team9889.lib.Pose;

import java.util.ArrayList;

/**
 * Created by Eric on 5/27/2022.
 */

@Autonomous(name = "\uD83D\uDFE5 \uD83E\uDD86 Red Duck WH Park \uD83E\uDD86 \uD83D\uDFE5", preselectTeleOp = "Teleop")
public class RedDuckWHPark extends AutoModeBase {
    @Override
    public void run(StartPosition startPosition, Boxes box) {
        ArrayList<Pose> path = new ArrayList<>();

        runAction(new Wait(timeToWait));

        Intake.down = 0.7;

        path.add(new Pose(-38, -55, -90, 1, 0, 6));
        path.add(new Pose(-32, -47, -127, 1, 0, 12));
        if (box == Boxes.LEFT) {
            path.add(new Pose(-28, -42, -127, 1, 0, 12));
        }
        runAction(new PurePursuit(path));
        path.clear();

        runAction(new Outtake(0, -1));
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
        Robot.getIntake().loadState = Intake.LoadState.OFF;

        path.add(new Pose(-34, -53, -127, 1, 0, 6));
        path.add(new Pose(-55, -53, -127, 1, 0, 12));
        path.add(new Pose(-69, -53, 90, 1, 0, 12));
        runAction(new PurePursuit(path, new Pose(5, 5, 3)));
        path.clear();

        runAction(new Wait(250));
        runAction(new DriveTillCarousel());
//        runAction(new SetUltrasonicPose(SetUltrasonicPose.Position.CAROUSEL));
        runAction(new Wait(250));
        runAction(new SpinDuck());

        path.add(new Pose(-65, -40, 90, 1, 0, 6));
        path.add(new Pose(-60, -33, -50, 1, 0, 12));
        runAction(new PurePursuit(path));
        path.clear();

        Robot.getIntake().overridePower = true;
        Intake.power = 0.5;
        Robot.getIntake().loadState = Intake.LoadState.INTAKE;
        runAction(new TurnUntilDuck());

        runAction(new DriveToDuck());
        runAction(new Wait(250));

        path.add(new Pose(-36, -52, -127, 1, 0, 12));
        runAction(new PurePursuit(path, new Pose(1, 1, 2)));
        path.clear();

        runAction(new Wait(timeToWaitDuck));

        runAction(new Score(Lift.LiftState.LAYER3, false));
        Robot.getIntake().loadState = Intake.LoadState.OFF;
        Robot.getIntake().overridePower = false;

        Robot.getIntake().loadState = Intake.LoadState.OFF;

        path.add(new Pose(-55, -47, -127, 1, 0, 6));
        path.add(new Pose(-59, -47, -180, 1, 0, 6));
        path.add(new Pose(-59.5, -25, 90, 1, 0, 6));
        path.add(new Pose(-24, 0, 0, 1, 0, 6));
        path.add(new Pose(9, 0, 0, 1, 0, 6));
        path.add(new Pose(9.5, -44, 0, 1, 0, 6));
        runAction(new PurePursuit(path));
        path.clear();

        path.add(new Pose(60, -44, 0, 1, 0, 6));
        runAction(new PurePursuit(path));
        path.clear();

        Intake.down = 0.58;
    }

    @Override
    public StartPosition side() {
        return StartPosition.RED_DUCK;
    }

    @Override
    public void initialize() {
        Pose2d startPos = new Pose2d(-42, -61.375, Math.toRadians(-90));
        Robot.rr.setPoseEstimate(startPos);
    }
}
