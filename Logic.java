import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

public class Logic {
	private VendingMachine vend;
	private int credit;					// credit is saved in terms of cents 
	private int currentConfig; 			// current configuration number
	private int enteredConfig;			// the configuration number that has been entered. awaiting approval (enter button)
	
	/**
	 * A method to push a welcome message to the display
	 */
	public void welcomeMessage() {
		vend.getDisplay().display("Welcome");
	}
	
	/**
	 * A method to send an OutOfOrder message to the display
	 */
	public void vendOutOfOrder() {
		vend.enableSafety();
		vend.getDisplay().display("OutOfOrder");
	}
	
	/**
	 * A method to push the currently accumulated credit to the display
	 */
	public void displayCredit() {
		vend.getDisplay().display("Current Credit: $" + (((double) credit)/100));
	}
	
	/**
	 * A method to display the price of the pop at a specific index 
	 * @param index - the selection number that corresponds to the desired pop
	 */
	public void displayPrice(int index) {
		vend.getDisplay().display("Price of " + vend.getPopKindName(index) + ": $" + (((double) vend.getPopKindCost(index)) / 100));
		this.displayCredit();
	}
	
	/**
	 * Method to show that an invalid coin was inserted
	 */
	public void invalidCoinInserted() {
		vend.getDisplay().display("Invalid coin!");
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
		vend.getDisplay().display("Despensing. \n Enjoy!");
	}
	
	/** 
	 * A method to determine what action should be done when a button is pressed 
	 * TODO how is disabling a part going to affect what action is taken?
	 * @param button
	 */
	public void determineButtonAction(PushButton button) {
		boolean found = false;
		
		// search through the selection buttons to see if the parameter button is a selection button
		for (int index = 0; (found == false) && (index < vend.getNumberOfSelectionButtons()); index++) {
			if (vend.getSelectionButton(index) == button) {
				if (vend.getPopKindCost(index) > credit) {
					try {
						vend.getPopCanRack(index).dispensePopCan();
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
			if (vend.getConfigurationPanel().getButton(index) == button) {
				enteredConfig = index;
				found = true;
			}
		}
		
		// check to see if the button is the configuration panels enter button.
		if ((found == false) && (button == vend.getConfigurationPanel().getEnterButton())) {
			currentConfig = enteredConfig;
			found = true;
			this.applyConfig();
			
		}
		
		if (found == false) {
			throw new SimulationException("Unknown Button pressed! Could not determine action");
		}
			
	}
	
	/**
	 * Method to display the current configuration of the Vending Machine   
	 */
	public void displayCurrentConfig() {
		vend.getConfigurationPanel().getDisplay().display("The current configuration is: " + currentConfig);
	}
	
	/**
	 * Method to display proposed changes to the Vending Machine's configuration  
	 */
	public void changeCurrentConfig() {
		vend.getConfigurationPanel().getDisplay().display("Change the current configuration to: " + currentConfig + "?");
	}
	
	/**
	 * A method to apply the configuration to the vending machine. 
	 */
	public void applyConfig() {
		// TODO apply these configurations 
	}
}
