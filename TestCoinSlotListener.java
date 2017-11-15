package ca.ucalgary.seng300.a2.test;
import static org.junit.Assert.*;

import org.junit.Test;
import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

import ca.ucalgary.seng300.a2.CoinSlotListenerDevice;
import ca.ucalgary.seng300.a2.EventLogInterface;
import ca.ucalgary.seng300.a2.VendingLogicInterface;

//******************************************************* This is too out of date, as the listeners are not explicity stated as needed
//******************************************************* we should not submit this!!

/**
 * 100% coverage, although
 *
 */
public class TestCoinSlotListener {
	
	/**
	 * Tests to that logic is enabled when asked to be
	 */
	@Test
	public void isEnabled() {
		int[] coins = {5,10,25,100};
		StubLogic3 logic = new StubLogic3(coins);
		logic.hardware.enable();
		assertTrue(logic.enabled);

	}

	/**
	 * Tests to that logic is disabled when asked to be
	 */
	@Test
	public void isDisabled() {
		int[] coins = {5,10,25,100};

		StubLogic3 logic = new StubLogic3(coins);
		logic.hardware.disable();
		assertFalse(logic.enabled);

	}
	
	
	@Test
	public void isValidCoin() throws DisabledException {
		int[] coins = {5,10,25,100};
		StubLogic3 logic = new StubLogic3(coins);
		
		logic.hardware.connect(new CoinChannel(new coinAcceptorStub()) , null);
		logic.hardware.addCoin(new Coin(10));
		
		assertEquals(10,logic.lastCoin);
		
	}
	
	@Test
	public void isInvalidCoin() throws DisabledException {
		int[] coins = {5,10,25,100};
		StubLogic3 logic = new StubLogic3(coins);
		logic.hardware.connect(null , new CoinChannel(new coinAcceptorStub()));
		logic.hardware.addCoin(new Coin(1));
		assertTrue(logic.invalid);
		
	}
	
	
	

}



//Stub for testing the 	Vending logic interface
class StubLogic3 implements VendingLogicInterface {
	
	public CoinSlotListenerDevice dev;
	public CoinSlot hardware;
	EventStub ev = new EventStub(); 

	public boolean enabled = true;
	public int lastCoin;
	public boolean invalid = false;

	/**
	* Creates a logic stub with a coinreceptacle with num capacity
	* @param int num, the capacity of the coin receptacle
	*/
	public StubLogic3(int[] num) {
		dev = new CoinSlotListenerDevice(this);
		hardware = new CoinSlot(num);
		hardware.register(dev);

	}

	/**
	* getter for event log. 
	* @return EventLotInterface ev
	*/
	@Override
	public EventLogInterface getEventLog() {
		return ev;
	}

	/** 
	* getter for currency value
	* @return always returns 0
	*/
	@Override
	public int getCurrencyValue() {

		return 0;
	}

	/** 
	* The following methods are implemented, but have no direct use for testing.
	* For this reason, they all havae no contents.
	*
	*/
	@Override
	public void welcomeMessageTimer() {

	}

	@Override
	public void welcomeMessage() {

	}

	@Override
	public void vendOutOfOrder() {

	}

	@Override
	public void displayCredit() {

	}

	@Override
	public void displayPrice(int index) {

	}

	@Override
	public void invalidCoinInserted() {
		invalid = true;
	}

	@Override
	public void validCoinInserted(Coin coin) {
		lastCoin = coin.getValue();
	}

	@Override
	public void dispensingMessage() {

	}

	@Override
	public void returnChange() {

	}

	@Override
	public void determineButtonAction(PushButton button) {

	}

	
	/**
	* Method overide for returning the harware index. Always returns 0
	* @param AbstractHardware<? extends AbstractHardwareListener> hardware, the hardware to be found
	* @return returns 0. 
	*/
	@Override
	public int findHardwareIndex(AbstractHardware<? extends AbstractHardwareListener> hardware) {

		return 0;
	}

	/**
	 * stub enable/disable, sets enabled flag in stublogic, there is only one
	 * "hardware" stublogic is concerned
	 */
	@Override
	public void disableHardware(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		enabled = false;
	}

	/**
	 * stub enable/disable, sets enabled flag in stublogic, there is only one
	 * "hardware" stublogic is concerned
	 */
	@Override
	public void enableHardware(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		enabled = true;

	}

	@Override
	public void setCurrentMessage(String newMessage) {
		
	}

}

class coinAcceptorStub implements CoinAcceptor {
	//	public int coin;

	@Override
	public void acceptCoin(Coin coin) throws CapacityExceededException, DisabledException {
	//	this.coin = coin.getValue();
	}

	@Override
	public boolean hasSpace() {
		return true;
	}
	
}
