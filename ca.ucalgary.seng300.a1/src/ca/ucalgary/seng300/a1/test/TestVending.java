package ca.ucalgary.seng300.a1.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lsmr.vending.Coin;
import org.lsmr.vending.PopCan;
import org.lsmr.vending.hardware.DisabledException;
import org.lsmr.vending.hardware.EmptyException;
import org.lsmr.vending.hardware.SimulationException;
import org.lsmr.vending.hardware.VendingMachine;

import ca.ucalgary.seng300.a1.Implementation;

/*
 * Created by
 * 	Dan Dunareanu 30002346
 * 	Mahsa Gaskarimahalleh
 * 	Andrew Garcia-Corley
 */
public class TestVending {
	
	private Implementation systemUnderTest;
	private VendingMachine vendingMachine;

	
	/*
	 *setting up vending machine and logic (Implementation class)
	 */
	@Before
	public void setUp() throws Exception {
		int[] arrayOfCoins = { 5, 10, 25, 100, 200 };
		List<String> popNames = Arrays.asList("Cola", "Diet Cola", "Orange", "Grape", "Grapefruit", "Lemon");
		List<Integer> popCosts = Arrays.asList(250, 250, 250, 250, 250, 250);
		systemUnderTest = new Implementation(arrayOfCoins, popNames, popCosts);
		vendingMachine = new VendingMachine(systemUnderTest.getAllowedCoins(), 6, 15, 10, 200);
		systemUnderTest.attachVendingMachine(vendingMachine);
	}
	
	
	/*
	 *testing a button press without sufficient funds inserted, will not dispense pop or subtract from money inserted
	 */
	@Test
	public void testPopRequestedInsufficientFunds() throws DisabledException, EmptyException {
		Coin toonie = new Coin(200);
		systemUnderTest.insert(toonie);
		systemUnderTest.buttonPress(0);
		assertTrue(systemUnderTest.getLastEvent().equals("Pressed"));
	}
	
	/*
	 *testing a button press with sufficient funds inserted, will dispense to chute and subtract pop price from money inserted
	 */
	@Test
	public void testPopRequestedSufficientFunds() throws DisabledException, EmptyException {
		Coin toonie = new Coin(200);
		Coin quarter = new Coin(25);
		PopCan pop = new PopCan("Cola");
		vendingMachine.getPopCanRack(5).load(pop);
		vendingMachine.getPopCanRack(5).load(pop);
		systemUnderTest.insert(toonie);
		systemUnderTest.insert(quarter);
		systemUnderTest.insert(quarter);
		systemUnderTest.buttonPress(5);
		assertEquals(0, systemUnderTest.getAccummulatedValueOfCoinsEntered());
		assertEquals(1, vendingMachine.getDeliveryChute().size());
	}
	
	/*
	 *testing a button press with sufficient funds, but no pop available to dispense, should not subtract 
	 *from money inserted and should not dispense any pop
	 */
	@Test 
	public void testPopRequestedSufficientFundsPopRackEmpty() throws DisabledException, EmptyException {
		Coin toonie = new Coin(200);
		Coin quarter = new Coin(25);
		Coin dime = new Coin(10);
		systemUnderTest.insert(toonie);
		systemUnderTest.insert(quarter);
		systemUnderTest.insert(quarter);
		systemUnderTest.insert(dime);
		systemUnderTest.buttonPress(0);
		assertEquals(260, systemUnderTest.getAccummulatedValueOfCoinsEntered());
		assertEquals(0, vendingMachine.getDeliveryChute().size());
	}
	
	/*
	 *testing a button press with insufficient funds, and no pop available to dispense, should not subtract 
	 *from money inserted and should not dispense any pop
	 */
	@Test
	public void testPopRequestedInsufficientFundsPopRackEmpty() throws DisabledException, EmptyException {
		Coin dime = new Coin(10);
		systemUnderTest.insert(dime);
		systemUnderTest.buttonPress(0);
		assertEquals(10, systemUnderTest.getAccummulatedValueOfCoinsEntered());
		assertEquals(0, vendingMachine.getDeliveryChute().size());
	}
	
	/*
	 *test a valid coin insert with both receptacles full, should throw a SimulationException
	 */
	@Test (expected = SimulationException.class)
	public void testValidCoinInsertCoinReceptaclesFull() throws DisabledException {
		Coin quarter = new Coin(25);
		for (int i = 0; i < vendingMachine.getCoinReceptacle().getCapacity(); i++)
		{
			systemUnderTest.insert(quarter);
		}
		for (int i = 0; i < vendingMachine.getStorageBin().getCapacity(); i++)
		{
			systemUnderTest.insert(quarter);
		}
		
		systemUnderTest.insert(quarter);
		
	}
	
	/*
	 *testing a coin while vending machine is disabled, should throw DisabledException
	 */
	@Test (expected = DisabledException.class)
	public void testCoinInsertWhenDisabled() throws DisabledException {
		vendingMachine.enableSafety();
		Coin nickel = new Coin(5);
		assertEquals(0, systemUnderTest.getAccummulatedValueOfCoinsEntered());
		systemUnderTest.insert(nickel);
	}
	
	/*
	 *push pop button while vending machine is disabled, should receive button pressed event but nothing more
	 */
	@Test
	public void testPopRequestWhenDisabled() throws EmptyException {
		vendingMachine.enableSafety();
		systemUnderTest.buttonPress(1);
		assertTrue(systemUnderTest.getLastEvent().equals("Pressed"));
	}
	
	/*
	 *test insertion of invalid coin, should direct coin to storageBin receptacle instead of normal receptacle 
	 */
	@Test
	public void invalidCoinInserted() throws DisabledException {
		Coin foreign = new Coin(28);
		systemUnderTest.insert(foreign);
		assertEquals(1, vendingMachine.getStorageBin().size());
	}
	
	/*
	 *test the tracking of valid coins inserted 
	 */
	@Test
	public void validCoinInserted() throws DisabledException {
		Coin loonie = new Coin(100);
		Coin nickel = new Coin(5);
		Coin dime = new Coin(10);
		Coin quarter = new Coin(25);
		Coin toonie = new Coin(200);
		systemUnderTest.insert(loonie);
		assertEquals(100, systemUnderTest.getAccummulatedValueOfCoinsEntered());
		systemUnderTest.insert(nickel);
		assertEquals(105, systemUnderTest.getAccummulatedValueOfCoinsEntered());
		systemUnderTest.insert(dime);
		assertEquals(115, systemUnderTest.getAccummulatedValueOfCoinsEntered());
		systemUnderTest.insert(quarter);
		assertEquals(140, systemUnderTest.getAccummulatedValueOfCoinsEntered());
		systemUnderTest.insert(toonie);
		assertEquals(340, systemUnderTest.getAccummulatedValueOfCoinsEntered());
	}
	
	
	/*
	 * set system to null
	 */
	@After
	public void tearDown() throws Exception {
		systemUnderTest = null;
		vendingMachine = null;
	}

}
