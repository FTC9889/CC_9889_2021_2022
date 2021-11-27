package com.team9889.ftc2020.subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Eric on 8/19/2019.
 */

public class Dumper extends Subsystem {

    public enum GateState {
        OPEN, CLOSED, NULL
    }

    public GateState gateState = GateState.NULL;

    @Override
    public void init(boolean auto) {
        if (auto) {
            gateState = GateState.CLOSED;
        }
    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) { }

    @Override
    public void update() {
        switch (gateState) {
            case CLOSED:
                SetGatePosition(0.8);
                break;

            case OPEN:
                SetGatePosition(0.55);
                break;
        }
    }

    @Override
    public void stop() { }

    public void SetGatePosition(double position){ Robot.getInstance().dumperGate.setPosition(position); }
}