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
	public int insertedCoinValue = 0;
	public int rejectedCoinValue = 0;
	

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
	public void validCoinInserted(CoinSlot slot, Coin coin) {
	    validCoinInsertedCount++;
	    insertedCoinValue += coin.getValue();
	    logic.validCoinInserted(coin);
	}

	@Override
	public void coinRejected(CoinSlot slot, Coin coin) {
	    coinRejectedCount++;
	    rejectedCoinValue += coin.getValue();
	    logic.invalidCoinInserted();
	}

    

}
