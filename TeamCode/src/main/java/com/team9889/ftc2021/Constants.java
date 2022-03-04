package com.team9889.ftc2021;

import com.acmerobotics.roadrunner.geometry.Pose2d;

/**
 * Class to store constants
 * Created by joshua9889 on 4/10/2017.
 */

public class Constants {

    //VuMark Licence Key
    public final static String kVuforiaLicenceKey = "AUUHzRL/////AAABmaGWp2jobkGOsFjHcltHEso2M1NH" +
            "Ko/nBDlUfwXKZBwui9l88f5YAT31+8u5yoy3IEJ1uez7rZrIyLdyR4cCMC+a6I7X/EzkR1F094ZegAsXdg9n" +
            "ht01zju+nxxi1+rabsSepS+TZfAa14/0rRidvX6jqjlpncDf8uGiP75f38cW6W4uFRPrLdufA8jMqODGux9d" +
            "w7VkFMqB+DQuNk8n1pvJP87FFo99kr653cjMwO4TYbztNmUYaQUXjHHNhOFxHufN42r7YcCErvX90n/gIvs4" +
            "wrvffXGyU/xkmSaTJzrgiy8R+ZJx2T0JcEJ0m1UUEoF2BmW4ONAVv/TkG9ESGf6iAmx66vrVm3tk6+YY+1q1";

    public final static String kRevHubMaster = "C";
    public final static String kRevHubSlave = "E";

    public final static String kWebcam = "Webcam";

    public static Pose2d pose;

    /*---------------------
    |                     |
    |     Drivetrain!     |
    |                     |
    ---------------------*/

    //Settings for Drive class
    public static class DriveConstants {
        public final static String kLeftDriveMasterId = "lf";
        public final static String kRightDriveMasterId = "rf";
        public final static String kLeftDriveSlaveId = "lb";
        public final static String kRightDriveSlaveId = "rb";

        public final static String kDistanceLeft = "dist_left";
        public final static String kDistanceRight = "dist_right";
        public final static String kDistanceForward = "dist_forward";
        public final static String kDistanceBack = "dist_back";

        public final static double WheelbaseWidth = 14.5;
        public final static double WheelDiameter = 3.77953;

        /**
         * ticks to inch
         * (Wheel Diameter * PI) / Counts Per Rotation
         */
        public final static double ENCODER_TO_DISTANCE_RATIO = (WheelDiameter * Math.PI) / 537.6;
        public final static double AngleToInchRatio = (Math.PI / 180.) * (WheelbaseWidth / 2);
        public final static double InchToTick = 537.6 / (Math.PI * 4.1);
    }

    public static class OdometryConstants {

//        private static double WheelDiameter = 38.45 / 25.4;  // https://www.rotacaster.com.au/shop/35mm-rotacaster-wheels/index
        private static double WheelDiameter = 35 / 25.4;  // https://www.rotacaster.com.au/shop/35mm-rotacaster-wheels/index
        public final static double ENCODER_TO_DISTANCE_RATIO = ((WheelDiameter * Math.PI) * ((((double) 40) / ((double) 24)))) / 1440; // https://www.rotacaster.com.au/shop/35mm-rotacaster-wheels/index  Step 4
//        0.005007837
    }

   /*---------------------
    |                     |
    |       Intake        |
    |                     |
    ---------------------*/

    //Settings for Intake
    public static class IntakeConstants {
        public final static String kIntake = "intake";
        public final static String kPassThrough = "pass_through";
        public final static String kIntakeLift = "intake_lift";
        public final static String kIntakeGate = "intake_gate";
    }

    /*---------------------
    |                     |
    |       Lift!         |
    |                     |
    ---------------------*/

    //Settings for Lift
    public static class LiftConstants {
        public final static String kLift = "lift";
        public final static String kAngleAdjust1 = "angle1";
        public final static String kAngleAdjust2 = "angle2";
        public final static String kAngleAdjust3 = "angle3";
        public final static String kDownLimit = "down_limit";
    }

    /*---------------------
    |                     |
    |       Dumper!       |
    |                     |
    ---------------------*/

    //Settings for Lift
    public static class DumperConstants {
        public final static String kGate = "gate";
    }

    /*---------------------
    |                     |
    |      Carousel!      |
    |                     |
    ---------------------*/

    //Settings for Lift
    public static class CarouselConstants {
        public final static String kCarousel = "carousel";
        public final static String kCarouselRedLimit = "red_limit";
        public final static String kCarouselBlueLimit = "blue_limit";
    }

    /*---------------------
    |                     |
    |       Camera!       |
    |                     |
    ---------------------*/

    //Settings for Camera
    public static class CameraConstants {
        public final static String kCamX = "cam_x";
        public final static String kCamY = "cam_y";
    }
}
