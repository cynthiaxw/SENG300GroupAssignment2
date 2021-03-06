package groupAssignment2;

import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

public class VendingLogic {
	private VendingMachine vm;			// The vending machine that this logic program is installed on
	private int credit;					// credit is saved in terms of cents 
		
	public VendingLogic(VendingMachine vend)
	{
		this.vm = vend;
		credit = 0;
		
		registerListeners();
	}
	
	public int getCurrencyValue(){
		return credit;
	}
	
	private void registerListeners()
	{
		//Register each of out listener objects here
		vm.getCoinSlot().register(new CoinSlotListenerDevice(this));
		vm.getCoinRack(0).register(new CoinRackListenerDevice(this)); //TODO Note that we can register for specific coin types, use the other register method
		vm.getCoinReceptacle().register(new CoinReceptacleListenerDevice(this));
		vm.getCoinReceptacle().register((CoinReceptacleListener) new CoinReturnListenerDevice(this));
		
		for (int i = 0; i < vm.getNumberOfSelectionButtons(); i++) {
			vm.getSelectionButton(i).register(new PushButtonListenerDevice(this));
		}
		
		// Configuration Panel has 37 buttons.  This is a hard coded value.
		for (int i = 0; i < 37; i++) {
			vm.getConfigurationPanel().getButton(i).register(new PushButtonListenerDevice(this));
		}
		
		vm.getConfigurationPanel().getEnterButton().register(new PushButtonListenerDevice(this));
	}

	/**
	 * A method to push a welcome message to the display
	 */
	public void welcomeMessage() {
		vm.getDisplay().display("Welcome");
	}
	
	/**
	 * A method to send an OutOfOrder message to the display
	 */
	public void vendOutOfOrder() {
		vm.enableSafety();
		vm.getDisplay().display("OutOfOrder");
	}
	
	/**
	 * A method to push the currently accumulated credit to the display
	 */
	public void displayCredit() {
		vm.getDisplay().display("Current Credit: $" + (((double) credit)/100));
	}
	
	/**
	 * A method to display the price of the pop at a specific index 
	 * @param index - the selection number that corresponds to the desired pop
	 */
	public void displayPrice(int index) {
		vm.getDisplay().display("Price of " + vm.getPopKindName(index) + ": $" + (((double) vm.getPopKindCost(index)) / 100));
		this.displayCredit();
	}
	
	/**
	 * Method to show that an invalid coin was inserted
	 */
	public void invalidCoinInserted() {
		vm.getDisplay().display("Invalid coin!");
		this.displayCredit();
	}
	
	/**
	 * Method called by the coinSlotListener to accumulate credit when valid coins are inserted.
	 * Update the credit and update the display 
	 * @param coin  The Coin that was inserted
	 */
	public void validCoinInserted(Coin coin) {
		credit += coin.getValue();
		this.displayCredit();
	}
	
	/**
	 * Method to confirm that the product is being dispensed 
	 */
	public void dispensingMessage() {
		vm.getDisplay().display("Despensing. Enjoy!");
	}
	
	/** 
	 * A method to determine what action should be done when a button is pressed 
	 * TODO how is disabling a part going to affect what action is taken?
	 * @param button
	 */
	public void determineButtonAction(PushButton button) {
		boolean found = false;
		
		// search through the selection buttons to see if the parameter button is a selection button
		for (int index = 0; (found == false) && (index < vm.getNumberOfSelectionButtons()); index++) {
			if (vm.getSelectionButton(index) == button) {
				if (vm.getPopKindCost(index) > credit) {
					try {
						vm.getPopCanRack(index).dispensePopCan();
						this.dispensingMessage();
						credit = 0;
						this.displayCredit();
					} catch (DisabledException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (EmptyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (CapacityExceededException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else {
					this.displayPrice(index);
					this.displayCredit();
					found = true;
				}		
			}
		}
		
		// search through the configuration panel to see if the parameter button is part of these buttons
		// NOTE!!! the configuration panel has a hard coded list of 37 buttons.  If this changes it could cause an error here!
		for (int index = 0; (found == false) && (index < 37); index++) {
			if (vm.getConfigurationPanel().getButton(index) == button) {
				// TODO figure out how to configure
				found = true;
			}
		}
		
		// check to see if the button is the configuration panels enter button.
		if ((found == false) && (button == vm.getConfigurationPanel().getEnterButton())) {
			// TODO figure out how to configure
			found = true;
			
		}
		
		if (found == false) {
			throw new SimulationException("Unknown Button pressed! Could not determine action");
		}
			
	}
	

}