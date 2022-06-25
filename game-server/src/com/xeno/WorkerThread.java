package com.xeno;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.mina.common.IoFuture;
import org.apache.mina.common.IoFutureListener;
import org.apache.mina.common.WriteFuture;

import com.xeno.io.MySQL;
import com.xeno.io.PlayerLoadResult;
import com.xeno.io.PlayerLoader;
import com.xeno.model.World;
import com.xeno.model.player.Player;
import com.xeno.model.player.PlayerDetails;
import com.xeno.packetbuilder.StaticPacketBuilder;
import com.xeno.util.log.Logger;

/**
 * Does blocking 'work'.
 * @author Graham
 *
 */
public class WorkerThread implements Runnable {
	
	public MySQL sql;
	
	/**
	 * Logger instance.
	 */
	private Logger logger = Logger.getInstance();
	
	/**
	 * Constructor.
	 */
	public WorkerThread(PlayerLoader loader) {
		this.loader = loader;
		this.sql = new MySQL();
		this.sql.createConnection();
		this.playersToLoad = new LinkedList<PlayerDetails>();
		this.playersToSave = new LinkedList<Player>();
	}

	/**
	 * Players to load.
	 */
	private Queue<PlayerDetails> playersToLoad;
	
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
					PlayerDetails d = null;
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
							logger.debug("Loaded " + d.getDisplayName() + "'s game: returncode = " + r.returnCode + ".");
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
							logger.debug("Saved " + p.getPlayerDetails().getDisplayName() + "'s game.");
						} else {
							logger.warning("Could not save " + p.getPlayerDetails().getDisplayName() + "'s game.");
						}
					}
					playersToSave.clear();
				}
			}
		}
	}

	public void cleanup() {
		// save ALL games
		logger.info("Saving all games...");
		int saved = 0;
		int total = 0;
		for(Player p : World.getInstance().getPlayerList()) {
			total++;
			if(loader.save(p)) {
				logger.debug("Saved " + p.getPlayerDetails().getDisplayName() + "'s game.");
				saved++;
			} else {
				logger.warning("Could not save " + p.getPlayerDetails().getDisplayName() + "'s game.");
			}
		}
		if(total == 0) {
			logger.info("No games to save.");
		} else {
			logger.info("Saved " + (saved/total*100) + "% of games ("+saved+"/"+total+").");
		}
	}

	public void loadPlayer(PlayerDetails d) {
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
