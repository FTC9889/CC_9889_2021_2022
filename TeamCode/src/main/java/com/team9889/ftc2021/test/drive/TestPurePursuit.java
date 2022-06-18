package com.team9889.ftc2021.test.drive;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.team9889.ftc2021.auto.AutoModeBase;
import com.team9889.ftc2021.auto.actions.lift.Score;
import com.team9889.ftc2021.auto.actions.lift.ScoreNextToHub;
import com.team9889.ftc2021.auto.actions.utl.Wait;
import com.team9889.ftc2021.subsystems.Lift;

/**
 * Created by Eric on 5/23/2022.
 */

@Disabled
@Autonomous
public class TestPurePursuit extends AutoModeBase {
    @Override
    public void run(StartPosition startPosition, Boxes box) {
//        ArrayList<Pose> path = new ArrayList<>();
//        Robot.rr.setPoseEstimate(new Pose2d(0, 0, 0));
//        waitForStart(true);
//
//        ThreadAction(new RobotUpdate(Robot));
//
//        path.add(new Pose(36, 0, 0, 0.5, 1));
//        path.add(new Pose(36.01, 36, 90, 0.5, 1));
////        path.add(new Pose(0, 36, 0));
////        path.add(new Pose(0.01, 0, 0));
//        runAction(new PurePursuit(path));

        waitForStart(true);

        ThreadAction(new ScoreNextToHub());
        runAction(new Wait(3000));
        runAction(new Score(Lift.LiftState.LAYER3_CLOSE));
    }

    @Override
    public StartPosition side() {
        return null;
    }

    @Override
    public void initialize() {

    }
}
