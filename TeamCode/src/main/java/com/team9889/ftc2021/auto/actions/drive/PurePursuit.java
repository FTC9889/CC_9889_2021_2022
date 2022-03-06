package com.team9889.ftc2021.auto.actions.drive;

import android.util.Log;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.subsystems.Robot;
import com.team9889.lib.control.controllers.MotionProfileFollower;
import com.team9889.lib.control.motion.ProfileParameters;
import com.team9889.lib.control.motion.TrapezoidalMotionProfile;

/**
 * Created by Eric on 3/4/2022.
 */

public class PurePursuit extends Action {
    MotionProfileFollower mpYProfile = new MotionProfileFollower(0.00002, 0,0.017, 0.0005);
    TrapezoidalMotionProfile yProfile = new TrapezoidalMotionProfile();

    MotionProfileFollower mpXProfile = new MotionProfileFollower(0.00002, 0, 0.048, 0.0005);
    TrapezoidalMotionProfile xProfile = new TrapezoidalMotionProfile();

    ElapsedTime timer = new ElapsedTime();

    ProfileParameters xParameters = new ProfileParameters(40, 36);
    ProfileParameters yParameters = new ProfileParameters(80, 65);

    double xOffset, yOffset;

    Pose2d[] poses;

    public PurePursuit(Pose2d[] poses) {
        this.poses = poses;
    }

    @Override
    public void start() {
        timer.reset();

        yProfile.calculate(poses[1].getY(), yParameters);
        mpYProfile.setProfile(yProfile);

        xProfile.calculate(poses[1].getX() / 2, xParameters);
        mpXProfile.setProfile(xProfile);

        xOffset = Robot.getInstance().rr.getPoseEstimate().getX();
        yOffset = Robot.getInstance().rr.getPoseEstimate().getY();

        mpXProfile.update(0, timer.milliseconds() / 1000);
        mpYProfile.update(0, timer.milliseconds() / 1000);
    }

    int i = 1;
    @Override
    public void update() {
        Vector2d robotPos = Robot.getInstance().rr.getPoseEstimate().vec().minus(new Vector2d(xOffset, yOffset));

        if (poses.length - 1 > i)
            if (robot_to_line(poses[i].vec(), poses[i + 1].vec(), robotPos, 10).getX() != 10000)
                i += 1;

        Log.i("Pose", poses[i].getX() + ", " + poses[i].getY());

        Vector2d point = robot_to_line(poses[i - 1].vec(), poses[i].vec(), robotPos, 10);

        Log.i("Point", point.getX() + ", " + point.getY());

        xProfile.calculate(point.getX() / 2, xParameters);
        yProfile.calculate(point.getY(), yParameters);

        TelemetryPacket packet = new TelemetryPacket();
        packet.fieldOverlay()
                .setFill("green")
                .fillCircle(robotPos.getX(), robotPos.getY(), 10)
                .setFill("blue")
                .fillPolygon(new double[]{robotPos.getX(), point.getX()}, new double[]{robotPos.getY(), point.getY()})
                .setFill("red")
                .strokeLine(poses[i - 1].getX(), poses[i - 1].getY(), poses[i].getX(), poses[i].getY());
        FtcDashboard.getInstance().sendTelemetryPacket(packet);


        double xSpeed = mpXProfile.update(0, timer.milliseconds() / 1000);
        double ySpeed = mpYProfile.update(0, timer.milliseconds() / 1000);
        Robot.getInstance().getMecanumDrive().setPower(xSpeed, ySpeed, 0);
    }

    @Override
    public boolean isFinished() {
        return mpXProfile.profile.getVelocity() == 0 && mpYProfile.profile.getVelocity() == 0;
    }

    @Override
    public void done() {
        Robot.getInstance().getMecanumDrive().setPower(0, 0, 0);
    }


    Vector2d robot_to_line(Vector2d startPos, Vector2d endPoint, Vector2d robot, double radius) {
        double slope = (endPoint.getY() - startPos.getY()) / (endPoint.getX() - startPos.getX());
        double y_intercept = endPoint.getY() - (slope * endPoint.getX());
        double x = robot.getX();
        double y = robot.getY();

        double a = 1 + Math.pow(slope, 2);
        double b = ((slope * y_intercept) * 2) + (-x * 2) + ((-y * 2) * slope);
        double c = Math.pow(y_intercept, 2) + ((-y * 2) * y_intercept) + (-Math.pow(radius, 2) + Math.pow(x, 2) + Math.pow(y, 2));

        double d = Math.pow(b, 2) - (4 * a * c);

        if (d >= 0) {
            double sol1 = (-b - Math.sqrt(d)) / (2 * a);
            double sol2 = (-b + Math.sqrt(d)) / (2 * a);

            double y1 = slope * sol1 + y_intercept;
            double y2 = slope * sol2 + y_intercept;

            if (y1 - startPos.getY() < 0 && endPoint.getY() - y1 > 0) {
                if (y2 - startPos.getY() < 0 && endPoint.getY() - y2 > 0){
                    return new Vector2d(10000, 10000);
                }
            }
            if (sol1 - startPos.getX() < 0 && endPoint.getX() - sol1 > 0) {
                if (sol2 - startPos.getX() < 0 && endPoint.getX() - sol2 > 0){
                    return new Vector2d(10000, 10000);
                }
            }

            double error1 = Math.abs(endPoint.getX() - sol1) + Math.abs(endPoint.getY() - y1);
            double error2 = Math.abs(endPoint.getX() - sol2) + Math.abs(endPoint.getY() - y2);

            Vector2d follow = new Vector2d(sol1, y1);
            if (error1 > error2) {
                follow = new Vector2d(sol2, y2);
            }

//            pygame.draw.circle(screen, (255, 0, 0), ((x * px_to_inch) + (72 * px_to_inch), (-y * px_to_inch) + (72 * px_to_inch))
//                           , r * px_to_inch, 3)
//            pygame.draw.line(screen, (0, 0, 255), ((x * px_to_inch) + (72 * px_to_inch), (-y * px_to_inch) + (72 * px_to_inch)),
//            ((follow.x * px_to_inch) + (72 * px_to_inch), (-follow.y * px_to_inch) + (72 * px_to_inch)))

            return follow;
        } else {
            return new Vector2d(10000, 10000);
        }
    }
}
