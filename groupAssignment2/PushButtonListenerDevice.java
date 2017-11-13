package groupAssignment2;

import org.lsmr.vending.hardware.PushButtonListener;
import org.lsmr.vending.hardware.VendingMachine;
import org.lsmr.vending.Coin;
import org.lsmr.vending.hardware.AbstractHardware;
import org.lsmr.vending.hardware.AbstractHardwareListener;
import org.lsmr.vending.hardware.PushButton;

public class PushButtonListenerDevice implements PushButtonListener {

	private VendingLogic logic;

	public int enabledCount = 0;
	public int disabledCount = 0;
	public int pressedCount = 0;
	
	public PushButtonListenerDevice(VendingLogic logic) {
		this.logic = logic;
	}

	
	@Override
	public void pressed(PushButton button) {
		pressedCount++;
	}


	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		enabledCount++;
		
	}


	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		disabledCount++;
		
	}
}
