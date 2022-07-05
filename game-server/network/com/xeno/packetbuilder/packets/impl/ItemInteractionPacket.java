package com.xeno.packetbuilder.packets.impl;

import com.xeno.content.DestroyItem;
import com.xeno.content.emote.Skillcape;
import com.xeno.entity.Location;
import com.xeno.entity.actor.attribute.Attribute;
import com.xeno.entity.actor.item.GroundItem;
import com.xeno.entity.actor.item.Item;
import com.xeno.entity.actor.item.ItemConstants;
import com.xeno.entity.actor.player.Player;
import com.xeno.entity.actor.player.task.CoordinateTask;
import com.xeno.net.Packet;
import com.xeno.net.definitions.ItemDefinition;
import com.xeno.net.entity.masks.Animation;
import com.xeno.packetbuilder.packets.OutgoingPacket;
import com.xeno.packetbuilder.packets.OutgoingPacketSignature;
import com.xeno.utility.LogUtility;
import com.xeno.utility.LogUtility.LogType;
import com.xeno.world.World;

@OutgoingPacketSignature(packetId = { 55, 27, 156, 134, 206, 135, 66, 231, 161, 153, 92 }, description = "Represents an event where a Player interacts with an Item")
public class ItemInteractionPacket implements OutgoingPacket {

	private static final int EQUIP = 55;
	private static final int ITEM_ON_ITEM = 27;
	private static final int INVEN_CLICK = 156;
	private static final int ITEM_ON_OBJECT = 134;
	private static final int OPERATE = 206;
	private static final int DROP = 135;
	private static final int PICKUP = 66;
	private static final int SWAP_SLOT = 231;
	private static final int SWAP_SLOT2 = 79;
	private static final int RIGHT_CLICK_OPTION1 = 161;
	private static final int RIGHT_CLICK_OPTION2 = 153;
	private static final int EXAMINE_ITEM = 92;

	@Override
	public void execute(Player player, Packet packet) {
		switch (packet.getId()) {
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
		if (slot > 28 || slot < 0 || player.getAttributes().exist(Attribute.DEAD) || player.getAttributes().exist(Attribute.LOCKED)) {
			return;
		}
		if (player.getInventory().getItemInSlot(slot) == item) {
			// player.getInterfaceManager().closeInterfaces();
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
		if (itemSlot > 28 || itemSlot < 0 || withSlot > 28 || withSlot < 0 || player.getAttributes().exist(Attribute.DEAD)
				|| player.getAttributes().exist(Attribute.LOCKED)) {
			return;
		}
		if (!player.getMapZoneManager().execute(player, zone -> zone.canUseItemOnItem(player, new Item(itemUsed), new Item(usedWith))))
			return;
		player.getInterfaceManager().closeInterfaces();
		if (player.getInventory().getSlot(itemSlot).getItemId() == itemUsed
				&& player.getInventory().getSlot(withSlot).getItemId() == usedWith) {
			player.getActionSender().sendMessage("Nothing interesting happens.");
		}

	}

	private void handleInvenClickItem(Player player, Packet packet) {
		int slot = packet.readLEShortA();
		int item = packet.readShortA();
		int childId = packet.readLEShort();
		int interfaceId = packet.readLEShort();
		if (slot > 28 || slot < 0 || player.getAttributes().exist(Attribute.DEAD) || player.getAttributes().exist(Attribute.LOCKED)) {
			return;
		}
		if (player.getInventory().getItemInSlot(slot) == item) {
			player.getInterfaceManager().closeInterfaces();
			switch (item) {
			case 952: // Spade
				player.setNextAnimation(new Animation(830));

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
		if (slot > 28 || slot < 0 || player.getAttributes().exist(Attribute.DEAD) || player.getAttributes().exist(Attribute.LOCKED)) {
			return;
		}
		System.out.println("Item on object = " + objectId + " " + objectX + " " + objectY);
		player.getInterfaceManager().closeInterfaces();
//	player.setFaceLocation(new FaceLocation(objectX, objectY));
		if (player.getInventory().getItemInSlot(slot) == item) {
		}
		switch (objectId) {

		case -28755: // Lumbridge fountain.
		case 24214: // Fountain in east Varrock.
		case 24265: // Varrock main fountain.
		case 11661: // Falador waterpump.
		case 11759: // Falador south fountain.
		case 879: // Camelot fountains.
		case 29529: // Sink.
		case 874: // Sink.

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
		if (slot < 0 || slot > 28 || player.getAttributes().exist(Attribute.DEAD) || player.getAttributes().exist(Attribute.LOCKED)) {
			return;
		}
		if (player.getEquipment().getItemInSlot(slot) == item) {
			player.getInterfaceManager().closeInterfaces();
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
		if (slot > 28 || slot < 0 || player.getAttributes().exist(Attribute.DEAD) || player.getAttributes().exist(Attribute.LOCKED)) {
			return;
		}
		if (player.getInventory().getItemInSlot(slot) == item) {
			player.getInterfaceManager().closeInterfaces();
			if (ItemConstants.isPlayerBound(player.getInventory().getItemInSlot(slot))) {
				DestroyItem.displayInterface(player, player.getInventory().getItemInSlot(slot));
				// return;
			}
			int id = player.getInventory().getItemInSlot(slot);
			int amt = player.getInventory().getAmountInSlot(slot);
			GroundItem i = new GroundItem(id, amt, Location.location(player.getLocation().getX(),
					player.getLocation().getY(), player.getLocation().getZ()), player);
			if (player.getMapZoneManager().execute(player, zone -> !zone.canDropItem(player, i)))
				return;
			if (player.getInventory().deleteItem(id, slot, amt)) {
				if (!World.getInstance().getGroundItems().addToStack(id, amt, player.getLocation(), player)) {
					World.getInstance().getGroundItems().newEntityDrop(i);
				}
			}
		}
	}

	private void handlePickupItem(final Player player, Packet packet) {
		int x = packet.readLEShort();
		final int id = packet.readShort();
		int y = packet.readLEShortA();
		Location l = Location.location(x, y, player.getLocation().getZ());
		if (x < 1000 || y < 1000 | id < 0 || player.getAttributes().exist(Attribute.DEAD)
				|| player.getAttributes().exist(Attribute.LOCKED)) {
			return;
		}
		player.getInterfaceManager().closeInterfaces();
		if (player.getLocation().equals(l)) {
			World.getInstance().getGroundItems().pickupItem(player, id, player.getLocation());
			return;
		}
		World.getInstance().registerCoordinateEvent(new CoordinateTask(player, l) {
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
		if (oldSlot > 28 || oldSlot < 0 || newSlot > 28 || oldSlot < 0 || player.getAttributes().exist(Attribute.DEAD)
				|| player.getAttributes().exist(Attribute.LOCKED)) {
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
		// No need to update the screen since the client does it for us!
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
		if (oldSlot > 28 || oldSlot < 0 || newSlot > 28 || oldSlot < 0 || player.getAttributes().exist(Attribute.DEAD)
				|| player.getAttributes().exist(Attribute.LOCKED)) {
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
		// No need to update the screen since the client does it for us!
		player.getActionSender().refreshInventory();
	}

	private void handleRightClickOne(Player player, Packet packet) {
		int childId = packet.readLEShort();
		int interfaceId = packet.readLEShort();
		int item = packet.readLEShortA();
		int slot = packet.readLEShortA();
		if (slot > 28 || slot < 0 || player.getAttributes().exist(Attribute.DEAD) || player.getAttributes().exist(Attribute.LOCKED)) {
			return;
		}
		if (player.getInventory().getItemInSlot(slot) == item) {
			player.getInterfaceManager().closeInterfaces();
			if (interfaceId == 149 && childId == 0) {
			}
		}
	}

	private void handleRightClickTwo(Player player, Packet packet) {
		int childId = packet.readLEShort();
		int interfaceId = packet.readLEShort();
		int slot = packet.readLEShort();
		int item = packet.readLEShort();
		if (slot < 0 || slot > 28 || player.getAttributes().exist(Attribute.DEAD) || player.getAttributes().exist(Attribute.LOCKED)) {
			return;
		}
		if (player.getInventory().getItemInSlot(slot) == item) {
			player.getInterfaceManager().closeInterfaces();
			switch (player.getInventory().getItemInSlot(slot)) {
			
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