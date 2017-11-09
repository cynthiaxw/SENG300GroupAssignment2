import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

public class VendingLogic {
	
	private int currencyValue;
	private VendingMachine vm;
	
	
	private CoinSlotListenerDevice coinSlotlistener;
	private CoinRackListenerDevice coinRackListener;
	private CoinReceptacleListenerDevice coinReceptListener;
	private CoinReturnListenerDevice coinReturnListener;
	

	//I am unsure if which of the remaining listeners are needed for the rest of the assignment
	
	
	public VendingLogic(VendingMachine vm)
	{
		this.vm = vm;
		currencyValue = 0;
		coinSlotlistener = new CoinSlotListenerDevice(this);
		coinRackListener = new CoinRackListenerDevice(this);
		coinReceptListener = new CoinReceptacleListenerDevice(this);
		coinReturnListener = new CoinReturnListenerDevice(this);
		
		registerListeners();
	}
	
	public int getCurrencyValue(){
		return currencyValue;
	}
	
	private void registerListeners()
	{
		//Register each of out listener objects here
		vm.getCoinSlot().register(coinSlotlistener);
		vm.getCoinRack(0).register(coinRackListener); //Note that we can register for specific coin types, use the other register method
		vm.getCoinReceptacle().register(coinReceptListener);
		vm.getCoinReceptacle().register(coinReceptListener);
		
	}

	public void enteredValidCoin(CoinSlot slot, Coin coin) {
		if (!slot.isDisabled())
		{
			currencyValue += coin.getValue();
		}
		//handle coin input
		
	}

	public void enteredInvalidCoin(CoinSlot slot, Coin coin) {
		// TODO handle wrong coin input
		
	}
	
	

}
