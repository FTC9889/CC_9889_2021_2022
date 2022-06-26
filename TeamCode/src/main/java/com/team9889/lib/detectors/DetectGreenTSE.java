package com.team9889.lib.detectors;

import android.util.Log;

import com.acmerobotics.dashboard.config.Config;
import com.team9889.ftc2021.auto.AutoModeBase;
import com.team9889.ftc2021.subsystems.Robot;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

/**
 * Created by edm11 on 6/25/2022.
 */

@Config
public class DetectGreenTSE extends OpenCvPipeline {
    public static int height = 30, width = 30, x = 0, y = 0;
    public static boolean drawRect = true;

    public static Scalar rgbLow = new Scalar(0, 0, 0), rgbHigh = new Scalar(150, 150, 150), defaultBlueHigh = new Scalar(130, 130, 130);

    Scalar leftRGB = new Scalar(0, 0, 0), middleRGB = new Scalar(0, 0, 0);

    AutoModeBase.Boxes position = AutoModeBase.Boxes.RIGHT;

    public Scalar getLeftBox() {
        return new Scalar((int) leftRGB.val[0], (int) leftRGB.val[1], (int) leftRGB.val[2]);
    }

    public Scalar getMiddleBox() {
        return new Scalar((int) middleRGB.val[0], (int) middleRGB.val[1], (int) middleRGB.val[2]);
    }

    public Scalar getLowBound() {
        return new Scalar((int) rgbLow.val[0], (int) rgbLow.val[1], (int) rgbLow.val[2]);
    }

    public Scalar getHighBound() {
        return new Scalar((int) rgbHigh.val[0], (int) rgbHigh.val[1], (int) rgbHigh.val[2]);
    }

    public AutoModeBase.Boxes getPosition () {
        return position;
    }

    @Override
    public Mat processFrame(Mat input) {
        Rect redLeft = new Rect(86, 185, 43, 13),
                redRight = new Rect(251, 186, 43, 13),
                blueLeft = new Rect(5, 186, 43, 13),
                blueRight = new Rect(173, 186, 43, 13);

        Rect leftRoi = Robot.getInstance().isRed ? redLeft : blueLeft;
        Rect rightRoi = Robot.getInstance().isRed ? redRight : blueRight;

        leftRGB = calculateAverageRGBInRoi(input, leftRoi);
        middleRGB = calculateAverageRGBInRoi(input, rightRoi);


        if (drawRect) {
            Imgproc.rectangle(input, new Rect(x, y, width, height), new Scalar(255, 0, 0));
//            Imgproc.rectangle(input, redLeft, leftRGB);
        }

        if (isScalarInThreshold(leftRGB, rgbLow, rgbHigh)) {
            if (Robot.getInstance().isRed)
                position = AutoModeBase.Boxes.LEFT;
            else
                position = AutoModeBase.Boxes.MIDDLE;
        } else if (isScalarInThreshold(middleRGB, rgbLow, rgbHigh)) {
            if (Robot.getInstance().isRed)
                position = AutoModeBase.Boxes.MIDDLE;
            else
                position = AutoModeBase.Boxes.RIGHT;
        } else {
            if (Robot.getInstance().isRed)
                position = AutoModeBase.Boxes.RIGHT;
            else
                position = AutoModeBase.Boxes.LEFT;
        }

        return input;
    }


    private Scalar calculateAverageRGBInRoi(Mat mat, Rect roi) {
        Mat image = new Mat(mat, roi);

        Scalar avgRGB = Core.mean(image);

        return avgRGB;
    }

    private boolean isScalarInThreshold(Scalar scalar, Scalar thresholdLow, Scalar thresholdHigh) {
        double red = scalar.val[0];
        double green = scalar.val[1];
        double blue = scalar.val[2];
        return thresholdLow.val[0] < red && thresholdHigh.val[0] > red
                && thresholdLow.val[1] < green && thresholdHigh.val[1] > green
                && thresholdLow.val[2] < blue && thresholdHigh.val[2] > blue;
    }
}
