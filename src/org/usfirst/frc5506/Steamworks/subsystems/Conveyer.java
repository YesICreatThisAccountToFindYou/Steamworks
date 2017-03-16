// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc5506.Steamworks.subsystems;

import org.usfirst.frc5506.Steamworks.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;


/**
 *
 */
public class Conveyer extends Subsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final SpeedController conveyer = RobotMap.conveyerConveyer;
    private final DigitalInput conveyerSwitch = RobotMap.conveyerConveyerSwitch;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    // true = conveyer is between flattened position and limit switch
	public boolean conveyorPos = false;
	
	// Teleop won't control conveyer when this is false (indicates routine doesn't have control)
	public boolean teleop = true;

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
    public void set(double speed){
        if (speed < 0 && getSwitch())
        	conveyorPos = true;
        else if (getSwitch())
        	conveyorPos = false;
        if (speed < 0 && conveyorPos)
        	speed /= 3;
        conveyer.set(speed);
    }
    
    public boolean getSwitch() {
    	return conveyerSwitch.get();
    }
}

