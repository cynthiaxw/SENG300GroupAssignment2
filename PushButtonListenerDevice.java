import org.lsmr.vending.hardware.PushButtonListener;
import org.lsmr.vending.Coin;
import org.lsmr.vending.hardware.AbstractHardware;
import org.lsmr.vending.hardware.AbstractHardwareListener;
import org.lsmr.vending.hardware.PushButton;

public class PushButtonListenerDevice implements PushButtonListener {

	private VendingLogic logic;
	
	public PushButtonListenerDevice(VendingLogic logic) {
		this.logic = logic;
	}

	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void pressed(PushButton button) {
		// TODO Auto-generated method stub
		
	}
}
