package com.team9889.ftc2021.auto.modes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2021.auto.AutoModeBase;
import com.team9889.ftc2021.auto.actions.carousel.SpinDuck;
import com.team9889.ftc2021.auto.actions.drive.DriveTillCarousel;
import com.team9889.ftc2021.auto.actions.drive.DriveToDuck;
import com.team9889.ftc2021.auto.actions.drive.PurePursuit;
import com.team9889.ftc2021.auto.actions.drive.TurnUntilDuck;
import com.team9889.ftc2021.auto.actions.lift.Score;
import com.team9889.ftc2021.auto.actions.utl.Wait;
import com.team9889.ftc2021.subsystems.Intake;
import com.team9889.ftc2021.subsystems.Lift;
import com.team9889.lib.Pose;

import java.util.ArrayList;

/**
 * Created by Eric on 5/27/2022.
 */

@Autonomous
public class RedDuck extends AutoModeBase {
    @Override
    public void run(StartPosition startPosition, Boxes box) {
        ArrayList<Pose> path = new ArrayList<>();

//        ThreadAction(new ScoreInPosition(Lift.LiftState.LAYER3, new Pose(-32, -47, -127)));

        path.add(new Pose(-38, -55, -90, 1, 0, 6));
        path.add(new Pose(-32, -47, -127, 1, 0, 12));
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
        path.add(new Pose(-60, -40, -50, 1, 0, 12));
        runAction(new PurePursuit(path));
        path.clear();

        Robot.getIntake().overridePower = true;
        Intake.power = 0.75;
        Robot.getIntake().loadState = Intake.LoadState.INTAKE;

        runAction(new Wait(250));
        runAction(new TurnUntilDuck());
        runAction(new DriveToDuck());

        path.add(new Pose(-32, -47, -127, 1, 0, 12));
        runAction(new PurePursuit(path));
        path.clear();

        runAction(new Score(Lift.LiftState.LAYER3, false));

        path.add(new Pose(-55, -47, -127, 1, 0, 12));
        path.add(new Pose(-63, -47, -45, 1, 0, 6));
        path.add(new Pose(-64, -34, -45, 1, 0, 12));
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
