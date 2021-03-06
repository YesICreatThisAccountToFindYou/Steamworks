package org.usfirst.frc5506.Steamworks.commands;

import org.usfirst.frc5506.Steamworks.Robot;
import org.usfirst.frc5506.Steamworks.Vision;

import edu.wpi.first.wpilibj.command.Command;

public class Gear extends Command {
	// degree rotation target
	public double angle = 0d;
	// ticks left. 
	public int timeleft = 0;
	
	public boolean targetFound = false;
	public boolean farAway = true;
	public boolean search;
	
	public Gear(boolean search) {
		super();
		this.search = search;
	}
	
	public Gear() { this(false); }
	
	public void execute() {
		timeleft--;
		System.out.println(angle + ":" + Robot.driveTrain.getGyroMod());
		if (targetFound && farAway && !Vision.izgud()) {
			farAway = false;
			setTimeout(0.5);
			// cause break mode to momentarily kick in, maybe slow down a little bit
			Robot.driveTrain.driveArcade(0, 0);
			return;
		}
		if (!targetFound) {
			targetFound = Vision.izgud();
			if (targetFound) {
				double distance = Vision.getDistance();
				angle = (Robot.driveTrain.getGyro() + Vision.getTurningAngle(distance)) % 360;
			} else if (search) {
				if (Robot.starting == 1) {
					Robot.driveTrain.driveLeftCurved(-0.4);
					Robot.driveTrain.driveRight(0);
				} else {
					Robot.driveTrain.driveRightCurved(-0.4);
					Robot.driveTrain.driveLeft(0);
				}
			}
		} else if (Math.abs((Robot.driveTrain.getGyroMod()) - angle) < 2) {
			Robot.driveTrain.driveRightCurved(Vision.izgud() ? -0.4 : -0.5);
			Robot.driveTrain.driveLeftCurved(Vision.izgud() ? -0.4 : -0.5);
		} else if (Math.abs((Robot.driveTrain.getGyroMod()) - angle) < 15) {
			if (Robot.driveTrain.getGyro() % 360 < angle) {
				Robot.driveTrain.driveRightCurved(Vision.izgud() ? -0.3 : -0.4);
				Robot.driveTrain.driveLeftCurved(Vision.izgud() ? -0.4 : -0.5);
			} else {
				Robot.driveTrain.driveRightCurved(Vision.izgud() ? -0.4 : -0.5);
				Robot.driveTrain.driveLeftCurved(Vision.izgud() ? -0.3 : -0.4);
			}
		} else {
			angle = Vision.izgud() ? (Robot.driveTrain.getGyro() + Vision.getTurningAngle()) % 360 : angle;
			if (Robot.driveTrain.getGyro() % 360 < angle) {
				Robot.driveTrain.driveRightCurved(0.5);
				Robot.driveTrain.driveLeftCurved(-0.5);
			} else {
				Robot.driveTrain.driveRightCurved(-0.5);
				Robot.driveTrain.driveLeftCurved(0.5);
			}
		}
	}

	public boolean isFinished() {
		return isTimedOut();
	}
	
	public void end() {
		Robot.driveTrain.driveArcade(0, 0);
	}
	
	public void interrupted() {
		Robot.driveTrain.driveArcade(0, 0);
	}
}
