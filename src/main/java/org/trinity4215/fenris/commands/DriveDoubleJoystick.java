
package org.trinity4215.fenris.commands;

import java.util.function.DoubleSupplier;

import org.trinity4215.bilbotbaggins.subsystems.Drivetrain;
import org.trinity4215.bilbotbaggins.subsystems.Drivetrain.DrivetrainConstants;
import org.trinity4215.fenris.Constants.JoystickConstants;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveDoubleJoystick extends CommandBase {
    
    private final Drivetrain drivetrain;
    private final DrivetrainConstants constants;

    private final DoubleSupplier leftSupplier;
    private final DoubleSupplier rightSupplier;

    public DriveDoubleJoystick(Drivetrain drivetrain, DoubleSupplier left, DoubleSupplier right) {
        this.drivetrain = drivetrain;
        constants = drivetrain.getConstants();
        leftSupplier = left;
        rightSupplier = right;
        addRequirements(drivetrain);

    }
    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double left = leftSupplier.getAsDouble();
        double right = rightSupplier.getAsDouble();
        
        // Deadzone Logic
        left = Math.abs(left) < JoystickConstants.kYDeadZone ? 0.0 : left;
        right = Math.abs(right) < JoystickConstants.kYDeadZone ? 0.0 : right;

        left = left * constants.kMaxSpeedPercent();
        right = right * constants.kMaxSpeedPercent();

        drivetrain.driveTank(-left, -right); 
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
