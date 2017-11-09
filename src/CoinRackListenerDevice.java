import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;


public class CoinRackListenerDevice implements CoinRackListener{

	private VendingLogic logic;
	
	public CoinRackListenerDevice (VendingLogic logic)
	{
		this.logic = logic;
	}
	
	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void coinsFull(CoinRack rack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void coinsEmpty(CoinRack rack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void coinAdded(CoinRack rack, Coin coin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void coinRemoved(CoinRack rack, Coin coin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void coinsLoaded(CoinRack rack, Coin... coins) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void coinsUnloaded(CoinRack rack, Coin... coins) {
		// TODO Auto-generated method stub
		
	}

	
	
	
}
