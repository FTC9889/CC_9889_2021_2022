package com.team9889.lib.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Eric on 1/8/2022.
 */
class CCServo {
    public Servo servo;
    private double position = 0;
    private int range = 0;

    public CCServo(HardwareMap hardwareMap, String id, int range, Servo.Direction direction){
        this.servo = hardwareMap.get(Servo.class, id);
        this.range = range;
        this.servo.setDirection(direction);
    }

    public CCServo(HardwareMap hardwareMap, String id, Servo.Direction direction){
        this.servo = hardwareMap.get(Servo.class, id);
        this.range = 360;
        this.servo.setDirection(direction);
    }

    public CCServo(HardwareMap hardwareMap, String id){
        this.servo = hardwareMap.get(Servo.class, id);
        this.servo.setDirection(Servo.Direction.FORWARD);
    }

    public void setPosition(double position){
        this.position = position;
        update();
    }

    public void setAngle(double angle){
        this.position = angle / range;
        update();
    }

    public double getPosition() {
        return position;
    }

    public double getAngle() {
        return position * range;
    }

    public void update(){
        servo.setPosition(position);
    }
}
