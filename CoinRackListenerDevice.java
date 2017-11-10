package groupAssignment2;

import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;



public class CoinRackListenerDevice implements CoinRackListener{

	private VendingLogic logic;
	public int enabledCount = 0;
	public int disabledCount = 0;
	public int coinValue = 0;
	public int coinCount = 0;
	public boolean coinsFull = false;
	public boolean coinsEmpty = false;
	
	public CoinRackListenerDevice (VendingLogic logic)
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

	@Override
	public void coinsFull(CoinRack rack) {
		coinsFull = true;
		
	}

	@Override
	public void coinsEmpty(CoinRack rack) {
		coinsEmpty = false;
		
	}

	@Override
	public void coinAdded(CoinRack rack, Coin coin) {
		coinValue+=coin.getValue();
		coinCount++;
		
	}

	@Override
	public void coinRemoved(CoinRack rack, Coin coin) {
		coinValue-=coin.getValue();
		coinCount--;
		
	}

	@Override
	public void coinsLoaded(CoinRack rack, Coin... coins) {
		for(Coin coin : coins) {
			 coinAdded(rack, coin);
		}
		
	}

	@Override
	public void coinsUnloaded(CoinRack rack, Coin... coins) {
		for(Coin coin : coins) {
			coinRemoved(rack, coin);
		}
		
	}

	
	
	
}
