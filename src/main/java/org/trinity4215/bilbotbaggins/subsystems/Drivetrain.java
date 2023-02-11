// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.bilbotbaggins.subsystems;


import org.trinity4215.bilbotbaggins.CombinedLogging;

import edu.wpi.first.wpilibj.ADIS16470_IMU;
import edu.wpi.first.wpilibj.ADIS16470_IMU.IMUAxis;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class Drivetrain extends SubsystemBase {


    private final ADIS16470_IMU gyro = new ADIS16470_IMU();
    private edu.wpi.first.wpilibj.ADIS16470_IMU.IMUAxis axis = null; 


    public Drivetrain() {
        axis = IMUAxis.kY;
    }


    public abstract void driveTank(double left, double right);

    public void driveArcade(double speed, double twist) {
        double right = (2 * speed + twist) / 2;

        double left = 2 * speed - right;

        driveTank(left, right);
    }
    
    
    public void resetGyro() {
        gyro.reset();
    }
    
    public double getGyroAngle() {
        return gyro.getAngle();
    }

    public double getGyroVel() {
        return gyro.getRate();
    }
    
    public double getGyroY() {
        
        IMUAxis curAxis = gyro.getYawAxis();
        CombinedLogging.putString("CurrentAxis", curAxis.toString());
        if (curAxis == IMUAxis.kY) {
            double angle = gyro.getAngle();
            CombinedLogging.putNumber("CurrentAngle", angle);
            return angle;
        } else {
            axis = IMUAxis.kY;
            gyro.setYawAxis(axis);
            double angle = gyro.getAngle();
            CombinedLogging.putNumber("CurrentAngle", angle);
            return angle;
        }
    }

    public double getGyroX() {

        IMUAxis curAxis = gyro.getYawAxis();
        CombinedLogging.putString("CurrentAxis", curAxis.toString());
        if (curAxis == IMUAxis.kX) {
            double angle = gyro.getAngle();
            CombinedLogging.putNumber("CurrentAngle", angle);
            return angle;
        } else {
            axis = IMUAxis.kX;
            gyro.setYawAxis(axis);
            double angle = gyro.getAngle();
            CombinedLogging.putNumber("CurrentAngle", angle);
            return angle;
        }
    }

    public double getGyroZ() {

        IMUAxis curAxis = gyro.getYawAxis();
        CombinedLogging.putString("CurrentAxis", curAxis.toString());
        if (curAxis == IMUAxis.kZ) {
            double angle = gyro.getAngle();
            CombinedLogging.putNumber("CurrentAngle", angle);
            return angle;
        } else {
            axis = IMUAxis.kZ;
            gyro.setYawAxis(axis);
            double angle = gyro.getAngle();
            CombinedLogging.putNumber("CurrentAngle", angle);
            return angle;
        }
    }

    public IMUAxis getCurrentGyroAxis() {
        return gyro.getYawAxis();
    }


    /**
     * Although it is not compiler-enforced, this 
     * should return <b>constant</b> constants.
     */
    public abstract DrivetrainConstants getConstants();

    /**
     * `NullPointerException` protector
     */
    public abstract void setConstants();


    /**
     * Abstracted constants for driving.
     * yes they're functions. The other option is
     * publicly available (& thus publicly
     * <i>changeable</i>)
     */
    public interface DrivetrainConstants {

        double kMaxSpeedPercent();
        double kMinTurnSpeed();
        /**
         * in degrees in each direction from the
         * target. The total deadzone size is twice 
         * this number
         */
        default double kAngularDeadZone() {
            return 1;
        }


        double kSlewValue();

        // this will probably need more stuff
    }

}
