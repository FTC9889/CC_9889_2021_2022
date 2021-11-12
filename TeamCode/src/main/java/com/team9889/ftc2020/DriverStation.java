package com.team9889.ftc2020;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Created by MannoMation on 12/14/2018.
 */
public class DriverStation {
    private Gamepad gamepad1;
    private Gamepad gamepad2;

    public DriverStation(Gamepad gamepad1, Gamepad gamepad2){
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    double getX(){
        return gamepad1.left_stick_x;
    }

    double getY() {
        return -gamepad1.left_stick_y;
    }

    double getSteer(){
        return gamepad1.right_stick_x;
    }

    boolean getIntake(){
        return gamepad1.a;
    }

    boolean getStopIntake() {
        return gamepad1.b || gamepad2.left_trigger > .3;
    }

    boolean getOuttake() {
        return gamepad1.y;
    }

    private boolean intakeHeightToggle = true;
    private boolean intakeDown = false;
    public  boolean intakeCodePress = false;
    boolean getIntakeHeight() {
        if ((gamepad1.x || intakeCodePress) && intakeHeightToggle) {
            intakeDown = !intakeDown;
            intakeHeightToggle = false;
        } else if (!gamepad1.x && !intakeCodePress)
            intakeHeightToggle = true;

        return intakeDown;
    }

    double getLiftUp() {
        return gamepad1.right_trigger;
    }

    double getLiftDown() {
        return gamepad1.left_trigger;
    }

    boolean getSetLift() {
        return gamepad1.left_bumper;
    }

    private boolean liftToggle = true;
    private boolean liftDown = true;
    public  boolean liftCodePress = false;
    boolean getLiftHeight() {
        if ((getSetLift() || liftCodePress) && liftToggle) {
            liftDown = !liftDown;
            liftToggle = false;
        } else if (!getSetLift() && !liftCodePress)
            liftToggle = true;

        return liftDown;
    }


    private boolean dumperToggle = true;
    private boolean dumperOpen = true;
    public  boolean dumperCodePress = false;
    boolean getDumperOpen() {
        if ((gamepad1.right_bumper || dumperCodePress) && dumperToggle) {
            dumperOpen = !dumperOpen;
            dumperToggle = false;
        } else if (!getSetLift() && !dumperCodePress)
            dumperToggle = true;

        return dumperOpen;
    }

//    private boolean aimToggle = true;
//    private boolean aimOn = false;
//    public boolean codeToggle = false;
//    boolean getAim() {
//        if((gamepad2.left_bumper || codeToggle) && aimToggle) {
//            aimOn = !aimOn;
//            aimToggle = false;
//            codeToggle = false;
//        } else if(!gamepad2.left_bumper && !codeToggle)
//            aimToggle = true;
//
//        return aimOn;
//    }

    boolean getAim() {
        return gamepad2.left_bumper;
    }

    boolean getShoot() {
        return gamepad1.right_bumper;
    }

    private boolean psToggle = true;
    public boolean psOn = false;
    boolean getPS() {
        if(gamepad1.left_trigger > 0.3 && psToggle) {
            psOn = !psOn;
            psToggle = false;
        } else if(gamepad1.left_trigger <= 0.1)
            psToggle = true;

        return psOn;
    }

    boolean getAutoPS () {return gamepad1.dpad_right;}

    private boolean psFirstToggle = true;
    private boolean psFirst = true;
    boolean getPSFirst() {
        if(gamepad2.a && psFirstToggle) {
            psFirst = !psFirst;
            psFirstToggle = false;
        } else if(!gamepad2.a)
            psFirstToggle = true;

        return psFirst;
    }

    private boolean psSecondToggle = true;
    private boolean psSecond = true;
    boolean getPSSecond() {
        if(gamepad2.b && psSecondToggle) {
            psSecond = !psSecond;
            psSecondToggle = false;
        } else if(!gamepad2.b)
            psSecondToggle = true;

        return psSecond;
    }

    private boolean psThirdToggle = true;
    private boolean psThird = false;
    boolean getMiddleGoal() {
        if(gamepad2.y && psThirdToggle) {
            psThird = !psThird;
            psThirdToggle = false;
        } else if(!gamepad2.y)
            psThirdToggle = true;

        return psThird;
    }

    private boolean goalToggle = true;
    private boolean middleGoal = false;
    boolean getGoal() {
        if(gamepad2.x && goalToggle) {
            middleGoal = !middleGoal;
            goalToggle = false;
        } else if(!gamepad2.x)
            goalToggle = true;

        return middleGoal;
    }

    boolean resetIMU() {
        return gamepad1.right_stick_button && gamepad1.left_stick_button;
    }


}
