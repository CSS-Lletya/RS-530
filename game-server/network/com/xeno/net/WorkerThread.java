package com.xeno.net;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.mina.common.IoFuture;
import org.apache.mina.common.IoFutureListener;
import org.apache.mina.common.WriteFuture;

import com.xeno.entity.actor.player.Player;
import com.xeno.entity.actor.player.PlayerCredentials;
import com.xeno.io.PlayerLoadResult;
import com.xeno.io.PlayerLoader;
import com.xeno.packetbuilder.StaticPacketBuilder;
import com.xeno.utility.LogUtility;
import com.xeno.utility.LogUtility.LogType;
import com.xeno.world.World;

/**
 * Does blocking 'work'.
 * @author Graham
 *
 */
public class WorkerThread implements Runnable {

	/**
	 * Constructor.
	 */
	public WorkerThread(PlayerLoader loader) {
		this.loader = loader;
		this.playersToLoad = new LinkedList<PlayerCredentials>();
		this.playersToSave = new LinkedList<Player>();
	}

	/**
	 * Players to load.
	 */
	private Queue<PlayerCredentials> playersToLoad;
	
	/**
	 * Players to save.
	 */
	private Queue<Player> playersToSave;
	
	/**
	 * The player loader.
	 */
	private PlayerLoader loader;

	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				cleanup();
				break;
			}
			synchronized(playersToLoad) {
				if(!playersToLoad.isEmpty()) {
					PlayerCredentials d = null;
					while((d = playersToLoad.poll()) != null) {
						PlayerLoadResult r = loader.load(d);
						StaticPacketBuilder spb = new StaticPacketBuilder().setBare(true);
						int slot = -1;
						if(r.returnCode == 2) {
							slot = World.getInstance().register(r.player);
							if(slot == -1) {
								r.returnCode = Constants.ReturnCodes.WORLD_FULL;
							}
						}
						spb.addByte((byte) r.returnCode);
						if(r.returnCode == 2) {
							spb.addByte((byte) r.player.getRights()); // rights
							spb.addByte((byte) 0);
							spb.addByte((byte) 0);//Flagged, will genrate mouse packets
							spb.addByte((byte) 0);
							spb.addByte((byte) 0);
							spb.addByte((byte) 0);
				            spb.addByte((byte) 0); // Generates packets
				            spb.addShort(slot);//PlayerID
				            spb.addByte((byte) 1); // membership flag #1?..this one enables all GE boxes
				            spb.addByte((byte) 1); // membership flag #2?
				            d.getSession().setAttachment(r.player);
						}
						WriteFuture f = d.getSession().write(spb.toPacket());
						if(r.returnCode != 2) {
							f.addListener(new IoFutureListener() {
								@Override
								public void operationComplete(IoFuture arg0) {
									arg0.getSession().close();
								}
							});
						} else {
							r.player.getActionSender().sendMapRegion();
							LogUtility.log(LogType.INFO, "Loaded " + d.getDisplayName() + "'s game: returncode = " + r.returnCode + ".");
						}
					}
					playersToLoad.clear();
				}
			}
			synchronized(playersToSave) {
				if(!playersToSave.isEmpty()) {
					Player p = null;
					while((p = playersToSave.poll()) != null) {
						if(loader.save(p)) {
							//sql.saveHighscores(p);
							LogUtility.log(LogType.INFO, "Saved " + p.getPlayerDetails().getDisplayName() + "'s game.");
						} else {
							LogUtility.log(LogType.WARN, "Could not save " + p.getPlayerDetails().getDisplayName() + "'s game.");
						}
					}
					playersToSave.clear();
				}
			}
		}
	}

	public void cleanup() {
		// save ALL games
		LogUtility.log(LogType.INFO, "Saving all games...");
		int saved = 0;
		int total = 0;
		for(Player p : World.getInstance().getPlayerList()) {
			total++;
			if(loader.save(p)) {
				LogUtility.log(LogType.INFO, "Saved " + p.getPlayerDetails().getDisplayName() + "'s game.");
				saved++;
			} else {
				LogUtility.log(LogType.INFO, "Could not save " + p.getPlayerDetails().getDisplayName() + "'s game.");
			}
		}
		if(total == 0) {
			LogUtility.log(LogType.INFO, "No games to save.");
		} else {
			LogUtility.log(LogType.INFO, "Saved " + (saved/total*100) + "% of games ("+saved+"/"+total+").");
		}
	}

	public void loadPlayer(PlayerCredentials d) {
		synchronized(playersToLoad) {
			playersToLoad.add(d);
		}
	}
	
	public void savePlayer(Player p) {
		synchronized(playersToSave) {
			playersToSave.add(p);
		}
	}

}
