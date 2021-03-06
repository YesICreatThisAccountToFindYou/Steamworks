// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc5506.Steamworks;

import org.usfirst.frc5506.Steamworks.commands.Auto;
import org.usfirst.frc5506.Steamworks.commands.Routine;
import org.usfirst.frc5506.Steamworks.commands.Teleop;
import org.usfirst.frc5506.Steamworks.subsystems.Conveyer;
import org.usfirst.frc5506.Steamworks.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

    Command auto;
    Command teleop;
    
    // 1 = left; 2 = center; 3 = right;
    public static byte starting = 1;
    // I technically don't need '<Byte>' here, but just for safety, I use it
	public static SendableChooser<Byte> pos;
    public static SendableChooser<Command> autochooser; // same for '<Command>' here
    public static SendableChooser<Boolean> rumble;
    
    public static int time = 0;

    public static OI oi;
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static DriveTrain driveTrain;
    public static Conveyer conveyer;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	RobotMap.init();
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        driveTrain = new DriveTrain();
        conveyer = new Conveyer();

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        // OI must be constructed after subsystems. If the OI creates Commands
        //(which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();

        // instantiate the command used for the autonomous period

        auto = new Auto();
        teleop = new Teleop();
        Vision.init();
        // front camera (ball end)
        CameraServer.getInstance().startAutomaticCapture(0).setExposureManual(40);
        // back camera (gear end)
        CameraServer.getInstance().startAutomaticCapture(1).setExposureManual(50);
        driveTrain.gyro.calibrate();
        starting = (byte) DriverStation.getInstance().getLocation();
        pos = new SendableChooser<Byte>();
        switch(starting) {
	    	case(1):
	    		pos.addDefault("Left", (byte) 1);
	    		pos.addObject("Center", (byte) 2);
	    		pos.addObject("Right", (byte) 3);
	    		break;
	    	case(3):
	    		pos.addObject("Left", (byte) 1);
	    		pos.addObject("Center", (byte) 2);
	    		pos.addDefault("Right", (byte) 3);
	    		break;
	    	case(2):
	    	default: // 'Center' is a safe bet.... right?
	    		pos.addObject("Left", (byte) 1);
	    		pos.addDefault("Center", (byte) 2);
	    		pos.addObject("Right", (byte) 3);
	    		break;
        }
        autochooser = new SendableChooser<Command>();
        autochooser.addObject("Gear", new Auto());
        autochooser.addObject("Mobility", new Auto(true));
        autochooser.addDefault("Play dead", new Routine("SafeReset")); // at least safe reset the conveyer
        rumble = new SendableChooser<Boolean>();
        rumble.addDefault("Rumble On", true);
        rumble.addObject("Rumble Off", false);
        SmartDashboard.putData("Rumble", rumble);
        SmartDashboard.putData("Position", pos);
        SmartDashboard.putData("Routine", autochooser);
        System.out.println("All systems go!");
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit(){
    	Robot.driveTrain.driveArcade(0, 0);
    	Robot.conveyer.set(0);
    }

    public void disabledPeriodic() {
        Scheduler.getInstance().run();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (auto != null) auto.cancel();
        if (teleop != null) teleop.start();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        //if (Vision.izgud())
        	//System.out.println(Vision.getDistance() + "\t" + Vision.getTurningAngle());
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }

	public void autonomousInit() {
	    // schedule the autonomous command (example)
    	auto = autochooser.getSelected();
	    if (auto != null) auto.start();
	}
	
	public void robotPeriodic() {
		// if Pi hasn't responded for a second, it's probably dead
		// Pi "responds" by setting "true" to "Pi" every time it processes a frame
		SmartDashboard.putBoolean("Pi", time < 50);
		if (Vision.isalive()) {
			time = 0;
			Vision.table.putBoolean("running", false);
		} else 
			time++;
		if (Vision.izgud() && !Vision.isalive()) {
			// clearly the Pi isn't on to target the peg
			Vision.table.putBoolean("sight", false);
		}
		SmartDashboard.putBoolean("Sight", Vision.izgud());
		
		// update selectors
		// and pray the DS is still alive to make these choices...
		starting = pos.getSelected();
		
	}
}
