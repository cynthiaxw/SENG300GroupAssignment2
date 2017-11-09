import org.lsmr.vending.Coin;
import org.lsmr.vending.hardware.AbstractHardware;
import org.lsmr.vending.hardware.AbstractHardwareListener;
import org.lsmr.vending.hardware.CoinReturn;
import org.lsmr.vending.hardware.CoinReturnListener;

public class CoinReturnListenerDevice implements CoinReturnListener {

	private VendingLogic logic;
	
	public CoinReturnListenerDevice(VendingLogic logic)
	{
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
	public void coinsDelivered(CoinReturn coinReturn, Coin[] coins) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void returnIsFull(CoinReturn coinReturn) {
		// TODO Auto-generated method stub
		
	}

}
