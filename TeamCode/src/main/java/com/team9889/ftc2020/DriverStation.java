package com.team9889.ftc2020;

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
        return gamepad1.b || gamepad2.left_trigger > .3;
    }

    boolean getOuttake() {
        return gamepad1.y;
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

    double getLiftUp() {
        return gamepad1.right_trigger + gamepad2.right_trigger;
    }

    double getLiftDown() {
        return gamepad1.left_trigger + gamepad2.left_trigger;
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
        if ((gamepad2.dpad_left || dumperCodePress) && dumperToggle) {
            dumperOpen = !dumperOpen;
            dumperToggle = false;
        } else if (!gamepad2.dpad_left && !dumperCodePress)
            dumperToggle = true;

        return dumperOpen;
    }


    private boolean carouselToggle = true;
    private boolean carouselOn = false;
    public  boolean carouselCodePress = false;
    boolean getCarouselOn() {
        if ((gamepad1.dpad_down || gamepad2.right_bumper || carouselCodePress) && carouselToggle) {
            carouselOn = !carouselOn;
            carouselToggle = false;
        } else if (!gamepad1.dpad_down && !gamepad2.right_bumper && !carouselCodePress)
            carouselToggle = true;

        return carouselOn;
    }


    private boolean blueToggle = true;
    private boolean blue = false;
    boolean getBlue() {
        if (gamepad2.dpad_up && blueToggle) {
            blue = !blue;
            blueToggle = false;
        } else if (gamepad2.dpad_up)
            blueToggle = true;

        return blue;
    }


    boolean resetIMU() {
        return gamepad1.right_stick_button && gamepad1.left_stick_button;
    }



    boolean getChangeLift() {
        return gamepad2.left_bumper;
    }

    boolean getShared() {
        return gamepad2.a;
    }

    boolean getFirstLayer() {
        return gamepad2.b;
    }

    boolean getSecondLayer() {
        return gamepad2.y;
    }

    boolean getThirdLayer() {
        return gamepad2.x;
    }
}
