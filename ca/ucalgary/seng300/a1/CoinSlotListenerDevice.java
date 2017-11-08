package ca.ucalgary.seng300.a1;

import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

public class CoinSlotListenerDevice implements CoinSlotListener, PushButtonListener {

	private int value = 0;
	private VendingMachine vending;
	
	public CoinSlotListenerDevice(CoinSlot slot) {
		slot.register(this);
		value = 0;	
	}
	
	public CoinSlotListenerDevice(PushButton button) {
		button.register(this);
	}
	
	/**
	 * Main constructor. Registers the listener to the coin slot and all 
	 * buttons of the vending machine
	 * @param vend the VendingMachine object to listen to
	 */
	public CoinSlotListenerDevice(VendingMachine vend) {
		this.vending = vend;
		for (int i = 0; i < vend.getNumberOfSelectionButtons(); i++) {
			vend.getSelectionButton(i).register(this);
		}
		vend.getCoinSlot().register(this);
		value = 0;
	}
	
	/**
     * Announces that the indicated hardware has been enabled.
     * 
     * @param hardware
     *            The device that has been enabled.
     */
	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		// Do nothing for now
	}

	/**
     * Announces that the indicated hardware has been disabled.
     * 
     * @param hardware
     *            The device that has been enabled.
     */
	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		// Do nothing for now
	}

	/**
	 * Updates value to track the amount of money inserted so far
	 */
	@Override
	public void validCoinInserted(CoinSlot slot, Coin coin) {
		if (!slot.isDisabled()) {
			value += coin.getValue();
		}
		
	}

	/**
	 * 
	 */
	@Override
	public void coinRejected(CoinSlot slot, Coin coin) {
		// TODO coins get returned, or do nothing for now? Since "Mr. Client" isn't worried about dispensing change yet
		
	}

	public int getValue() {
		return value;
	}
	
	/**
	 * A setter method for value. Should be called when pop is dispensed to model the payment.
	 * 
	 */
	public void payForItem(int amount) {
		if (amount >= 0 && amount <= value) {
			value -= amount;
		}
		else {
			// TODO could create a custom exception or just have it do nothing. Thoughts?
		}
	}

	@Override
	public void pressed(PushButton button) {
		// TODO Auto-generated method stub
		int index = findButtonIndex(button);
		int cost = vending.getPopKindCost(index);
		
		if (value >= cost) {
			payForItem(cost);
			//dispense the pop and deduct the cost from value
		}
		else {
			// Probably print error message to display in future assignment?
		}
		
	}
	
	/**
	 * Finds the index of a PushButton in this objects VendingMachine reference's
	 * button array
	 * @param button the PushButton to search for in the VendingMachine button array
	 * @return the index of the PushButton in the button array, -1 if it's not there
	 */
	public int findButtonIndex (PushButton button) {
		for (int i = 0; i < vending.getNumberOfSelectionButtons(); i++) {
			if (vending.getSelectionButton(i) == button) {
				return i;
			}
		}
		return -1;
	}
	
}
