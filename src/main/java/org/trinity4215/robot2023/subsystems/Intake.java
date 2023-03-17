// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.subsystems;

import org.trinity4215.robot2023.CombinedLogging;
import org.trinity4215.robot2023.Constants.IntakeConstants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxRelativeEncoder.Type;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

enum IntakeDriveDirection {
    kForward, kBackwards
}

public class Intake extends SubsystemBase {

    private static Intake subsystemInst = null;

    private CANSparkMax spinMotor = new CANSparkMax(IntakeConstants.kRunSparkID, MotorType.kBrushed);
    private RelativeEncoder spinEncoder = spinMotor.getEncoder(Type.kQuadrature,
            IntakeConstants.kRevEncoderCountsPerRevolution);

    private CANSparkMax raiseMotor = new CANSparkMax(IntakeConstants.kRaiseSparkID, MotorType.kBrushed);
    private RelativeEncoder raiseEncoder = raiseMotor.getEncoder(Type.kQuadrature,
            IntakeConstants.kRevEncoderCountsPerRevolution);

    private DigitalInput armDeployedLimitSwitch = new DigitalInput(IntakeConstants.kLimitSwitchPort);

    private boolean _isArmInDeadzone = false;

    private Intake() {

        spinMotor.restoreFactoryDefaults();
        spinMotor.setIdleMode(IdleMode.kBrake);
        spinEncoder.setPositionConversionFactor(IntakeConstants.kPositionConversionFactor);
        spinEncoder.setPosition(0);

        raiseMotor.restoreFactoryDefaults();
        raiseMotor.setIdleMode(IdleMode.kCoast);
        raiseEncoder.setPositionConversionFactor(IntakeConstants.kPositionConversionFactor);

        System.out.println(this + ": Intake initialized!");

    }

    public static Intake getInstance() {
        if (subsystemInst == null) {
            subsystemInst = new Intake();
        }
        return subsystemInst; // Ensures that only one Drivetrain instance exists at once
    }

    public void runIntake(double speed) {
        spinMotor.set(speed);
    }

    public double getAbsoluteEncoderPosition() {
        return raiseEncoder.getPosition();
    }

    public void driveToDegrees(double targetPosition, double speed) {
        _isArmInDeadzone = false;
        double currentPosition = raiseEncoder.getPosition();
        CombinedLogging.putNumber("IntakeRaiseEncoderPosition", currentPosition);

        if (currentPosition > 10000) {
            raiseMotor.set(speed);
            return;
        }

        if ((Math.abs(currentPosition) - Math.abs(targetPosition) < IntakeConstants.kOuterDeadzoneArea / 2)
                && (Math.abs(currentPosition) - Math.abs(targetPosition) > -IntakeConstants.kOuterDeadzoneArea / 2)) {
            speed = .3 * speed;
            CombinedLogging.putBoolean(IntakeConstants.kOuterDeadzoneArea / 2 + "deg", true);
        } else {
            CombinedLogging.putBoolean(IntakeConstants.kOuterDeadzoneArea / 2 + "deg", false);
        }

        if (currentPosition < targetPosition - (IntakeConstants.kInnerDeadzoneArea / 2)) {
            raiseMotor.set(speed);
        } else if (currentPosition > targetPosition + (IntakeConstants.kInnerDeadzoneArea / 2)) {
            raiseMotor.set(-speed);
        } else {
            stopRaise();
            _isArmInDeadzone = true;
        }
    }

    public boolean isArmInDeadzone() {
        return _isArmInDeadzone;
    }

    public boolean isArmDown() {
        return armDeployedLimitSwitch.get();
    }

    public enum IntakeType {
        CONE, CUBE
    }

    public void spit(IntakeType intakeType) {
        spinMotor.set(IntakeConstants.kIntakeSpitSpeed * (intakeType == IntakeType.CONE ? 1 : -1));
    }

    public void suck(IntakeType intakeType) {
        spinMotor.set(IntakeConstants.kIntakeSuckSpeed * (intakeType == IntakeType.CONE ? 1 : -1));
    }

    public boolean hasCone(IntakeType intakeType) {

        if (intakeType == IntakeType.CONE) {

        }
        return false;
    }

    public void stopSpin() {
        spinMotor.set(0);
    }

    public void stopRaise() {
        raiseMotor.set(0);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

}
