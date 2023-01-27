package org.trinity4215.fenris.commands;

import java.util.function.DoubleSupplier;

import org.trinity4215.bilbotbaggins.subsystems.Drivetrain;
import org.trinity4215.bilbotbaggins.subsystems.Drivetrain.DrivetrainConstants;
import org.trinity4215.fenris.Constants.JoystickConstants;
import org.trinity4215.fenris.subsystems.FenrisDrivetrain.DriveConstants;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveSingleJoystick extends CommandBase {
    private final Drivetrain drivetrain;
    private final DrivetrainConstants constants;
    private final DoubleSupplier ySupplier;
    private final DoubleSupplier twistSupplier;

    public DriveSingleJoystick(Drivetrain drivetrain, DoubleSupplier y, DoubleSupplier twist) {
        this.drivetrain = drivetrain;
        constants = drivetrain.getConstants();
        ySupplier = y;
        twistSupplier = twist;
        addRequirements(drivetrain);

    }
    
    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double y = ySupplier.getAsDouble();
        double twist = twistSupplier.getAsDouble();
        
        y = Math.abs(y) < JoystickConstants.kYDeadZone ? 0.0 : y;
        twist = Math.abs(twist) < JoystickConstants.kTwistDeadZone ? 0.0 : twist;
        
        y = y * constants.kMaxSpeedPercent() * -1; //correct the y-axis (backwards is now backwards!)
        twist = twist * constants.kMaxSpeedPercent();


        drivetrain.driveArcade(y, twist);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        drivetrain.driveTank(0, 0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
