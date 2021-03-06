// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc5506.Steamworks.commands;
import org.usfirst.frc5506.Steamworks.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * syntax: command1;command2;/command3:0.3;command4:/1:5;command5;...lastCommand
 * 
 * Use ":" to split arguments, e.g. "command:2:3" runs "command" with 2 as arg1 and 3 as arg2
 * 		For example, to run the RunLeft(speed, time) command with 1 as speed and 2 as time, "RunLeft:1:2"
 * Use ";" to seperate commands, e.g. to run "command1" then "command2", use "command1;command2"
 * Use "/" before a command to run in parallel, e.g. to run "command1",
 * 		then run "command2" and "command3" at the same time (after "command1"), use "command1;/command2:command3"
 * 
 * 		Don't put "/" before your last command, there is no reason to.
 * 
 * Commands:
 * 
 * Drive(seconds)
 * 		Drives straight forward at full speed
 * TurnLeft(seconds)
 * 		Runs left motor back at full speed and right forwards at full speed
 * TurnRight(seconds)
 * 		Runs right motor back at full speed and left forwards at full speed
 * RunLeft(speed, time)
 * RunRight(speed, time)
 * CurveLeft(speed, time)
 * 		Runs the left motor, applying acceleration curve as necessary
 * CurveRight(speed, time)
 * 		Runs the right motor, applying acceleration curve as necessary
 * TurnTo(angle)
 * 		Runs one motor forwards to efficiently turn to a specific angle
 * Gear
 * 		Lines up and puts the gear on the peg
 * FlattenConveyer
 * 		Moves conveyer forwards into flattened position for depositing balls
 * ResetConveyer
 * 		Moves conveyer to middle limit switch
 * PushGear
 * 		Moves conveyer towards gear end (as far as it will go)
 * 		Sorry, too lazy to come up with a better name
 * BumpConveyer
 * 		Moves conveyer towards gear end for 1 second
 * SafeReset
 * 		Runs 'BumpConveyer;ResetConveyer'
 * 		Used to ensure safe position of conveyer belt when code is started
 * Wait(seconds)
 * 		Pauses
 * Stop
 * 		Stops driveTrain motors. DOES NOT STOP CONVEYER BELT.
 * 		Conveyer belt will be stopped anyway when relevant commands finish.
 * 
 * IMPORTANT: commands don't automatically stop DriveTrain motors!
 * Use the Stop command to stop motors!
 */
public class RoutineCommand extends Command {
    private String command;
    private double arg;
    private double arg2;
    private boolean requiresDriveTrain = false;
    private boolean requiresConveyer = false;
    
    // used by some commands
    private boolean timeoutSet = false;
    
    // this allows a command to stop during execute() so I don't need two switch statements, shortens code significantly
    private boolean done = false;
    
    
    public RoutineCommand(String script) {
    	String[] split = script.split(":");
        command = split[0].toLowerCase();
        arg = split.length > 1 ? Double.valueOf(split[1]) : 0d;
        arg2 = split.length > 2 ? Double.valueOf(split[2]) : 0d;

        // IT IS IMPERITIVE THAT COMMANDS THAT MOVE SOMETHING ARE ADDED HERE
        // otherwise, Routine will fight with Teleop, hindering proper operation
        if (command.equals("drive") || command.equals("turnleft") || command.equals("turnright") || command.equals("gear") ||
        	command.equals("runleft") || command.equals("runright") || command.equals("curveleft") || command.equals("curveright"))
        	requiresDriveTrain = true;
        if (command.equals("flattenconveyer") || command.equals("resetconveyer") || command.equals("pushgear") || command.equals("bumpconveyer"))
        	requiresConveyer = true;
        //if (requiresDriveTrain)
        //	requires(Robot.driveTrain);
        if (requiresConveyer)
        	requires(Robot.conveyer);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	timeoutSet = false;
    	if (requiresConveyer)
        	Robot.conveyer.teleop = false;
    	if (requiresDriveTrain)
    		Robot.driveTrain.teleop = false;
    	switch(command) {
    		case("drive"):
    			setTimeout(arg);
    			Robot.driveTrain.driveArcade(1, 0);
    			break;
    		case("turnleft"):
    			setTimeout(arg);
    			Robot.driveTrain.driveArcade(0, 0.5);
    			break;
    		case("turnright"):
    			setTimeout(arg);
    			Robot.driveTrain.driveArcade(0, -0.5);
    			break;
    		case("curveleft"):
    			setTimeout(arg2);
    			Robot.driveTrain.driveLeftCurved(arg);
    			break;
    		case("curveright"):
    			setTimeout(arg2);
    			Robot.driveTrain.driveRightCurved(arg);
    			break;
    		case("runleft"):
    			setTimeout(arg2);
    			Robot.driveTrain.driveLeft(arg);
    			break;
    		case("runright"):
    			setTimeout(arg2);
    			Robot.driveTrain.driveRight(arg);
    			break;
    		case("bumpconveyer"):
    			setTimeout(1);
    			Robot.conveyer.set(1);
    		case("wait"):
    			setTimeout(arg);
    			break;
    		case("stop"):
    			Robot.driveTrain.driveArcade(0, 0);
    			done = true;
    			break;
    		default:
    			break;
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	switch(command) {
    		case("turnto"):
    			double angle = Robot.driveTrain.getTurningAngle(arg);
    			System.out.println("Turning by: " + angle);
    			if (Math.abs(angle) < 10) {
    				done = true;
    				Robot.driveTrain.driveArcade(0, 0);
    			} else if (Math.abs(angle) < 35) {
    				if (angle < 0) {
    					Robot.driveTrain.driveLeftCurved(0.6);
    					Robot.driveTrain.driveRight(0);
    				} else {
    					Robot.driveTrain.driveRightCurved(0.6);
    					Robot.driveTrain.driveLeft(0);
    				}
    			} else {
    				if (angle < 0) {
    					Robot.driveTrain.driveLeftCurved(0.6);
    					Robot.driveTrain.driveRight(-0.6);
    				} else {
    					Robot.driveTrain.driveRightCurved(0.6);
    					Robot.driveTrain.driveLeft(-0.6);
    				}
    			}
    			break;
    		case("flattenconveyer"):
    			Robot.conveyer.set(-1);
    			if (Robot.conveyer.conveyorPos && !timeoutSet) {
    				timeoutSet = true;
    				setTimeout(1);
    			}
    			break;
    		case("resetconveyer"):
    			Robot.conveyer.set(Robot.conveyer.conveyorPos ? 1 : -1);
				done = Robot.conveyer.getSwitch();
				break;
    		case("pushgear"):
    			Robot.conveyer.set(1);
    			if (!Robot.conveyer.conveyorPos && !timeoutSet) {
    				timeoutSet = true;
    				setTimeout(8);
    			}
    			break;
			default:
				break;
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return done || isTimedOut() || (requiresConveyer && Robot.oi.getDriverJoystick().getRawButton(1));
    }

    // Called once after isFinished returns true
    protected void end() {
    	// notice the lack of "else" here
    	// this allows a command to safely require both the DriveTrain and the Conveyer
    	if (requiresDriveTrain)
    		Robot.driveTrain.teleop = true;
    	if (requiresConveyer) {
    		Robot.conveyer.set(0);
    		Robot.conveyer.teleop = true;
    	}
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	// notice the lack of "else" here
    	// this allows a command to safely require both the DriveTrain and the Conveyer
    	if (requiresDriveTrain)
    		Robot.driveTrain.teleop = true;
    	if (requiresConveyer) {
    		Robot.conveyer.set(0);
    		Robot.conveyer.teleop = true;
    	}
    }
}
