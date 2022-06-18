package com.team9889.ftc2021.subsystems;

import android.util.Log;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import com.team9889.ftc2021.Constants;
import com.team9889.ftc2021.DriverStation;
import com.team9889.ftc2021.auto.actions.ActionVariables;
import com.team9889.lib.android.FileReader;
import com.team9889.lib.android.FileWriter;
import com.team9889.lib.hardware.ModernRoboticsUltrasonic;
import com.team9889.lib.hardware.Motor;
import com.team9889.lib.hardware.RevIMU;
import com.team9889.lib.roadrunner.drive.SampleMecanumDrive;
import com.team9889.lib.roadrunner.drive.StandardTrackingWheelLocalizer;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.RevBulkData;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.team9889.ftc2021.Team9889Linear.writeLastKnownPosition;


/**
 * Created by Eric on 7/26/2019.
 */

public class Robot {

    public WebcamName webcam, frontCam;
    public OpenCvCamera camera, frontCVCam;

    public Motor fLDrive, fRDrive, bLDrive, bRDrive;
    public RevIMU imu = null;

    public AnalogInput distLeft, distRight, distForward, distBack;
//    public ModernRoboticsI2cRangeSensor rangeSensor;
    public ModernRoboticsUltrasonic rangeSensor;

    public Motor intake, passThrough;
    public Servo intakeLift;
    public RevColorSensorV3 inLeft;
    public DistanceSensor inRight;
    public RevTouchSensor intakeGate;

    public Motor lift;
    public Servo angleAdjust1, angleAdjust2, angleAdjust3;
    public RevTouchSensor downLimit;

    public Servo dumperGate;

    public Servo capRelease;
    public Servo capArm;

    public Motor carousel;
    public RevTouchSensor redLimit, blueLimit;

    public Servo camYAxis;

    public Servo flag;

    public RevBulkData bulkDataMaster, bulkDataSlave;
    public ExpansionHubEx revHubMaster, revHubSlave;

    public HardwareMap hardwareMap;

    public ActionVariables actionVariables = new ActionVariables();

    public boolean isRed = true, auto;

    public ElapsedTime robotTimer = new ElapsedTime(), loopTime = new ElapsedTime();

    public double result = Double.POSITIVE_INFINITY;

    public boolean slowdown = false;

    public Telemetry telemetry;

    public FileWriter writer;
    public FileReader reader;
    public String[] lines;

    private static Robot mInstance = null;

    public static Robot getInstance() {
        if (mInstance == null)
            mInstance = new Robot();

        return mInstance;
    }

    private MecanumDrive mMecanumDrive = new MecanumDrive();
    private Intake mIntake = new Intake();
    private Lift mLift = new Lift();
    private CapArm mCapArm = new CapArm();
    private Dumper mDumper = new Dumper();
    private Carousel mCarousel = new Carousel();
    private Camera mCamera = new Camera();

    public boolean rrCancelable = false;
    public SampleMecanumDrive rr;
    public StandardTrackingWheelLocalizer localizer;

    public DriverStation driverStation;

    // List of subsystems
    private List<Subsystem> subsystems = Arrays.asList(mMecanumDrive, mIntake, mLift, mCapArm, mDumper, mCarousel, mCamera);

    public void init(HardwareMap hardwareMap, boolean auto){
        this.hardwareMap = hardwareMap;

        Date currentData = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd.M.yyyy hh:mm:ss");

        RobotLog.a("Robot Init Started at " + format.format(currentData));
        reader = new FileReader("AutoInfo.txt");
        lines = reader.lines();
        reader.close();

        writer = new FileWriter("AutoInfo.txt");

        //Rev Hubs
        revHubMaster = hardwareMap.get(ExpansionHubEx.class, Constants.kRevHubMaster);
        revHubSlave = hardwareMap.get(ExpansionHubEx.class, Constants.kRevHubSlave);

        //Camera
        webcam = hardwareMap.get(WebcamName.class, Constants.kWebcam);
        frontCam = hardwareMap.get(WebcamName.class, Constants.kFrontCam);
        camera = OpenCvCameraFactory.getInstance().createWebcam(webcam);
        frontCVCam = OpenCvCameraFactory.getInstance().createWebcam(frontCam);
//        camXAxis = hardwareMap.get(Servo.class, Constants.CameraConstants.kCamX);
        camYAxis = hardwareMap.get(Servo.class, Constants.CameraConstants.kCamY);

        //Drive
        fLDrive = new Motor(hardwareMap, Constants.DriveConstants.kLeftDriveMasterId, 1,
                DcMotorSimple.Direction.REVERSE, true, false, true);
        bLDrive = new Motor(hardwareMap, Constants.DriveConstants.kLeftDriveSlaveId, 1,
                DcMotorSimple.Direction.REVERSE, true, false, true);
        fRDrive = new Motor(hardwareMap, Constants.DriveConstants.kRightDriveMasterId, 1,
                DcMotorSimple.Direction.FORWARD, true, false, true);
        bRDrive = new Motor(hardwareMap, Constants.DriveConstants.kRightDriveSlaveId, 1,
                DcMotorSimple.Direction.FORWARD, true, false, true);

        distLeft = hardwareMap.get(AnalogInput.class, Constants.DriveConstants.kDistanceLeft);
        distRight = hardwareMap.get(AnalogInput.class, Constants.DriveConstants.kDistanceRight);
//        distForward = hardwareMap.get(DistanceSensor.class, Constants.DriveConstants.kDistanceForward);
//        distBack = hardwareMap.get(DistanceSensor.class, Constants.DriveConstants.kDistanceBack);

        rangeSensor = new ModernRoboticsUltrasonic("sensor_range", hardwareMap);

        //Intake
        intake = new Motor(hardwareMap, Constants.IntakeConstants.kIntake, 1,
                DcMotorSimple.Direction.REVERSE, false, true, false);

        passThrough = new Motor(hardwareMap, Constants.IntakeConstants.kPassThrough, 1,
                DcMotorSimple.Direction.REVERSE, false, true, false);

        intakeLift = hardwareMap.get(Servo.class, Constants.IntakeConstants.kIntakeLift);

        inLeft = hardwareMap.get(RevColorSensorV3.class, Constants.IntakeConstants.kLeft);
        inRight = hardwareMap.get(DistanceSensor.class, Constants.IntakeConstants.kRight);

        intakeGate = hardwareMap.get(RevTouchSensor.class, Constants.IntakeConstants.kIntakeGate);

        //Lift
        lift = new Motor(hardwareMap, Constants.LiftConstants.kLift, 1,
                DcMotorSimple.Direction.FORWARD, true, true, true);

        angleAdjust1 = hardwareMap.get(Servo.class, Constants.LiftConstants.kAngleAdjust1);
        angleAdjust2 = hardwareMap.get(Servo.class, Constants.LiftConstants.kAngleAdjust2);
        angleAdjust3 = hardwareMap.get(Servo.class, Constants.LiftConstants.kAngleAdjust3);

        downLimit = hardwareMap.get(RevTouchSensor.class, Constants.LiftConstants.kDownLimit);

        //Dumper
        dumperGate = hardwareMap.get(Servo.class, Constants.DumperConstants.kGate);
//        dumperGate = new CCServo(hardwareMap, Constants.DumperConstants.kGate, 90, 400, Servo.Direction.FORWARD);

        //Cap
        capArm = hardwareMap.get(Servo.class, Constants.CapConstants.kArm);
        capRelease = hardwareMap.get(Servo.class, Constants.CapConstants.kRelease);
//        capArm = new CCServo(hardwareMap, Constants.CapConstants.kArm, 180, 240, Servo.Direction.FORWARD);

        //Carousel
        carousel = new Motor(hardwareMap, Constants.CarouselConstants.kCarousel, 1,
                DcMotorSimple.Direction.FORWARD, true, true, false);

        redLimit = hardwareMap.get(RevTouchSensor.class, Constants.CarouselConstants.kCarouselRedLimit);
        blueLimit = hardwareMap.get(RevTouchSensor.class, Constants.CarouselConstants.kCarouselBlueLimit);

        flag = hardwareMap.get(Servo.class, Constants.IntakeConstants.kIntakeFlag);


        imu = new RevIMU("imu", hardwareMap);

        // Init all subsystems
        for (Subsystem subsystem : subsystems) {
            subsystem.init(auto);
        }

        this.auto = auto;

        rr = new SampleMecanumDrive(hardwareMap);
//        localizer = (StandardTrackingWheelLocalizer) rr.getLocalizer();

//        this.driverStation = driverStation;

        robotTimer.reset();
        loopTime.reset();
    }

    // Update data from Hubs and Apply new data
    public void update() {
        Log.v("Loop Time 1", "" + loopTime.milliseconds());
        // Update Bulk Data
        try {
            bulkDataMaster = revHubMaster.getBulkInputData();
            bulkDataSlave = revHubSlave.getBulkInputData();

            Log.v("Loop Time 2", "" + loopTime.milliseconds());

            // Update Motors
            fRDrive.update(bulkDataMaster);
            fLDrive.update(bulkDataMaster);
            bRDrive.update(bulkDataMaster);
            bLDrive.update(bulkDataMaster);
            lift.update(bulkDataSlave);

            Log.v("Loop Time 3", "" + loopTime.milliseconds());

//            capArm.update(loopTime.milliseconds());

            // Update Subsystems
            for (Subsystem subsystem : subsystems)
                subsystem.update();

            Log.v("Loop Time 4", "" + loopTime.milliseconds());

            if (auto) {
                rr.getLocalizer().update();
            }
        } catch (Exception e){
            Log.v("Exception@robot.update", "" + e);
        }

        Log.v("Loop Time 5", "" + loopTime.milliseconds());

        loopTime.reset();
    }

    // Output Telemetry for all subsystems
    public void outputToTelemetry(Telemetry telemetry) {
        telemetry.addData("Loop Time", loopTime.milliseconds());

        if (isRed)
            telemetry.addData("<font size=\"+2\" color=\"red\">Side</font>", "");
        else
            telemetry.addData("<font size=\"+2\" color=\"aqua\">Side</font>", "");

        for (Subsystem subsystem : subsystems)
            subsystem.outputToTelemetry(telemetry);


//        TelemetryPacket packet = new TelemetryPacket();
//        packet.fieldOverlay()
//                .setFill("green")
//                .fillRect(rr.getPoseEstimate().getX() - 6.5, rr.getPoseEstimate().getY() - 6.5, 13, 13);
//        FtcDashboard.getInstance().sendTelemetryPacket(packet);
    }

    // Stop all subsystems
    public void stop(){
        for (Subsystem subsystem : subsystems)
            subsystem.stop();

        if (isRed) {
            writeLastKnownPosition("Red", "Side");
        } else {
            writeLastKnownPosition("Blue", "Side");
        }
//        revHubMaster.close();
//        revHubSlave.close();
    }

    public MecanumDrive getMecanumDrive(){
        return mMecanumDrive;
    }

    public Intake getIntake(){
        return mIntake;
    }

    public Lift getLift(){
        return mLift;
    }

    public CapArm getCapArm(){
        return mCapArm;
    }

    public Dumper getDumper(){
        return mDumper;
    }

    public Carousel getCarousel(){
        return mCarousel;
    }

    public Camera getCamera(){
        return mCamera;
    }

}
