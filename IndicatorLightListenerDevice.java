package groupAssignment2;
import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

public class IndicatorLightListenerDevice implements IndicatorLightListener{

	public int enabledCount = 0;
	public int disabledCount = 0;
	public boolean lightActivated = false;
	public boolean lightDeactivated = false;
	
	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
	    enabledCount++;
	}

	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
	    disabledCount++;
	}

	@Override
	public void activated(IndicatorLight light) {
		lightActivated = true;
		
	}

	@Override
	public void deactivated(IndicatorLight light) {
		lightDeactivated = true;
		
	}
	
}
