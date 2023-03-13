// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.subsystems;

import java.security.InvalidKeyException;

import org.trinity4215.robot2023.CombinedLogging;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxRelativeEncoder.Type;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

enum IntakeDriveDirection {
  kForward, kBackwards
}

public class Intake extends SubsystemBase {

  private static Intake subsystemInst = null;
  private CANSparkMax intakeRunSparkMax = new CANSparkMax(17, MotorType.kBrushed);
  private CANSparkMax intakeRaiseSparkMax = new CANSparkMax(18, MotorType.kBrushed);
  private RelativeEncoder intakeRaiseEncoder = intakeRaiseSparkMax.getEncoder(Type.kQuadrature, 8192);

  /** Creates a new Intake. */
  public Intake() {

    intakeRunSparkMax.restoreFactoryDefaults();
    intakeRunSparkMax.setIdleMode(IdleMode.kBrake);

    intakeRaiseSparkMax.restoreFactoryDefaults();
    // intakeRaiseSparkMax.setIdleMode(IdleMode.kBrake);

    intakeRaiseEncoder.setPositionConversionFactor(180);

    System.out.println("Intake initialized!");

  }

  public static Intake getInstance() {
    if (subsystemInst == null) {
      subsystemInst = new Intake();
    }
    return subsystemInst; // Ensures that only one Drivetrain instance exists at once
  }

  public void runIntake(double speed) {
    intakeRunSparkMax.set(speed);
  }

  public double getAbsoluteEncoderPosition() {
    return intakeRaiseEncoder.getPosition();
  }

  public void driveToDegrees(double targetPosition, double speed) {
    double currentPosition = intakeRaiseEncoder.getPosition();
    CombinedLogging.putNumber("IntakeRaiseEncoderPosition", currentPosition);
    if (currentPosition < targetPosition - 1) {
      intakeRaiseSparkMax.set(speed);
    } else if (currentPosition > targetPosition + 1) {
      intakeRaiseSparkMax.set(-speed);
    } else {
       intakeRaiseSparkMax.set(0);
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
