package com.team9889.ftc2021;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Created by MannoMation on 12/14/2018.
 */
public class DriverStation {
    boolean gamepad2Lift = false;

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
        return gamepad1.b || gamepad2.back;
    }

    boolean getOuttake() {
        return gamepad1.y;
    }

    boolean getAutoLineup() {
        return gamepad1.x;
    }

    private boolean intakeHeightToggle = true;
    public boolean intakeDown = false;
    public  boolean intakeCodePress = false;
    boolean getIntakeHeight() {
        if ((gamepad1.x || intakeCodePress) && intakeHeightToggle) {
            intakeDown = !intakeDown;
            intakeHeightToggle = false;
        } else if (!gamepad1.x && !intakeCodePress)
            intakeHeightToggle = true;

        return intakeDown;
    }

    double getLiftExtend() {
        return (gamepad1.right_trigger - gamepad1.left_trigger) - gamepad2.right_stick_y;
    }

    double getLiftRaise(){
        return -gamepad2.left_stick_y;
    }

    boolean getSetLift() {
        return gamepad1.left_bumper;
    }

    boolean getScore() {
        return gamepad1.right_bumper;
    }

    private boolean liftToggle = true;
    public boolean liftDown = true;
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
    public boolean dumperOpen = false;
    public  boolean dumperCodePress = false;
    boolean getDumperOpen() {
        if ((gamepad2.left_bumper || dumperCodePress) && dumperToggle) {
            dumperOpen = !dumperOpen;
            dumperToggle = false;
        } else if (!gamepad2.left_bumper && !dumperCodePress)
            dumperToggle = true;

        return dumperOpen;
    }


    private boolean carouselToggle = true;
    private boolean carouselOn = false;
    public  boolean carouselCodePress = false;
    boolean getCarouselOn() {
        if ((gamepad2.right_bumper || carouselCodePress) && carouselToggle) {
            carouselOn = !carouselOn;
            carouselToggle = false;
        } else if (!gamepad2.right_bumper && !carouselCodePress)
            carouselToggle = true;

        return carouselOn;
    }


    private boolean slowOn = false;
    private boolean slowToggle = false;
    boolean getSlow() {
        if (gamepad1.dpad_down && !slowToggle) {
            slowOn = !slowOn;
            slowToggle = true;
            return true;
        } else if (!gamepad1.dpad_down)
            slowToggle = false;

        return slowOn;
    }

    private boolean blueToggle = false;
    boolean getBlue() {
        if ((gamepad2.left_stick_button && gamepad2.right_stick_button) && !blueToggle) {
            blueToggle = true;
            return true;
        } else if (!gamepad2.left_stick_button && !gamepad2.right_stick_button)
            blueToggle = false;

        return false;
    }


    private boolean capForwardLastValue = false;
    boolean getCapForward() {
        if (gamepad2.dpad_right && !capForwardLastValue) {
            capForwardLastValue = true;
            return true;
        }

        capForwardLastValue = gamepad2.dpad_right;
        return false;
    }

    private boolean capBackLastValue = false;
    boolean getCapBack() {
        if (gamepad2.dpad_left && !capBackLastValue) {
            capBackLastValue = true;
            return true;
        }

        capBackLastValue = gamepad2.dpad_left;
        return false;
    }

    boolean resetCap() {
        return gamepad1.left_trigger >= 0.2;
    }


    boolean resetIMU() {
        return gamepad1.right_stick_button && gamepad1.left_stick_button;
    }



    boolean getDown() {
        return gamepad2.b;
    }

    boolean getFirstLayer() {
        return gamepad2.a;
    }

    boolean getSecondLayer() {
        return gamepad2.x;
    }

    boolean getThirdLayer() {
        return gamepad2.y;
    }

    boolean getSharedClose() {
        return gamepad2.dpad_down;
    }

    boolean getSharedFar() {
        return gamepad2.dpad_up;
    }

    boolean getDefault() {
        return gamepad1.left_bumper;
    }

    boolean getSetDefault() {
        return gamepad2.right_trigger >= 0.2;
    }
}
