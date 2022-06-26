package com.team9889.ftc2021.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.team9889.ftc2021.auto.AutoModeBase;
import com.team9889.lib.detectors.Blank;
import com.team9889.lib.detectors.DetectGreenTSE;
import com.team9889.lib.detectors.ScanForDuck;
import com.team9889.lib.detectors.ScanForHub;
import com.team9889.lib.detectors.ScanForTSEObject;
import com.team9889.lib.detectors.ScanForTSEObjectRGB;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Point;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;

/**
 * Created by MannoMation on 10/27/2018.
 */

@Config
public class Camera extends Subsystem{
//    ScanForTSE scanForTSE = new ScanForTSE();
    ScanForTSEObject scanForTSE = new ScanForTSEObject();
    public ScanForDuck scanForDuck = new ScanForDuck();
    public ScanForHub scanForHub = new ScanForHub();

    public DetectGreenTSE detectGreenTSE = new DetectGreenTSE();

    public Blank blank = new Blank();

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
//                    setScanForTSE();
//                    Robot.getInstance().camera.setPipeline(blank);
                    Robot.getInstance().camera.setPipeline(detectGreenTSE);
                } else {
                    Robot.getInstance().camera.setPipeline(scanForHub);
                }
            }

            @Override
            public void onError(int errorCode) {

            }
        });

        Robot.getInstance().frontCVCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                Robot.getInstance().frontCVCam.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
                if (auto) {
//                    setScanForTSE();
                    Robot.getInstance().frontCVCam.setPipeline(scanForDuck);
                } else {
                    Robot.getInstance().frontCVCam.setPipeline(scanForDuck);
                }
            }

            @Override
            public void onError(int errorCode) {

            }
        });

        Robot.getInstance().camYAxis.setPosition(0.37);
    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) {
//        if (auto) {
            telemetry.addData("Pos", getPosOfTarget());
            telemetry.addData("Box", getTSEPos());

            telemetry.addData("Duck Point", scanForDuck.getPoint());
//        }
    }

    @Override
    public void update() {
    }

    @Override
    public void stop() {
        Robot.getInstance().camera.stopStreaming();
        Robot.getInstance().frontCVCam.stopStreaming();
    }

//    public AutoModeBase.Boxes getTSEPos() {
//        AutoModeBase.Boxes box = AutoModeBase.Boxes.RIGHT;
//
//        if (scanForTSE.getPoint().size() > 0) {
//            if (Robot.getInstance().isRed) {
//                if (scanForTSE.getPoint().size() >= 2) {
//                    box = AutoModeBase.Boxes.RIGHT;
//                } else if (scanForTSE.getPoint().get(0).x < 75) {
//                    box = AutoModeBase.Boxes.MIDDLE;
//                } else if (scanForTSE.getPoint().get(0).x >= 75) {
//                    box = AutoModeBase.Boxes.LEFT;
//                }
//            } else {
//                if (scanForTSE.getPoint().size() >= 2) {
//                    box = AutoModeBase.Boxes.LEFT;
//                } else if (Math.abs(getPosOfTarget().x) >= 75) {
//                    box = AutoModeBase.Boxes.MIDDLE;
//                } else if (Math.abs(getPosOfTarget().x) < 75) {
//                    box = AutoModeBase.Boxes.RIGHT;
//                }
//            }
//        }
//
//        return box;
//    }

    public AutoModeBase.Boxes getTSEPos() {
//        AutoModeBase.Boxes box = AutoModeBase.Boxes.RIGHT;
//
//        if (Robot.getInstance().isRed) {
//            if (Math.abs(getPosOfTarget().x) == 80) {
//                box = AutoModeBase.Boxes.RIGHT;
//            } else if (Math.abs(getPosOfTarget().x) < 75) {
//                box = AutoModeBase.Boxes.LEFT;
//            } else if (Math.abs(getPosOfTarget().x) >= 75) {
//                box = AutoModeBase.Boxes.MIDDLE;
//            }
//        } else {
//            if (Math.abs(getPosOfTarget().x) == 80) {
//                box = AutoModeBase.Boxes.LEFT;
//            } else if (Math.abs(getPosOfTarget().x) >= 75) {
//                box = AutoModeBase.Boxes.RIGHT;
//            } else if (Math.abs(getPosOfTarget().x) < 75) {
//                box = AutoModeBase.Boxes.MIDDLE;
//            }
//        }
//
//        return box;

        return detectGreenTSE.getPosition();
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
//                if (scanForTSE.getPoint().size() > 0)
//                    posToReturn = scanForTSE.getPoint().get(0);
//                else
//                    posToReturn = new Point(1e10, 1e10);
//                break;

                posToReturn = scanForTSE.getPoint();
        }

        return posToReturn;
    }
}
