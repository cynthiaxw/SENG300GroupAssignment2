import java.util.TimerTask;

import org.lsmr.vending.hardware.VendingMachine;

public class MyTimer extends TimerTask{
	private VendingMachine vm;	
	
	public MyTimer(VendingMachine vend){
		this.vm = vend;
	}
	@Override
	public void run() {
		vm.getDisplay().display("Hi there!");
		
	}

}
