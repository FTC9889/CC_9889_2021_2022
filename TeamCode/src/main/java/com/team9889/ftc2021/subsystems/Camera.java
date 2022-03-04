package com.team9889.ftc2021.subsystems;

import android.util.Log;

import com.acmerobotics.dashboard.config.Config;
import com.team9889.ftc2021.auto.AutoModeBase;
import com.team9889.lib.detectors.ScanForTSE;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Point;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;

/**
 * Created by MannoMation on 10/27/2018.
 */

@Config
public class Camera extends Subsystem{
    ScanForTSE scanForTSE = new ScanForTSE();

    public enum CameraStates {
        TSE, NULL
    }
    public CameraStates currentCamState = CameraStates.NULL;
    public CameraStates wantedCamState = CameraStates.NULL;

    enum Pipelines {
        TSE, NULL
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
                    setScanForTSE();
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
            telemetry.addData("Pos", getPosOfTarget());
            telemetry.addData("Box", getTSEPos());

            Log.i("Box", "" + getTSEPos() + ", Pos: " + getPosOfTarget());
        }
    }

    @Override
    public void update() {
    }

    @Override
    public void stop() {
        Robot.getInstance().camera.stopStreaming();
    }

    public AutoModeBase.Boxes getTSEPos() {
        AutoModeBase.Boxes box = AutoModeBase.Boxes.RIGHT;

        if (Robot.getInstance().isRed) {
            if (Math.abs(getPosOfTarget().x) == 80) {
                box = AutoModeBase.Boxes.RIGHT;
            } else if (Math.abs(getPosOfTarget().x) < 75) {
                box = AutoModeBase.Boxes.LEFT;
            } else if (Math.abs(getPosOfTarget().x) >= 75) {
                box = AutoModeBase.Boxes.MIDDLE;
            }
        } else {
            if (Math.abs(getPosOfTarget().x) == 80) {
                box = AutoModeBase.Boxes.LEFT;
            } else if (Math.abs(getPosOfTarget().x) >= 75) {
                box = AutoModeBase.Boxes.RIGHT;
            } else if (Math.abs(getPosOfTarget().x) < 75) {
                box = AutoModeBase.Boxes.MIDDLE;
            }
        }

        return box;
    }

    public void setScanForTSE() {
        Robot.getInstance().camera.setPipeline(scanForTSE);
        currentPipeline = Pipelines.TSE;
    }

    public void setTSECamPos() {
        wantedCamState = CameraStates.TSE;
    }

    public void setCamPositions(double x, double y) {
//        Robot.getInstance().camXAxis.setPosition(x);
        Robot.getInstance().camYAxis.setPosition(y);
    }

    public Point getPosOfTarget () {
        Point posToReturn = new Point();
        switch (currentPipeline) {
            case TSE:
                posToReturn = scanForTSE.getPoint();
                break;
        }

        return posToReturn;
    }
}
