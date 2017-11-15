package ca.ucalgary.seng300.a2.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.lsmr.vending.Coin;
import org.lsmr.vending.PopCan;
import org.lsmr.vending.hardware.AbstractHardware;
import org.lsmr.vending.hardware.AbstractHardwareListener;
import org.lsmr.vending.hardware.CapacityExceededException;
import org.lsmr.vending.hardware.DisabledException;
import org.lsmr.vending.hardware.PopCanAcceptor;
import org.lsmr.vending.hardware.PopCanChannel;
import org.lsmr.vending.hardware.PopCanRack;
import org.lsmr.vending.hardware.PushButton;

import ca.ucalgary.seng300.a2.EventLogInterface;
import ca.ucalgary.seng300.a2.PopCanRackListenerDevice;
import ca.ucalgary.seng300.a2.VendingLogicInterface;

public class TestPopCanRackListenerDevice {

	/**
	 * Tests to that logic is enabled when asked to be
	 */
	@Test
	public void isEnabled() {
		StubLogic5 logic = new StubLogic5(1);
		logic.hardware.enable();
		assertTrue(logic.enabled);

	}

	/**
	 * Tests to that logic is disabled when asked to be
	 */
	@Test
	public void isDisabled() {
		StubLogic5 logic = new StubLogic5(1);
		logic.hardware.disable();
		assertFalse(logic.enabled);

	}
	
	@Test(expected = CapacityExceededException.class)
	public void isExceed() throws CapacityExceededException, DisabledException {
		StubLogic5 logic = new StubLogic5(1);
		logic.hardware.connect(new PopCanChannel(new popCanStubAcceptor()));
		logic.hardware.acceptPopCan(new PopCan("foo"));
		logic.hardware.acceptPopCan(new PopCan("foo"));

	}
	
	@Test(expected = DisabledException.class)
	public void isDisableEx() throws CapacityExceededException, DisabledException {
		StubLogic5 logic = new StubLogic5(1);
		logic.hardware.disable();
		logic.hardware.acceptPopCan(new PopCan("foo"));

	}
		
}


class StubLogic5 implements VendingLogicInterface {

	public PopCanRackListenerDevice dev;
	public PopCanRack hardware;
	EventStub ev = new EventStub();

	public boolean enabled = true;

	/**
	 * Creates a logic stub with a indicator light
	 */
	public StubLogic5(int num) {
		dev = new PopCanRackListenerDevice(this);
		hardware = new PopCanRack(num);
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

	@Override
	public void setCurrentMessage(String newMessage) {
 
	}
	}


class popCanStubAcceptor implements PopCanAcceptor{

	@Override
	public void acceptPopCan(PopCan popCan) throws CapacityExceededException, DisabledException {
	}

	@Override
	public boolean hasSpace() {
		return true;
	}
	
}
