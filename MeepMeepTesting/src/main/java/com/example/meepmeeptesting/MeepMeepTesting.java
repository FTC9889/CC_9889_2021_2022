package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.DriveTrainType;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setDimensions(13, 15)
                .setDriveTrainType(DriveTrainType.MECANUM)
                .setConstraints(48, 33, Math.toRadians(410), Math.toRadians(238), 11.73)
                .setStartPose(new Pose2d(7, -63, Math.toRadians(-90)))
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(7, 63, Math.toRadians(90)))

                                //-----------------
                                //|   Preloaded   |
                                //-----------------
                                // Drive to preloaded
                                .setReversed(true)
                                .lineToSplineHeading(new Pose2d(0, 55, Math.toRadians(70)))

                                // Score preloaded
                                .waitSeconds(1)

                                //-----------------
                                //|   1st Cycle   |
                                //-----------------
                                // Drive to warehouse and Intake
                                .setReversed(false)
                                .splineToSplineHeading(new Pose2d(8, 60), Math.toRadians(0))
                                .splineToConstantHeading(new Vector2d(15, 64), Math.toRadians(0))
                                .splineToSplineHeading(new Pose2d(55, 64), Math.toRadians(0))
                                .addDisplacementMarker(40, () -> {
                                })

                                // Drive to hub
                                .setReversed(true)
                                .splineTo(new Vector2d(15, 64), Math.toRadians(-180))
                                .splineTo(new Vector2d(0, 55), Math.toRadians(-110))
                                .addDisplacementMarker(40, () -> {
                                })

                                // Score
                                .waitSeconds(1)

                                //-----------------
                                //|   2nd Cycle   |
                                //-----------------
                                // Drive to warehouse and Intake
                                .setReversed(false)
                                .splineToSplineHeading(new Pose2d(8, 61), Math.toRadians(0))
                                .splineToConstantHeading(new Vector2d(15, 65), Math.toRadians(0))
                                .splineToSplineHeading(new Pose2d(55, 65), Math.toRadians(0))
                                .addDisplacementMarker(40, () -> {
                                })

                                // Drive to hub
                                .setReversed(true)
                                .splineTo(new Vector2d(15, 65), Math.toRadians(-180))
                                .splineTo(new Vector2d(0, 56), Math.toRadians(-110))
                                .addDisplacementMarker(40, () -> {
                                })

                                // Score
                                .waitSeconds(1)

                                //-----------------
                                //|   3rd Cycle   |
                                //-----------------
                                // Drive to warehouse and Intake
                                .setReversed(false)
                                .splineToSplineHeading(new Pose2d(8, 61), Math.toRadians(0))
                                .splineToConstantHeading(new Vector2d(15, 65), Math.toRadians(0))
                                .splineToSplineHeading(new Pose2d(55, 65), Math.toRadians(0))
                                .addDisplacementMarker(40, () -> {
                                })

                                // Drive to hub
                                .setReversed(true)
                                .splineTo(new Vector2d(15, 65), Math.toRadians(-180))
                                .splineTo(new Vector2d(0, 56), Math.toRadians(-110))
                                .addDisplacementMarker(40, () -> {
                                })

                                // Score
                                .waitSeconds(1)

                                //-----------------
                                //|   4th Cycle   |
                                //-----------------
                                // Drive to warehouse and Intake
                                .setReversed(false)
                                .splineToSplineHeading(new Pose2d(8, 62), Math.toRadians(0))
                                .splineToConstantHeading(new Vector2d(15, 66), Math.toRadians(0))
                                .splineToSplineHeading(new Pose2d(55, 66), Math.toRadians(0))
                                .addDisplacementMarker(40, () -> {
                                })

                                // Drive to hub
                                .setReversed(true)
                                .splineTo(new Vector2d(15, 66), Math.toRadians(-180))
                                .splineTo(new Vector2d(0, 57), Math.toRadians(-110))
                                .addDisplacementMarker(40, () -> {
                                })

                                // Score
                                .waitSeconds(1)

                                //-----------------
                                //|     Park      |
                                //-----------------
                                // Drive to warehouse and Intake
                                .setReversed(false)
                                .splineToSplineHeading(new Pose2d(8, 62), Math.toRadians(0))
                                .splineToConstantHeading(new Vector2d(15, 66), Math.toRadians(0))
                                .splineToSplineHeading(new Pose2d(45, 66), Math.toRadians(0))

                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_FREIGHTFRENZY_ADI_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}