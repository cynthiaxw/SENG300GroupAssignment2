package groupAssignment2;
import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

public class IndicatorLightListenerDevice implements IndicatorLightListener{

	public int enabledCount = 0;
	public int disabledCount = 0;
	public boolean lightActivated = false;
	public boolean lightDeactivated = false;
	private VendingLogic logic;
	
	public IndicatorLightListenerDevice(VendingLogic logic)
	{
		this.logic = logic;
		
	}
	
	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
	    enabledCount++;
	    logic.enableHardware(hardware);
	}

	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
	    disabledCount++;
	    logic.disableHardware(hardware);
	}

	@Override
	public void activated(IndicatorLight light) {
		lightActivated = true;
		logic.getEventLog().writeToLog("IndicatorLight was turned on.");
		
	}

	@Override
	public void deactivated(IndicatorLight light) {
		lightDeactivated = true;
		logic.getEventLog().writeToLog("IndicatorLight was turned off.");
		
	}
	
}
