import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;
import java.util.Timer;
import java.util.TimerTask;

public class VendingLogic {
	private VendingMachine vm;			// The vending machine that this logic program is installed on
	private int credit;					// credit is saved in terms of cents 
	private EventLog EL;
	private Boolean[] circuitEnabled;
	
	public VendingLogic(VendingMachine vend)
	{
		this.vm = vend;
		credit = 0;
		EL = new EventLog();
		registerListeners();
		circuitEnabled = new Boolean[vm.getNumberOfSelectionButtons()];
		for (int i = 0; i < circuitEnabled.length; i++) {
			circuitEnabled[i] = false;
		}
	}
	
	//getter for EL
	public EventLog getEventLog(){
		return EL;
	}
	
	public int getCurrencyValue(){
		return credit;
	}
	
	private void registerListeners()
	{
		//Register each of our listener objects here
		vm.getCoinSlot().register(new CoinSlotListenerDevice(this));
		for (int i = 0; i < vm.getNumberOfCoinRacks(); i++) {
			vm.getCoinRack(i).register(new CoinRackListenerDevice(this));
		}
		vm.getCoinReceptacle().register(new CoinReceptacleListenerDevice(this));
		
		try {
			vm.getCoinReturn().register(new CoinReturnListenerDevice(this));}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
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
	 * Method for displaying a message for 5 seconds and erase it for 10s, if credit in VM is zero.
	 */
	
	public void welcomeMessageTimer(){
		TimerTask task = new MyTimer(vm);
		Timer timer = new Timer();
		while (credit == 0){
			timer.schedule(task, 10000, 5000);
		}
	}

	/**
	 * A method to push a welcome message to the display
	 */
	public void welcomeMessage() {
		vm.getDisplay().display("Hi there!");
	}
	
	/**
	 * A method to clear the welcome message from the display
	 */
	public void clearMessage() {
		vm.getDisplay().display("");
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
		try {
			Thread.sleep(5000);					// wait for 5 seconds
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.displayCredit();
	}
	
	/**
	 * Method called by the coinSlotListener to accumulate credit when valid coins are inserted.
	 * Update the credit and update the display.  Recalculate if the 
	 * @param coin  The Coin that was inserted
	 */
	public void validCoinInserted(Coin coin) {
		credit += coin.getValue();
		
		if (!isExactChangePossible())
			vm.getExactChangeLight().activate();
		else 
			vm.getExactChangeLight().deactivate();
		
		this.displayCredit();
	}
	
	/**
	 * Method to confirm that the product is being dispensed 
	 */
	public void dispensingMessage() {
		vm.getDisplay().display("Despensing. Enjoy!");
	}
	
	/**
	 * A method to return change to the user
	 */
	public void returnChange() {
		int[] coinKinds = {200, 100, 25, 10, 5};		// legal value of Canadian coins. only types returned
		for (int i = 0; i < coinKinds.length; i++) {
			CoinRack rack = vm.getCoinRackForCoinKind(coinKinds[i]);		// the coin rack for the coin value indicated by the loop
			if (rack != null) {									// if rack = null. coin kind is not a valid change option
				while ((!vm.isSafetyEnabled()) && (credit > coinKinds[i]) && (!rack.isDisabled()) && (rack.size() > 0)) {
					try {
						rack.releaseCoin();
						credit -= coinKinds[i];			// subtracting (i) cents from the credit
					} catch (CapacityExceededException e) {
						// should never happen, receptacle full should enable the safety, which is in the loop guard
						e.printStackTrace();
					} catch (EmptyException e) {
						// should never happen, checked for in the loop guard
						e.printStackTrace();
					} catch (DisabledException e) {
						// should never happen, checked for in the loop guard
						e.printStackTrace();
					}
				}
			}
		}
		if (!isExactChangePossible())
			vm.getExactChangeLight().activate();
		else 
			vm.getExactChangeLight().deactivate();
	}
	
	/**
	 * a Method to determine if exact change is possible given the prices of the pop and the current credit
	 * Checks if the credit - price can be created using the available coins is the racks
	 * checks for every pop price in the machine.
	 *   
	 * @return possible - A boolean describing if it is possible to create change for every possible transaction.
	 */
	public boolean isExactChangePossible() {
		boolean possible = true;
		for (int i = 0; i < vm.getNumberOfSelectionButtons(); i++) {		// get the price for every possible pop
			int credRemaining = credit;
			int price = vm.getPopKindCost(i);
			if (credRemaining >= price) {
				credRemaining -= price;
				int changePossible = 0;
				
				int[] coinKinds = {200, 100, 25, 10, 5};		// legal value of Canadian coins. only types returned
				for (int value = 0; value < coinKinds.length; value++) {
					CoinRack rack = vm.getCoinRackForCoinKind(coinKinds[value]);		// the coin rack for the coin value indicated by the loop
					if (rack != null) {									// if rack = null. coin kind is not a valid change option
						int coinsNeeded = 0;
						while ((!rack.isDisabled()) && (credRemaining > changePossible) && (rack.size() > coinsNeeded)) {
							coinsNeeded++;
							changePossible += coinKinds[value];			// sum of available coins
						}
					}
				}
				if (credRemaining != changePossible)			// if after going through all the coin racks, the exact change cannot be created
					possible = false;			//  return that it is not possible to 
			}
		}
		
		return possible;
	}
	
	/** 
	 * A method to determine what action should be done when a button is pressed 
	 * TODO how is disabling a part going to affect what action is taken?
	 * @param button
	 */
	public void determineButtonAction(PushButton button) {
		boolean found = false;
		
		if (vm.isSafetyEnabled() == false) {
			// search through the selection buttons to see if the parameter button is a selection button
			for (int index = 0; (found == false) && (index < vm.getNumberOfSelectionButtons()); index++) {
				if (vm.getSelectionButton(index) == button) {
					selectionButtonAction(index);
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

	/**
	 * Method to react to the press of a selection button
	 * @param index - the index of the selection button that was pressed
	 */
	public void selectionButtonAction(int index) {
		if ((vm.getPopKindCost(index) <= credit) && (circuitEnabled[index] == true)) {
			try {
				vm.getPopCanRack(index).dispensePopCan();
				this.dispensingMessage();
				credit -= vm.getPopKindCost(index);		// deduct the price of the pop
				returnChange();
				if (credit == 0)
					this.welcomeMessage();
				else
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
		else if (circuitEnabled[index] != true) {
			vm.getDisplay().display("Option unavailable");
		}
		else {
			this.displayPrice(index);
			this.displayCredit();
		}
	}
	
	/**
	 * A method to determine which pop can rack or push button an event has occurred on
	 * needed for EventLog information
	 * 
	 * @param hardware - the hardware that the event occurred on 
	 * @return The index of the hardware according to the vending machine. -1 means error could not find
	 */
	public int findHardwareIndex(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		
		if (hardware instanceof PopCanRack) {
			for (int index = 0; index < vm.getNumberOfPopCanRacks(); index++) {
				if (vm.getPopCanRack(index) == hardware) {
					return index;
				}
			}
		}
		
		else if (hardware instanceof PushButton) {
			for (int index = 0; index < vm.getNumberOfSelectionButtons(); index++) {
				if (vm.getSelectionButton(index) == hardware) {
					return index;
				}
			}
			
			for (int index = 0; index < 37; index++) {
				if (vm.getConfigurationPanel().getButton(index) == hardware) {
					return index;
				}
			}
		}
		
		else if (hardware instanceof CoinRack)
			for (int i = 0; i < vm.getNumberOfCoinRacks(); i++) {
				if (hardware == vm.getCoinRack(i)) {
					return i;
				}
			}
		
		return -1; // -1 will be the error index
	}
	
	/**
	 * Method to disable a piece of hardware. If hardware is a selection button or pop rack, machine can remain 
	 *   operational, otherwise, disable vending machine 
	 * @param hardware
	 */
	public void disableHardware(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		if (hardware instanceof PopCanRack) {
			circuitEnabled[findHardwareIndex(hardware)] = false;
		}
		else if (hardware instanceof PushButton) {
			for (int i = 0; i < vm.getNumberOfSelectionButtons(); i++) {
				if (hardware == vm.getSelectionButton(i)) {
					circuitEnabled[i] = false;
				}
			}
		}
		else {
			vm.getOutOfOrderLight().activate();
			returnChange();
			vm.enableSafety();
		}
	}
	
	/**
	 * Method to disable a piece of hardware. If hardware is a selection button or pop rack, machine can remain 
	 *   operational, otherwise, disable vending machine 
	 * @param hardware
	 */
	public void enableHardware(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		if (hardware instanceof PopCanRack) {
			int index = findHardwareIndex(hardware);
			if ((vm.getSelectionButton(index).isDisabled() == false) && (vm.isSafetyEnabled() == false))
				circuitEnabled[index] = true;
		}
		else if (hardware instanceof PushButton) {
			for (int i = 0; i < vm.getNumberOfSelectionButtons(); i++) {
				if (hardware == vm.getSelectionButton(i)) {
					circuitEnabled[i] = true;
				}
			}
		}
		else {
			vm.getOutOfOrderLight().deactivate();
			vm.disableSafety();
		}
	}
	
}