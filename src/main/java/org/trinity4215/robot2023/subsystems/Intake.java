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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
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

  private DigitalInput topLimitSwitch = new DigitalInput(8);
  private DigitalInput bottomLimitSwitch = new DigitalInput(9);

  private double manualZeroOffset = 0;

  private double targetPosition = 90;

  /** Creates a new Intake. */
  public Intake() {


    spinMotor.restoreFactoryDefaults();
    spinMotor.setIdleMode(IdleMode.kBrake);
    spinEncoder.setPositionConversionFactor(IntakeConstants.kPositionConversionFactor);

    raiseMotor.restoreFactoryDefaults();
    raiseMotor.setIdleMode(IdleMode.kBrake);
    raiseEncoder.setPositionConversionFactor(IntakeConstants.kPositionConversionFactor);

    System.out.println("Intake initialized!");

    SmartDashboard.putData(
        "set intake arm target",
        new InstantCommand(
            () -> {
              targetPosition = SmartDashboard.getNumber("IntakeTargetPosition", 90);
              SmartDashboard.putNumber("IntakeTargetPosition (Actual)", targetPosition);
            },
            this));

    SmartDashboard.putBoolean("EnableLimitSwitches", true);

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

  private double getAbsoluteEncoderRawPositionDegrees() {
    return raiseEncoder.getPosition();
  }

  public double getConditionedAbsoluteEncoderPosition() {
    double rawPos = getAbsoluteEncoderRawPositionDegrees();
    double outputPos;
    if (rawPos > 10000) {
      outputPos = rawPos - 98876;
    } else {
      outputPos = rawPos;
    }
    return outputPos;
  }

  public double getZeroedConditionedAbsoluteEncoderPosition() {
    return getConditionedAbsoluteEncoderPosition() + manualZeroOffset;
  }

  public void setManualZeroOffset(double offset) {
    manualZeroOffset = offset;
  }

  public void driveToDegrees(double targetPosition) {
    double currentPosition = getZeroedConditionedAbsoluteEncoderPosition();
    CombinedLogging.putNumber("IntakeRaiseEncoderPosition", currentPosition);

    boolean goingUp = !((targetPosition - currentPosition) > 0);
    SmartDashboard.putBoolean("GoingUp", goingUp);
    double speed = goingUp
        ? backwardSpeedAt(currentPosition)
        : forwardSpeedAt(currentPosition);

    boolean switchesEnabled = SmartDashboard.getBoolean("EnableLimitSwitches", true);

    if (goingUp && getTopLimitSwitch() && switchesEnabled) {
      raiseMotor.stopMotor();
      return;
    }
    if ((!goingUp) && getBottomLimitSwitch() && switchesEnabled) {
      raiseMotor.stopMotor();
      return;
    }

    if (currentPosition < targetPosition - (IntakeConstants.kInnerDeadzoneArea / 2)) {
      raiseMotor.set(-speed);
      SmartDashboard.putBoolean("at setpoint", false);
    } else if (currentPosition > targetPosition + (IntakeConstants.kInnerDeadzoneArea / 2)) {
      raiseMotor.set(speed);
      SmartDashboard.putBoolean("at setpoint", false);
    } else {
      raiseMotor.set(holdSpeedAt(currentPosition));
      SmartDashboard.putBoolean("at setpoint", true);
    }
  }

  private double forwardSpeedAt(double degrees) {
    if (degrees < -15) {
      return .2;
    } else if (degrees >= -15 && degrees < 0) {
      return .15;
    } else if (degrees >= 0 && degrees < 20) {
      return .07;
    } else if (degrees >= 20 && degrees < 45) {
      return 0;
    } else {
      return -.04;
    }
  }

  private double backwardSpeedAt(double degrees) {
    if (degrees < -15) {
      return .03;
    } else if (degrees >= -15 && degrees < 0) {
      return .05;
    } else if (degrees >= 0 && degrees < 25) {
      return .08;
    } else if (degrees >= 25 && degrees < 50) {
      return .13;
    } else {
      return .25;
    }
  }

  private double holdSpeedAt(double degrees) {
    if (degrees < -15) {
      return -.08;
    } else if (degrees >= -15 && degrees < 0) {
      return -.05;
    } else if (degrees >= 0 && degrees < 25) {
      return .05;
    } else if (degrees >= 25 && degrees < 50) {
      return .06;
    } else {
      return .1;
    }
  }

  enum IntakeType {
    CONE, CUBE
  }

  public void spit(IntakeType intakeType) {
    spinMotor.set(IntakeConstants.kIntakeSpitSpeed * (intakeType == IntakeType.CONE ? 1 : -1));
  }

  public void suck(IntakeType intakeType) {
    spinMotor.set(IntakeConstants.kIntakeSuckSpeed * (intakeType == IntakeType.CONE ? 1 : -1));
  }

  public void shoot() {
    spinMotor.set(-1);
  }

  public boolean hasCone(IntakeType intakeType) {

    if (intakeType == IntakeType.CONE) {

    }
    return false;
  }

  public void stopSpinMotor() {
    spinMotor.set(0);
  }

  public boolean getTopLimitSwitch() {
    return !topLimitSwitch.get();
  }

  public boolean getBottomLimitSwitch() {
    return bottomLimitSwitch.get();
  }

  @Override
  public void periodic() {

    SmartDashboard.putNumber("IntakeAbsolutEncoderPosition", getConditionedAbsoluteEncoderPosition());
    SmartDashboard.putNumber("IntakeZeroedAbsEncPos", getZeroedConditionedAbsoluteEncoderPosition());
    SmartDashboard.putBoolean("TopLimitSwitch", getTopLimitSwitch());
    SmartDashboard.putBoolean("BottomLimitSwitch", getBottomLimitSwitch());
    if (getBottomLimitSwitch()) {
      double curPos = getConditionedAbsoluteEncoderPosition();
      setManualZeroOffset(90 - curPos);
    } else if (getTopLimitSwitch()) {
      double curPos = getConditionedAbsoluteEncoderPosition();
      setManualZeroOffset(-30 - curPos);
    }
    SmartDashboard.putNumber("ManualZeroOffset", manualZeroOffset);

  }

  public double getTargetPosition() {
    return targetPosition;
  }

  public void setTargetPosition(double degrees) {
    targetPosition = degrees > 95 ? 95 : (degrees < -35 ? -35 : degrees);
  }
}
