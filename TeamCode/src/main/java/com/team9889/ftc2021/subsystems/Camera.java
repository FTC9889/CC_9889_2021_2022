package com.team9889.ftc2021.subsystems;

import android.util.Log;

import com.acmerobotics.dashboard.config.Config;
import com.team9889.ftc2021.auto.AutoModeBase;
import com.team9889.lib.detectors.ScanForWorm;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Point;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;

/**
 * Created by MannoMation on 10/27/2018.
 */

@Config
public class Camera extends Subsystem{
    ScanForWorm scanForWorm = new ScanForWorm();

    public enum CameraStates {
        WORM, NULL
    }
    public CameraStates currentCamState = CameraStates.NULL;
    public CameraStates wantedCamState = CameraStates.NULL;

    enum Pipelines {
        WORM, NULL
    }
    public Pipelines currentPipeline = Pipelines.NULL;

    boolean auto;

    @Override
    public void init(final boolean auto) {
        this.auto = auto;
        
        Robot.getInstance().camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                Robot.getInstance().camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
                if (auto) {
                    setScanForWorm();
                } else {

                }
            }

            @Override
            public void onError(int errorCode) {

            }
        });
    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) {
        if (auto) {
            telemetry.addData("Team Shipping Element", getPosOfTarget());
            telemetry.addData("Box", getWormPos());

            Log.i("Box", "" + getWormPos() + ", Pos: " + getPosOfTarget());
        }
    }

    @Override
    public void update() {
        if (currentCamState != wantedCamState) {
            switch (wantedCamState) {
                case WORM:
                    setCamPositions(0, 0.9);
                    break;

                case NULL:
                    break;
            }

            currentCamState = wantedCamState;
        }
    }

    @Override
    public void stop() {
        Robot.getInstance().camera.stopStreaming();
    }

    public AutoModeBase.Boxes getWormPos() {
        AutoModeBase.Boxes box = AutoModeBase.Boxes.LEFT;

        if (Robot.getInstance().isRed) {
            if (Math.abs(getPosOfTarget().x) == 40) {
                box = AutoModeBase.Boxes.LEFT;
            } else if (Math.abs(getPosOfTarget().x) < 25) {
                box = AutoModeBase.Boxes.MIDDLE;
            } else if (Math.abs(getPosOfTarget().x) >= 20) {
                box = AutoModeBase.Boxes.RIGHT;
            }
        } else {
            if (Math.abs(getPosOfTarget().x) == 40) {
                box = AutoModeBase.Boxes.RIGHT;
            } else if (Math.abs(getPosOfTarget().x) < 25) {
                box = AutoModeBase.Boxes.LEFT;
            } else if (Math.abs(getPosOfTarget().x) >= 20) {
                box = AutoModeBase.Boxes.MIDDLE;
            }
        }

        return box;
    }

    public void setScanForWorm() {
        Robot.getInstance().camera.setPipeline(scanForWorm);
        currentPipeline = Pipelines.WORM;
    }

    public void setWormCamPos() {
        wantedCamState = CameraStates.WORM;
    }

    public void setCamPositions(double x, double y) {
//        Robot.getInstance().camXAxis.setPosition(x);
        Robot.getInstance().camYAxis.setPosition(y);
    }

    public Point getPosOfTarget () {
        Point posToReturn = new Point();
        switch (currentPipeline) {
            case WORM:
                posToReturn = scanForWorm.getPoint();
                break;
        }

        return posToReturn;
    }
}
