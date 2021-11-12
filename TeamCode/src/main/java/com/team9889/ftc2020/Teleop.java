package com.team9889.ftc2020;


import android.icu.lang.UProperty;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2020.subsystems.Dumper;
import com.team9889.ftc2020.subsystems.Intake;
import com.team9889.ftc2020.subsystems.Lift;

/**
 * Created by MannoMation on 1/14/2019.
 */

@TeleOp
public class Teleop extends Team9889Linear {
    @Override
    public void runOpMode() {
        DriverStation driverStation = new DriverStation(gamepad1, gamepad2);

        waitForStart(false);

        while (opModeIsActive()) {
            /* Mecanum Drive */
            Robot.getMecanumDrive().xSpeed += driverStation.getX();
            Robot.getMecanumDrive().ySpeed += driverStation.getY();
            Robot.getMecanumDrive().turnSpeed += driverStation.getSteer();


            /* Intake */
            if (driverStation.getIntake()) {
                Robot.getIntake().StartIntake();
            } else if (driverStation.getOuttake()) {
                Robot.getIntake().StartOuttake();
            } else if (driverStation.getStopIntake()) {
                Robot.getIntake().StopIntake();
            }

            if (driverStation.getIntakeHeight()) {
                Robot.getIntake().intakeHeightState = Intake.IntakeHeightState.DOWN;
            } else {
                Robot.getIntake().intakeHeightState = Intake.IntakeHeightState.UP;
            }


            /* Lift */
            if (driverStation.getLiftUp() > 0.1) {
                Robot.getLift().wantedLiftState = Lift.LiftState.NULL;
                Robot.getLift().currentLiftState = Lift.LiftState.NULL;

                Robot.getLift().SetLiftPower(driverStation.getLiftUp());
            } else if (driverStation.getLiftDown() > 0.1) {
                Robot.getLift().wantedLiftState = Lift.LiftState.NULL;
                Robot.getLift().currentLiftState = Lift.LiftState.NULL;

                Robot.getLift().SetLiftPower(driverStation.getLiftDown());
            } else if (driverStation.getSetLift() || Robot.getLift().currentLiftState != Lift.LiftState.NULL) {
                if (driverStation.getLiftHeight()) {
                    Robot.getLift().wantedLiftState = Lift.LiftState.DOWN;
                } else {
                    Robot.getLift().wantedLiftState = Lift.LiftState.LAYER3;
                }
            }


            /* Dumper */
            if (driverStation.getDumperOpen()) {
                Robot.getDumper().gateState = Dumper.GateState.OPEN;
            } else {
                Robot.getDumper().gateState = Dumper.GateState.CLOSED;
            }

            
            /* Carousel */


            /* Camera */


            /* Telemetry */
            Robot.outputToTelemetry(telemetry);
            telemetry.update();

            Robot.update();
        }
    }
}
