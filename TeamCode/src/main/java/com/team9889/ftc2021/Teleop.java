package com.team9889.ftc2021;


import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2021.subsystems.Dumper;
import com.team9889.ftc2021.subsystems.Intake;
import com.team9889.ftc2021.subsystems.Lift;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by MannoMation on 1/14/2019.
 */

@Config
@TeleOp
public class Teleop extends Team9889Linear {
    public static double x = 0, y = 15;

    boolean rrFirst = true;

    @Override
    public void runOpMode() {
        DriverStation driverStation = new DriverStation(gamepad1, gamepad2);
        Robot.driverStation = driverStation;

        telemetry.setDisplayFormat(Telemetry.DisplayFormat.HTML);

        waitForStart(false);

        Trajectory trajectory = Robot.rr.trajectoryBuilder(new Pose2d(31, -62), true)
                .splineToConstantHeading(new Vector2d(18, -62), Math.toRadians(180))
                .splineTo(new Vector2d(7, -53), Math.toRadians(118))
                .build();

        while (opModeIsActive()) {


            /* Mecanum Drive */
            if (driverStation.resetIMU()) {
                Robot.getMecanumDrive().writeAngleToFile();
            } else {
                if (!driverStation.getSlow()) {
                    Robot.getMecanumDrive().xSpeed += driverStation.getX();
                    Robot.getMecanumDrive().ySpeed += driverStation.getY();
                    if (Robot.getLift().wantedLiftState != Lift.LiftState.LAYER3)
                        Robot.getMecanumDrive().turnSpeed += driverStation.getSteer();
                    else
                        Robot.getMecanumDrive().turnSpeed += driverStation.getSteer() / 3;
                } else {
                    Robot.getMecanumDrive().xSpeed += driverStation.getX() / 3;
                    Robot.getMecanumDrive().ySpeed += driverStation.getY() / 3;
                    Robot.getMecanumDrive().turnSpeed += driverStation.getSteer() / 5;
                }
            }


            if (driverStation.getAutoLineup()) {
                if (rrFirst) {
                    if (Robot.isRed) {
                        Robot.rr.setPoseEstimate(new Pose2d(31, -62));
                    } else {
                        Robot.rr.setPoseEstimate(new Pose2d(31, 62));
                    }
                    Robot.rr.followTrajectoryAsync(trajectory);
                    Robot.getMecanumDrive().rrControl = true;
                    rrFirst = false;
                }

                Robot.rr.update();
            } else {
                Robot.getMecanumDrive().rrControl = false;
                rrFirst = true;
            }


            /* Intake */
            if (driverStation.getIntake()) {
                Robot.getIntake().StartIntake();
            } else if (driverStation.getOuttake()) {
                Robot.getIntake().StartOuttake();
            } else if (driverStation.getStopIntake()) {
                Robot.getIntake().StopIntake();
            }

//            if (driverStation.getIntakeHeight()) {
//                Robot.getIntake().wantedIntakeHeightState = Intake.IntakeHeightState.DOWN;
//            } else {
//                Robot.getIntake().wantedIntakeHeightState = Intake.IntakeHeightState.UP;
//            }


            /* Lift */
            if (driverStation.getLiftUp() > 0.1) {
                Robot.getLift().wantedLiftState = Lift.LiftState.NULL;
                Robot.getLift().currentLiftState = Lift.LiftState.NULL;
                driverStation.gamepad2Lift = false;

                Robot.getLift().SetLiftPower(driverStation.getLiftUp());
            } else if (driverStation.getLiftDown() > 0.1) {
                Robot.getLift().wantedLiftState = Lift.LiftState.NULL;
                Robot.getLift().currentLiftState = Lift.LiftState.NULL;
                driverStation.gamepad2Lift = false;

                Robot.getLift().SetLiftPower(-driverStation.getLiftDown());
            } else if (driverStation.getSecondLayer()) {
                Robot.getLift().wantedLiftState = Lift.LiftState.LAYER2;
            } else if (driverStation.getThirdLayer()) {
                Robot.getLift().wantedLiftState = Lift.LiftState.LAYER3;
            } else if (driverStation.getShared()) {
                Robot.getLift().wantedLiftState = Lift.LiftState.SHARED;
            } else if (driverStation.getDown()) {
                Robot.getLift().wantedLiftState = Lift.LiftState.DOWN;
            } else if (Robot.getLift().currentLiftState == Lift.LiftState.NULL) {
                Robot.getLift().SetLiftPower(0);
            }

            if (driverStation.getLiftAngleUp()) {
                Lift.angle += 0.005;
                Robot.getLift().wantedLiftState = Lift.LiftState.NULL;
                Robot.getLift().currentLiftState = Lift.LiftState.NULL;
            } else if (driverStation.getLiftAngleDown()) {
                Lift.angle -= 0.005;
                Robot.getLift().wantedLiftState = Lift.LiftState.NULL;
                Robot.getLift().currentLiftState = Lift.LiftState.NULL;
            }

            if (Math.abs(driverStation.getLiftRaise()) > 0.1) {
                Lift.angle += driverStation.getLiftRaise() / 30;
                Robot.getLift().wantedLiftState = Lift.LiftState.NULL;
                Robot.getLift().currentLiftState = Lift.LiftState.NULL;
            }


            /* Dumper */
            if (driverStation.getDumperOpen()) {
                Robot.getDumper().gateState = Dumper.GateState.OPEN;
            } else {
                if (Robot.getIntake().intakeState == Intake.IntakeState.OFF) {
                    Robot.getDumper().gateState = Dumper.GateState.PARTIAL;
                } else {
                    Robot.getDumper().gateState = Dumper.GateState.CLOSED;
                }
            }

            if (driverStation.getScore()) {
                Robot.getLift().scoreState = Lift.ScoreState.RELEASE;
            }

            
            /* Carousel */
            if (driverStation.getCarouselOn()) {
                Robot.getCarousel().TurnOn();
            } else {
                Robot.getCarousel().TurnOff();
            }

            if (driverStation.getCarouselDrive() && Robot.getCarousel().GetLimit()) {
                Robot.getMecanumDrive().xSpeed -= .7;
            }


            if (driverStation.getBlue()) {
                Robot.isRed = !Robot.isRed;
            }

            /* Camera */
            if (Robot.isRed) {
                writeLastKnownPosition("Red", "Side");
            } else {
                writeLastKnownPosition("Blue", "Side");
            }

            /* Telemetry */
            if (Robot.isRed)
                telemetry.addData("<font size=\"+2\" color=\"red\">Side</font>", "");
            else
                telemetry.addData("<font size=\"+2\" color=\"aqua\">Side</font>", "");
            Robot.outputToTelemetry(telemetry);
            telemetry.update();

            Robot.update();
        }
    }

    @Override
    public void initialize() {

    }
}
