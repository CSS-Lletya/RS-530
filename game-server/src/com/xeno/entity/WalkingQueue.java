package com.xeno.entity;

import com.xeno.entity.actor.player.Player;
import com.xeno.utility.Utility;

/**
 * Handles walking.
 * @author Graham
 *
 */
public class WalkingQueue {
	
	private class Point {
		private int x;
		private int y;
		private int dir;
	}
	
	private static final int SIZE = 50;
	
	public int wQueueReadPtr = 0;
	public int wQueueWritePtr = 0;
	public Point[] walkingQueue = new Point[SIZE];
	public byte[][][] regionData = new byte[4][104][104];
	
	private Player entity;
	
	private boolean isRunning = false, isRunToggled = false;

	private int lastDirection;
	
	public boolean isRunToggled() {
		return isRunToggled;
	}
	
	public void setRunToggled(boolean isRunToggled) {
		this.isRunToggled = isRunToggled;
	}
	
	public WalkingQueue(Player entity) {
		this.entity = entity;
		for(int i = 0; i < SIZE; i++) {
			walkingQueue[i] = new Point();
			walkingQueue[i].x = 0;
			walkingQueue[i].y = 0;
			walkingQueue[i].dir = -1;
		}
		this.lastDirection = 6;
		reset();
		//load();
	}
	
	public void setIsRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	
	public void reset() {
		walkingQueue[0].x   = entity.getLocation().getLocalX();
		walkingQueue[0].y   = entity.getLocation().getLocalY();
		walkingQueue[0].dir = -1;
		wQueueReadPtr = wQueueWritePtr = 1;
	}
	
	public void addToWalkingQueue(int x, int y) {
		int diffX = x - walkingQueue[wQueueWritePtr - 1].x, diffY = y - walkingQueue[wQueueWritePtr - 1].y;
		int max = Math.max(Math.abs(diffX), Math.abs(diffY));
		for (int i = 0; i < max; i++) {
			if (diffX < 0) {
				diffX++;
			} else if (diffX > 0) {
				diffX--;
			}
			if (diffY < 0) {
				diffY++;
			} else if (diffY > 0) {
				diffY--;
			}
			addStepToWalkingQueue(x - diffX, y - diffY);
		}
	}
	
	public void forceWalk(int x, int y) {
		reset();
		addToWalkingQueue(entity.getLocation().getLocalX() + x, entity.getLocation().getLocalY() + y);
	}
	
	public void addStepToWalkingQueue(int x, int y) {
		int diffX = x - walkingQueue[wQueueWritePtr - 1].x;
		int diffY = y - walkingQueue[wQueueWritePtr - 1].y;
		int dir = Utility.direction(diffX, diffY);
		if (wQueueWritePtr >= SIZE) {
			return;
		}
		if (entity.toPlayer().getMapZoneManager().execute(entity.toPlayer(), zone -> !zone.canMove(entity.toPlayer(), dir))){
			return;
		}
		System.out.println(entity.toPlayer().getCurrentMapZone().get().canMove(entity.toPlayer(), dir));
		if (dir != -1) {
			walkingQueue[wQueueWritePtr].x = x;
			walkingQueue[wQueueWritePtr].y = y;
			walkingQueue[wQueueWritePtr++].dir = dir;
		}
	}
	
	public void getNextPlayerMovement() {
//		entity.toPlayer().getMapZoneManager().executeVoid(entity.toPlayer(), zone -> zone.moved(entity.toPlayer()));
		entity.getSprite().setSprites(-1, -1);
		if(entity.getTeleportTo() != null) {
			entity.setDistanceEvent(null);
			reset();
			Location lastRegion = entity.getLocation();
			int rx = lastRegion.getRegionX();
			int ry = lastRegion.getRegionY();
			entity.setLocation(entity.getTeleportTo());
			entity.resetTeleportTo();
			entity.getUpdateFlags().setDidTeleport(true);
			if((rx-entity.getLocation().getRegionX()) >= 4 || (rx-entity.getLocation().getRegionX()) <= -4) {
				entity.getUpdateFlags().setDidMapRegionChange(true);
			}
			if((ry-entity.getLocation().getRegionY()) >= 4 || (ry-entity.getLocation().getRegionY()) <= -4) {
				entity.getUpdateFlags().setDidMapRegionChange(true);
			}
		} else {
			Location oldLocation = entity.getLocation();
			int walkDir = getNextWalkingDirection();
			int runDir  = -1;
			if(isRunning || isRunToggled) {
				if(entity.playerDetails.getRunEnergy() > 0) {
					runDir = getNextWalkingDirection();
					if(runDir != -1) {
						entity.playerDetails.setRunEnergy(entity.playerDetails.getRunEnergy() - 1);
						entity.getActionSender().sendEnergy();
					}
				} else {
					if(isRunToggled) {
						entity.getActionSender().sendConfig(173, 0);
						isRunToggled = isRunning = false;
					}
					isRunning = false;
				}
			}
			Location lastRegion = entity.getUpdateFlags().getLastRegion();
			if (lastRegion == null) {
				return;
			}
			int rx = lastRegion.getRegionX();
			int ry = lastRegion.getRegionY();
			if((rx-entity.getLocation().getRegionX()) >= 4) {
				entity.getUpdateFlags().setDidMapRegionChange(true);
			} else if((rx-entity.getLocation().getRegionX()) <= -4) {
				entity.getUpdateFlags().setDidMapRegionChange(true);
			}
			if((ry-entity.getLocation().getRegionY()) >= 4) {
				entity.getUpdateFlags().setDidMapRegionChange(true);
			} else if((ry-entity.getLocation().getRegionY()) <= -4) {
				entity.getUpdateFlags().setDidMapRegionChange(true);
			}
			if(entity.getUpdateFlags().didMapRegionChange()) {
				if(walkDir != -1) {
					wQueueReadPtr--;
				}
				if(runDir != -1) {
					wQueueReadPtr--;
				}
				walkDir = -1;
				runDir = -1;
				entity.setLocation(oldLocation);
			}
			entity.getSprite().setSprites(walkDir, runDir);
		}
	}

	private int getNextWalkingDirection() {
		if (wQueueReadPtr == wQueueWritePtr) {
			return -1;
		}
		int dir = walkingQueue[wQueueReadPtr++].dir;
		int xdiff = Utility.DIRECTION_DELTA_X[dir];
		int ydiff = Utility.DIRECTION_DELTA_Y[dir];
		Location newLocation = Location.location(entity.getLocation().getX()+xdiff, entity.getLocation().getY()+ydiff, entity.getLocation().getZ());
		entity.setLocation(newLocation);
		lastDirection = dir;
		return dir;
	}
	
	public boolean isRunning() {
		return isRunning;
	}

	public void setLastDirection(int lastDirection) {
		this.lastDirection = lastDirection;
	}

	public int getLastDirection() {
		return lastDirection;
	}
	
		/*private int[] bigX = new int[4096];
	    private int[] bigY = new int[4096];
	    private int[][] anIntArrayArray901 = new int[104][104];
	    private int[][] anIntArrayArray825 = new int[104][104];
	    private static Class11[] aClass11Array1230 = new Class11[4];
	    private static Class73[] aClass73Array1059 = new Class73[4];
	    
	    void load() {
	        for(int j = 0; j < 4; j++) {
	            aClass11Array1230[j] = new Class11();
	            aClass73Array1059[j] = new Class73(104, 104);
	        }
			System.out.println("WALKING STUFF LOADED");
	    }
		
		public boolean doWalkTo(int i, int j, int k, int i1, int startY, int k1, int l1, int endY, int startX, boolean flag, int endX) {
			int baseX = (entity.getLocation().getRegionX() - 6) * 8;
			int baseY = (entity.getLocation().getRegionY() - 6) * 8;
			int plane = entity.getLocation().getZ();
			byte byte0 = 104;
			byte byte1 = 104;
			for(int l2 = 0; l2 < byte0; l2++)
			{
				for(int i3 = 0; i3 < byte1; i3++)
				{
					anIntArrayArray901[l2][i3] = 0;
					anIntArrayArray825[l2][i3] = 0x5f5e0ff;
				}

			}

			int j3 = startX;
			int k3 = startY;
			anIntArrayArray901[startX][startY] = 99;
			anIntArrayArray825[startX][startY] = 0;
			int l3 = 0;
			int i4 = 0;
			bigX[l3] = startX;
			bigY[l3++] = startY;
			boolean flag1 = false;
			int j4 = bigX.length;
			int ai[][] = null;//aClass11Array1230[plane].anIntArrayArray294;
			try {
				saveArray(ai);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ai = aClass11Array1230[plane].anIntArrayArray294;
			while(i4 != l3)
			{
				j3 = bigX[i4];
				k3 = bigY[i4];
				i4 = (i4 + 1) % j4;
				if(j3 == endX && k3 == endY)
				{
					flag1 = true;
					break;
				}
				if(i1 != 0)
				{
					if((i1 < 5 || i1 == 10) && aClass11Array1230[plane].method219(endX, j3, k3, j, i1 - 1, endY))
					{
						flag1 = true;
						break;
					}
					if(i1 < 10 && aClass11Array1230[plane].method220(endX, endY, k3, i1 - 1, j, j3))
					{
						flag1 = true;
						break;
					}
				}
				if(k1 != 0 && k != 0 && aClass11Array1230[plane].method221(endY, endX, j3, k, l1, k1, k3))
				{
					flag1 = true;
					break;
				}
				int l4 = anIntArrayArray825[j3][k3] + 1;
				if(j3 > 0 && anIntArrayArray901[j3 - 1][k3] == 0 && (ai[j3 - 1][k3] & 0x1280108) == 0)
				{
					bigX[l3] = j3 - 1;
					bigY[l3] = k3;
					l3 = (l3 + 1) % j4;
					anIntArrayArray901[j3 - 1][k3] = 2;
					anIntArrayArray825[j3 - 1][k3] = l4;
				}
				if(j3 < byte0 - 1 && anIntArrayArray901[j3 + 1][k3] == 0 && (ai[j3 + 1][k3] & 0x1280180) == 0)
				{
					bigX[l3] = j3 + 1;
					bigY[l3] = k3;
					l3 = (l3 + 1) % j4;
					anIntArrayArray901[j3 + 1][k3] = 8;
					anIntArrayArray825[j3 + 1][k3] = l4;
				}
				if(k3 > 0 && anIntArrayArray901[j3][k3 - 1] == 0 && (ai[j3][k3 - 1] & 0x1280102) == 0)
				{
					bigX[l3] = j3;
					bigY[l3] = k3 - 1;
					l3 = (l3 + 1) % j4;
					anIntArrayArray901[j3][k3 - 1] = 1;
					anIntArrayArray825[j3][k3 - 1] = l4;
				}
				if(k3 < byte1 - 1 && anIntArrayArray901[j3][k3 + 1] == 0 && (ai[j3][k3 + 1] & 0x1280120) == 0)
				{
					bigX[l3] = j3;
					bigY[l3] = k3 + 1;
					l3 = (l3 + 1) % j4;
					anIntArrayArray901[j3][k3 + 1] = 4;
					anIntArrayArray825[j3][k3 + 1] = l4;
				}
				if(j3 > 0 && k3 > 0 && anIntArrayArray901[j3 - 1][k3 - 1] == 0 && (ai[j3 - 1][k3 - 1] & 0x128010e) == 0 && (ai[j3 - 1][k3] & 0x1280108) == 0 && (ai[j3][k3 - 1] & 0x1280102) == 0)
				{
					bigX[l3] = j3 - 1;
					bigY[l3] = k3 - 1;
					l3 = (l3 + 1) % j4;
					anIntArrayArray901[j3 - 1][k3 - 1] = 3;
					anIntArrayArray825[j3 - 1][k3 - 1] = l4;
				}
				if(j3 < byte0 - 1 && k3 > 0 && anIntArrayArray901[j3 + 1][k3 - 1] == 0 && (ai[j3 + 1][k3 - 1] & 0x1280183) == 0 && (ai[j3 + 1][k3] & 0x1280180) == 0 && (ai[j3][k3 - 1] & 0x1280102) == 0)
				{
					bigX[l3] = j3 + 1;
					bigY[l3] = k3 - 1;
					l3 = (l3 + 1) % j4;
					anIntArrayArray901[j3 + 1][k3 - 1] = 9;
					anIntArrayArray825[j3 + 1][k3 - 1] = l4;
				}
				if(j3 > 0 && k3 < byte1 - 1 && anIntArrayArray901[j3 - 1][k3 + 1] == 0 && (ai[j3 - 1][k3 + 1] & 0x1280138) == 0 && (ai[j3 - 1][k3] & 0x1280108) == 0 && (ai[j3][k3 + 1] & 0x1280120) == 0)
				{
					bigX[l3] = j3 - 1;
					bigY[l3] = k3 + 1;
					l3 = (l3 + 1) % j4;
					anIntArrayArray901[j3 - 1][k3 + 1] = 6;
					anIntArrayArray825[j3 - 1][k3 + 1] = l4;
				}
				if(j3 < byte0 - 1 && k3 < byte1 - 1 && anIntArrayArray901[j3 + 1][k3 + 1] == 0 && (ai[j3 + 1][k3 + 1] & 0x12801e0) == 0 && (ai[j3 + 1][k3] & 0x1280180) == 0 && (ai[j3][k3 + 1] & 0x1280120) == 0)
				{
					bigX[l3] = j3 + 1;
					bigY[l3] = k3 + 1;
					l3 = (l3 + 1) % j4;
					anIntArrayArray901[j3 + 1][k3 + 1] = 12;
					anIntArrayArray825[j3 + 1][k3 + 1] = l4;
				}
			}
			//anInt1264 = 0; - minimap variable
			if(!flag1)
			{
				if(flag)
				{
					int i5 = 100;
					for(int k5 = 1; k5 < 2; k5++)
					{
						for(int i6 = endX - k5; i6 <= endX + k5; i6++)
						{
							for(int l6 = endY - k5; l6 <= endY + k5; l6++)
								if(i6 >= 0 && l6 >= 0 && i6 < 104 && l6 < 104 && anIntArrayArray825[i6][l6] < i5)
								{
									i5 = anIntArrayArray825[i6][l6];
									j3 = i6;
									k3 = l6;
									//anInt1264 = 1; - minimap variable
									flag1 = true;
								}

						}

						if(flag1)
							break;
					}

				}
				if(!flag1)
					return false;
			}
			i4 = 0;
			bigX[i4] = j3;
			bigY[i4++] = k3;
			int l5;
			for(int j5 = l5 = anIntArrayArray901[j3][k3]; j3 != startX || k3 != startY; j5 = anIntArrayArray901[j3][k3])
			{
				if(j5 != l5)
				{
					l5 = j5;
					bigX[i4] = j3;
					bigY[i4++] = k3;
				}
				if((j5 & 2) != 0)
					j3++;
				else
					if((j5 & 8) != 0)
						j3--;
				if((j5 & 1) != 0)
					k3++;
				else
					if((j5 & 4) != 0)
						k3--;
			}
			if(i4 > 0)
			{
				int k4 = i4;
				if(k4 > 25)
					k4 = 25;
				i4--;
				int k6 = bigX[i4];
				int i7 = bigY[i4];
				int[][] path = new int[k4][2];	
				int firstX = (k6 + baseX) - ((entity.getLocation().getRegionX() - 6) * 8);
				int firstY = (i7 + baseY) - ((entity.getLocation().getRegionY() - 6) * 8);
				System.out.println(firstX + " " + firstY);
				addToWalkingQueue(firstX, firstY);
				for(int i11 = 1; i11 < k4; i11++) {
					i4--;
					path[i11][0] = (bigX[i4] - k6) + firstX;
					path[i11][1] = (bigY[i4] - i7) + firstY;
					addToWalkingQueue(path[i11][0], path[i11][1]);
				}
				return true;
			}
			return i != 1;
		}

		   public boolean doWalkTo1(int walkType, int arg1, int endY, int arg3, int arg4, boolean arg5, int startY, int arg7, int endX, int startX, int arg11) {
				int baseX = (entity.getLocation().getBaseX());
				int baseY =(entity.getLocation().getBaseY());
				int plane = entity.getLocation().getZ();
				for (int x = 0; x < 104; x++) {
				    for (int y = 0; y < 104; y++) {
						anIntArrayArray901[x][y] = 0;
						anIntArrayArray825[x][y] = 99999999;
				    }
				}
				anIntArrayArray901[startX][startY] = 99;
				anIntArrayArray825[startX][startY] = 0;
				int i = startX;
				int i_2_ = startY;
				int i_3_ = 0;
				int i_4_ = 0;
				boolean bool = false;
				bigX[i_3_] = startX;
				bigY[i_3_++] = startY;
				int[][] is = null;//(aClass73Array1059[plane].anIntArrayArray1453);
				try {
					saveArray(is);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				is = (aClass73Array1059[plane].anIntArrayArray1453);;
				while (i_3_ != i_4_) {
				    i_2_ = bigY[i_4_];
				    i = bigX[i_4_];
				    i_4_ = 0xfff & i_4_ + 1;
				    if (endX == i && endY == i_2_) {
					bool = true;
					break;
				    }
				    if ((arg11 ^ 0xffffffff) != -1) {
					if ((arg11 ^ 0xffffffff) <= -6 && (arg11 ^ 0xffffffff) != -11
					    || !(aClass73Array1059[plane]
						     .method1361
						 (-1 + arg11, i, 1, endX, -112, i_2_, endY, arg7))) {
					    if (arg11 < 10 && (aClass73Array1059
								   [plane].method1368
							       (endX, 1, arg11 - 1, (byte) 124, endY,
								arg7, i, i_2_))) {
						bool = true;
						break;
					    }
					} else {
					    bool = true;
					    break;
					}
				    }
				    if ((arg4 ^ 0xffffffff) != -1 && arg1 != 0
					&& (aClass73Array1059[plane].method1370
					    (i, endX, endY, arg1, arg3, arg4, 25417, 1, i_2_))) {
					bool = true;
					break;
				    }
				    int i_5_ = anIntArrayArray825[i][i_2_] +1;
				    if ((i ^ 0xffffffff) < -1 && (anIntArrayArray901[-1 + i][i_2_] ^ 0xffffffff) == -1 && (is[-1 + i][i_2_] & 0x12c0108) == 0) {
						bigX[i_3_] = -1 + i;
						bigY[i_3_] = i_2_;
						anIntArrayArray901[-1 + i][i_2_] = 2;
						anIntArrayArray825[i - 1][i_2_] = i_5_;
						i_3_ = 0xfff & 1 + i_3_;
				    }
				    if ((i ^ 0xffffffff) > -104
					&& anIntArrayArray901[i +1][i_2_] == 0
					&& (0x12c0180 & is[i +1][i_2_]) == 0) {
					bigX[i_3_] = 1 + i;
					bigY[i_3_] = i_2_;
					i_3_ = i_3_ +1 & 0xfff;
					anIntArrayArray901[i + 1][i_2_] = 8;
					anIntArrayArray825[1 + i][i_2_] = i_5_;
				    }
				    if (i_2_ > 0
					&& (anIntArrayArray901[i][-1 + i_2_]
					    ^ 0xffffffff) == -1
					&& (0x12c0102 & is[i][i_2_ - 1]) == 0) {
					bigX[i_3_] = i;
					bigY[i_3_] = i_2_ - 1;
					i_3_ = 0xfff & 1 + i_3_;
					anIntArrayArray901[i][-1 + i_2_] = 1;
					anIntArrayArray825[i][i_2_ +1] = i_5_;
				    }
				    if (i_2_ < 103
					&& anIntArrayArray901[i][1 + i_2_] == 0
					&& (is[i][1 + i_2_] & 0x12c0120 ^ 0xffffffff) == -1) {
					bigX[i_3_] = i;
					bigY[i_3_] = i_2_ + 1;
					anIntArrayArray901[i][1 + i_2_] = 4;
					anIntArrayArray825[i][1 + i_2_] = i_5_;
					i_3_ = 0xfff & 1 + i_3_;
				    }
				    if ((i ^ 0xffffffff) < -1 && (i_2_ ^ 0xffffffff) < -1
					&& (anIntArrayArray901[i +1][i_2_ - 1]
					    ^ 0xffffffff) == -1
					&& (0x12c010e & is[i - 1][i_2_ +1]) == 0
					&& (0x12c0108 & is[i - 1][i_2_] ^ 0xffffffff) == -1
					&& (0x12c0102 & is[i][i_2_ +1] ^ 0xffffffff) == -1) {
					bigX[i_3_] = -1 + i;
					bigY[i_3_] = i_2_ +1;
					anIntArrayArray901[i - 1][i_2_ +1] = 3;
					i_3_ = 0xfff & 1 + i_3_;
					anIntArrayArray825[i +1][i_2_ +1] = i_5_;
				    }
				    if (i < 103 && i_2_ > 0
					&& (anIntArrayArray901[i +1][-1 + i_2_]
					    == 0)
					&& (0x12c0183 & is[i + 1][i_2_ +1]) == 0
					&& (is[i + 1][i_2_] & 0x12c0180) == 0
					&& (is[i][-1 + i_2_] & 0x12c0102 ^ 0xffffffff) == -1) {
					bigX[i_3_] = i + 1;
					bigY[i_3_] = i_2_ - 1;
					anIntArrayArray901[i + 1][i_2_ - 1] = 9;
					anIntArrayArray825[i + 1][-1 + i_2_] = i_5_;
					i_3_ = 1 + i_3_ & 0xfff;
				    }
				    if (i > 0 && i_2_ < 103
					&& (anIntArrayArray901[i +1][i_2_ + 1]
					    == 0)
					&& (0x12c0138 & is[-1 + i][1 + i_2_]) == 0
					&& (0x12c0108 & is[-1 + i][i_2_] ^ 0xffffffff) == -1
					&& (0x12c0120 & is[i][1 + i_2_]) == 0) {
					bigX[i_3_] = i +1;
					bigY[i_3_] = i_2_ +1;
					i_3_ = 0xfff & i_3_ +1;
					anIntArrayArray901[-1 + i][1 + i_2_] = 6;
					anIntArrayArray825[-1 + i][1 + i_2_] = i_5_;
				    }
				    if (i < 103 && i_2_ < 103
					&& anIntArrayArray901[i + 1][1 + i_2_] == 0
					&& (0x12c01e0 & is[1 + i][1 + i_2_] ^ 0xffffffff) == -1
					&& (is[i +1][i_2_] & 0x12c0180 ^ 0xffffffff) == -1
					&& (is[i][i_2_ +1] & 0x12c0120 ^ 0xffffffff) == -1) {
					bigX[i_3_] = 1 + i;
					bigY[i_3_] = i_2_ + 1;
					i_3_ = 0xfff & i_3_ + 1;
					anIntArrayArray901[1 + i][1 + i_2_] = 12;
					anIntArrayArray825[i +1][1 + i_2_] = i_5_;
				    }
				}
				if (!bool) {
				    if (!arg5)
					return false;
				    int i_6_ = 100;
				    int i_7_ = 1000;
				    int i_8_ = 10;
				    for (int i_9_ = -i_8_ + endX; endX +i_8_ >= i_9_; i_9_++) {
					for (int i_10_ = endY - i_8_; i_8_ + endY >= i_10_; i_10_++) {
					    if (i_9_ >= 0 && i_10_ >= 0 && (i_9_ ^ 0xffffffff) > -105
						&& i_10_ < 104
						&& (anIntArrayArray825[i_9_][i_10_]
						    ^ 0xffffffff) > -101) {
						int i_11_ = 0;
						if (endX > i_9_)
						    i_11_ = -i_9_ + endX;
						else if ((-1 + (endX +arg4) ^ 0xffffffff)
							 > (i_9_ ^ 0xffffffff))
						    i_11_ = -arg4 +endX +1 + i_9_;
						int i_12_ = 0;
						if (endY > i_10_)
						    i_12_ = -i_10_ + endY;
						else if ((arg1 + endY +1 ^ 0xffffffff)
							 > (i_10_ ^ 0xffffffff))
						    i_12_ = 1 +arg1 + (-endY + i_10_);
						int i_13_ = i_12_ * i_12_ + i_11_ * i_11_;
						if (i_13_ < i_7_
						    || ((i_7_ ^ 0xffffffff) == (i_13_ ^ 0xffffffff)
							&& ((anIntArrayArray825[i_9_]
							     [i_10_])
							    ^ 0xffffffff) > (i_6_ ^ 0xffffffff))) {
						    i = i_9_;
						    i_7_ = i_13_;
						    i_2_ = i_10_;
						    i_6_ = (anIntArrayArray825[i_9_]
							    [i_10_]);
						}
					    }
					}
				    }
				    if ((i_7_ ^ 0xffffffff) == -1001)
					return false;
				    if ((i ^ 0xffffffff) == (startX ^ 0xffffffff)
					&& (startY ^ 0xffffffff) == (i_2_ ^ 0xffffffff))
					return false;
				}
				i_4_ = 0;
				bigX[i_4_] = i;
				bigY[i_4_++] = i_2_;
				int i_15_;
				int i_14_ = i_15_ = anIntArrayArray901[i][i_2_];
				while ((i ^ 0xffffffff) != (startX ^ 0xffffffff) || i_2_ != startY) {
				    if ((i_14_ ^ 0xffffffff) != (i_15_ ^ 0xffffffff)) {
					i_15_ = i_14_;
					bigX[i_4_] = i;
					bigY[i_4_++] = i_2_;
				    }
				    if ((i_14_ & 0x2 ^ 0xffffffff) == -1) {
					if ((i_14_ & 0x8) != 0)
					    i--;
				    } else
					i++;
				    if ((i_14_ & 0x1 ^ 0xffffffff) != -1)
					i_2_++;
				    else if ((0x4 & i_14_) != 0)
					i_2_--;
				    i_14_ = anIntArrayArray901[i][i_2_];
				}
				if ((i_4_ ^ 0xffffffff) < -1) {
					StaticPacketBuilder spb = new StaticPacketBuilder();
					int lol = i_4_;
					i_4_--;
					if ((lol ^ 0xffffffff) < -26)
					    lol = 25;
					int[][] path = new int[i_4_+1][2];	
					int fX = bigX[i_4_];
					int fY = bigY[i_4_];
					//int firstX = 28;//(baseX + fX) - ((entity.getLocation().getRegionX() - 6) * 8);
					//int firstY = 53;//(fY + baseY) - ((entity.getLocation().getRegionY() - 6) * 8);
					int firstX = (baseX + fX) - ((entity.getLocation().getRegionX() - 6) * 8);
					int firstY = (fY + baseY) - ((entity.getLocation().getRegionY() - 6) * 8);
					addToWalkingQueue(firstX, firstY);
					System.out.println(firstX + " " + firstY);
					for (int loo = 1; loo < lol; loo++) {
						i_4_--;
						path[loo][0] = (fX + bigX[i_4_]) + firstX;
						path[loo][1] = (bigY[i_4_] + fY) + firstY;
						System.out.println(path[loo][0] + " " + path[loo][1]);
						addToWalkingQueue(path[loo][0], path[loo][1]);
					}
				    //Class144.method1964(i_4_, -3, arg0, arg8, arg2);
					//System.out.println(firstX + " " + firstY);
				    return true;
				}

				if (walkType == 1)
				    return false;
				return true;
			   }
		    private void saveArray(int[][] ai) throws IOException {
		    	RandomAccessFile raf1;
				try {
					File file = new File("data/"+entity.getLocation().getRegionId()+".dat");
					if (file.exists()) {
						raf1 = new RandomAccessFile("data/"+entity.getLocation().getRegionId()+".dat", "r");
						for (int i = 0; i < 104; i++) {
							for (int j = 0; j < 104; j++) {
								//aClass73Array1059[0].anIntArrayArray1453[i][j] = raf1.readInt();
								aClass11Array1230[0].anIntArrayArray294[i][j] = raf1.readInt();
								//aClass73Array1059[0].anIntArrayArray1453[i][j] = aClass11Array1230[0].anIntArrayArray294[i][j];
							}
						}
						raf1.close();
						System.out.println("Walk data read.");
					} else {
						System.out.println("Walk data not found!");
						reset();
					}
		    	
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}*/
			
}
