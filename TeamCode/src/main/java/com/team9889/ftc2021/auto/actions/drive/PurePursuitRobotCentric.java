package com.team9889.ftc2021.auto.actions.drive;

import android.util.Log;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;
import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.auto.actions.ActionVariables;
import com.team9889.ftc2021.subsystems.Intake;
import com.team9889.ftc2021.subsystems.Robot;
import com.team9889.lib.CruiseLib;
import com.team9889.lib.Pose;

import java.util.ArrayList;

import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;

/**
 * Created by Eric on 3/4/2022.
 */

@Config
public class PurePursuitRobotCentric extends Action {
    public static double speedToAdd = 0.08;

    double maxSpeed = 0, timeout = -1;
    ElapsedTime timer = new ElapsedTime();

    boolean stopWhenIntake = false;

    ArrayList<Pose> path;
    Pose tolerance = new Pose(2, 2, 3);
    int step = 1;

    public PurePursuitRobotCentric(ArrayList<Pose> path) {
        this.path = path;
    }

    public PurePursuitRobotCentric(ArrayList<Pose> path, double timeout) {
        this.path = path;
        this.timeout = timeout;
    }

    public PurePursuitRobotCentric(ArrayList<Pose> path, Pose tolerance) {
        this.path = path;
        this.tolerance = tolerance;
    }

    public PurePursuitRobotCentric(ArrayList<Pose> path, Pose tolerance, boolean stopWhenIntake) {
        this.path = path;
        this.tolerance = tolerance;
        this.stopWhenIntake = stopWhenIntake;
    }

    public PurePursuitRobotCentric(ArrayList<Pose> path, Pose tolerance, double timeout) {
        this.path = path;
        this.timeout = timeout;
        this.tolerance = tolerance;
    }

    @Override
    public void start() {
        ActionVariables.stopDriving = false;

        if (!Robot.getInstance().isRed) {
            for (int i = 0; i < path.size(); i++) {
                path.get(i).y *= -1;
                path.get(i).theta *= -1;
            }
        }

        Pose2d pose = Robot.getInstance().rr.getLocalizer().getPoseEstimate();
        path.add(0, new Pose(pose.getX(), pose.getY(), toDegrees(pose.getHeading())));

        timer.reset();
    }

    @Override
    public void update() {
        Pose2d pose = Robot.getInstance().rr.getLocalizer().getPoseEstimate();

        if (path.size() - 1 > step) {
            if (RobotToLine(path.get(step), path.get(step + 1), pose, path.get(step).radius).x != 10000) {
                step += 1;
            }
        }

        Pose point = RobotToLine(path.get(step - 1), path.get(step), pose, path.get(step).radius);

        if (point.x == 10000) {
            point = path.get(step);
        }

        // Speed
        if (maxSpeed < path.get(step).maxSpeed) {
            maxSpeed += speedToAdd;
        } else {
            maxSpeed = path.get(step).maxSpeed;
        }

//        double xSpeed = CruiseLib.limitValue(xPID.update(pose.getX(), point.x), 0, -maxSpeed, 0, maxSpeed);
//        double speed = CruiseLib.limitValue(yPID.update(pose.getY(), point.y), 0, -maxSpeed, 0, maxSpeed);

        double relativeDist = sqrt(pow(point.y - pose.getY(), 2) + pow(point.x - pose.getX(), 2));

        double speed = relativeDist / abs(relativeDist) * (path.get(step).thetaFollowPoint == -1 ? -1 : 1);

        speed *= Range.clip((abs(relativeDist) / path.get(step).radius),0,1);

        speed *= Range.clip((abs(relativeDist) / 6.0),0,1);

        speed = CruiseLib.limitValue(speed, -0.05, -maxSpeed, 0.05, maxSpeed);


        //Turn
        double angleToPoint = toDegrees(atan2(point.y-pose.getY(), point.x-pose.getX())) + (path.get(step).thetaFollowPoint == -1 ? 180 : 0);

        double relativePointAngle = -CruiseLib.angleWrap(angleToPoint - toDegrees(pose.getHeading()));

//        Log.v("Distance", (point.x - pose.getX()) + ", " + (point.y - pose.getY()) + ", "
//                + sqrt(pow(point.y-pose.getY(), 2) + pow(point.x-pose.getX(), 2)));
//
//        Log.v("Angle to Point", "" + angleToPoint);
//        Log.v("Robot Angle", "" + pose.getHeading());

        double kp = 1./90.;
        double turnSpeed = CruiseLib.limitValue(kp * relativePointAngle, -0.05, -1, 0.05, 1);

        Robot.getInstance().getMecanumDrive().setPower(0, speed, turnSpeed);


//        TelemetryPacket packet = new TelemetryPacket();
//        for (int i = 0; i < path.size() - 1; i++) {
//            packet.fieldOverlay()
//                    .setStroke("red")
//                    .strokeLine(path.get(i).x, path.get(i).y, path.get(i + 1).x, path.get(i + 1).y);
//        }
//
//        packet.fieldOverlay()
//                .setFill("green")
//                .fillRect(pose.getX() - 6.5, pose.getY() - 6.5, 13, 13);
//
//        packet.fieldOverlay()
//                .setStroke("blue")
//                .strokeCircle(pose.getX(), pose.getY(), path.get(step).radius);
//
//        packet.fieldOverlay()
//                .setStroke("black")
//                .strokeLine(pose.getX(), pose.getY(), point.x, point.y);
//        FtcDashboard.getInstance().sendTelemetryPacket(packet);
    }

    @Override
    public boolean isFinished() {
        Pose error = Pose.getError(Pose.Pose2dToPose(Robot.getInstance().rr.getLocalizer().getPoseEstimate()),
                path.get(path.size() - 1));
        return ((abs(error.x) < tolerance.x && abs(error.y) < tolerance.y)
                && step == path.size() - 1) || ActionVariables.stopDriving ||
                (timeout != -1 && timer.milliseconds() > timeout)
                || (stopWhenIntake && Robot.getInstance().getIntake().loadState != Intake.LoadState.INTAKE);
    }

    @Override
    public void done() {
        Robot.getInstance().getMecanumDrive().setPower(0, 0, 0);
        ActionVariables.stopDriving = false;
    }


    Pose RobotToLine (Pose point1, Pose point2, Pose2d pose, double radius) {
        double xDiff = point2.x - point1.x;
        if (xDiff == 0)
            xDiff += 0.000001;

        double slope = (point2.y - point1.y) / xDiff;
        double yIntercept = point2.y - (slope * point2.x);

        double a = 1 + pow(slope, 2);
        double b = ((slope * yIntercept) * 2) + (-pose.getX() * 2) + ((-pose.getY() * 2) * slope);
        double c = pow(yIntercept, 2) + ((-pose.getY() * 2) * yIntercept) + (-pow(radius, 2) + pow(pose.getX(), 2) + pow(pose.getY(), 2));

        double d = pow(b, 2) - (4 * a * c);

        if (d >= 0) {
            double sol1 = (-b - sqrt(d)) / (2 * a);
            double sol2 = (-b + sqrt(d)) / (2 * a);

            double y1 = slope * sol1 + yIntercept;
            double y2 = slope * sol2 + yIntercept;

            if (y1 - point1.y < 0 && point2.y - y1 > 0)
                if (y2 - point1.y < 0 && point2.y - y2 > 0)
                    return new Pose(10000, 10000, 10000);

            if (sol1 - point1.x < 0 && point2.x - sol1 > 0)
                if (sol2 - point1.x < 0 && point2.x - sol2 > 0)
                    return new Pose(10000, 10000, 10000);

            double error1 = abs(point2.x - sol1) + abs(point2.y - y1);
            double error2 = abs(point2.x - sol2) + abs(point2.y - y2);

            Pose follow = new Pose(sol1, y1, 0);
            if (error1 > error2)
                follow = new Pose(sol2, y2, 0);

//            follow = Pose.limitPoseInLine(follow, point1, point2);

            if (sqrt(pow(point2.x - pose.getX(), 2) + pow(point2.y - pose.getY(), 2)) < radius) {
                follow = point2;
            }

            return follow;
        } else {
            Log.v("d", d + "");
            Log.v("PurePursuit", "HELP!!!");
            return new Pose(10000, 10000, 10000);
        }
    }
}