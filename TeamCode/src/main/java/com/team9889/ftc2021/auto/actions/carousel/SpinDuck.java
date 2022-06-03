package com.team9889.ftc2021.auto.actions.carousel;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2021.auto.actions.Action;
import com.team9889.ftc2021.subsystems.Robot;

/**
 * Created by Eric on 5/27/2022.
 */
public class SpinDuck extends Action {
    ElapsedTime timer = new ElapsedTime();

    @Override
    public void start() {
        Robot.getInstance().getCarousel().override = true;
        Robot.getInstance().getCarousel().SetWheelsPower(.5);
    }

    @Override
    public void update() {
        if (timer.milliseconds() > 1700) {
            Robot.getInstance().getCarousel().SetWheelsPower(0.5);
        }
    }

    @Override
    public boolean isFinished() {
        return timer.milliseconds() > 3000;
    }

    @Override
    public void done() {
        Robot.getInstance().getCarousel().override = false;
        Robot.getInstance().carousel.setPower(0);
    }
}
