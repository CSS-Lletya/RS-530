package com.xeno.packethandler;

import org.apache.mina.common.IoSession;

import com.xeno.content.Clan;
import com.xeno.content.ConfigureAppearance;
import com.xeno.content.combat.MagicCombat;
import com.xeno.content.combat.constants.AttackInterfaceConfig;
import com.xeno.content.emote.Emotes;
import com.xeno.entity.player.Player;
import com.xeno.model.player.skills.SkillMenu;
import com.xeno.model.player.skills.magic.AutoCast;
import com.xeno.model.player.skills.magic.Lunar;
import com.xeno.model.player.skills.magic.Teleport;
import com.xeno.model.player.skills.prayer.Prayer;
import com.xeno.model.player.skills.prayer.ProtectedItems;
import com.xeno.net.Packet;
import com.xeno.util.EnterVariable;
import com.xeno.util.LogUtility;
import com.xeno.util.LogUtility.LogType;
import com.xeno.world.World;

/**
 * 
 * All packets representing 'buttons'.
 * @author Luke132
 */
public class ActionButton implements PacketHandler {

	private static final int CLOSE = 184; // d
	private static final int ACTIONBUTTON = 155; // d
	private static final int ACTIONBUTTON2 = 10; // d
	private static final int ACTIONBUTTON3 = 132; //d
	
	@Override
	public void handlePacket(Player player, IoSession session, Packet packet) {
		switch(packet.getId()) {
			case CLOSE:
				handleCloseButton(player, packet);
				break;
			
			case ACTIONBUTTON:
			case ACTIONBUTTON2:
				handleActionButton(player, packet);
				break;
				
			case ACTIONBUTTON3:
				handleActionButton3(player, packet);
				break;
		}
	}

	private void handleActionButton3(Player player, Packet packet) {
		int id = packet.readShort();
		int interfaceId = packet.readShort();
		int junk = packet.readLEShort();
		System.out.println("ACTIONBUTTON-3 "+ id);
		switch(id) {
			case 2:
				break;
		
			case 13:
				
				break;
				
			case 12:
				
				break;
				
			case 11:
				
				break;
				
			case 10:
				
				break;
				
			case 17:
				
				break;
				
			case 16:
				
				break;
				
			case 15:
				
				break;
				
			case 14:
				
				break;
				
			case 9:
				
				break;
				
			case 8:
				
				break;
				
			case 7:
			
				break;
				
			case 6:
				
				break;
				
			case 5:
			
				break;
				
			case 4:
				break;
				
			case 3:
				
				break;
				
			case 21:
				
				break;
				
			case 20:
			
				break;

			case 19:
				
				break;
	
			case 18:
				
				break;
				
			case 22:
				
				break;
				
			case 26:
				
				break;
				
			case 25:
				
				break;
				
			case 24:
				
				break;
				
			case 23:
				
				break;
				
			case 1:
				switch(id) {
					case 1:
						
						break;
				}
				break;
				
			default:
				LogUtility.log(LogType.INFO, "ACTIONBUTTON3 = " + id);
				break;
		}
	
	}

	private void handleCloseButton(Player player, Packet packet) {
		if (player.getTrade() != null) {
			player.getTrade().decline();
		}
		player.getActionSender().closeInterfaces();
	}

	private void handleActionButton(Player player, Packet packet) {
		int interfaceId = packet.readShort() & 0xFFFF;
		int buttonId = packet.readShort() & 0xFFFF;
		int buttonId2 = 0;
		if(packet.getLength() >= 6) {
			buttonId2 = packet.readShort() & 0xFFFF;
		}
		if(buttonId2 == 65535) {
			buttonId2 = 0;
		}
		System.out.println("button = " + interfaceId + " " +buttonId + " " +buttonId2);
		switch(interfaceId) {
            case 578: // Fairy Ring Warning
            	if (buttonId == 18) {
                    handleCloseButton(player, packet);
                }
            case 734: //Fairy rings
                if (buttonId == 23) { //1st Plus
                    if (player.firstColumn == 2) {
                        player.firstColumn = 4;
                    } else if (player.firstColumn == 3) {
                        player.firstColumn = 3;
                    }    else if (player.firstColumn >= 4) {
                        player.firstColumn = 1;
                    }
                    else{
                        player.firstColumn++;
                    }
                } else if (buttonId == 24) { //1st Subtract
                    if (player.firstColumn <= 1) {
                        player.firstColumn = 4;
                    } else {
                        player.firstColumn--;
                    }
                } else if (buttonId == 25) { //2nd Plus
                    if (player.secondColumn == 2) {
                        player.secondColumn = 4;
                    } else if (player.secondColumn == 3) {
                        player.secondColumn = 3;
                    }    else if (player.secondColumn >= 4) {
                        player.secondColumn = 1;
                    }
                    else{
                        player.secondColumn++;
                    }
                } else if (buttonId == 26) { //2nd Subtract
                    if (player.secondColumn <= 1) {
                        player.secondColumn = 4;
                    } else {
                        player.secondColumn--;
                    }
                } else if (buttonId == 27) { //3rd Plus
                    if (player.thirdColumn == 2) {
                        player.thirdColumn = 4;
                    } else if (player.thirdColumn == 3) {
                        player.thirdColumn = 3;
                    }    else if (player.thirdColumn >= 4) {
                        player.thirdColumn = 1;
                    }
                    else{
                        player.thirdColumn++;
                    }
                } else if (buttonId == 28) { //3rd Subtract
                    if (player.thirdColumn <= 1) {
                        player.thirdColumn = 4;
                    } else {
                        player.thirdColumn--;
                    }
                } else if (buttonId == 21) { //Confirm
                   
                }
                break;

			case 161: // Slayer points interfaces
			case 163:
			case 164:
				
				break;
			
		
			case 675: // Craft jewellery:
				player.getActionSender().displayEnterAmount();
				player.setTemporaryAttribute("interfaceVariable", new EnterVariable(675, buttonId));
				break;
		
			case 154: // Craft normal leather.
			
				break;
				
			case 542: // Craft glass.
				switch (buttonId) {
					
				}
				break;
			
			case 271 : // Prayer tab.
				if (!Prayer.canUsePrayer(player, buttonId)) {
					Prayer.deactivateAllPrayers(player);
					break;
				}
				switch(buttonId) {
					case 5: // Thick skin.
						Prayer.togglePrayer(player, 1, 1);
						break;
						
					case 15: // Rock skin.
						Prayer.togglePrayer(player, 1, 2);
						break;
						
					case 31: // Steel skin.
						Prayer.togglePrayer(player, 1, 3);
						break;
						
					case 7: // Burst of strength.
						Prayer.togglePrayer(player, 2, 1);
						break;
						
					case 17: // Superhuman strength.
						Prayer.togglePrayer(player, 2, 2);
						break;
						
					case 33: // Ultimate strength.
						Prayer.togglePrayer(player, 2, 3);
						break;
						
					case 9: // Clarity of thought.
						Prayer.togglePrayer(player, 3, 1);
						break;
						
					case 19: // Improved reflexes.
						Prayer.togglePrayer(player, 3, 2);
						break;
						
					case 35: // Incredible reflexes.
						Prayer.togglePrayer(player, 3, 3);
						break;
						
					case 37: // Magic protect.
						Prayer.togglePrayer(player, 4, 1);
						break;
						
					case 39: // Ranged protect.
						Prayer.togglePrayer(player, 4, 2);
						break;
						
					case 41: // Melee protect.
						Prayer.togglePrayer(player, 4, 3);
						break;
						
					case 47: // Retribution.
						Prayer.togglePrayer(player, 4, 4);
						break;
						
					case 49: // Redemption.
						Prayer.togglePrayer(player, 4, 5);
						break;
						
					case 51: // Smite.
						Prayer.togglePrayer(player, 4, 6);
						break;
						
					case 55: // Chivalry.
						Prayer.togglePrayer(player, 5, 1);
						break;
						
					case 57: // Piety.
						Prayer.togglePrayer(player, 5, 2);
						break;
						
					case 25: // Protect item.
						Prayer.togglePrayer(player, 6, 1);
						break;
						
					case 21: // Rapid restore
						Prayer.togglePrayer(player, 7, 1);
						break;
						
					case 23: // Rapid heal.
						Prayer.togglePrayer(player, 7, 2);
						break;
						
					case 11: // Sharp eye.
						Prayer.togglePrayer(player, 8, 1);
						break;
						
					case 27: // Hawk Eye.
						Prayer.togglePrayer(player, 8, 2);
						break;
						
					case 43: // Eagle Eye.
						Prayer.togglePrayer(player, 8, 3);
						break;
						
					case 13: // Mystic will.
						Prayer.togglePrayer(player, 9, 1);
						break;
						
					case 29: // Mystic Lore.
						Prayer.togglePrayer(player, 9, 2);
						break;
						
					case 45: // Mystic Might.
						Prayer.togglePrayer(player, 9, 3);
						break;
				}
				break;
				
			case 90: // Staff attack interface.
				switch(buttonId) {
					case 5: // Select spell (Magic XP)
						AutoCast.configureSelectSpellInterface(player);
						break;
						
					case 9: // Auto retaliate.
						player.getSettings().toggleAutoRetaliate();
						break;
						
 					default:
 						AutoCast.cancel(player, true);
 						AttackInterfaceConfig.configureButton(player, interfaceId, buttonId);
 						break;
				}
				break;
				
			case 388: // Ancient magic autocast select spell.
				switch(buttonId) {
					case 0: // Smoke rush.
						AutoCast.setAutoCastSpell(player, 16, 8, true);
						break;
						
					case 1: // Shadow rush.
						AutoCast.setAutoCastSpell(player, 17, 12, true);
						break;
						
					case 2: // Blood rush.
						AutoCast.setAutoCastSpell(player, 18, 4, true);
						break;
						
					case 3: // Ice rush.
						AutoCast.setAutoCastSpell(player, 19, 0, true);
						break;
											
					case 4: // Smoke burst.
						AutoCast.setAutoCastSpell(player, 20, 10, true);
						break;
						
					case 5: // Shadow burst.
						AutoCast.setAutoCastSpell(player, 21, 14, true);
						break;
						
					case 6: // Blood burst.
						AutoCast.setAutoCastSpell(player, 22, 6, true);
						break;
						
					case 7: // Ice burst.
						AutoCast.setAutoCastSpell(player, 23, 2, true);
						break;
						
					case 8: // Smoke blitz.
						AutoCast.setAutoCastSpell(player, 24, 9, true);
						break;
						
					case 9: // Shadow blitz.
						AutoCast.setAutoCastSpell(player, 25, 13, true);
						break;
						
					case 10: // Blood blitz.
						AutoCast.setAutoCastSpell(player, 26, 5, true);
						break;
						
					case 11: // Ice blitz.
						AutoCast.setAutoCastSpell(player, 27, 1, true);
						break;

					case 12: // Smoke barrage.
						AutoCast.setAutoCastSpell(player, 28, 11, true);
						break;
						
					case 13: // Shadow barrage.
						AutoCast.setAutoCastSpell(player, 29, 15, true);
						break;
						
					case 14: // Blood barrage.
						AutoCast.setAutoCastSpell(player, 30, 7, true);
						break;
						
					case 15: // Ice barrage.
						AutoCast.setAutoCastSpell(player, 31, 3, true);
						break;

					case 16: // Cancel.
						AutoCast.cancel(player, false);
						break;
				}
				break;
				
			case 406: // Void knight mace autocast select spell.
				switch(buttonId) {
					case 0: // Crumble undead.
						AutoCast.setAutoCastSpell(player, 32, 22, false);
						break;
						
					case 1: // Guthix claws.
						AutoCast.setAutoCastSpell(player, 34, 42, false);
						break;
						
					case 2: // Wind wave.
						AutoCast.setAutoCastSpell(player, 12, 45, false);
						break;
						
					case 3: // Water wave.
						AutoCast.setAutoCastSpell(player, 13, 48, false);
						break;
						
					case 4: // Earth wave.
						AutoCast.setAutoCastSpell(player, 14, 52, false);
						break;
						
					case 5: // Fire wave.
						AutoCast.setAutoCastSpell(player, 15, 55, false);
						break;
					
					case 6: // Cancel.
						AutoCast.cancel(player, false);
						break;
			}
			break;
				
			case 310: // Slayer staff autocast select spell.
				switch(buttonId) {
					case 0: // Crumble undead.
						AutoCast.setAutoCastSpell(player, 32, 22, false);
						break;
						
					case 1: // Slayer dart.
						AutoCast.setAutoCastSpell(player, 33, 31, false);
						break;
						
					case 2: // Wind wave.
						AutoCast.setAutoCastSpell(player, 12, 45, false);
						break;
						
					case 3: // Water wave.
						AutoCast.setAutoCastSpell(player, 13, 48, false);
						break;
						
					case 4: // Earth wave.
						AutoCast.setAutoCastSpell(player, 14, 52, false);
						break;
						
					case 5: // Fire wave.
						AutoCast.setAutoCastSpell(player, 15, 55, false);
						break;
					
					case 6: // Cancel.
						AutoCast.cancel(player, false);
						break;
				}
				break;
				
			case 319: // Normal magic autocast select spell.
				switch(buttonId) {
					case 0: // Wind strike.
						AutoCast.setAutoCastSpell(player, 0, 1, false);
						break;
						
					case 1: // Water strike.
						AutoCast.setAutoCastSpell(player, 1, 4, false);
						break;
						
					case 2: // Earth strike.
						AutoCast.setAutoCastSpell(player, 2, 6, false);
						break;
						
					case 3: // Fire strike.
						AutoCast.setAutoCastSpell(player, 3, 8, false);
						break;
						
					case 4: // Wind bolt.
						AutoCast.setAutoCastSpell(player, 4, 10, false);
						break;
						
					case 5: // Water bolt.
						AutoCast.setAutoCastSpell(player, 5, 14, false);
						break;
						
					case 6: // Earth bolt.
						AutoCast.setAutoCastSpell(player, 6, 17, false);
						break;
						
					case 7: // Fire bolt.
						AutoCast.setAutoCastSpell(player, 7, 20, false);
						break;
						
					case 8: // Wind blast.
						AutoCast.setAutoCastSpell(player, 8, 24, false);
						break;
						
					case 9: // Water blast.
						AutoCast.setAutoCastSpell(player, 9, 27, false);
						break;
						
					case 10: // Earth blast.
						AutoCast.setAutoCastSpell(player, 10, 33, false);
						break;
						
					case 11: // Fire blast.
						AutoCast.setAutoCastSpell(player, 11, 38, false);
						break;

					case 12: // Wind wave.
						AutoCast.setAutoCastSpell(player, 12, 45, false);
						break;
						
					case 13: // Water wave.
						AutoCast.setAutoCastSpell(player, 13, 48, false);
						break;
						
					case 14: // Earth wave.
						AutoCast.setAutoCastSpell(player, 14, 52, false);
						break;
						
					case 15: // Fire wave.
						AutoCast.setAutoCastSpell(player, 15, 55, false);
						break;
						
					case 16: // Cancel.
						AutoCast.cancel(player, false);
						break;
				}
				break;
		
			case 182: // Logout tab.
				player.getActionSender().logout();
				break;
			
			case 261: // Settings tab.
				switch(buttonId) {
					case 16: // Display settings.
						player.getActionSender().displayInterface(742);
						break;
						
					case 18: // Audio settings.
						player.getActionSender().displayInterface(743);
						break;
		
					case 3: // Run toggle.
						if(!player.getWalkingQueue().isRunToggled()) {
							player.getWalkingQueue().setRunToggled(true);
							player.getActionSender().sendConfig(173, 1);
						} else {
							player.getWalkingQueue().setRunToggled(false);
							player.getActionSender().sendConfig(173, 0);
						}
						break;
						
					case 4: // Chat effect toggle.
						if(!player.getSettings().isChatEffectsEnabled()) {
							player.getSettings().setChatEffectsEnabled(true);
							player.getActionSender().sendConfig(171, 0);
						} else {
							player.getSettings().setChatEffectsEnabled(false);
							player.getActionSender().sendConfig(171, 1);
						}
						break;
						
					case 5: // Split private chat toggle.
						if(!player.getSettings().isPrivateChatSplit()) {
							player.getSettings().setPrivateChatSplit(true);
							player.getActionSender().sendConfig(287, 1);
						} else {
							player.getSettings().setPrivateChatSplit(false);
							player.getActionSender().sendConfig(287, 0);
						}
						break;
						
					case 7: // Accept aid toggle.
						if(!player.getSettings().isAcceptAidEnabled()) {
							player.getSettings().setAcceptAidEnabled(true);
							player.getActionSender().sendConfig(427, 1);
						} else {
							player.getSettings().setAcceptAidEnabled(false);
							player.getActionSender().sendConfig(427, 0);
						}
						break;
						
					case 6: // Mouse buttons toggle.
						if(!player.getSettings().isMouseTwoButtons()) {
							player.getSettings().setMouseTwoButtons(true);
							player.getActionSender().sendConfig(170, 0);
						} else {
							player.getSettings().setMouseTwoButtons(false);
							player.getActionSender().sendConfig(170, 1);
						}
						break;
				}
				break;
				
			case 589: // Clan chat 
				if (buttonId == 9) {
					for(Long friend : player.getFriends().getFriendsList()) {
						player.getActionSender().sendFriend(friend, player.getFriends().getWorld(friend));
					}
					World.getInstance().getClanManager().openClanSetup(player);
					break;
				}
				break;
				
			case 590: // Clan chat setup
 				Clan clan = World.getInstance().getClanManager().getClanByOwner(player, player.getUsername());
				if (clan == null) {
					break;
				}
				switch(buttonId) {
					case 22: // Clan name
						player.getActionSender().displayEnterText("Enter clan name :");
						player.setTemporaryAttribute("interfaceVariable", new EnterVariable(590, 0));
						break;
						
	 				case 23: // "Who can enter chat" - anyone.
	 					clan.setEnterRights(Clan.NO_RANK);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getEnterRights()), 590, 23);
	 					break;
	 					
	 				case 24: // "Who can talk in chat" - anyone.
	 					clan.setTalkRights(Clan.NO_RANK);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getTalkRights()), 590, 24);
	 					break;
	 					
	 				case 26: // "Who can share loot" - anyone.
	 					clan.setLootRights(Clan.NO_RANK);
	 					player.getActionSender().modifyText(clan.getRankString(clan.getLootRights()), 590, 26);
	 					break;
				}
				break;
				
			case 763: // Bank inventory
				switch(buttonId) {
					case 0: // Deposit 1.
						player.getBank().deposit(buttonId2, 1);
						player.getBank().refreshBank();
						break;
				}
				break;
				
			case 762: // Bank
				switch(buttonId) {		
					case 73: // withdraw 1.
						player.getBank().withdraw(buttonId2, 1);
						player.getBank().refreshBank();
						break;
						
					case 16: // Note item.
						player.getBank().asNote();
						break;
						
					case 41: // first (main) bank tab
						player.getBank().setCurrentTab(10);
						break;
						
					case 39: // first bank tab
						player.getBank().setCurrentTab(2);
						break;
						
					case 37: // second bank tab
						player.getBank().setCurrentTab(3);
						break;
						
					case 35: // third bank tab
						player.getBank().setCurrentTab(4);
						break;
						
					case 33: // fourth bank tab
						player.getBank().setCurrentTab(5);
						break;
						
					case 31: // fifth bank tab
						player.getBank().setCurrentTab(6);
						break;
						
					case 29: // sixth bank tab
						player.getBank().setCurrentTab(7);
						break;
						
					case 27: // seventh bank tab
						player.getBank().setCurrentTab(8);
						break;
						
					case 25: // eighth bank tab
						player.getBank().setCurrentTab(9);
						break;
				}
				break;
				
			case 626: // Stake duel confirmation interface.
				
				break;
				
			case 631: // Stake duel first interface.
				
				break;
				
			case 387: // Equipment tab. 
				switch(buttonId) {
					case 55: // Character display.
						player.getEquipment().displayEquipmentScreen();
						break;
						
					case 52: // Items kept on death.
						ProtectedItems.displayItemsInterface(player);
						break;
				}
				break;
				
			case 274: // Quest tab.
				switch(buttonId) {
					case 3: // Achievment diary toggle.
						player.getActionSender().sendTab(85, 259);
						player.getSettings().setAchievementDiaryTab(true);
						break;
				}
				break;
				
			case 259: // Achievment diary tab.
				switch(buttonId) {
					case 8: // Quest tab toggle.
						player.getActionSender().sendTab(85, 274);
						player.getSettings().setAchievementDiaryTab(false);
						break;
				}
				break;
				
			case 620: // Shop interface.
				if (player.getShopSession() == null) {
					return;
				}
				switch(buttonId) {
					case 26: // Player stock tab.
						player.getShopSession().openPlayerShop();
						break;
						
					case 25: // Main stock tab.
						player.getShopSession().openMainShop();
						break;
						
					case 23: // Value (main stock)	
					case 24: // Value (player stock)
						player.getShopSession().valueItem(buttonId2, false);
						break;
				}
				break;
				
			case 621: // Shop inventory.
				if (player.getShopSession() == null) {
					return;
				}
				switch(buttonId) {
					case 0: // Value (player stock)
						player.getShopSession().valueItem(buttonId2, true);
						break;
				}
				break;
				
			case 192: // Normal Magic tab.
				switch(buttonId) {
					case 0: // Home Teleport.
						Teleport.homeTeleport(player);
						break;
						
					case 15: // Varrock teleport.
						Teleport.teleport(player, 0);
						break;
						
					case 18: // Lumbridge teleport.
						Teleport.teleport(player, 1);
						break;
						
					case 21: // Falador teleport.
						Teleport.teleport(player, 2);
						break;
						
					case 23: // POH teleport.
						player.getActionSender().sendMessage("This teleport is unavailable.");
						break;
						
					case 26: // Camelot teleport.
						Teleport.teleport(player, 3);
						break;
						
					case 32: // Ardougne teleport.
						Teleport.teleport(player, 4);
						break;
						
					case 37: // Watchtower teleport.
						player.getActionSender().sendMessage("This teleport is unavailable.");
						//Teleport.teleport(player, 5);
						break;
						
					case 44: // Trollheim teleport.
						Teleport.teleport(player, 6);
						break;
						
					case 47: // Ape Atoll teleport.
						player.getActionSender().sendMessage("This teleport is unavailable.");
						break;
						
					case 58: // Charge.
						MagicCombat.castCharge(player);
						break;
				}
				break;
				
			case 193: // Ancient magic tab.
				switch(buttonId) {
					case 20: // Paddewwa teleport.
						Teleport.teleport(player, 7);
						break;
						
					case 21: // Senntisten teleport.
						Teleport.teleport(player, 8);
						break;
						
					case 22: // Kharyrll teleport.
						Teleport.teleport(player, 9);
						break;
						
					case 23: // Lassar teleport.
						Teleport.teleport(player, 10);
						break;
						
					case 24: // Dareeyak teleport.
						Teleport.teleport(player, 11);
						break;
						
					case 25: // Carrallanger teleport.
						Teleport.teleport(player, 12);
						break;
						
					case 27: // Ghorrock teleport.
						Teleport.teleport(player, 14);
						break;
						
					case 26: // Annakarl teleport.
						Teleport.teleport(player, 13);
						break;
						
					case 28: // Ancients Home teleport.
						Teleport.homeTeleport(player);
						break;
				}
				break;
				
			case 13: // Bank pin buttons.
				if (buttonId == 29) {
					player.getBank().forgotPin();
					break;
				}
				player.getBank().handleEnterPin(buttonId);
				break;
				
			case 14: // Bank pin settings.
				switch(buttonId) {
					case 60: // Set new bank pin.
						player.getBank().displayFirstConfirmation();
						break;
					
					case 61: // Change recovery delay.
						player.getBank().changePinDelay();
						break;
						
					case 91: // "No, I might forget it!".
						if (player.getBank().isPinPending()) {
							player.getBank().cancelPendingPin();
							break;
						}
						player.getBank().openPinSettings(2);
						break;
						
					case 89: // Yes i want to set a pin.
						if (player.getBank().isPinPending()) {
							player.getBank().verifyPin(true);
							break;
						}
						player.getBank().openEnterPin();
						break;
						
					case 65: // Cancel pin that's pending.
						player.getBank().openPinSettings(4);
						break;
						
					case 62: // Change pin.
						player.getBank().changePin();
						break;
						
					case 63: // Delete pin.
						player.getBank().deletePin();
						break;
				}
				break;
				
			case 464: // Emote tab.
				Emotes.emote(player, buttonId);
				break;
				
 			case 320: // Skills Tab.
				SkillMenu.display(player, buttonId);
				break;
				
 			case 499: // Skill menu side menu.
 				SkillMenu.subMenu(player, buttonId);
 				break;

 			case 336: // Trade/duel inventory - trade 1.
 				if (player.getTrade() != null) {
 					player.getTrade().tradeItem(buttonId2, 1);
 					break;
 				}
 				
 				break;
 				
 			case 335: // Trade interface.
 				if (player.getTrade() == null) {
 					break;
 				}
				switch(buttonId) {
					case 16: // Accept trade.
						player.getTrade().accept();
						break;
						
					case 18: // Decline trade.
						player.getTrade().decline();
						break;
						
					case 30: // Offer 1
						player.getTrade().removeItem(buttonId2, 1);
						break;
				}
 				break;
 				
 			case 334: // Trade confirmation.
 				if (player.getTrade() == null) {
 					break;
 				}
 				switch(buttonId) {
	 				case 21: // Decline trade.
	 					player.getTrade().decline();
						break;
						
	 				case 20: // Accept trade.
	 					player.getTrade().accept();
	 					break;
	 			}
 				break;
 				
 			case 750: // Run button
				if(!player.getWalkingQueue().isRunToggled()) {
					player.getWalkingQueue().setRunToggled(true);
					player.getActionSender().sendConfig(173, 1);
				} else {
					player.getWalkingQueue().setRunToggled(false);
					player.getActionSender().sendConfig(173, 0);
				}
				break;
				
 			case 667: // Equipment/bonuses interface.
 				if (buttonId == 14) {
 					player.getEquipment().unequipItem(buttonId2);
 					break;
 				}
 				break;
				
 			case 771: // Character design interface.
 				ConfigureAppearance.sortButton(player, buttonId);
 				break;
 				
 			case 311: // Smelt interface.
 				
 				break;
 				
 			case 300: // Bar smithing interface.
 				
 				break;
 				
 			case 92: // Unarmed attack interface.
 				switch(buttonId) {			
 					case 24: // Auto retaliate.
 						player.getSettings().toggleAutoRetaliate();
 						break;
 						
 					default: 
 						AttackInterfaceConfig.configureButton(player, interfaceId, buttonId);
 						break;
 				}
 				break;
 				
 			case 85: // Spear attack interface.
 				switch(buttonId) {
					case 8: // Special attack.
						player.getSpecialAttack().toggleSpecBar();
						break;
						
					case 24: // Auto retaliate.
						player.getSettings().toggleAutoRetaliate();
						break;
						
 					default: 
 						AttackInterfaceConfig.configureButton(player, interfaceId, buttonId);
 						break;
 				}
 				break;
 				
 			case 93: // Whip attack interface.
 				switch(buttonId) {
 					case 8: // Special attack.
 						player.getSpecialAttack().toggleSpecBar();
 						break;
 						
 					case 24: // Auto retaliate.
 						player.getSettings().toggleAutoRetaliate();
 						break;
 						
 					default: 
 						AttackInterfaceConfig.configureButton(player, interfaceId, buttonId);
 						break;
 				}
 				break;
 				
 			case 89: // Dagger attack interface.
 				switch(buttonId) {					
 					case 10: // Special attack.
 						player.getSpecialAttack().toggleSpecBar();
 						break;
 						
 					case 26: // Auto retaliate.
 						player.getSettings().toggleAutoRetaliate();
 						break;
 						
 					default: 
 						AttackInterfaceConfig.configureButton(player, interfaceId, buttonId);
 						break;
 				}
 				break;
 				
 			case 82: // Longsword/scimitar attack interface.
 				switch(buttonId) {					
 					case 10: // Special attack.
 						player.getSpecialAttack().toggleSpecBar();
 						break;
 						
 					case 26: // Auto retaliate.
 						player.getSettings().toggleAutoRetaliate();
 						break;
 						
 					default: 
 						AttackInterfaceConfig.configureButton(player, interfaceId, buttonId);
 						break;
 				}
 				break;
 				
 			case 78: // Claw attack interface.
 				switch(buttonId) {
 					case 10: // Special attack.
 						player.getSpecialAttack().toggleSpecBar();
 						break;
 						
 					case 26: // Auto retaliate.
 						player.getSettings().toggleAutoRetaliate();
 						break;
 						
 					default: 
 						AttackInterfaceConfig.configureButton(player, interfaceId, buttonId);
 						break;
 				}
 				break;
 				
 			case 81: // Godsword attack interface.
 				switch(buttonId) {
 					case 10: // Special attack.
 						player.getSpecialAttack().toggleSpecBar();
 						break;
 						
 					case 26: // Auto retaliate.
 						player.getSettings().toggleAutoRetaliate();
 						break;
 						
 					default: 
 						AttackInterfaceConfig.configureButton(player, interfaceId, buttonId);
 						break;
 				}
 				break;
 				
 			case 88: // Mace attack interface.
 				switch(buttonId) {
 					case 10: // Special attack.
 						player.getSpecialAttack().toggleSpecBar();
 						break;
 						
 					case 26: // Auto retaliate.
 						player.getSettings().toggleAutoRetaliate();
 						break;
 						
 					default: 
 						AttackInterfaceConfig.configureButton(player, interfaceId, buttonId);
 						break;
 				}
 				break;
 				
 			case 76: // Granite maul attack interface.
 				switch(buttonId) {
 					case 8: // Special attack.
 						player.getSpecialAttack().toggleSpecBar();
 						break;
 						
 					case 24: // Auto retaliate.
 						player.getSettings().toggleAutoRetaliate();
 						break;
 						
 					default: 
 						AttackInterfaceConfig.configureButton(player, interfaceId, buttonId);
 						break;
 				}
 				break;
 				
 			case 77: // Bow attack interface.
 				switch(buttonId) {					
 					case 11: // Special attack.
 						player.getSpecialAttack().toggleSpecBar();
 						break;
 						
 					case 27: // Auto retaliate.
 						player.getSettings().toggleAutoRetaliate();
 						break;
 						
 					default: 
 						AttackInterfaceConfig.configureButton(player, interfaceId, buttonId);
 						break;
 				}
 				break;
 				
 			case 75: // Battleaxe attack interface.
 				switch(buttonId) {
 					case 10: // Special attack.
 						player.getSpecialAttack().toggleSpecBar();
 						player.getSpecialAttack().dragonBattleaxe();
 						break;
 						
 					case 26: // Auto retaliate.
 						player.getSettings().toggleAutoRetaliate();
 						break;
 						
 					default: 
 						AttackInterfaceConfig.configureButton(player, interfaceId, buttonId);
 						break;
 				}
 				break;
 				
 			case 91: // Thrown weapon
 				switch(buttonId) {
					case 24: // Auto retaliate.
						player.getSettings().toggleAutoRetaliate();
						break;
						
 					default: 
 						AttackInterfaceConfig.configureButton(player, interfaceId, buttonId);
 						break;
				}
 				break;
 				
 			case 430: // Lunar interface
 				switch(buttonId) {	
	 				case 14: // Vengeance
	 					Lunar.castLunarSpell(player, buttonId);
	 					break;
 				}
 				break;
 				
 			case 102: // Items on death interface
 				if (buttonId <= 3) {
 					player.getActionSender().sendMessage("You will keep this item if you should you die.");
 				} else {
 					player.getActionSender().sendMessage("You will lose this item if you should you die.");
 				}
 				break;
 				
			default:
				if (interfaceId != 548 && interfaceId != 751) {
					LogUtility.log(LogType.INFO, "Unhandled ActionButton : " + interfaceId + " " + buttonId + " " + buttonId2);
				}
				break;
		}
	}
}
