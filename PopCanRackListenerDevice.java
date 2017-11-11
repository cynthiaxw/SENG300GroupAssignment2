import org.lsmr.vending.PopCan;
import org.lsmr.vending.hardware.AbstractHardware;
import org.lsmr.vending.hardware.AbstractHardwareListener;
import org.lsmr.vending.hardware.PopCanRack;
import org.lsmr.vending.hardware.PopCanRackListener;

public class PopCanRackListenerDevice implements PopCanRackListener {

	VendingLogicInterface logic;
	
	public PopCanRackListenerDevice(VendingLogicInterface vl) {
		logic = vl;
	}
	
	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		logic.enableHardware(hardware);
	}

	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		logic.disableHardware(hardware);
	}

	@Override
	public void popCanAdded(PopCanRack popCanRack, PopCan popCan) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void popCanRemoved(PopCanRack popCanRack, PopCan popCan) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void popCansFull(PopCanRack popCanRack) {
		int myRack = logic.findHardwareIndex(popCanRack);
		logic.getEventLog().writeToLog("Pop Can Rack #" + myRack + " is full.");
		
	}

	@Override
	public void popCansEmpty(PopCanRack popCanRack) {
		int myRack = logic.findHardwareIndex(popCanRack);
		logic.getEventLog().writeToLog("Pop Can Rack #" + myRack + " is empty.");
		logic.disableHardware(popCanRack);
	}

	@Override
	public void popCansLoaded(PopCanRack rack, PopCan... popCans) {
		int myRack = logic.findHardwareIndex(rack);
		logic.getEventLog().writeToLog("Pop Can Rack #" + myRack + " was loaded with " + popCans.length + "cans.");
	}

	@Override
	public void popCansUnloaded(PopCanRack rack, PopCan... popCans) {
		int myRack = logic.findHardwareIndex(rack);
		logic.getEventLog().writeToLog("Pop Can Rack #" + myRack + " had " + popCans.length + "pop cans unloaded.");
	}

}
