package com.team9889.ftc2021.auto.modes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2021.auto.AutoModeBase;
import com.team9889.ftc2021.auto.actions.drive.PurePursuit;
import com.team9889.ftc2021.auto.actions.utl.RobotUpdate;

/**
 * Created by Eric on 11/13/2021.
 */

//@Disabled
@Autonomous(group = "Test")
public class Test extends AutoModeBase {
    @Override
    public void initialize() {}

    @Override
    public void run(StartPosition startPosition, Boxes box) {
        ThreadAction(new RobotUpdate(Robot));
        Robot.rr.setPoseEstimate(new Pose2d(0, 0, 0));

        runAction(new PurePursuit(new Pose2d[] {new Pose2d(0, 0, 0), new Pose2d(1, 24, 0), new Pose2d(-24, 48, 0), new Pose2d(0, 72, 0)}));
    }

    @Override
    public StartPosition side() {
        return null;
    }
}
