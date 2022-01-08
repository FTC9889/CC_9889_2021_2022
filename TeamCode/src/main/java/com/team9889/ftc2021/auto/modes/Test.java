package com.team9889.ftc2021.auto.modes;

import android.util.Log;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.team9889.ftc2021.auto.AutoModeBase;
import com.team9889.ftc2021.auto.actions.utl.RobotUpdate;
import com.team9889.ftc2021.auto.actions.utl.Wait;
import com.team9889.ftc2021.subsystems.Lift;

/**
 * Created by Eric on 11/13/2021.
 */

@Autonomous
public class Test extends AutoModeBase {
    @Override
    public void run(StartPosition startPosition, Boxes box) {
        ThreadAction(new RobotUpdate());

        Robot.rr.followTrajectory(
                Robot.rr.trajectoryBuilder(new Pose2d())
                        .splineTo(new Vector2d(36, 36), Math.toRadians(0))

                        .addDisplacementMarker(20, () -> {
                            Log.i("Hello", "");
                            Robot.getLift().wantedLiftState = Lift.LiftState.LAYER3;
                        })

                        .addDisplacementMarker(55, () -> {
                            Robot.getLift().wantedLiftState = Lift.LiftState.DOWN;
                        })
                        .splineTo(new Vector2d(72, 0), Math.toRadians(0))
                        .build()
        );

        Log.v("Lift", "" + Robot.getLift().wantedLiftState);

        runAction(new Wait(2000));
    }

    @Override
    public StartPosition side() {
        return null;
    }
}
