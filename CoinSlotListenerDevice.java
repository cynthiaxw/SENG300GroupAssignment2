package groupAssignment2;

import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;



public class CoinSlotListenerDevice implements CoinSlotListener {

	private VendingLogic logic;
	
	public CoinSlotListenerDevice(VendingLogic logic)
	{
		this.logic = logic;
		
	}
	

	public int enabledCount = 0;
	public int disabledCount = 0;
	public int validCoinInsertedCount = 0;
	public int coinRejectedCount = 0;

	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
	    enabledCount++;
	}

	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
	    disabledCount++;
	}

	@Override
	public void validCoinInserted(CoinSlot slot, Coin coin) {
	    validCoinInsertedCount++;
	}

	@Override
	public void coinRejected(CoinSlot slot, Coin coin) {
	    coinRejectedCount++;
	}

    

}
