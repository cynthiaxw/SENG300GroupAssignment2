import java.util.TimerTask;

import org.lsmr.vending.hardware.VendingMachine;

public class MyTimer extends TimerTask{
	
	private VendingMachine vm;	
	
	/**
	* Constructor uses the vending machine vend to get the display to interact with
	* @param VendingMachine vend, the vendingmachine that the timer works with
	*/
	public MyTimer(VendingMachine vend){
		this.vm = vend;
	}
	
	/**
	* Displays the message "Hi there!" on the display
	*/
	@Override
	public void run() {
		vm.getDisplay().display("Hi there!");
		
	}

}
