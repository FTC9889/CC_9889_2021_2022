package com.team9889.ftc2021.subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Eric on 8/19/2019.
 */

public class Dumper extends Subsystem {
    double lastPosition = 0;

    public enum GateState {
        OPEN, CLOSED, PARTIAL, NULL
    }

    public GateState gateState = GateState.NULL;

    @Override
    public void init(boolean auto) {
        if (auto) {
            gateState = GateState.PARTIAL;
        }
    }

    @Override
    public void outputToTelemetry(Telemetry telemetry) { }

    @Override
    public void update() {
        switch (gateState) {
            case CLOSED:
                SetGatePosition(0.05);
                break;

            case PARTIAL:
                SetGatePosition(0.05);
                break;

            case OPEN:
                SetGatePosition(0.6);
                break;
        }
    }

    @Override
    public void stop() { }

    public void SetGatePosition(double position){
        Robot.getInstance().dumperGate.setPosition(position);
    }
}