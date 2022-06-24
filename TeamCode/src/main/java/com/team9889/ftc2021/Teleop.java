package com.team9889.ftc2021;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2021.subsystems.Dumper;
import com.team9889.ftc2021.subsystems.Intake;
import com.team9889.ftc2021.subsystems.Lift;

/**
 * Created by MannoMation on 1/14/2019.
 */

@TeleOp
@Config
public class Teleop extends Team9889Linear {
    public static double capArm = 0.15;
    boolean overrideTimer = false;
    ElapsedTime timer = new ElapsedTime();

    @Override
    public void runOpMode() {
        DriverStation driverStation = new DriverStation(gamepad1, gamepad2);
        Robot.driverStation = driverStation;

        waitForStart(false);

        timer.reset();

        while (opModeIsActive()) {
            /* Mecanum Drive */
            if (driverStation.resetIMU()) {
                Robot.getMecanumDrive().writeAngleToFile();
            } else {
                if (!driverStation.getSlow()) {
                    Robot.getMecanumDrive().xSpeed += driverStation.getX() * 0.8;
                    Robot.getMecanumDrive().ySpeed += driverStation.getY() * 0.8;
                    if (Robot.getLift().wantedLiftState == Lift.LiftState.DOWN ||
                            Robot.getLift().wantedLiftState == Lift.LiftState.NULL)
                        Robot.getMecanumDrive().turnSpeed += driverStation.getSteer() * 0.8;
                    else
                        Robot.getMecanumDrive().turnSpeed += driverStation.getSteer() / 3 * 0.8;

                    Robot.slowdown = false;
                } else {
                    Robot.getMecanumDrive().xSpeed += driverStation.getX() / 3;
                    Robot.getMecanumDrive().ySpeed += driverStation.getY() / 3;
                    Robot.getMecanumDrive().turnSpeed += driverStation.getSteer() / 5;

                    Robot.slowdown = true;
                }
            }


            /* Intake */
            if (driverStation.getIntake()) {
                Robot.getIntake().loadState = Intake.LoadState.INTAKE;
            } else if (driverStation.getStopIntake()) {
                Robot.getIntake().loadState = Intake.LoadState.OFF;
            } else if (driverStation.getOuttake()) {
                Robot.getIntake().loadState = Intake.LoadState.OUTTAKE;
            } else if (gamepad1.x) {
                Robot.getIntake().loadState = Intake.LoadState.TRANSFER;
            }

//            if (gamepad1.dpad_up) {
//                Robot.getIntake().IntakeUp();
//            } else if (gamepad1.dpad_down) {
//                Robot.getIntake().IntakeDown();
//            }


            /* Lift */
            if (driverStation.getSetDefault()) {
                if (driverStation.getFirstLayer()) {
                    Robot.getLift().defaultLiftState = Lift.LiftState.SMART;
                } else if (driverStation.getSecondLayer()) {
                    Robot.getLift().defaultLiftState = Lift.LiftState.LAYER2;
                } else if (driverStation.getThirdLayer()) {
                    Robot.getLift().defaultLiftState = Lift.LiftState.LAYER3;
                } else if (driverStation.getDown()) {
                    Robot.getLift().defaultLiftState = Lift.LiftState.DOWN;
                } else if (driverStation.getSharedClose()) {
                    Robot.getLift().defaultLiftState = Lift.LiftState.SHARED_CLOSE;
                } else if (driverStation.getSharedFar()) {
                    Robot.getLift().defaultLiftState = Lift.LiftState.SHARED_FAR;
                }
            } else {
                if (Math.abs(driverStation.getLiftExtend()) > 0.1) {
                    Robot.getLift().wantedLiftState = Lift.LiftState.NULL;
                    Robot.getLift().currentLiftState = Lift.LiftState.NULL;

                    Robot.getLift().SetLiftPower(driverStation.getLiftExtend() / (Robot.getLift().defaultLiftState == Lift.LiftState.SHARED_CLOSE ? 2 : 1));
                } else if (driverStation.getFirstLayer()) {
                    Robot.getLift().wantedLiftState = Lift.LiftState.SMART;
                } else if (driverStation.getSecondLayer()) {
                    Robot.getLift().wantedLiftState = Lift.LiftState.LAYER2;
                } else if (driverStation.getThirdLayer()) {
                    Robot.getLift().wantedLiftState = Lift.LiftState.LAYER3;
                } else if (driverStation.getDown()) {
                    Robot.getLift().wantedLiftState = Lift.LiftState.DOWN;
                } else if (driverStation.getSharedClose() && (Robot.getCapArm().step == 0 || Robot.getCapArm().step == 4)) {
                    Robot.getLift().wantedLiftState = Lift.LiftState.SHARED_CLOSE;
                } else if (driverStation.getSharedFar() && (Robot.getCapArm().step == 0 || Robot.getCapArm().step == 4)) {
                    Robot.getLift().wantedLiftState = Lift.LiftState.SHARED_FAR;
                } else if (driverStation.getDefault()) {
                    Robot.getLift().wantedLiftState = Robot.getLift().defaultLiftState;
                } else if (Robot.getLift().currentLiftState == Lift.LiftState.NULL) {
                    Robot.getLift().SetLiftPower(0);
                }
            }

            if (Math.abs(driverStation.getLiftRaise()) > 0.1) {
                if (Robot.getCapArm().step == 0 || Robot.getCapArm().step == 4) {
                    Lift.angle += driverStation.getLiftRaise() / 30;
                    Robot.getLift().wantedLiftState = Lift.LiftState.NULL;
                    Robot.getLift().currentLiftState = Lift.LiftState.NULL;
                } else {
                    Robot.getCapArm().manualControl = true;
                    Robot.getCapArm().position -= driverStation.getLiftRaise() / 80;
                    Robot.getCapArm().setCapArm(Robot.getCapArm().position);
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


            /* Cap */
            if (timer.milliseconds() > 77000 || overrideTimer) {
                if (driverStation.getCapForward()) {
                    Robot.getCapArm().step += 1;
                    Robot.getCapArm().manualControl = false;
                } else if (driverStation.getCapBack()) {
                    Robot.getCapArm().step -= 1;
                    Robot.getCapArm().manualControl = false;
                } else if (driverStation.resetCap()) {
                    Robot.getCapArm().step = 0;
                    Robot.getCapArm().manualControl = false;
                }
            }

            if (driverStation.getOverrideTimer()) {
                overrideTimer = true;
            }

//            if (gamepad1.back) {
//                Robot.capArm.setPosition(capArm);
//                Robot.getCapArm().manualControl = true;
//            }

            
            /* Carousel */
            if (driverStation.getCarouselOn()) {
                Robot.getCarousel().TurnOn();
            } else {
                Robot.getCarousel().TurnOff();
            }


            /* Side */
            if (driverStation.getBlue()) {
                Robot.isRed = !Robot.isRed;
            }


            /* Telemetry */
            Robot.outputToTelemetry(telemetry);
            telemetry.update();

            Robot.update();
        }
    }

    @Override
    public void initialize() {

    }
}
