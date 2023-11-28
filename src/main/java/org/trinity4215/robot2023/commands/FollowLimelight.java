// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.commands;

import org.trinity4215.robot2023.Constants.DriveConstants;
import org.trinity4215.robot2023.subsystems.Drivetrain;
import org.trinity4215.robot2023.subsystems.Limelight;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class FollowLimelight extends CommandBase {
    private Limelight limelight;
    private Drivetrain drivetrain;

    private double limelightZeroAngle = 0;

    /** Creates a new FollowLimelight. */
    public FollowLimelight(Limelight limelight, Drivetrain drivetrain) {
        // Use addRequirements() here to declare subsystem dependencies.
        this.limelight = limelight;
        this.drivetrain = drivetrain;
        addRequirements(limelight, drivetrain);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
        NetworkTableEntry tx = table.getEntry("tx");
        NetworkTableEntry ty = table.getEntry("ty");
        NetworkTableEntry ta = table.getEntry("ta");
        NetworkTableEntry tid = table.getEntry("tid");
        double x = tx.getDouble(0.0);
        double y = ty.getDouble(0.0);
        double area = ta.getDouble(0.0);
        double fwdSpeed = (10-area)/7;
        double rotSpeed = x/50;
         double id = tid.getDouble(0.0);   
            double finalLeft = 0;
            double finalRight = 0;
            
            SmartDashboard.putNumber("LimelightX", x);
            SmartDashboard.putNumber("LimelightY" , y);
            SmartDashboard.putNumber("LimelightArea" , area);
            SmartDashboard.putNumber("rotSpeed", rotSpeed);
            SmartDashboard.putNumber("fwdSpeed", fwdSpeed);
            SmartDashboard.putNumber("id", id);

            
            if ((id != 4) || (id !=5));{
                fwdSpeed = 0;
                rotSpeed = 0;
            }
            if (area == 0){
                fwdSpeed = 0;
                rotSpeed = 0;
              }else{
                if (area > 9.5){
                    fwdSpeed = 0;
                    rotSpeed = 0;
                }
          
                if((x < 3) &(x > -3)){
                   
                    rotSpeed = 0;
                }
            }
            finalRight = (-rotSpeed + fwdSpeed)/3;
            finalLeft = (rotSpeed + fwdSpeed)/3;
            // SmartDashboard.putNumber("Speed", (curAngle - tagYaw - limelightZeroAngle));
            // SmartDashboard.putNumber("sind(angle)", rotSpeed);
            // int deadzoneScalar = Math.abs(rotSpeed) <= DriveConstants.kDeadzone ? 0 : 1;
            // SmartDashboard.putBoolean("in dead zone", deadzoneScalar == 0);
            // double output = -1 * rotSpeed
            // * deadzoneScalar
            // * DriveConstants.kMaxSpeedPercent
            // * (1 - DriveConstants.kMinTurnSpeed) + DriveConstants.kMinTurnSpeed;

            SmartDashboard.putNumber("OutputL", finalLeft);
            SmartDashboard.putNumber("OutputR", finalRight);
            //drivetrain.driveDualJoystickPercent(finalLeft, finalRight);

       
        // System.out.println(r);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
