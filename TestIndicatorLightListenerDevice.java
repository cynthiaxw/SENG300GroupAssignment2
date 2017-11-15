package ca.ucalgary.seng300.a2.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.lsmr.vending.Coin;
import org.lsmr.vending.hardware.AbstractHardware;
import org.lsmr.vending.hardware.AbstractHardwareListener;
import org.lsmr.vending.hardware.IndicatorLight;
import org.lsmr.vending.hardware.PushButton;

import ca.ucalgary.seng300.a2.EventLogInterface;
import ca.ucalgary.seng300.a2.IndicatorLightListenerDevice;
import ca.ucalgary.seng300.a2.VendingLogicInterface;

public class TestIndicatorLightListenerDevice {
	

	/**
	 * Tests to that logic is enabled when asked to be
	 */
	@Test
	public void isEnabled() {
		StubLogic4 logic = new StubLogic4();
		logic.hardware.enable();
		assertTrue(logic.enabled);

	}

	/**
	 * Tests to that logic is disabled when asked to be
	 */
	@Test
	public void isDisabled() {
		StubLogic4 logic = new StubLogic4();
		logic.hardware.disable();
		assertFalse(logic.enabled);

	}
	@Test
	public void isActive() {
		StubLogic4 logic = new StubLogic4();
		logic.hardware.activate();
		assertTrue(logic.dev.lightActivated);
	}
	
	@Test
	public void isDeactive() {
		StubLogic4 logic = new StubLogic4();
		logic.hardware.deactivate();
		assertFalse(logic.dev.lightActivated);
	}
	
}

class StubLogic4 implements VendingLogicInterface {

	public IndicatorLightListenerDevice dev;
	public IndicatorLight hardware;
	EventStub ev = new EventStub();

	public boolean enabled = true;

	/**
	 * Creates a logic stub with a indicator light
	 */
	public StubLogic4() {
		dev = new IndicatorLightListenerDevice(this);
		hardware = new IndicatorLight();
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
 
		
	}}
