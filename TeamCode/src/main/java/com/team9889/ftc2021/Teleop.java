package com.team9889.ftc2021;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2021.subsystems.Carousel;
import com.team9889.ftc2021.subsystems.Dumper;
import com.team9889.ftc2021.subsystems.Intake;
import com.team9889.ftc2021.subsystems.Lift;

/**
 * Created by MannoMation on 1/14/2019.
 */

@Config
@TeleOp
public class Teleop extends Team9889Linear {
    ElapsedTime timer = new ElapsedTime();

    @Override
    public void runOpMode() {
        DriverStation driverStation = new DriverStation(gamepad1, gamepad2);
        Robot.driverStation = driverStation;

        waitForStart(false);

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


            /* Intake */
            if (driverStation.getIntake()) {
                Robot.getIntake().StartIntake();
            } else if (driverStation.getOuttake()) {
                Robot.getIntake().StartOuttake();
            } else if (driverStation.getStopIntake()) {
                Robot.getIntake().StopIntake();
            }


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

            if (Math.abs(driverStation.getLiftRaise()) > 0.1) {
                Lift.angle += driverStation.getLiftRaise() / 30;
                Robot.getLift().wantedLiftState = Lift.LiftState.NULL;
                Robot.getLift().currentLiftState = Lift.LiftState.NULL;
            } else if (Math.abs(driverStation.getLiftRaiseSlow()) > 0.1) {
                Lift.angle += driverStation.getLiftRaiseSlow() / 60;
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

            if (driverStation.getCarouselFaster()) {
                Carousel.power += 0.01;
                Carousel.time -= 100;
            } else if (driverStation.getCarouselSlower()) {
                Carousel.power -= 0.01;
                Carousel.time += 100;
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

            telemetry.addData("Loop Time", timer.milliseconds());
            timer.reset();
            Robot.outputToTelemetry(telemetry);
            telemetry.update();

            Robot.update();
        }
    }

    @Override
    public void initialize() {

    }
}
