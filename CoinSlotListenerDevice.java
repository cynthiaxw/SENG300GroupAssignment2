import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;



public class CoinSlotListenerDevice implements CoinSlotListener {

	private VendingLogic logic;
	
	public CoinSlotListenerDevice(VendingLogic logic)
	{
		this.logic = logic;
		
	}
	
	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		// Nothing for now
	}

	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		// TODO Nothing for now
		
	}

	@Override
	public void validCoinInserted(CoinSlot slot, Coin coin) {
		//logic.enteredValidCoin(slot, coin);
		
	}

	@Override
	public void coinRejected(CoinSlot slot, Coin coin) {
		//logic.enteredInvalidCoin(slot, coin);
		
	}

}
