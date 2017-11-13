package ca.ucalgary.seng300.a2.test;

import ca.ucalgary.seng300.a2.EventLogInterface;

class EventStub implements EventLogInterface {

	/**
	* Method does nothing. This is to prevent writing a work log when testing
	*/
	@Override
	public void writeToLog(String s) {
	}


}