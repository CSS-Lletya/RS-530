package com.xeno.io;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.xeno.model.World;
import com.xeno.model.player.Player;
import com.xeno.model.player.PlayerDetails;
import com.xeno.net.Constants;
import com.xeno.util.MD5;
import com.xeno.util.log.Logger;

public class MySQL {

    private Connection connection;
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String DATABASE = "forum";
	
	public MySQL() {
		
	}
	
	public void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection("jdbc:mysql://localhost/" + DATABASE, USERNAME, PASSWORD);
			Logger.getInstance().info("Connected to MySQL database.");
		} catch (Exception e) {
			Logger.getInstance().info("COULD NOT CONNECT TO MYSQL DATABASE");
		}
	}
	
    public void createTable() {
        try {
        	Statement statement = (Statement) connection.createStatement();
        	statement.executeUpdate("create table Players (" +"username TEXT)");
        	statement.close();
        } catch (SQLException e) {
        	 e.printStackTrace();
        }
    }
    
    public void write(String username) {
        try {
            Statement statement = (Statement) connection.createStatement();
            statement.executeUpdate("INSERT INTO " + DATABASE + " " + "VALUES ('" + username + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public PlayerLoadResult loadPlayer(PlayerLoader loader, PlayerDetails details) {
		PlayerLoadResult result = new PlayerLoadResult();
		result.returnCode = Constants.ReturnCodes.INVALID_PASSWORD;
    	try {
    		String username = details.getUsername();
    		String password = details.getPassword();
    		Statement statement = (Statement) connection.createStatement();
    		ResultSet set = statement.executeQuery("SELECT * FROM `user` WHERE username = '" + username + "'");
    		String user = "";
    		String pass = "";
    		String salt = "";
    		String pass2 = "";
    		int group = 1;
    		int uid = -1;
    		try {
    			while(set != null && set.next())  {
    				user = set.getString("username").toLowerCase();
    				pass = set.getString("password");
                    salt = set.getString("salt");
                    group = set.getInt("usergroupid");
                    pass2 = passHash(password, salt);
                    uid = set.getInt("userid");
    			}
    			System.out.println(password + " " + pass + " " + pass2);
    		} catch (SQLException e) {
    			//return result;
    		}
    		if (user.equals("")) {
    			//Statement stmt = (Statement) connection.createStatement();
    			//statement.executeUpdate("INSERT INTO Players " + "VALUES ('" + username + "')'");
    	        //stmt.close();
    			//System.out.println("NewSQL entry created for " + username);
    			System.out.println("User " + username + " not found in databse.");
    			//return result;
    		}
			/*if(!user.equals(username) || !pass2.equals(pass)) {
				result.returnCode = Constants.ReturnCodes.INVALID_PASSWORD;
			} else if (World.getInstance().isUpdateInProgress()) {
				result.returnCode = Constants.ReturnCodes.UPDATE_IN_PROGRESS;
			} else if (World.getInstance().isOnline(username)) {
				result.returnCode = Constants.ReturnCodes.ALREADY_ONLINE;
			} else if (group == 7) {
				result.returnCode = Constants.ReturnCodes.BANNED;
			} else if (uid == -1) {
				result.returnCode = Constants.ReturnCodes.COULD_NOT_COMPLETE;
			} else {*/
				result.returnCode = Constants.ReturnCodes.LOGIN_OK;
				details.setForumGroup(group);
				//loader.load(result, details, uid);
			//}
    		return result;
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	return result;
    }
    
	public void saveHighscores(Player p) {
		if (true) return;
          try {
        	  Statement statement = (Statement) connection.createStatement();
        	  // save overall
    		  statement.executeUpdate("UPDATE `mybb_player_overalls` " +
    		  		"SET total_levels='"+p.getLevels().getTotalLevel()+
    		  		"', total_exp='"+p.getLevels().getTotalXp()+
    		  		"', combat_level='"+p.getLevels().getCombatLevel()+
    		  		"' WHERE `uid`='"+p.getPlayerDetails().getForumUID()+"'");
    		  // save individual skills
    		  p.getPlayerDetails().setForumUID(1);
    		  statement.executeUpdate("UPDATE `mybb_player_skills` " +
    		  		"SET " +
    		  		"attack_lvl='"+p.getLevels().getLevelForXp(0)+"', " + "attack_exp='"+p.getLevels().getXp(0)+"', " +
    		  		"defence_lvl='"+p.getLevels().getLevelForXp(1)+"', " + "defence_exp='"+p.getLevels().getXp(1)+"', " +
    		  		"strength_lvl='"+p.getLevels().getLevelForXp(2)+"', " + "strength_exp='"+p.getLevels().getXp(2)+"', " +
    		  		"hitpoints_lvl='"+p.getLevels().getLevelForXp(3)+"', " + "hitpoints_exp='"+p.getLevels().getXp(3)+"', " +
    		  		"range_lvl='"+p.getLevels().getLevelForXp(4)+"', " + "range_exp='"+p.getLevels().getXp(4)+"', " +
    		  		"prayer_lvl='"+p.getLevels().getLevelForXp(5)+"', " + "prayer_exp='"+p.getLevels().getXp(5)+"', " +
    		  		"magic_lvl='"+p.getLevels().getLevelForXp(6)+"', " + "magic_exp='"+p.getLevels().getXp(6)+"', " +
    		  		"cooking_lvl='"+p.getLevels().getLevelForXp(7)+"', " + "cooking_exp='"+p.getLevels().getXp(7)+"', " +
    		  		"woodcutting_lvl='"+p.getLevels().getLevelForXp(8)+"', " + "woodcutting_exp='"+p.getLevels().getXp(8)+"', " +
    		  		"fletching_lvl='"+p.getLevels().getLevelForXp(9)+"', " + "fletching_exp='"+p.getLevels().getXp(9)+"', " +
    		  		"fishing_lvl='"+p.getLevels().getLevelForXp(10)+"', " + "fishing_exp='"+p.getLevels().getXp(10)+"', " +
    		  		"firemaking_lvl='"+p.getLevels().getLevelForXp(11)+"', " + "firemaking_exp='"+p.getLevels().getXp(11)+"', " +
    		  		"crafting_lvl='"+p.getLevels().getLevelForXp(12)+"', " + "crafting_exp='"+p.getLevels().getXp(12)+"', " +
    		  		"smithing_lvl='"+p.getLevels().getLevelForXp(13)+"', " + "smithing_exp='"+p.getLevels().getXp(13)+"', " +
    		  		"mining_lvl='"+p.getLevels().getLevelForXp(14)+"', " + "mining_exp='"+p.getLevels().getXp(14)+"', " +
    		  		"herblore_lvl='"+p.getLevels().getLevelForXp(15)+"', " + "herblore_exp='"+p.getLevels().getXp(15)+"', " +
    		  		"agility_lvl='"+p.getLevels().getLevelForXp(16)+"', " + "agility_exp='"+p.getLevels().getXp(16)+"', " +
    		  		"thieving_lvl='"+p.getLevels().getLevelForXp(17)+"', " + "thieving_exp='"+p.getLevels().getXp(17)+"', " +
    		  		"slayer_lvl='"+p.getLevels().getLevelForXp(18)+"', " + "slayer_exp='"+p.getLevels().getXp(18)+"', " +
    		  		"farming_lvl='"+p.getLevels().getLevelForXp(19)+"', " + "farming_exp='"+p.getLevels().getXp(19)+"', " +
    		  		"runecrafting_lvl='"+p.getLevels().getLevelForXp(20)+"', " + "runecrafting_exp='"+p.getLevels().getXp(20)+"', " +
    		  		"hunter_lvl='"+p.getLevels().getLevelForXp(21)+"', " + "hunter_exp='"+p.getLevels().getXp(21)+"', " +
    		  		"construction_lvl='"+p.getLevels().getLevelForXp(22)+"', " + "construction_exp='"+p.getLevels().getXp(22)+"', " +
    		  		"summoning_lvl='"+p.getLevels().getLevelForXp(23)+"', " + "summoning_exp='"+p.getLevels().getXp(23)+"' " +
    		  		"WHERE `uid`='"+p.getPlayerDetails().getForumUID()+"'");
          } catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
    
    public String passHash(String in, String salt) {
        String a = new MD5(in).compute();
        String b = new MD5(a + salt).compute();
        return b;
    }
}
