package org.firstinspires.ftc.teamcode.decode_auto;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class DriveTrain {
    private final DcMotor lfMotor;
    private final DcMotor lrMotor;
    private final DcMotor rfMotor;
    private final DcMotor rrMotor;

    public DriveTrain(HardwareMap hardwareMap) {
        lfMotor = hardwareMap.get(DcMotor.class, "FL");
        lrMotor = hardwareMap.get(DcMotor.class, "BL");
        rfMotor = hardwareMap.get(DcMotor.class, "FR");
        rrMotor = hardwareMap.get(DcMotor.class, "BR");

        lfMotor.setDirection(DcMotor.Direction.FORWARD);
        lrMotor.setDirection(DcMotor.Direction.FORWARD);
        rfMotor.setDirection(DcMotor.Direction.REVERSE);
        rrMotor.setDirection(DcMotor.Direction.REVERSE);

        setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setRunMode(DcMotor.RunMode runMode) {
        lfMotor.setMode(runMode);
        lrMotor.setMode(runMode);
        rfMotor.setMode(runMode);
        rrMotor.setMode(runMode);
    }
    public void drive(double forward, double strafe, double turn) {
        double leftFrontPower = forward + strafe + turn;
        double rightFrontPower = forward - strafe - turn;
        double leftBackPower = forward - strafe + turn;
        double rightBackPower = forward + strafe - turn;

        double max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));

        if (max > 1.0) {
            leftFrontPower /= max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }

        lfMotor.setPower(leftFrontPower);
        rfMotor.setPower(rightFrontPower);
        lrMotor.setPower(leftBackPower);
        rrMotor.setPower(rightBackPower);
    }

    public void stop() {
        drive(0, 0, 0);
    }
}
