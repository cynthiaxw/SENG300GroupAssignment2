package groupAssignment2;

import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;



public class CoinRackListenerDevice implements CoinRackListener{

	private VendingLogic logic;
	public int enabledCount = 0;
	public int disabledCount = 0;
	public int coinValue = 0;
	public int coinCount = 0;
	public boolean racksFull = false;
	public boolean racksEmpty = false;
	
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

	/**
     * Announces that the indicated coin rack is full of coins.
     * 
     * @param rack
     *            The rack where the event occurred.
     */
	@Override
	public void coinsFull(CoinRack rack) {
		if(rack.getCapacity()<=rack.size()) {
			racksFull = true;
		}
		else racksFull = false;
	}

	/**
     * Announces that the indicated coin rack is empty of coins.
     * 
     * @param rack
     *            The rack where the event occurred.
     */
	@Override
	public void coinsEmpty(CoinRack rack) {
		if(rack.size() == 0) {
			racksEmpty = true;
		}
		else racksEmpty = false;
		
	}

	 /**
     * Announces that the indicated coin has been added to the indicated coin
     * rack.
     * 
     * @param rack
     *            The rack where the event occurred.
     * @param coin
     *            The coin that was added.
     */
	@Override
	public void coinAdded(CoinRack rack, Coin coin) {
		coinValue+=coin.getValue();
		coinCount++;
		
	}

	/**
     * Announces that the indicated coin has been added to the indicated coin
     * rack.
     * 
     * @param rack
     *            The rack where the event occurred.
     * @param coin
     *            The coin that was removed.
     */
	@Override
	public void coinRemoved(CoinRack rack, Coin coin) {
		coinValue-=coin.getValue();
		coinCount--;
		
	}

	/**
     * Announces that the indicated sequence of coins has been added to the
     * indicated coin rack. Used to simulate direct, physical loading of the
     * rack.
     * 
     * @param rack
     *            The rack where the event occurred.
     * @param coins
     *            The coins that were loaded.
     */
	@Override
	public void coinsLoaded(CoinRack rack, Coin... coins) {
		for(Coin coin : coins) {
			 coinAdded(rack, coin);
		}
		
	}

	/**
     * Announces that the indicated sequence of coins has been removed to the
     * indicated coin rack. Used to simulate direct, physical unloading of the
     * rack.
     * 
     * @param rack
     *            The rack where the event occurred.
     * @param coins
     *            The coins that were unloaded.
     */
	@Override
	public void coinsUnloaded(CoinRack rack, Coin... coins) {
		for(Coin coin : coins) {
			coinRemoved(rack, coin);
		}
		
	}

	
	
	
}
