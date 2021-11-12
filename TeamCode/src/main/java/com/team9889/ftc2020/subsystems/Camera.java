package com.team9889.ftc2020.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.team9889.ftc2020.auto.AutoModeBase;
import com.team9889.lib.detectors.ScanForRS;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Point;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;

/**
 * Created by MannoMation on 10/27/2018.
 */

@Config
public class Camera extends Subsystem{
    ScanForRS scanForRS = new ScanForRS();

    public enum CameraStates {
        RSRIGHT, RSLEFT, NULL
    }
    public CameraStates currentCamState = CameraStates.NULL;
    public CameraStates wantedCamState = CameraStates.NULL;

    enum Pipelines {
        RS, NULL
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
                    setScanForRS();
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

        }
    }

    @Override
    public void update() {
        if (currentCamState != wantedCamState) {
            switch (wantedCamState) {
                case RSRIGHT:
                    setCamPositions(.64, .9);
                    break;

                case RSLEFT:
                    setCamPositions(.4, .9);
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

    public AutoModeBase.Boxes getRSBox () {
        AutoModeBase.Boxes box = AutoModeBase.Boxes.CLOSE;
        if (Math.abs(getPosOfTarget().y) == 0) {
            box = AutoModeBase.Boxes.CLOSE;
        } else if (Math.abs(getPosOfTarget().y) < 15) {
            box = AutoModeBase.Boxes.MIDDLE;
        } else if (Math.abs(getPosOfTarget().y) >= 15) {
            box = AutoModeBase.Boxes.FAR;
        }

        return box;
    }

    public void setScanForRS () {
        Robot.getInstance().camera.setPipeline(scanForRS);
        currentPipeline = Pipelines.RS;
    }

    public void setRSCamPosRight() {
        wantedCamState = CameraStates.RSRIGHT;
    }

    public void setRSCamPosLeft() {
        wantedCamState = CameraStates.RSLEFT;
    }

    public void setCamPositions(double x, double y) {
//        Robot.getInstance().camXAxis.setPosition(x);
        Robot.getInstance().camYAxis.setPosition(y);
    }

    public Point getPosOfTarget () {
        Point posToReturn = new Point();
        switch (currentPipeline) {
            case RS:
                posToReturn = scanForRS.getPoint();
                break;
        }

        return posToReturn;
    }
}
