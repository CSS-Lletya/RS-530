package com.xeno.packethandler;

import org.apache.mina.common.IoSession;

import com.xeno.content.DestroyItem;
import com.xeno.content.emote.Skillcape;
import com.xeno.entity.item.GroundItem;
import com.xeno.entity.item.ItemConstants;
import com.xeno.entity.player.Player;
import com.xeno.event.CoordinateEvent;
import com.xeno.net.Packet;
import com.xeno.net.definitions.ItemDefinition;
import com.xeno.util.LogUtility;
import com.xeno.util.LogUtility.LogType;
import com.xeno.world.Location;
import com.xeno.world.World;

/**
 * All item related packets.
 * @author Luke132
 * @author Graham
 */
public class ItemInteract implements PacketHandler {

	private static final int EQUIP = 55; // d
	private static final int ITEM_ON_ITEM = 27; // d
	private static final int INVEN_CLICK = 156; // d
	private static final int ITEM_ON_OBJECT = 134; // d
	private static final int OPERATE = 206; // d
	private static final int DROP = 135; // d
	private static final int PICKUP = 66; // d
	private static final int SWAP_SLOT = 231; // d
	private static final int SWAP_SLOT2 = 79; // d
	private static final int RIGHT_CLICK_OPTION1 = 161; // d
	private static final int RIGHT_CLICK_OPTION2 = 153; // d
	private static final int EXAMINE_ITEM = 92; // d
	
	@Override
	public void handlePacket(Player player, IoSession session, Packet packet) {
		switch(packet.getId()) {
			case EQUIP:
				handleEquipItem(player, packet);
				break;
				
			case ITEM_ON_ITEM:
				handleItemOnItem(player, packet);
				break;
				
			case INVEN_CLICK:
				handleInvenClickItem(player, packet);
				break;
				
			case ITEM_ON_OBJECT:
				handleItemOnObject(player, packet);
				break;
				
			case OPERATE:
				handleOperateItem(player, packet);
				break;
				
			case DROP:
				handleDropItem(player, packet);
				break;
				
			case PICKUP:
				handlePickupItem(player, packet);
				break;
				
			case SWAP_SLOT:
				handleSwapSlot(player, packet);
				break;
				
			case SWAP_SLOT2:
				handleSwapSlot2(player, packet);
				break;
				
			case RIGHT_CLICK_OPTION1:
				handleRightClickOne(player, packet);
				break;
				
			case RIGHT_CLICK_OPTION2:
				handleRightClickTwo(player, packet);
				break;
				
			case EXAMINE_ITEM:
				handleExamineItem(player, packet);
				break;
		}
	}

	private void handleEquipItem(Player player, Packet packet) {
		int item = packet.readLEShort();
		int slot = packet.readShortA();
		int interfaceId = packet.readInt(); // actually readInt1
		if (slot > 28 || slot < 0 || player.isDead() || player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		if (player.getInventory().getItemInSlot(slot) == item) {
			//player.getActionSender().closeInterfaces();
			player.getEquipment().equipItem(player.getInventory().getItemInSlot(slot), slot);
		}
	}
	
	private void handleItemOnItem(Player player, Packet packet) {
		int itemSlot = packet.readShort();
		int unused = packet.readInt();
		int withSlot = packet.readLEShort();
		int unused2 = packet.readInt();
		int itemUsed = packet.readLEShortA();
		int usedWith = packet.readLEShortA();
		if (itemSlot > 28 || itemSlot < 0 || withSlot > 28 || withSlot < 0 || player.isDead() || player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		player.getActionSender().closeInterfaces();
		if (player.getInventory().getSlot(itemSlot).getItemId() == itemUsed && player.getInventory().getSlot(withSlot).getItemId() == usedWith) {
				player.getActionSender().sendMessage("Nothing interesting happens.");
		}
		
	}
	
	private void handleInvenClickItem(Player player, Packet packet) {
		int slot = packet.readLEShortA();
		int item = packet.readShortA();
		int childId = packet.readLEShort();
		int interfaceId = packet.readLEShort();
		if (slot > 28 || slot < 0 || player.isDead() || player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		if (player.getInventory().getItemInSlot(slot) == item) {
			player.getActionSender().closeInterfaces();
			switch(item) {
				case 952: // Spade
					player.animate(830);
					
					player.getActionSender().sendMessage("You find nothing.");
					break;
			}
		}
	}
	
	private void handleItemOnObject(Player player, Packet packet) {
		int objectX = packet.readShortA();
		int item = packet.readShort();
		int objectY = packet.readLEShort();
		int slot = packet.readShort();
		int interfaceId = packet.readLEShort();
		int child = packet.readShort();
		int objectId = packet.readShortA();
		player.setClickItem(item);
		if (slot > 28 || slot < 0 || player.isDead() || player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		System.out.println("Item on object = " + objectId + " " + objectX + " " + objectY);
		player.getActionSender().closeInterfaces();
	//	player.setFaceLocation(new FaceLocation(objectX, objectY));
		if (player.getInventory().getItemInSlot(slot) == item) {
			}
			switch(objectId) {
				
					
				case -28755: // Lumbridge fountain.
				case 24214:	// Fountain in east Varrock.
				case 24265:	// Varrock main fountain.
				case 11661:	// Falador waterpump.
				case 11759:	// Falador south fountain.
				case 879:	// Camelot fountains.
				case 29529:	// Sink.
				case 874:	// Sink.
					
					break;
					
				case -28580: // Lumbridge furnace
				case 11666: // Falador furnace
					
					break;
					
				case 2783: // Anvil
					
					break;
					
				default:
					player.getActionSender().sendMessage("Nothing interesting happens.");
					break;
			}
		}
	
	private void handleOperateItem(Player player, Packet packet) {
		int item = packet.readShortA();
		int slot = packet.readLEShort();
		int interfaceId = packet.readLEShort();
		int childId = packet.readLEShort();
		if (slot < 0 || slot > 28 || player.isDead() || player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		if (player.getEquipment().getItemInSlot(slot) == item) {
			player.getActionSender().closeInterfaces();
			if (slot == 1 && Skillcape.emote(player)) {
				return;
			}
			player.getActionSender().sendMessage("This item isn't operable.");
		}
	}
	
	private void handleDropItem(Player player, Packet packet) {
		int item = packet.readShortA();
		int slot = packet.readShortA();
		int interfaceId = packet.readLEShort();
		int childId = packet.readShort();
		if (slot > 28 || slot < 0 || player.isDead() || player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		if (player.getInventory().getItemInSlot(slot) == item) {
			player.getActionSender().closeInterfaces();
			if (ItemConstants.isPlayerBound(player.getInventory().getItemInSlot(slot))) {
				DestroyItem.displayInterface(player, player.getInventory().getItemInSlot(slot));
				//return;
			}
			int id = player.getInventory().getItemInSlot(slot);
			int amt = player.getInventory().getAmountInSlot(slot);
			GroundItem i = new GroundItem(id, amt, Location.location(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()), player);
			if (player.getInventory().deleteItem(id, slot, amt)) {
				if (!World.getInstance().getGroundItems().addToStack(id, amt, player.getLocation(), player)) {
					World.getInstance().getGroundItems().newEntityDrop(i);
				}
			}
		}
	}
	
	private void handlePickupItem(final Player player, Packet packet) {
		int x = packet.readLEShort();
		final int id  = packet.readShort();
		int y  = packet.readLEShortA();
		Location l = Location.location(x, y, player.getLocation().getZ());
		if (x < 1000 || y < 1000 | id < 0 || player.isDead() || player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		player.getActionSender().closeInterfaces();
		if(player.getLocation().equals(l)) {
			World.getInstance().getGroundItems().pickupItem(player, id, player.getLocation());
			return;
		}
		World.getInstance().registerCoordinateEvent(new CoordinateEvent(player, l) {
			@Override
			public void run() {
				World.getInstance().getGroundItems().pickupItem(player, id, player.getLocation());
			}
		});
	}
	
	private void handleSwapSlot(Player player, Packet packet) {
		int oldSlot = packet.readShort();
		int childId = packet.readLEShort();
		int interfaceId = packet.readLEShort();
		int newSlot = packet.readShortA();
		int swapType = packet.readByteS();
		int oldItem = player.getInventory().getItemInSlot(oldSlot);
		int oldAmount = player.getInventory().getAmountInSlot(oldSlot);
		int newItem = player.getInventory().getItemInSlot(newSlot);
		int newAmount = player.getInventory().getAmountInSlot(newSlot);
		if (oldSlot > 28 || oldSlot < 0 || newSlot > 28 || oldSlot < 0 || player.isDead() || player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		switch (interfaceId) {
			case 149:
				if (swapType == 0 && childId == 0) {
					player.getInventory().getSlot(oldSlot).setItemId(newItem);
					player.getInventory().getSlot(oldSlot).setItemAmount(newAmount);
					player.getInventory().getSlot(newSlot).setItemId(oldItem);
					player.getInventory().getSlot(newSlot).setItemAmount(oldAmount);
				}
				break;
			
			default:
				LogUtility.log(LogType.WARN, "UNHANDLED ITEM SWAP 1 : interface = " + interfaceId);
				break;
		}
		//No need to update the screen since the client does it for us!
	}
	
	private void handleSwapSlot2(Player player, Packet packet) {
		int interfaceId = packet.readLEShort();
		int child = packet.readShort();
		int newSlot = packet.readLEShort();
		int interface2 = packet.readShort();
		int child2 = packet.readShort();
		int oldSlot = packet.readLEShort();
		int oldItem = player.getInventory().getItemInSlot(oldSlot);
		int oldAmount = player.getInventory().getAmountInSlot(oldSlot);
		int newItem = player.getInventory().getItemInSlot(newSlot);
		int newAmount = player.getInventory().getAmountInSlot(newSlot);
		if (oldSlot > 28 || oldSlot < 0 || newSlot > 28 || oldSlot < 0 || player.isDead() || player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		switch (interfaceId) {
			case 621: // Shop.
			case 763: // Bank.
			case 336: // Duel
				player.getInventory().getSlot(oldSlot).setItemId(newItem);
				player.getInventory().getSlot(oldSlot).setItemAmount(newAmount);
				player.getInventory().getSlot(newSlot).setItemId(oldItem);
				player.getInventory().getSlot(newSlot).setItemAmount(oldAmount);
				break;
			
			default:
				LogUtility.log(LogType.WARN, "UNHANDLED ITEM SWAP 2 : interface = " + interfaceId);
				break;
		}
		//No need to update the screen since the client does it for us!
		player.getActionSender().refreshInventory();
	}
	
	private void handleRightClickOne(Player player, Packet packet) {
		int childId = packet.readLEShort();
		int interfaceId = packet.readLEShort();
		int item = packet.readLEShortA();
		int slot = packet.readLEShortA();
		if (slot > 28 || slot < 0 || player.isDead() || player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		if (player.getInventory().getItemInSlot(slot) == item) {
			player.getActionSender().closeInterfaces();
			if (interfaceId == 149 && childId == 0) {
			}
		}
	}
	
	private void handleRightClickTwo(Player player, Packet packet) {
		int childId = packet.readLEShort();
		int interfaceId = packet.readLEShort();
		int slot = packet.readLEShort();
		int item = packet.readLEShort();
		if (slot < 0 || slot > 28 || player.isDead() || player.getTemporaryAttribute("cantDoAnything") != null) {
			return;
		}
		if (player.getInventory().getItemInSlot(slot) == item) {
			player.getActionSender().closeInterfaces();
			switch(player.getInventory().getItemInSlot(slot)) {
				case 5509: // Small pouch.
					player.getActionSender().sendMessage("There is " + player.getSettings().getSmallPouchAmount() + " Pure essence in your small pouch. (holds 3).");
					break;
					 
				case 5510: // Medium pouch.
					player.getActionSender().sendMessage("There is " + player.getSettings().getMediumPouchAmount() + " Pure essence in your medium pouch. (holds 6).");
					 break;
					 
				 case 5512: // Large pouch.
					 player.getActionSender().sendMessage("There is " + player.getSettings().getLargePouchAmount() + " Pure essence in your large pouch. (holds 9).");
					 break;
					 
				 case 5514: // Giant pouch.
					 player.getActionSender().sendMessage("There is " + player.getSettings().getGiantPouchAmount() + " Pure essence in your giant pouch. (holds 12).");
					 break;
			}
		}
	}
	
	private void handleExamineItem(Player player, Packet packet) {
		int item = packet.readLEShortA();
		if (item < 0 || item > 14630) {
			return;
		}
		String examine = ItemDefinition.forId(item).getExamine();
		player.getActionSender().sendMessage(examine);
	}
}
