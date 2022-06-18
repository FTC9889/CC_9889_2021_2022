package com.team9889.ftc2021.test.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.team9889.ftc2021.auto.AutoModeBase;
import com.team9889.ftc2021.auto.actions.drive.PurePursuit;
import com.team9889.ftc2021.auto.actions.utl.RobotUpdate;
import com.team9889.ftc2021.auto.actions.utl.Wait;
import com.team9889.lib.Pose;

import java.util.ArrayList;

/**
 * Created by Eric on 5/25/2022.
 */

@Disabled
@Autonomous
public class TunePurePursuit extends AutoModeBase{
    @Override
    public void run(AutoModeBase.StartPosition startPosition, AutoModeBase.Boxes box) {
        ArrayList<Pose> path1 = new ArrayList<>(), path2 = new ArrayList<>();
        Robot.rr.setPoseEstimate(new Pose2d(0, 0, 0));
        waitForStart(true);

        ThreadAction(new RobotUpdate(Robot));

        path1.add(new Pose(72, 0, 0, 0.8));

        path2.add(new Pose(0, 0, 0, 0.8));

        while (opModeIsActive()) {

            runAction(new PurePursuit(path1));

            runAction(new Wait(1000));

            runAction(new PurePursuit(path2));

            runAction(new Wait(1000));
        }
    }

    @Override
    public StartPosition side() {
        return null;
    }

    @Override
    public void initialize() {

    }
}
