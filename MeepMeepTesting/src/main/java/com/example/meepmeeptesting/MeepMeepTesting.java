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
                .setConstraints(69.5, 40, Math.toRadians(150), Math.toRadians(150), 11.73)
                .setStartPose(new Pose2d(5.125, -61.375, Math.toRadians(-90)))
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(5.125, -61.375, Math.toRadians(-90)))

                                //-----------------
                                //|   Preloaded   |
                                //-----------------
                                // Drive to preloaded
                                .setReversed(true)
                                .lineToSplineHeading(new Pose2d(4.875, -51.375, Math.toRadians(-58)))

                                // Score preloaded
                                .waitSeconds(1)

                                //-----------------
                                //|   1st Cycle   |
                                //-----------------
                                // Drive to warehouse and Intake
                                .setReversed(false)
                                .splineToLinearHeading(new Pose2d(18, -65, Math.toRadians(-0)), Math.toRadians(-0))


                                .splineToConstantHeading(new Vector2d(55, -65), Math.toRadians(-0))


                                .setReversed(true)
                                .splineTo(new Vector2d(35, -65), Math.toRadians(180))
                                .splineTo(new Vector2d(18, -65), Math.toRadians(180))
                                .splineTo(new Vector2d(4.875, -51.375), Math.toRadians(122))

                                // Score
                                .waitSeconds(1)

                                //-----------------
                                //|   2nd Cycle   |
                                //-----------------
                                // Drive to warehouse and Intake
                                .setReversed(false)
                                .splineToLinearHeading(new Pose2d(18, -65, Math.toRadians(-0)), Math.toRadians(-0))


                                .splineToConstantHeading(new Vector2d(55, -65), Math.toRadians(-0))


                                .setReversed(true)
                                .splineTo(new Vector2d(35, -65), Math.toRadians(180))
                                .splineTo(new Vector2d(18, -65), Math.toRadians(180))
                                .splineTo(new Vector2d(4.875, -51.375), Math.toRadians(122))

                                // Score
                                .waitSeconds(1)

                                //-----------------
                                //|   3rd Cycle   |
                                //-----------------
                                // Drive to warehouse and Intake
                                .setReversed(false)
                                .splineToLinearHeading(new Pose2d(18, -65, Math.toRadians(-0)), Math.toRadians(-0))


                                .splineToConstantHeading(new Vector2d(55, -65), Math.toRadians(-0))


                                .setReversed(true)
                                .splineTo(new Vector2d(35, -65), Math.toRadians(180))
                                .splineTo(new Vector2d(18, -65), Math.toRadians(180))
                                .splineTo(new Vector2d(4.875, -51.375), Math.toRadians(122))

                                // Score
                                .waitSeconds(1)

                                //-----------------
                                //|   4th Cycle   |
                                //-----------------
                                // Drive to warehouse and Intake
                                .setReversed(false)
                                .splineToLinearHeading(new Pose2d(18, -65, Math.toRadians(-0)), Math.toRadians(-0))


                                .splineToConstantHeading(new Vector2d(55, -65), Math.toRadians(-0))


                                .setReversed(true)
                                .splineTo(new Vector2d(35, -65), Math.toRadians(180))
                                .splineTo(new Vector2d(18, -65), Math.toRadians(180))
                                .splineTo(new Vector2d(4.875, -51.375), Math.toRadians(122))

                                // Score
                                .waitSeconds(1)

                                //-----------------
                                //|     Park      |
                                //-----------------
                                // Drive to warehouse and Intake
                                .setReversed(false)
                                .splineToLinearHeading(new Pose2d(18, -65, Math.toRadians(-0)), Math.toRadians(-0))


                                .splineToConstantHeading(new Vector2d(55, -65), Math.toRadians(-0))

                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_FREIGHTFRENZY_ADI_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}