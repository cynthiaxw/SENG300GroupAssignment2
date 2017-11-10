package groupAssignment2;

import org.lsmr.vending.Coin;
import org.lsmr.vending.hardware.AbstractHardware;
import org.lsmr.vending.hardware.AbstractHardwareListener;
import org.lsmr.vending.hardware.CoinReturn;
import org.lsmr.vending.hardware.CoinReturnListener;

public class CoinReturnListenerDevice implements CoinReturnListener {

	private VendingLogic logic;
	public int enabledCount = 0;
	public int disabledCount = 0;
	public int deliveredCoinCount = 0;
	public int deliveredCoinValue = 0;
	public boolean returnsfull = false;
	
	public CoinReturnListenerDevice(VendingLogic logic)
	{
		this.logic = logic;
	}
	
	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
	    enabledCount++;
	}

	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
	    disabledCount++;
	}
	
	/**
     * Indicates that the coin return will not be able to hold any more
     * coins.
     * 
     * @param coinReturn
     *            The device on which the event occurred.
     */
	@Override
	public void coinsDelivered(CoinReturn coinReturn, Coin[] coins) {
		for(Coin coin:coins) {
			deliveredCoinCount++;
			deliveredCoinValue+=coin.getValue();
		}
		
	}
	
	/**
     * Indicates that the coin return will not be able to hold any more
     * coins.
     * 
     * @param coinReturn
     *            The device on which the event occurred.
     */
	@Override
	public void returnIsFull(CoinReturn coinReturn) {
		if(coinReturn.getCapacity()<=coinReturn.size()) {
			returnsfull = true;
		}
	}

}
