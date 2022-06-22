package com.team9889.ftc2021.auto.actions.drive;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;
import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.auto.actions.ActionVariables;
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
public class PurePursuit extends Action {
    public static double speedToAdd = 0.02;
    private double maxTranslationalSpeed = 0;

    ArrayList<Pose> path;
    Pose tolerance = new Pose(2, 2, 3);
    int step = 1;
    private boolean debugging = false;

    public PurePursuit(ArrayList<Pose> path) {
        this.path = path;
    }

    public PurePursuit(ArrayList<Pose> path, Pose tolerance) {
        this.path = path;
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
    }

    @Override
    public void update() {
        // Current Robot Position
        Pose2d robotPoseEstimate = Robot.getInstance().rr.getLocalizer().getPoseEstimate();

        // If we are not targeting the last point the in the path....
        if (path.size() - 1 > step) {

            // and we are have a valid path to the next point
            if (RobotToLine(path.get(step), path.get(step + 1), robotPoseEstimate, path.get(step).radius).x != 10000) {
                step += 1;
            }
        }

        // I think this is the target point that the robot wants to drive too. The "fake" target
        Pose currentFakeTarget = RobotToLine(path.get(step - 1), path.get(step), robotPoseEstimate, path.get(step).radius);

        // Speed
        // Acceleration control
        // Currently at 0.02 * dt units/sec^2 ??
        if (maxTranslationalSpeed < path.get(step).maxSpeed) {
            maxTranslationalSpeed += speedToAdd;
        } else {
            maxTranslationalSpeed = path.get(step).maxSpeed;
        }

        // Distance away from current pose to fake pose
        double relativeXDist = currentFakeTarget.x - robotPoseEstimate.getX();
        double relativeYDist = currentFakeTarget.y - robotPoseEstimate.getY();

        // IDK how this exactly relates to speed, but it scales it somehow
        double xSpeed = relativeXDist / (abs(relativeXDist) + abs(relativeYDist));
        double ySpeed = relativeYDist / (abs(relativeXDist) + abs(relativeYDist));

        // IDK how this relates to speed, it scales it somehow
        xSpeed *= Range.clip((abs(relativeXDist) / path.get(step).radius),0,1);
        ySpeed *= Range.clip((abs(relativeYDist) / path.get(step).radius),0,1);

        // IDK how this relates to speed, it scales but why is it divided by 6? where it come from?
        xSpeed *= Range.clip((abs(relativeXDist) / 6.0),0,1);
        ySpeed *= Range.clip((abs(relativeYDist) / 6.0),0,1);

        // Ultimately it clips the values inside the range of values that the robot can move
        xSpeed = CruiseLib.limitValue(xSpeed, -0.05, -maxTranslationalSpeed, 0.05, maxTranslationalSpeed);
        ySpeed = CruiseLib.limitValue(ySpeed, -0.05, -maxTranslationalSpeed, 0.05, maxTranslationalSpeed);

        // Angle of fake point from angle of robot i think
        double angleToPoint = atan2(relativeXDist, relativeYDist);
        angleToPoint = toDegrees(angleToPoint);
        angleToPoint += (path.get(step).thetaFollowPoint == -1 ? 180 : 0);

        // Point relative to field
        double relativePointAngle = -CruiseLib.angleWrap(angleToPoint - toDegrees(robotPoseEstimate.getHeading()));

        // Debugging Relative point and Distance away from fake point
        if (debugging)
            RobotLog.v("Distance", String.format("%s, %s, %s",
                relativeXDist, relativeYDist, Math.hypot(relativeYDist, relativeXDist))
            );

        // This is our turning speed
        double turnSpeed;

        if (Math.hypot(relativeYDist, relativeXDist) < 10 || path.get(step).thetaFollowPoint == 0) {
            double maxTurnSpeed = 0.3;

            turnSpeed = CruiseLib.limitValue(
                    -CruiseLib.angleWrap(path.get(step).theta - toDegrees(robotPoseEstimate.getHeading())) / 180.0,
                    0, -maxTurnSpeed, 0, maxTurnSpeed);

            if (debugging) {
                RobotLog.v("Angle", path.get(step).theta + ", " + toDegrees(robotPoseEstimate.getHeading()) + ", " +
                        -CruiseLib.angleWrap(path.get(step).theta - toDegrees(robotPoseEstimate.getHeading())) / 180.0);
            }
        } else {
            turnSpeed = CruiseLib.limitValue(relativePointAngle / 90.0, -0.05, -1, 0.05, 1);

            if (debugging)
                RobotLog.v("Angle", angleToPoint + ", " + toDegrees(robotPoseEstimate.getHeading()));
        }

        Robot.getInstance().getMecanumDrive().setFieldCentricPowerAuto(xSpeed, ySpeed, turnSpeed);

        // Debugging Speeds and Positions on Dashboard
        if (debugging) {
            Robot.getInstance().telemetry.addData("X Speed", xSpeed);
            Robot.getInstance().telemetry.addData("Y Speed", ySpeed);
            Robot.getInstance().telemetry.addData("Turn Speed", turnSpeed);

            TelemetryPacket packet = new TelemetryPacket();
            for (int i = 0; i < path.size() - 1; i++) {
                packet.fieldOverlay()
                        .setStroke("red")
                        .strokeLine(path.get(i).x, path.get(i).y, path.get(i + 1).x, path.get(i + 1).y);
            }

            packet.fieldOverlay()
                    .setFill("green")
                    .fillRect(robotPoseEstimate.getX() - 6.5, robotPoseEstimate.getY() - 6.5, 13, 13);

            packet.fieldOverlay()
                    .setStroke("blue")
                    .strokeCircle(robotPoseEstimate.getX(), robotPoseEstimate.getY(), 18);

            packet.fieldOverlay()
                    .setStroke("black")
                    .strokeLine(robotPoseEstimate.getX(), robotPoseEstimate.getY(), currentFakeTarget.x, currentFakeTarget.y);
            FtcDashboard.getInstance().sendTelemetryPacket(packet);
        }
    }

    @Override
    public boolean isFinished() {
        Pose currentPoseEstimate = Pose.Pose2dToPose(Robot.getInstance().rr.getLocalizer().getPoseEstimate());
        Pose error = Pose.getDifference(currentPoseEstimate,path.get(path.size() - 1));

        // Position Tolerance
        boolean isInXTolerance = abs(error.x) < tolerance.x;
        boolean isInYTolerance = abs(error.y) < tolerance.y;
        boolean isInThetaTolerance = abs(CruiseLib.angleWrap(error.theta)) < tolerance.theta;
        boolean isPoseInTolerance = isInXTolerance && isInYTolerance && isInThetaTolerance;

        // Path Following
        boolean isPathFollowCompleted = step == path.size() - 1;

        // Externally Interrupted
        boolean isInterrupted = ActionVariables.stopDriving;

        return (isPoseInTolerance && isPathFollowCompleted) || isInterrupted;
    }

    @Override
    public void done() {
        Robot.getInstance().getMecanumDrive().setFieldCentricPowerAuto(0, 0, 0);
        ActionVariables.stopDriving = false;
    }


    /**
     * For argument sake imma pretend this is the pose of the target point at some distance
     * away from the robot on the line made by the two points
     **/
    private Pose RobotToLine (Pose point1, Pose point2, Pose2d pose, double radius) {
        // Slope of last following point and the next point
        double slope = (point2.y - point1.y) / (point2.x - point1.x);

        // Y Intercept of the current point
        double yIntercept = point2.y - (slope * point2.x);

        // IDK what this is doing (magic math from somewhere)
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
            return new Pose(10000, 10000, 10000);
        }
    }
}