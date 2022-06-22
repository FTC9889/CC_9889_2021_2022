package com.team9889.lib;

import com.acmerobotics.roadrunner.geometry.Pose2d;

/**
 * Created by Eric on 5/24/2022.
 */
public class Pose {
    public double x, y, theta, maxSpeed, radius;
    public int thetaFollowPoint;

    public Pose (double x, double y, double theta, double maxSpeed, int thetaFollowPoint, double radius) {
        this.x = x;
        this.y = y;
        this.theta = theta;
        this.maxSpeed = maxSpeed;
        this.thetaFollowPoint = thetaFollowPoint;
        this.radius = radius;
    }

    public Pose (double x, double y, double theta, double maxSpeed, int thetaFollowPoint) {
        this.x = x;
        this.y = y;
        this.theta = theta;
        this.maxSpeed = maxSpeed;
        this.thetaFollowPoint = thetaFollowPoint;
        this.radius = 20;
    }

    public Pose (double x, double y, double theta, double maxSpeed) {
        this.x = x;
        this.y = y;
        this.theta = theta;
        this.maxSpeed = maxSpeed;
        this.thetaFollowPoint = 0;
        this.radius = 20;
    }

    public Pose (double x, double y, double theta) {
        this.x = x;
        this.y = y;
        this.theta = theta;
        this.maxSpeed = 1;
        this.thetaFollowPoint = 0;
        this.radius = 20;
    }

    public static Pose Pose2dToPose(Pose2d pose2d) {
        return new Pose(pose2d.getX(), pose2d.getY(), Math.toDegrees(pose2d.getHeading()));
    }

    public static Pose getDifference(Pose pose1, Pose pose2) {
        return new Pose(pose2.x - pose1.x, pose2.y - pose1.y, pose2.theta - pose1.theta);
    }

    public static Pose limitPoseInLine(Pose point, Pose line1, Pose line2) {
        Pose returnPose = new Pose(0, 0, point.theta);

        if (point.x > 0) {
            if (line1.x > line2.x) {
                returnPose.x = Math.min(line1.x, point.x);
            } else {
                returnPose.x = Math.min(line2.x, point.x);
            }
        } else {
            if (line1.x < line2.x) {
                returnPose.x = Math.max(line1.x, point.x);
            } else {
                returnPose.x = Math.max(line2.x, point.x);
            }
        }

        if (point.y > 0) {
            if (line1.y > line2.y) {
                returnPose.y = Math.min(line1.y, point.y);
            } else {
                returnPose.y = Math.min(line2.y, point.y);
            }
        } else {
            if (line1.y < line2.y) {
                returnPose.y = Math.max(line1.y, point.y);
            } else {
                returnPose.y = Math.max(line2.y, point.y);
            }
        }

        return returnPose;
    }
}

