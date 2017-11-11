package groupAssignment2;
import org.lsmr.vending.hardware.AbstractHardware;
import org.lsmr.vending.hardware.AbstractHardwareListener;
import org.lsmr.vending.hardware.Display;
import org.lsmr.vending.hardware.DisplayListener;

public class DisplayListenerDevice implements DisplayListener {
	
	VendingLogic logic;
	
	public DisplayListenerDevice(VendingLogic logic) {
		this.logic = logic;
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
	public void messageChange(Display display, String oldMessage, String newMessage) {
		logic.getEventLog().writeToLog(newMessage);
	}
}
