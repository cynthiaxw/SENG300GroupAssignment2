package ca.ucalgary.seng300.a1;

import java.util.Arrays;
import java.util.List;

import org.lsmr.vending.Coin;
import org.lsmr.vending.PopCan;
import org.lsmr.vending.hardware.DisabledException;
import org.lsmr.vending.hardware.VendingMachine;
import org.lsmr.vending.hardware.AbstractHardware;
import org.lsmr.vending.hardware.AbstractHardwareListener;
import org.lsmr.vending.hardware.CapacityExceededException;
import org.lsmr.vending.hardware.CoinRack;
import org.lsmr.vending.hardware.CoinRackListener;
import org.lsmr.vending.hardware.CoinReceptacle;
import org.lsmr.vending.hardware.CoinReceptacleListener;
import org.lsmr.vending.hardware.CoinSlot;
import org.lsmr.vending.hardware.CoinSlotListener;
import org.lsmr.vending.hardware.DeliveryChute;
import org.lsmr.vending.hardware.DeliveryChuteListener;
import org.lsmr.vending.hardware.EmptyException;
import org.lsmr.vending.hardware.PopCanRack;
import org.lsmr.vending.hardware.PopCanRackListener;
import org.lsmr.vending.hardware.SelectionButton;
import org.lsmr.vending.hardware.SelectionButtonListener;



public class Implementation implements CoinSlotListener, SelectionButtonListener, PopCanRackListener, CoinReceptacleListener, CoinRackListener, DeliveryChuteListener {

	/*
	 * global variables for the class
	 */
	private int[] allowedCoins;
	private VendingMachine vendingMachine;
	private int accummulatedValueOfCoinsEntered;
	private String lastEvent = "";
	private List<String> popNames;
	private List<Integer> popCosts;

	/*
	 * class constructor, takes in the array of coin values permitted, list of pop names, and list of pop costs
	 */
	public Implementation(int[] listOfAllowedCoins, List<String> popName, List<Integer> popCost) {
		this.allowedCoins = listOfAllowedCoins;
		this.popNames = popName;
		this.popCosts = popCost;
		this.accummulatedValueOfCoinsEntered = 0;
	}

	/*
	 * attach the hardware to this class, enabling interaction and listener notification
	 */
	public void attachVendingMachine(VendingMachine vm) {
		this.vendingMachine = vm;
		vendingMachine.getCoinSlot().register(this);
		for (int i = 0; i < vendingMachine.getNumberOfSelectionButtons(); i++) {
			vendingMachine.getSelectionButton(i).register(this);
		}
		for (int i = 0; i < vendingMachine.getNumberOfPopCanRacks(); i++) {
			vendingMachine.getPopCanRack(i).register(this);
		}
		vendingMachine.getCoinReceptacle().register(this);
		for (int i = 0; i < this.allowedCoins.length; i++) {
			vendingMachine.getCoinRack(i).register(this);
		}
		vendingMachine.getDeliveryChute().register(this);
		
		
		vendingMachine.configure(popNames, popCosts);
		
		

	}

	/*
	 * checks if coin is valid, if so, add to coinslot
	 * otherwise, add to storageBin receptacle
	 */
	public void insert(Coin coin) throws DisabledException {
		boolean success = false;
		for (Integer inArray : allowedCoins) {
			if (inArray.equals(coin.getValue())) {
				accummulatedValueOfCoinsEntered += coin.getValue();
				vendingMachine.getCoinSlot().addCoin(coin);
				success = true;
			}
		}
		if (!success) {
			try {
				vendingMachine.getStorageBin().acceptCoin(coin);
			} catch (CapacityExceededException e) {

			}
		}
		
	}

	/*
	 * return accumulated value of coins entered
	 */
	public int getAccummulatedValueOfCoinsEntered() {
		return accummulatedValueOfCoinsEntered;
	}

	/*
	 * return the array of valid coin values
	 */
	public int[] getAllowedCoins() {
		return allowedCoins;
	}

	/*
	 * simulate the press of a button
	 * call the listener to announce the specified button has been pressed
	 * if funds are sufficient, attempt to dispense a pop and store the coins
	 * then subtract pop cost from current credits inserted
	 */
	public void buttonPress(int buttonIndex) throws EmptyException{
		vendingMachine.getSelectionButton(buttonIndex).press();
		if (vendingMachine.getPopKindCost(buttonIndex) <= accummulatedValueOfCoinsEntered) {
			try {
				vendingMachine.getPopCanRack(buttonIndex).dispensePopCan();
				vendingMachine.getCoinReceptacle().storeCoins();
				accummulatedValueOfCoinsEntered -= vendingMachine.getPopKindCost(buttonIndex);
			} catch (CapacityExceededException | DisabledException | EmptyException e) {

			}

		} else {

		}
	}

	
	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		lastEvent = "Enabled";

	}

	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		lastEvent = "Disabled";

	}

	
	
	//CoinSlotListener
	@Override 
	public void validCoinInserted(CoinSlot slot, Coin coin) {
		lastEvent = "Accepted";
//		System.out.println(coin.getValue());

	}

	@Override
	public void coinRejected(CoinSlot slot, Coin coin) {
		lastEvent = "Rejected";
	}

	
	
	//SelectionButtonListener
	@Override
	public void pressed(SelectionButton button) {
		lastEvent = "Pressed";

	}

	public String getLastEvent() {
		return lastEvent;
	}

	public void removeEvent() {
		lastEvent = "";
	}

	
	
	//CoinRackListener
	@Override
	public void coinsFull(CoinRack rack) {
		lastEvent = "Coin rack is full";
		
	}

	@Override
	public void coinsEmpty(CoinRack rack) {
		lastEvent = "Coin rack is empty";
		
	}

	@Override
	public void coinAdded(CoinRack rack, Coin coin) {
		lastEvent = "Coin has been added to coin rack";
		
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

	
	
	//PopCanRackListener
	@Override
	public void popCanAdded(PopCanRack popCanRack, PopCan popCan) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void popCanRemoved(PopCanRack popCanRack, PopCan popCan) {
		lastEvent = "popCan has been removed from popCanRack";
		
	}

	@Override
	public void popCansFull(PopCanRack popCanRack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void popCansEmpty(PopCanRack popCanRack) {
		lastEvent = "PopCanRack is empty";
		
	}

	@Override
	public void popCansLoaded(PopCanRack rack, PopCan... popCans) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void popCansUnloaded(PopCanRack rack, PopCan... popCans) {
		// TODO Auto-generated method stub
		
	}


	
	//CoinReceptacleListener
	@Override
	public void coinAdded(CoinReceptacle receptacle, Coin coin) {
		lastEvent = "Coin added";
		
	}

	@Override
	public void coinsRemoved(CoinReceptacle receptacle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void coinsFull(CoinReceptacle receptacle) {
		lastEvent = "Coin Receptacle is full";
		
	}

	@Override
	public void coinsLoaded(CoinReceptacle receptacle, Coin... coins) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void coinsUnloaded(CoinReceptacle receptacle, Coin... coins) {
		// TODO Auto-generated method stub
		
	}

	
	
	//DeliverChuteListener
	@Override
	public void itemDelivered(DeliveryChute chute) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doorOpened(DeliveryChute chute) {
		lastEvent = "DeliverChute opened";
		
	}

	@Override
	public void doorClosed(DeliveryChute chute) {
		lastEvent = "DeliverChute closed";
		
	}

	@Override
	public void chuteFull(DeliveryChute chute) {
		lastEvent = "DeliverChute full";
		
	}

}


