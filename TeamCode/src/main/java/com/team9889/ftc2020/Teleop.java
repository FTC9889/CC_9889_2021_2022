package com.team9889.ftc2020;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2020.subsystems.Dumper;
import com.team9889.ftc2020.subsystems.Intake;
import com.team9889.ftc2020.subsystems.Lift;

/**
 * Created by MannoMation on 1/14/2019.
 */

@Config
@TeleOp
public class Teleop extends Team9889Linear {
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
                Robot.getMecanumDrive().xSpeed += driverStation.getX();
                Robot.getMecanumDrive().ySpeed += driverStation.getY();
                Robot.getMecanumDrive().turnSpeed += driverStation.getSteer();
            }


            /* Intake */
            if (driverStation.getIntake()) {
                Robot.getIntake().StartIntake();
            } else if (driverStation.getOuttake()) {
                Robot.getIntake().StartOuttake();
            } else if (driverStation.getStopIntake()) {
                Robot.getIntake().StopIntake();
            }

            if (driverStation.getIntakeHeight()) {
                Robot.getIntake().wantedIntakeHeightState = Intake.IntakeHeightState.DOWN;
            } else {
                Robot.getIntake().wantedIntakeHeightState = Intake.IntakeHeightState.UP;
            }


            /* Lift */
            if (!driverStation.getChangeLift()) {
                if (driverStation.getShared()) {
                    Robot.getLift().wantedLiftState = Lift.LiftState.SHARED;
                    driverStation.gamepad2Lift = true;
                    driverStation.liftDown = false;
                } else if (driverStation.getFirstLayer()) {
                    Robot.getLift().wantedLiftState = Lift.LiftState.LAYER1;
                    driverStation.gamepad2Lift = true;
                    driverStation.liftDown = false;
                } else if (driverStation.getSecondLayer()) {
                    Robot.getLift().wantedLiftState = Lift.LiftState.LAYER2;
                    driverStation.gamepad2Lift = true;
                    driverStation.liftDown = false;
                } else if (driverStation.getThirdLayer()) {
                    Robot.getLift().wantedLiftState = Lift.LiftState.LAYER3;
                    driverStation.gamepad2Lift = true;
                    driverStation.liftDown = false;
                }
            }

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
            } else if (driverStation.getSetLift() || Robot.getLift().currentLiftState != Lift.LiftState.NULL &&
                !driverStation.gamepad2Lift) {
                if (driverStation.getLiftHeight()) {
                    Robot.getLift().wantedLiftState = Lift.LiftState.DOWN;
                } else {
                    Robot.getLift().wantedLiftState = Robot.getLift().defaultLiftState;
                }

                driverStation.gamepad2Lift = false;
            } else if (Robot.getLift().currentLiftState == Lift.LiftState.NULL) {
                Robot.getLift().SetLiftPower(0);
            }

            if (driverStation.getChangeLift()) {
                if (driverStation.getShared()) {
                    Robot.getLift().defaultLiftState = Lift.LiftState.SHARED;
                } else if (driverStation.getFirstLayer()) {
                    Robot.getLift().defaultLiftState = Lift.LiftState.LAYER1;
                } else if (driverStation.getSecondLayer()) {
                    Robot.getLift().defaultLiftState = Lift.LiftState.LAYER2;
                } else if (driverStation.getThirdLayer()) {
                    Robot.getLift().defaultLiftState = Lift.LiftState.LAYER3;
                }
            }


            /* Dumper */
            if (driverStation.getDumperOpen()) {
                Robot.getDumper().gateState = Dumper.GateState.OPEN;
            } else {
                Robot.getDumper().gateState = Dumper.GateState.CLOSED;
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

            Robot.isRed = !driverStation.getBlue();


            /* Camera */


            /* Telemetry */
            telemetry.addData("Red Side", Robot.isRed);
            Robot.outputToTelemetry(telemetry);
            telemetry.update();

            Robot.update();
        }
    }
}
