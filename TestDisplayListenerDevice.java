package ca.ucalgary.seng300.a2.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

import ca.ucalgary.seng300.a2.DisplayListenerDevice;
import ca.ucalgary.seng300.a2.EventLogInterface;
import ca.ucalgary.seng300.a2.VendingLogicInterface;

/**
 *  100% coverage, although display is pretty simple.
 */

public class TestDisplayListenerDevice {

	/**
	 * Tests to that logic is enabled when asked to be
	 */
	@Test
	public void isEnabled() {
		StubLogic2 logic = new StubLogic2();
		logic.hardware.enable();
		assertTrue(logic.enabled);

	}

	/**
	 * Tests to that logic is disabled when asked to be
	 */
	@Test
	public void isDisabled() {
		StubLogic2 logic = new StubLogic2();
		logic.hardware.disable();
		assertFalse(logic.enabled);

	}
	
	/**
	 * Is a message written?
	 */
	@Test
	public void isDisplayMessage() {
		StubLogic2 logic = new StubLogic2();
		logic.hardware.display("Message");
		
		assertTrue(logic.ev.write);
		assertTrue(logic.written);

		

	}

}

// Stub for testing the Vending logic interface
class StubLogic2 implements VendingLogicInterface {

	public DisplayListenerDevice dev;
	public Display hardware;
	EventStub1 ev = new EventStub1(); // How does this work not in a constructor or method?

	public boolean enabled = true;
	public boolean written = false;

	/**
	 * Creates a logic stub with a Display
	 */
	public StubLogic2() {
		dev = new DisplayListenerDevice(this);
		hardware = new Display();
		hardware.register(dev);

	}

	/**
	 * getter for event log.
	 * 
	 * @return EventLotInterface ev
	 */
	@Override
	public EventLogInterface getEventLog() {
		return ev;
	}

	/**
	 * getter for currency value
	 * 
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

	/**
	 * Method overide for returning the harware index. Always returns 0
	 * 
	 * @param AbstractHardware<?
	 *            extends AbstractHardwareListener> hardware, the hardware to be
	 *            found
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
		written = true;
	}
	
	class EventStub1 implements EventLogInterface{
		public boolean write = false;
		
		public void writeToLog(String s) {
			write = true;
		}

		
	}

}
