package com.team9889.ftc2021.test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2021.Team9889Linear;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by Eric on 4/30/2022.
 */

@TeleOp
public class Intake extends Team9889Linear {
    public static double smoothing = 10;

    double leftValue = 0;
    double rightValue = 0;

    @Override
    public void initialize() {

    }

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart(false);

        while (opModeIsActive()) {
            if (gamepad1.a) {
                Robot.getIntake().IntakeOn();
            } else if (gamepad1.b || leftValue < 1.5) {
                Robot.getIntake().IntakeOff();
                Robot.getIntake().SetIntake(-0.1);
            }

            if (gamepad1.x || leftValue < 1.5) {
                Robot.getIntake().PassThroughOn();
            } else if (gamepad1.y) {
                Robot.getIntake().PassThroughOff();
            }

            if (gamepad1.dpad_up || leftValue < 1.5) {
                Robot.getIntake().IntakeUp();
            } else if (gamepad1.dpad_down) {
                Robot.getIntake().IntakeDown();
            }

            leftValue += (Robot.inLeft.getDistance(DistanceUnit.INCH) - leftValue) / smoothing;
//            rightValue += (Robot.inRight.getDistance(DistanceUnit.INCH) - rightValue) / smoothing;

            telemetry.addData("0", 0);
            telemetry.addData("5", 3);

            telemetry.addData("Left", leftValue);
//            telemetry.addData("Right", Robot.inRight.getDistance(DistanceUnit.INCH));
            telemetry.update();

            Robot.update();
        }
    }
}
