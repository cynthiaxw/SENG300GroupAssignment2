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
	 * A method to return change to the user
	 */
	public void returnChange() {
		
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
				if ((vm.getPopKindCost(index) <= credit) && (circuitEnabled[index] == true)) {
					try {
						vm.getPopCanRack(index).dispensePopCan();
						this.dispensingMessage();
						credit = 0;   // TODO properly deduct price. return change if necessary.
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
				found = true;
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
			hardware.disable();
		}
		else if (hardware instanceof PushButton) {
			for (int i = 0; i < vm.getNumberOfSelectionButtons(); i++) {
				if (hardware == vm.getSelectionButton(i)) {
					circuitEnabled[i] = false;
				}
			}
			hardware.disable();
		}
		else {
			vm.getOutOfOrderLight().activate();
			returnChange();
			vm.enableSafety();
			hardware.disable();
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
			hardware.enable();
		}
		else if (hardware instanceof PushButton) {
			for (int i = 0; i < vm.getNumberOfSelectionButtons(); i++) {
				if (hardware == vm.getSelectionButton(i)) {
					circuitEnabled[i] = true;
				}
			}
			hardware.disable();
		}
		else {
			vm.getOutOfOrderLight().activate();
			vm.enableSafety();
			hardware.disable();
		}
	}
	
}
