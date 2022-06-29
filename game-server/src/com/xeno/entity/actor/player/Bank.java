package com.xeno.entity.actor.player;

import com.xeno.entity.actor.item.Item;
import com.xeno.entity.actor.item.ItemConstants;
import com.xeno.event.AreaEvent;
import com.xeno.net.definitions.ItemDefinition;
import com.xeno.util.Utility;
import com.xeno.world.World;

public class Bank {

	private transient Player p;
	public static final int MAX_SLOTS = 496;
	private transient boolean banking = false;
	private Item[] bank = new Item[MAX_SLOTS];
	private transient boolean asNote = false;
	private transient int setNewPinAttempt = 0;
	private transient int[] pinSeq;
	private transient int pinStatus = 0;
	private transient int[] tempPin1 = new int[4];
	private transient int[] tempPin2 = new int[4];
	private transient boolean pinCorrect;
	private int[] bankPin;
	private int recoveryDelay = 3;
	private transient boolean deletePin;
	public transient boolean changingPin = false;
	private long lastPinChange;
	private long lastDeletionRequest;
	public transient int openStatus = 0;
	private transient int[] numbers = {0,1,2,3,4,5,6,7,8,9};
	//private int[] tabAmounts = new int[11];
	private transient int currentTab = 10;
	private int lastXAmount = 1;
	public static final String[] MESSAGES = {"Now click the SECOND digit.", "Time for the THIRD digit.", "Finally, the FOURTH digit.", "Finally, the FOURTH digit."};
	public static final int[] ASTERIK = {21, 22, 23, 24};
	
	public Bank() {
		for (int i = 0; i < MAX_SLOTS; i++) {
			bank[i] = new Item(-1, 0);
		}
	}
	
	public void openBank() {
		p.getWalkingQueue().reset();
		p.getActionSender().clearMapFlag();
		//int items = 20 * 1024^0 + 3 * 1024^1 + 4 * 1024^2; // Used with config2 1246
		if (!banking) {
			if (!pinCorrect && bankPin != null) {
				if (isPinPending()) {
					verifyPin(false);
					return;
				}
				if (!isPinPending()) {
					pinStatus = 0;
					tempPin1 = new int[4];
					openEnterPin();
					return;
				}
			}
			p.getActionSender().sendConfig2(563, 4194304);
			p.getActionSender().sendConfig(1248, -2013265920);
			p.getActionSender().sendConfig(1249, lastXAmount);
			p.getActionSender().sendBankOptions();
			refreshBank();
			p.getActionSender().displayInventoryInterface(763);
			p.getActionSender().displayInterface(762);
			p.getActionSender().showChildInterface(762, 18, false);
			p.getActionSender().showChildInterface(762, 19, false);
			p.getActionSender().showChildInterface(762, 23, false);
			//setTabConfig();
			banking = true;
		}
	}

	private void setTabConfig() {
		/*int config = 0;
		config += tabAmounts[2];
		config += tabAmounts[3] * 1024;
		config += tabAmounts[4] * 1048576;
		p.getActionSender().sendConfig(1246, config);
		config = 0;
		config += tabAmounts[5];
		config += tabAmounts[6] * 1024;
		config += tabAmounts[7] * 1048576;
		p.getActionSender().sendConfig(1247, config);
		config = -2013265920;
		config += tabAmounts[8];
		config += tabAmounts[9] * 1024;
		p.getActionSender().sendConfig(1248, config);*/
	}
	//20 * 1024^0 + 3 * 1024^1 + 4 * 1024^2
	public void openBank(final boolean dialogue, int x, int y) {
		openStatus = 0;
		changingPin = false;
		World.getInstance().registerCoordinateEvent(new AreaEvent(p, x-1, y-1, x+1, y+1) {
			@Override
			public void run() {
				if (dialogue) {
					return;
				}
				openBank();
			}
		});
	}
	
	public void openPinSettings(int i) {
		pinStatus = 0;
		setNewPinAttempt = 0;
		tempPin1 = new int[4];
		p.getActionSender().showChildInterface(14, 89, false); // "Yes i really want to do that" text.
		p.getActionSender().showChildInterface(14, 91, false); // "No forget i asked" text
		p.getActionSender().showChildInterface(14, 87, false); // Big black square with red bars
		if (!pinCorrect && bankPin != null) {
			openStatus = 1;
			if (isPinPending()) {
				verifyPin(false);
				return;
			}
			if (!isPinPending()) {
				openEnterPin();
				return;
			}
		}
		if (i == 1 && !isPinPending()) {
			p.getActionSender().showChildInterface(14, 60, false);
			p.getActionSender().showChildInterface(14, 65, false);
			p.getActionSender().showChildInterface(14, 61, false);
			p.getActionSender().modifyText("You have a PIN", 14, 69);
			p.getActionSender().modifyText("Players are reminded", 14, 42);
			p.getActionSender().modifyText("that they should NEVER tell", 14, 43);
			p.getActionSender().modifyText("anyone their bank PINs or", 14, 44);
			p.getActionSender().modifyText("password, nor should they", 14, 45);
			p.getActionSender().modifyText("ever enter their PINs on any", 14, 46);
			p.getActionSender().modifyText("website form.", 14, 47);
			p.getActionSender().modifyText("", 14, 48);
			p.getActionSender().modifyText("", 14, 49);
			p.getActionSender().modifyText("", 14, 50);
		} else if (i == 2){
			changingPin = false;
			p.getActionSender().modifyText("No changes made.", 14, 42);
			p.getActionSender().modifyText("", 14, 43);
			p.getActionSender().modifyText("", 14, 44);
			p.getActionSender().modifyText("", 14, 45);
			p.getActionSender().modifyText("", 14, 46);
			p.getActionSender().modifyText("", 14, 47);
			p.getActionSender().modifyText("", 14, 48);
			p.getActionSender().modifyText("", 14, 49);
			p.getActionSender().modifyText("", 14, 50);
		} else if (i == 3) {
			p.getActionSender().modifyText("Those numbers did not", 14, 42);
			p.getActionSender().modifyText("match.", 14, 43);
			p.getActionSender().modifyText("Your PIN has not been set,", 14, 45);
			p.getActionSender().modifyText("please try again if you wish", 14, 46);
			p.getActionSender().modifyText("to set a new PIN.", 14, 47);
			p.getActionSender().modifyText("", 14, 48);
			p.getActionSender().modifyText("", 14, 49);
			p.getActionSender().modifyText("", 14, 50);
		} else if (i == 4) {
			bankPin = null;
			lastPinChange = 0;
			lastDeletionRequest = 0;
			p.getActionSender().showChildInterface(14, 65, false);
			p.getActionSender().showChildInterface(14, 60, true);
			p.getActionSender().showChildInterface(14, 61, true);
			p.getActionSender().modifyText("The PIN has been cancelled", 14, 42);
			p.getActionSender().modifyText("and will NOT be set.", 14, 43);
			p.getActionSender().modifyText("", 14, 44);
			p.getActionSender().modifyText("You still do not have a Bank", 14, 45);
			p.getActionSender().modifyText("PIN.", 14, 46);
			p.getActionSender().modifyText("", 14, 47);
			p.getActionSender().modifyText("", 14, 48);
			p.getActionSender().modifyText("", 14, 49);
			p.getActionSender().modifyText("", 14, 50);
		} else
		if (i == 5 || isPinPending()) {
			p.getActionSender().modifyText("PIN coming soon", 14, 69);
			p.getActionSender().showChildInterface(14, 65, true);
			p.getActionSender().showChildInterface(14, 60, false);
			p.getActionSender().showChildInterface(14, 61, false);
			p.getActionSender().showChildInterface(14, 64, false);
			p.getActionSender().showChildInterface(14, 62, false);
			p.getActionSender().showChildInterface(14, 63, false);
			p.getActionSender().modifyText("You have requested that a", 14, 42);
			p.getActionSender().modifyText("PIN be set on your bank", 14, 43);
			p.getActionSender().modifyText("account. This will take effect", 14, 44);
			p.getActionSender().modifyText("in another " + (recoveryDelay - Utility.getDaysFromMillis(lastPinChange)) + " days.", 14, 45);
			p.getActionSender().modifyText("", 14, 46);
			p.getActionSender().modifyText("If you wish to cancel this", 14, 47);
			p.getActionSender().modifyText("PIN, please use the button", 14, 48);
			p.getActionSender().modifyText("on the left.", 14, 49);
			p.getActionSender().modifyText("", 14, 50);
		} else if (i == 6 && changingPin) {
			changingPin = false;
			p.getActionSender().modifyText("Those numbers did not", 14, 42);
			p.getActionSender().modifyText("match.", 14, 43);
			p.getActionSender().modifyText("", 14, 44);
			p.getActionSender().modifyText("Your pin has NOT been", 14, 45);
			p.getActionSender().modifyText("changed, please try again", 14, 46);
			p.getActionSender().modifyText("If you wish to set a new PIN", 14, 47);
			p.getActionSender().modifyText("", 14, 48);
			p.getActionSender().modifyText("", 14, 49);
			p.getActionSender().modifyText("", 14, 50);
		} else if (i == 7 && changingPin) {
			changingPin = false;
			lastDeletionRequest = 0;
			p.getActionSender().showChildInterface(14, 60, false);
			p.getActionSender().showChildInterface(14, 65, false);
			p.getActionSender().showChildInterface(14, 61, false);
			p.getActionSender().modifyText("You have a PIN", 14, 69);
			p.getActionSender().modifyText("Your PIN has been changed", 14, 42);
			p.getActionSender().modifyText("to the number you entered.", 14, 43);
			p.getActionSender().modifyText("This takes effect", 14, 44);
			p.getActionSender().modifyText("immediately.", 14, 45);
			p.getActionSender().modifyText("", 14, 46);
			p.getActionSender().modifyText("If you cannot remember that", 14, 47);
			p.getActionSender().modifyText("new number, we STRONGLY", 14, 48);
			p.getActionSender().modifyText("advise you to delete the PIN", 14, 49);
			p.getActionSender().modifyText("now.", 14, 50);
		} else if (i == 8) {
			lastDeletionRequest = 0;
			p.getActionSender().showChildInterface(14, 60, false);
			p.getActionSender().showChildInterface(14, 65, false);
			p.getActionSender().showChildInterface(14, 61, false);
			p.getActionSender().modifyText("You have a PIN", 14, 69);
			p.getActionSender().modifyText("The requested removal of", 14, 42);
			p.getActionSender().modifyText("your PIN has been cancelled.", 14, 43);
			p.getActionSender().modifyText("Your PIN will NOT be deleted.", 14, 44);
			p.getActionSender().modifyText("", 14, 45);
			p.getActionSender().modifyText("If it wasn't you that tried to", 14, 46);
			p.getActionSender().modifyText("delete it, someone else may", 14, 47);
			p.getActionSender().modifyText("have been on your account.", 14, 48);
			p.getActionSender().modifyText("- Please consider changing", 14, 49);
			p.getActionSender().modifyText("your password immediately!", 14, 50);
		} else if (i == 9 && deletePin) {
			bankPin = null;
			deletePin = false;
			p.getActionSender().modifyText("Your Bank PIN has now been", 14, 42);
			p.getActionSender().modifyText("deleted.", 14, 43);
			p.getActionSender().modifyText("", 14, 44);
			p.getActionSender().modifyText("This means that there is no", 14, 45);
			p.getActionSender().modifyText("PIN protection on your bank", 14, 46);
			p.getActionSender().modifyText("account.", 14, 47);
			p.getActionSender().modifyText("", 14, 48);
			p.getActionSender().modifyText("", 14, 49);
			p.getActionSender().modifyText("", 14, 50);
			p.getActionSender().sendMessage("Your bank PIN has been successfully deleted.");
		}
		if (bankPin == null) {
			tempPin2 = new int[4];
			p.getActionSender().modifyText("No PIN set", 14, 69);
			p.getActionSender().showChildInterface(14, 62, false);
			p.getActionSender().showChildInterface(14, 63, false);
			p.getActionSender().showChildInterface(14, 64, false);
			p.getActionSender().showChildInterface(14, 65, false);
			p.getActionSender().showChildInterface(14, 60, true);
			p.getActionSender().showChildInterface(14, 61, true);
		}
		p.getActionSender().modifyText(recoveryDelay + " days", 14, 71);
		p.getActionSender().displayInterface(14);
	}
	
	public void handleEnterPin(int buttonId) {
		if ((bankPin == null && !isPinPending()) || changingPin) {
			if (pinStatus == 4 && setNewPinAttempt == 1) {
				return;
			}
			p.getActionSender().modifyText(MESSAGES[pinStatus], 13, 32);
			p.getActionSender().modifyText("*", 13, ASTERIK[pinStatus]);
			if (setNewPinAttempt == 0) {
				tempPin1[pinStatus] = (pinSeq[buttonId - 1]);
			} else
			if (setNewPinAttempt == 1) {
				tempPin2[pinStatus] = (pinSeq[buttonId - 1]);
			}
			pinStatus++;
			if (pinStatus == 4 && setNewPinAttempt == 0) {
				setNewPinAttempt++;
				pinStatus = 0;
				p.getActionSender().modifyText("Now please enter that number again!", 13, 28);
				p.getActionSender().modifyText("First click the FIRST digit.", 13, 32);
				for (int i = 21; i <= 24; i++) {
					p.getActionSender().modifyText("?", 13, i);
				}
			} else
			if (pinStatus == 4 && setNewPinAttempt == 1) {
				for (int i = 0; i < tempPin1.length; i++) {
					if (tempPin1[i] != tempPin2[i]) {
						p.getActionSender().sendMessage("The two PIN numbers you entered did not match.");
						openPinSettings(changingPin ? 6 : 3);
						return;
					}
				}
				if (bankPin == null && !changingPin) {
					lastPinChange = System.currentTimeMillis();
				}
				bankPin = tempPin1;
				pinCorrect = true;
				openPinSettings(changingPin ? 7 : 5);
				return;
			}
			scrambleNumbers();
		} else {
			if (pinStatus >= 4) {
				return;
			}
			p.getActionSender().modifyText(MESSAGES[pinStatus], 13, 32);
			p.getActionSender().modifyText("*", 13, ASTERIK[pinStatus]);
			tempPin1[pinStatus] = (pinSeq[buttonId - 1]);
			pinStatus++;
			scrambleNumbers();
			if (pinStatus == 4) {
				for (int i = 0; i < tempPin1.length; i++) {
					if (tempPin1[i] != bankPin[i]) {
						p.getActionSender().closeInterfaces();
						p.getActionSender().modifyText("The PIN number you entered was incorrect.", 210, 1);
						p.getActionSender().sendChatboxInterface2(210);
						return;
					}
				}
				pinCorrect = true;
				p.getActionSender().sendMessage("You have correctly entered your PIN.");
				if (lastDeletionRequest != 0) {
					openPinSettings(8);
					return;
				}
				if (openStatus == 0) {
					openBank();
				} else {
					openPinSettings(1);
				}
			}
		}
	}
	
	public void cancelPendingPin() {
		if (bankPin == null || !isPinPending()) {
			return;
		}
		pinCorrect = true;
		openPinSettings(4);
	}
	
	public void changePin() {
		if (bankPin == null || changingPin || isPinPending()) {
			return;
		}
		tempPin2 = new int[4];
		changingPin = true;
		p.getActionSender().showChildInterface(14, 87, true);
		p.getActionSender().showChildInterface(14, 89, true);
		p.getActionSender().showChildInterface(14, 91, true);
		p.getActionSender().modifyText("Do you really wish to change your Bank PIN?", 14, 73);
		p.getActionSender().modifyText("Yes, I am ready for a new one.", 14, 89);
		p.getActionSender().modifyText("No thanks, I'll stick to my current one.", 14, 91);
	}
	
	public void deletePin() {
		if (bankPin == null || isPinPending()) {
			return;
		}
		deletePin = true;
		p.getActionSender().showChildInterface(14, 87, true);
		p.getActionSender().showChildInterface(14, 89, true);
		p.getActionSender().showChildInterface(14, 91, true);
		p.getActionSender().modifyText("Do you really wish to delete your Bank PIN?", 14, 73);
		p.getActionSender().modifyText("Yes, I don't need a PIN anymore.", 14, 89);
		p.getActionSender().modifyText("No thanks, I'd rather keep the extra security.", 14, 91);
	}
	
	public void verifyPin(boolean verified) {
		int i = recoveryDelay - Utility.getDaysFromMillis(lastPinChange);
		if (!verified) {
			p.getActionSender().showChildInterface(14, 87, true);
			p.getActionSender().showChildInterface(14, 89, true);
			p.getActionSender().showChildInterface(14, 91, true);
			p.getActionSender().modifyText("A PIN will be set on your account in another " + i + " days.", 14, 73);
			p.getActionSender().modifyText("Yes, I asked for this. I want this PIN.", 14, 89);
			p.getActionSender().modifyText("No, I didn't ask for this. Cancel it.", 14, 91);
			p.getActionSender().displayInterface(14);
			return;
		}
		pinCorrect = true;
		p.getActionSender().sendMessage("Your PIN is still pending.");
		if (openStatus == 0) {
			openBank();
		} else {
			openPinSettings(1);
		}
	}
	
	public void openEnterPin() {
		if (deletePin) {
			openPinSettings(9);
			return;
		}
		if (bankPin == null || changingPin) {
			p.getActionSender().showChildInterface(13, 29, false);
		} else if (bankPin != null && !changingPin){
			p.getActionSender().setRightClickOptions(2, 851997, -1, -1);
		}
		if (lastDeletionRequest != 0) {
			int i = recoveryDelay -  Utility.getDaysFromMillis(lastDeletionRequest);
			p.getActionSender().modifyText("Your Bank PIN will be deleted in another " + i + " days.", 13, 31);
		} else {
			p.getActionSender().modifyText("Bank of " + p.getPlayerDetails().getDisplayName(), 13, 31);
		}
		scrambleNumbers();
		p.getActionSender().displayInterface(13);
	}
	
	public void changePinDelay() {
		String s = bankPin == null ? "But you" : "";
		String s1 = bankPin == null ? "haven't got one..." : "";
		recoveryDelay = recoveryDelay == 3 ? 7 : 3;
		p.getActionSender().modifyText(recoveryDelay + " days", 14, 71);
		p.getActionSender().modifyText("Your recovery delay has", 14, 42);
		p.getActionSender().modifyText("now been set to " + recoveryDelay + " days.", 14, 43);
		p.getActionSender().modifyText("", 14, 44);
		p.getActionSender().modifyText("You would have to wait this", 14, 45);
		p.getActionSender().modifyText("long to delete your PIN if", 14, 46);
		p.getActionSender().modifyText("you forgot it. " + s, 14, 47);
		p.getActionSender().modifyText(s1, 14, 48);
	}
	
	public void displayFirstConfirmation() {
		p.getActionSender().showChildInterface(14, 87, true);
		p.getActionSender().showChildInterface(14, 89, true);
		p.getActionSender().showChildInterface(14, 91, true);
		p.getActionSender().modifyText("Do you really wish to set a PIN on your bank account?", 14, 73);
		p.getActionSender().modifyText("Yes, I really want a bank PIN. I will never forget it!", 14, 89);
		p.getActionSender().modifyText("No, I might forget it!", 14, 91);
	}
	
	public void scrambleNumbers() {
		pinSeq = new int[10];
		int id[] = {0,1,2,3,4,5,6,7,8,9};
		numbers = id;
		int j = -1;
		for (int i = 0; i <= 9; i++) {
			int num = -1;
			for (; ;) {
				num = Utility.random(numbers.length - 1);
				if (numbers[num] == -1) {
					continue;
				} else {
					j = numbers[num];
					numbers[num] = -1;
					break;
				}
			}
			p.getActionSender().moveChildInterface(13, 13, 0, 0);
			p.getActionSender().modifyText(""+j, 13, (i + 11));
			pinSeq[i] = j;
		}
	}

	public boolean isPinPending() {
		if (lastPinChange == 0) {
			return false;
		}
		int hours = recoveryDelay == 7 ? 168 : 72;
		return Utility.getHoursFromMillis(lastPinChange) < hours;
	}
	
	public void forgotPin() {
		int i = recoveryDelay -  Utility.getDaysFromMillis(lastDeletionRequest);
		p.getActionSender().closeInterfaces();
		if (lastDeletionRequest == 0) {
			lastDeletionRequest = System.currentTimeMillis();
			i = recoveryDelay -  Utility.getDaysFromMillis(lastDeletionRequest);
			p.getActionSender().modifyText("Since you don't know your PIN, it will be deleted in another " + i, 212, 1);
			p.getActionSender().modifyText("days. If you wish to cancel this change, you may do so by entering", 212, 2);
			p.getActionSender().modifyText("your PIN correctly next time you attempt to use your Bank.", 212, 3);
			p.getActionSender().sendChatboxInterface2(212);
		} else {
			p.getActionSender().modifyText("You have already requested that your PIN be deleted.", 211, 1);
			p.getActionSender().modifyText("This will take effect after another " + i + " days.", 211, 2);
			p.getActionSender().sendChatboxInterface2(211);
		}
	}
	
	public void refreshBank() {
		p.getActionSender().sendItems(-1, 64000, 95, bank);
		p.getActionSender().sendItems(-1, 64000, 93, p.getInventory().getItems());
		p.getActionSender().refreshInventory();
	}
	
	/*public void sendTabConfig() {
		int config = 0;
		config += getItemsInTab(p, 2);
		config += getItemsInTab(p, 3) * 1024;
		config += getItemsInTab(p, 4) * 1048576;
		p.getActionSender().sendConfig(1246, config);
		config = 0;
		config += getItemsInTab(p, 5);
		config += getItemsInTab(p, 6) * 1024;
		config += getItemsInTab(p, 7) * 1048576;
		p.getActionSender().sendConfig(1247, config);
		config = -2013265920;
		config += getItemsInTab(p, 8);
		config += getItemsInTab(p, 9) * 1024;
		p.getActionSender().sendConfig(1248, config);
	}*/
	
	public void asNote() {
		if (p.getTemporaryAttribute("withdrawNote") == null) {
			p.setTemporaryAttribute("withdrawNote", (Boolean) true);
			asNote = true;
			return;
		}
		if ((Boolean) p.getTemporaryAttribute("withdrawNote")) {
			p.setTemporaryAttribute("withdrawNote", (Boolean) false);
		} else {
			p.setTemporaryAttribute("withdrawNote", (Boolean) true);
		}
		asNote = (Boolean) p.getTemporaryAttribute("withdrawNote");
	}
	
	public void deposit(int invenSlot, int amount) {
		if (!banking) {
			return;
		}
		Inventory inv = p.getInventory();
		int item = inv.getItemInSlot(invenSlot);
		int slot = findItem(item);
		int amountToAdd = amount;
		boolean isNote = ItemDefinition.forId(item).isNoted();
		boolean stackable = ItemDefinition.forId(item).isStackable();
		int itemToAdd = item;
		boolean enteringIntoBlankSlot = false;
		boolean addedItem = false;
		if (amountToAdd > inv.getItemAmount(itemToAdd)) {
			amountToAdd = inv.getItemAmount(itemToAdd);
		}
		if (inv.getItemInSlot(invenSlot) == item && item != -1) {
			if (isNote) {
				itemToAdd = ItemConstants.getUnNotedItem(itemToAdd);
				slot = findItem(itemToAdd);
			}
			if (slot == -1) {
				slot = findFreeSlot();
				if (slot == -1) {
					p.getActionSender().sendMessage("Your bank is full!");
					return;
				}
				enteringIntoBlankSlot = true;
			}
			if (amount > 1 && !stackable && !isNote) {
				for (int i = 0; i < amount; i++) {
					if (!inv.deleteItem(item)) {
						break;
					}
					bank[slot].setItemId(itemToAdd);
					bank[slot].setItemAmount(bank[slot].getItemAmount() + 1);
					addedItem = true;
				}
				/*if (enteringIntoBlankSlot && addedItem) {
					tabAmounts[currentTab]++;
					currentTab = 10; // TODO remove once force tab switching is fixed
				}*/
				refreshBank();
				return;
			}
			if (amount == 1 || stackable || isNote) {
				if (!inv.deleteItem(item, amountToAdd)) {
					return;
				}
				bank[slot].setItemId(itemToAdd);
				bank[slot].setItemAmount(bank[slot].getItemAmount() + amountToAdd);
				addedItem = true;
				/*if (enteringIntoBlankSlot && addedItem) {
					tabAmounts[currentTab]++;
					currentTab = 10; // TODO remove once force tab sswitching is fixed
				}*/
				refreshBank();
				return;
			}
		}
	}
	
	public void withdraw(int slot, int amount) {
		if (!banking) {
			return;
		}
		int item = getItemInSlot(slot);
		if (item == -1) {
			return;
		}
		Inventory inv = p.getInventory();
		int amountToRemove = amount;
		int itemToRemove = item;
		boolean stackable = ItemDefinition.forId(item).isStackable();
		if (amountToRemove > bank[slot].getItemAmount()) {
			amountToRemove = bank[slot].getItemAmount();
		}
		if (asNote) {
			itemToRemove = ItemConstants.getNotedItem(itemToRemove);
			if (itemToRemove == item || stackable) {
				itemToRemove = item;
				p.getActionSender().sendMessage("That item can't be withdrawn as a note.");
			}
		}
		if (amount > 1 && !stackable && !asNote) {
			for (int i = 0; i < amount; i++) {
				if (!inv.addItem(itemToRemove)) {
					break;
				}
				bank[slot].setItemAmount(bank[slot].getItemAmount() - 1);
				if (bank[slot].getItemAmount() <= 0) {
					bank[slot].setItemId(-1);
					bank[slot].setItemAmount(0);
					/*tabAmounts[currentTab]--;
					currentTab = 10; // TODO remove once force tab sswitching is fixed
					*/
					arrangeBank();
					break;
				}
			}
			refreshBank();
			return;
		} else if (amount == 1 || stackable || asNote) {
			if (!inv.addItem(itemToRemove, amountToRemove)) {
				return;
			}
			bank[slot].setItemAmount(bank[slot].getItemAmount() - amountToRemove);
			if (bank[slot].getItemAmount() <= 0) {
				bank[slot].setItemId(-1);
				bank[slot].setItemAmount(0);
				//tabAmounts[currentTab]--;
				//currentTab = 10; // TODO remove once force tab switching is fixed
				arrangeBank();
			}
			refreshBank();
			return;
		}
	}

	public void arrangeBank() {
		Item[] oldData = bank;
		bank = new Item[MAX_SLOTS];
		int ptr = 0;
		for(int i = 0; i < MAX_SLOTS; i++) {
			if(oldData[i].getItemId() != -1) {
				bank[ptr++] = oldData[i];
			}
		}
		for(int i = ptr; i < MAX_SLOTS; i++) {
			bank[i] = new Item(-1, 0);
		}
	}
	
	public void setCurrentTab(int tab) {
		this.currentTab = tab;
	}
	
	public void setBanking(boolean b) {
		this.banking = b;
	}
	
	public int findFreeSlot() {
		for (int i = 0; i < MAX_SLOTS; i++) {
			if (bank[i].getItemId() == -1) {
				return i;
			}
		}
		return -1;
	}
	
	public int findItem(int itemId) {
		for (int i = 0; i < MAX_SLOTS; i++) {
			if (bank[i].getItemId() == itemId) {
				return i;
			}
		}
		return -1;
	}
	
	public boolean hasItem(int itemId) {
		return findItem(itemId) > -1;
	}
	
	public void replaceItem(int itemId) {
		
	}
	
	public int getAmountInSlot(int slot) {
		return bank[slot].getItemAmount();
	}

	public int getItemInSlot(int slot) {
		return bank[slot].getItemId();
	}
	
	public Item getSlot(int slot) {
		return bank[slot];
	}
	
	public Item[] getBank() {
		return bank;
	}

	public void setPlayer(Player player) {
		this.p = player;
	}
	
	public boolean isBanking() {
		return banking;
	}
	
	public void setLastXAmount(int i) {
		this.lastXAmount = i;
		p.getActionSender().sendConfig(1249, lastXAmount);
	}

	public int getLastXAmount() {
		return lastXAmount;
	}
}
