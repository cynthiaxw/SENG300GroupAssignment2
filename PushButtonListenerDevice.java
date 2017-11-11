package groupAssignment2;

import org.lsmr.vending.hardware.PushButtonListener;
import org.lsmr.vending.hardware.VendingMachine;
import org.lsmr.vending.Coin;
import org.lsmr.vending.hardware.AbstractHardware;
import org.lsmr.vending.hardware.AbstractHardwareListener;
import org.lsmr.vending.hardware.PushButton;

public class PushButtonListenerDevice implements PushButtonListener {

	private VendingLogicInterface logic;
	
	public PushButtonListenerDevice(VendingLogicInterface logic) {
		this.logic = logic;
	}

	
	@Override
	public void pressed(PushButton button) {
		logic.determineButtonAction(button);
	}


	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		logic.enableHardware(hardware);
		
	}


	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		logic.disableHardware(hardware);
		
	}
}
