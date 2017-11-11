package ca.ucalgary.seng300.a2.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.lsmr.vending.Coin;
import org.lsmr.vending.hardware.AbstractHardware;
import org.lsmr.vending.hardware.AbstractHardwareListener;
import org.lsmr.vending.hardware.CapacityExceededException;
import org.lsmr.vending.hardware.CoinReturn;
import org.lsmr.vending.hardware.DisabledException;
import org.lsmr.vending.hardware.PushButton;

import ca.ucalgary.seng300.a2.CoinReturnListenerDevice;
import ca.ucalgary.seng300.a2.EventLogInterface;
import ca.ucalgary.seng300.a2.VendingLogicInterface;


class EventStub1 implements EventLogInterface {

	@Override
	public void writeToLog(String s) {
	}

	@Override
	public void timeStamp() {
	}

}

class StubLogic1 implements VendingLogicInterface {
	public CoinReturnListenerDevice dev;
	public CoinReturn hardware;
	EventStub1 ev = new EventStub1();

	public boolean enabled = true;

	public StubLogic1(int num) {
		dev = new CoinReturnListenerDevice(this);
		hardware = new CoinReturn(num);
		hardware.register(dev);

	}

	@Override
	public EventLogInterface getEventLog() {
		return ev;
	}

	@Override
	public int getCurrencyValue() {

		return 0;
	}

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

	}

	@Override
	public void validCoinInserted(Coin coin) {

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

	@Override
	public int findHardwareIndex(AbstractHardware<? extends AbstractHardwareListener> hardware) {

		return 0;
	}

	@Override
	public void disableHardware(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		enabled = false;
	}

	@Override
	public void enableHardware(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		enabled = true;

	}

}


public class TestCoinReturnListenerDevice {

	@Test
	public void isEnabled() {
		StubLogic1 logic = new StubLogic1(1);
		logic.hardware.enable();
		assertTrue(logic.enabled);

	}

	@Test
	public void isDisabled() {
		StubLogic1 logic = new StubLogic1(1);
		logic.hardware.disable();
		assertFalse(logic.enabled);

	}

	@Test
	public void isAccept() throws CapacityExceededException, DisabledException {
		StubLogic1 logic = new StubLogic1(2);
		logic.hardware.acceptCoin(new Coin(5));
	assertEquals(1,logic.dev.deliveredCoinCount);
	assertEquals(5,logic.dev.deliveredCoinValue);



	}
	
	@Test(expected = CapacityExceededException.class)
	public void isExceeded() throws CapacityExceededException, DisabledException {
		StubLogic1 logic = new StubLogic1(1);
		logic.hardware.acceptCoin(new Coin(5));
		logic.hardware.acceptCoin(new Coin(5));

	}

	@Test(expected = DisabledException.class)
	public void isExceptions() throws CapacityExceededException, DisabledException {
		StubLogic1 logic = new StubLogic1(1);
		logic.hardware.disable();
		logic.hardware.acceptCoin(new Coin(5));

	}



}
