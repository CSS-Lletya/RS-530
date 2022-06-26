package com.xeno.util;

import com.xeno.net.Packet;

/**
 * A collection of miscellaneous methods and constants.
 * @author Graham
 * @author Luke132
 *
 */
public class Utility {
	

	public static String intToKOrM(int number) {
		int newAmount = (number / 1000);
		String s = "";
		if (number >= 1000 && number < 1000000) {
			s += newAmount + "K";
		} else if (number >= 1000000) {
			s += newAmount + "M";
		}
		return s;
	}
	
	public static int getSecondsFromMillis(long time) {
		return (int) ((System.currentTimeMillis() - time) / 1000);
	}
	
	public static int getMinutesFromMillis(long time) {
		int seconds = (int) ((System.currentTimeMillis() - time) / 1000);
		return (seconds / 60);
	}
	
	public static int getHoursFromMillis(long time) {
		int seconds = (int) ((System.currentTimeMillis() - time) / 1000);
		int minutes = seconds / 60;
		return (minutes / 60);
	}
	
	public static int getDaysFromMillis(long time) {
		int seconds = (int) ((System.currentTimeMillis() - time) / 1000);
		int minutes = seconds / 60;
		int hours = minutes / 60;
		return (hours / 24);
	}
	/**
	 * Convert a string to a long.
	 * @param s
	 * @return
	 */
	public static long playerNameToLong(String s) {
		long l = 0L;
		for(int i = 0; i < s.length() && i < 12; i++) {
			char c = s.charAt(i);
			l *= 37L;
			if(c >= 'A' && c <= 'Z') l += (1 + c) - 65;
			else if(c >= 'a' && c <= 'z') l += (1 + c) - 97;
			else if(c >= '0' && c <= '9') l += (27 + c) - 48;
		}
		while(l % 37L == 0L && l != 0L) l /= 37L;
		return l;
	}
	
	public static int random(int range) {
		return (int)(java.lang.Math.random() * (range + 1));
	}
	
	/**
	 * Valid characters.
	 */
	public static char[] VALID_CHARS = {
		'_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 
		'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 
		't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', 
		'3', '4', '5', '6', '7', '8', '9'
	};
	
	/**
	 * Convert a long to a string.
	 * @param l
	 * @return
	 */
	public static String longToPlayerName(long l) {
		if (l <= 0L || l >= 0x5b5b57f8a98a5dd1L) {
			return null;
		}
		if (l % 37L == 0L) {
			return null;
		}
		int i = 0;
		char ac[] = new char[12];
		while (l != 0L) {
			long l1 = l;
			l /= 37L;
			ac[11 - i++] = VALID_CHARS[(int)(l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}
	
	/**
	 * Format a player's name for use in the protocol.
	 * @return
	 */
	public static String formatPlayerNameForProtocol(String name) {
		name = name.replaceAll(" ", "_");
		name = name.toLowerCase();
		return name;
	}
	
	/**
	 * Format a player's name for display.
	 * @param name
	 * @return
	 */
	public static String formatPlayerNameForDisplay(String name) {
		name = name.replaceAll("_", " ");
		name = name.toLowerCase();
		StringBuilder newName = new StringBuilder();
		boolean wasSpace = true;
		for(int i = 0; i < name.length(); i++) {
			if(wasSpace) {
				newName.append((new String()+name.charAt(i)).toUpperCase());
				wasSpace = false;
			} else {
				newName.append(name.charAt(i));
			}
			if(name.charAt(i) == ' ') {
				wasSpace = true;
			}
		}
		return newName.toString();
	}
	
	public static String decryptPlayerChat(Packet str, int totalChars) {
		try {
			if (totalChars == 0) {
				return "";
			}
			int charsDecoded = 0;
			int i_4_ = 0;
			String s = "";
			for (;;) {
				byte i_7_ = str.readByte();
				if (i_7_ >= 0) {
					i_4_++;
				} else {
					i_4_ = anIntArray627[i_4_];
				}
				int i_8_;
				if ((i_8_ = anIntArray627[i_4_]) < 0) {
					s += (char) (byte) (i_8_ ^ 0xffffffff);
					if (totalChars <= ++charsDecoded) {
						break;
					}
					i_4_ = 0;
				}
				if (((i_7_ & 0x40) ^ 0xffffffff) != -1) {
					i_4_ = anIntArray627[i_4_];
				} else {
					i_4_++;
				}
				if ((i_8_ = anIntArray627[i_4_]) < 0) {
					s += (char) (byte) (i_8_ ^ 0xffffffff);
					if (++charsDecoded >= totalChars) {
						break;
					}
					i_4_ = 0;
				}
				if ((0x20 & i_7_) == 0) {
					i_4_++;
				} else {
					i_4_ = anIntArray627[i_4_];
				}
				if ((i_8_ = anIntArray627[i_4_]) < 0) {
					s += (char) (byte) (i_8_ ^ 0xffffffff);
					if (totalChars <= ++charsDecoded) {
						break;
					}
					i_4_ = 0;
				}
				if (((0x10 & i_7_) ^ 0xffffffff) == -1) {
					i_4_++;
				} else {
					i_4_ = anIntArray627[i_4_];
				}
				if ((i_8_ = anIntArray627[i_4_]) < 0) {
					s += (char) (byte) (i_8_ ^ 0xffffffff);
					if (totalChars <= ++charsDecoded) {
						break;
					}

					i_4_ = 0;
				}
				if (((0x8 & i_7_) ^ 0xffffffff) != -1) {
					i_4_ = anIntArray627[i_4_];
				} else {
					i_4_++;
				}
				if ((i_8_ = anIntArray627[i_4_]) < 0) {
					s += (char) (byte) (i_8_ ^ 0xffffffff);
					if (++charsDecoded >= totalChars) {
						break;
					}
					i_4_ = 0;
				}
				if ((0x4 & i_7_) == 0) {
					i_4_++;
				} else {
					i_4_ = anIntArray627[i_4_];
				}
				if ((i_8_ = anIntArray627[i_4_]) < 0) {
					s += (char) (byte) (i_8_ ^ 0xffffffff);
					if (totalChars <= ++charsDecoded) {
						break;
					}
					i_4_ = 0;
				}
				if (((i_7_ & 0x2) ^ 0xffffffff) != -1) {
					i_4_ = anIntArray627[i_4_];
				} else {
					i_4_++;
				}
				if ((i_8_ = anIntArray627[i_4_]) < 0) {
					s += (char) (byte) (i_8_ ^ 0xffffffff);
					if (totalChars <= ++charsDecoded) {
						break;
					}
					i_4_ = 0;
				}
				if (((i_7_ & 0x1) ^ 0xffffffff) != -1) {
					i_4_ = anIntArray627[i_4_];
				} else {
					i_4_++;
				}
				if ((i_8_ = anIntArray627[i_4_]) < 0) {
					s += (char) (byte) (i_8_ ^ 0xffffffff);
					if (++charsDecoded >= totalChars) {
						break;
					}
					i_4_ = 0;
				}
			}
			return s;
		} catch (RuntimeException runtimeexception) {
		}
		return "";
	}
	
	/*public static byte[] CHAT_BIT_SIZES = { 22, 22, 22, 22, 22, 22, 21, 22, 22,
		20, 22, 22, 22, 21, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
		22, 22, 22, 22, 22, 22, 3, 8, 22, 16, 22, 16, 17, 7, 13, 13, 13,
		16, 7, 10, 6, 16, 10, 11, 12, 12, 12, 12, 13, 13, 14, 14, 11, 14,
		19, 15, 17, 8, 11, 9, 10, 10, 10, 10, 11, 10, 9, 7, 12, 11, 10, 10,
		9, 10, 10, 12, 10, 9, 8, 12, 12, 9, 14, 8, 12, 17, 16, 17, 22, 13,
		21, 4, 7, 6, 5, 3, 6, 6, 5, 4, 10, 7, 5, 6, 4, 4, 6, 10, 5, 4, 4,
		5, 7, 6, 10, 6, 10, 22, 19, 22, 14, 22, 22, 22, 22, 22, 22, 22, 22,
		22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
		22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
		22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
		22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
		22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
		22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
		22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 21, 22, 21, 22, 22, 22, 21,
		22, 22 };

	public static int[] CHAT_MASKS = { 0, 1024, 2048, 3072, 4096, 5120, 6144,
			8192, 9216, 12288, 10240, 11264, 16384, 18432, 17408, 20480, 21504,
			22528, 23552, 24576, 25600, 26624, 27648, 28672, 29696, 30720,
			31744, 32768, 33792, 34816, 35840, 36864, 536870912, 16777216,
			37888, 65536, 38912, 131072, 196608, 33554432, 524288, 1048576,
			1572864, 262144, 67108864, 4194304, 134217728, 327680, 8388608,
			2097152, 12582912, 13631488, 14680064, 15728640, 100663296,
			101187584, 101711872, 101974016, 102760448, 102236160, 40960,
			393216, 229376, 117440512, 104857600, 109051904, 201326592,
			205520896, 209715200, 213909504, 106954752, 218103808, 226492416,
			234881024, 222298112, 224395264, 268435456, 272629760, 276824064,
			285212672, 289406976, 223346688, 293601280, 301989888, 318767104,
			297795584, 298844160, 310378496, 102498304, 335544320, 299892736,
			300941312, 301006848, 300974080, 39936, 301465600, 49152,
			1073741824, 369098752, 402653184, 1342177280, 1610612736,
			469762048, 1476395008, -2147483648, -1879048192, 352321536,
			1543503872, -2013265920, -1610612736, -1342177280, -1073741824,
			-1543503872, 356515840, -1476395008, -805306368, -536870912,
			-268435456, 1577058304, -134217728, 360710144, -67108864,
			364904448, 51200, 57344, 52224, 301203456, 53248, 54272, 55296,
			56320, 301072384, 301073408, 301074432, 301075456, 301076480,
			301077504, 301078528, 301079552, 301080576, 301081600, 301082624,
			301083648, 301084672, 301085696, 301086720, 301087744, 301088768,
			301089792, 301090816, 301091840, 301092864, 301093888, 301094912,
			301095936, 301096960, 301097984, 301099008, 301100032, 301101056,
			301102080, 301103104, 301104128, 301105152, 301106176, 301107200,
			301108224, 301109248, 301110272, 301111296, 301112320, 301113344,
			301114368, 301115392, 301116416, 301117440, 301118464, 301119488,
			301120512, 301121536, 301122560, 301123584, 301124608, 301125632,
			301126656, 301127680, 301128704, 301129728, 301130752, 301131776,
			301132800, 301133824, 301134848, 301135872, 301136896, 301137920,
			301138944, 301139968, 301140992, 301142016, 301143040, 301144064,
			301145088, 301146112, 301147136, 301148160, 301149184, 301150208,
			301151232, 301152256, 301153280, 301154304, 301155328, 301156352,
			301157376, 301158400, 301159424, 301160448, 301161472, 301162496,
			301163520, 301164544, 301165568, 301166592, 301167616, 301168640,
			301169664, 301170688, 301171712, 301172736, 301173760, 301174784,
			301175808, 301176832, 301177856, 301178880, 301179904, 301180928,
			301181952, 301182976, 301184000, 301185024, 301186048, 301187072,
			301188096, 301189120, 301190144, 301191168, 301193216, 301195264,
			301194240, 301197312, 301198336, 301199360, 301201408, 301202432 };
	
	private static int[] CHAT_DECRYPT_KEYS = { 215, 203, 83, 158, 104, 101, 93,
			84, 107, 103, 109, 95, 94, 98, 89, 86, 70, 41, 32, 27, 24, 23, -1,
			-2, 26, -3, -4, 31, 30, -5, -6, -7, 37, 38, 36, -8, -9, -10, 40,
			-11, -12, 55, 48, 46, 47, -13, -14, -15, 52, 51, -16, -17, 54, -18,
			-19, 63, 60, 59, -20, -21, 62, -22, -23, 67, 66, -24, -25, 69, -26,
			-27, 199, 132, 80, 77, 76, -28, -29, 79, -30, -31, 87, 85, -32,
			-33, -34, -35, -36, 197, -37, 91, -38, 134, -39, -40, -41, 97, -42,
			-43, 133, 106, -44, 117, -45, -46, 139, -47, -48, 110, -49, -50,
			114, 113, -51, -52, 116, -53, -54, 135, 138, 136, 129, 125, 124,
			-55, -56, 130, 128, -57, -58, -59, 183, -60, -61, -62, -63, -64,
			148, -65, -66, 153, 149, 145, 144, -67, -68, 147, -69, -70, -71,
			152, 154, -72, -73, -74, 157, 171, -75, -76, 207, 184, 174, 167,
			166, 165, -77, -78, -79, 172, 170, -80, -81, -82, 178, -83, 177,
			182, -84, -85, 187, 181, -86, -87, -88, -89, 206, 221, -90, 189,
			-91, 198, 254, 262, 195, 196, -92, -93, -94, -95, -96, 252, 255,
			250, -97, 211, 209, -98, -99, 212, -100, 213, -101, -102, -103,
			224, -104, 232, 227, 220, 226, -105, -106, 246, 236, -107, 243,
			-108, -109, 231, 237, 235, -110, -111, 239, 238, -112, -113, -114,
			-115, -116, 241, -117, 244, -118, -119, 248, -120, 249, -121, -122,
			-123, 253, -124, -125, -126, -127, 259, 258, -128, -129, 261, -130,
			-131, 390, 327, 296, 281, 274, 271, 270, -132, -133, 273, -134,
			-135, 278, 277, -136, -137, 280, -138, -139, 289, 286, 285, -140,
			-141, 288, -142, -143, 293, 292, -144, -145, 295, -146, -147, 312,
			305, 302, 301, -148, -149, 304, -150, -151, 309, 308, -152, -153,
			311, -154, -155, 320, 317, 316, -156, -157, 319, -158, -159, 324,
			323, -160, -161, 326, -162, -163, 359, 344, 337, 334, 333, -164,
			-165, 336, -166, -167, 341, 340, -168, -169, 343, -170, -171, 352,
			349, 348, -172, -173, 351, -174, -175, 356, 355, -176, -177, 358,
			-178, -179, 375, 368, 365, 364, -180, -181, 367, -182, -183, 372,
			371, -184, -185, 374, -186, -187, 383, 380, 379, -188, -189, 382,
			-190, -191, 387, 386, -192, -193, 389, -194, -195, 454, 423, 408,
			401, 398, 397, -196, -197, 400, -198, -199, 405, 404, -200, -201,
			407, -202, -203, 416, 413, 412, -204, -205, 415, -206, -207, 420,
			419, -208, -209, 422, -210, -211, 439, 432, 429, 428, -212, -213,
			431, -214, -215, 436, 435, -216, -217, 438, -218, -219, 447, 444,
			443, -220, -221, 446, -222, -223, 451, 450, -224, -225, 453, -226,
			-227, 486, 471, 464, 461, 460, -228, -229, 463, -230, -231, 468,
			467, -232, -233, 470, -234, -235, 479, 476, 475, -236, -237, 478,
			-238, -239, 483, 482, -240, -241, 485, -242, -243, 499, 495, 492,
			491, -244, -245, 494, -246, -247, 497, -248, 502, -249, 506, 503,
			-250, -251, 505, -252, -253, 508, -254, 510, -255, -256, 0, };
	
	public static String textUnpack(byte packedData[], int off, int size) {
		if (size == 0)
			return "";
		int offset = 1;
		int length = packedData[0] & 0xff;
		try {
			int charsDecoded = 0;
			int keyIndex = 0;
			StringBuilder sb = new StringBuilder();
			for (;;) {
				byte textByte = packedData[offset++];
				if (textByte >= 0) {
					keyIndex++;
				} else {
					keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
				}
				int charId;
				if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (length <= ++charsDecoded) {
						break;
					}
					keyIndex = 0;
				}
				if ((textByte & 0x40) != 0) {
					keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
				} else {
					keyIndex++;
				}
				if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (++charsDecoded >= length) {
						break;
					}
					keyIndex = 0;
				}
				if ((0x20 & textByte) == 0) {
					keyIndex++;
				} else {
					keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
				}
				if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (length <= ++charsDecoded) {
						break;
					}
					keyIndex = 0;
				}
				if ((0x10 & textByte) == 0) {
					keyIndex++;
				} else {
					keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
				}
				if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (length <= ++charsDecoded) {
						break;
					}
	
					keyIndex = 0;
				}
				if ((0x8 & textByte) != 0) {
					keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
				} else {
					keyIndex++;
				}
				if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (++charsDecoded >= length) {
						break;
					}
					keyIndex = 0;
				}
				if ((0x4 & textByte) == 0) {
					keyIndex++;
				} else {
					keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
				}
				if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (length <= ++charsDecoded) {
						break;
					}
					keyIndex = 0;
				}
				if ((textByte & 0x2) != 0) {
					keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
				} else {
					keyIndex++;
				}
				if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (length <= ++charsDecoded) {
						break;
					}
					keyIndex = 0;
				}
				if ((textByte & 0x1) != 0) {
					keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
				} else {
					keyIndex++;
				}
				if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (++charsDecoded >= length) {
						break;
					}
					keyIndex = 0;
				}
			}
			return sb.toString();
		} catch (Exception e) {
		}
		return "Error";
	}
	
	public static String decryptPlayerChat(byte packedData[], int size) {
		if (size == 0)
			return "";
		int offset = 1;
		int length = packedData[0] & 0xff;
		try {
			int charsDecoded = 0;
			int keyIndex = 0;
			StringBuilder sb = new StringBuilder();
			for (;;) {
				byte textByte = packedData[offset++];
				if (textByte >= 0) {
					keyIndex++;
				} else {
					keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
				}
				int charId;
				if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (length <= ++charsDecoded) {
						break;
					}
					keyIndex = 0;
				}
				if ((textByte & 0x40) != 0) {
					keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
				} else {
					keyIndex++;
				}
				if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (++charsDecoded >= length) {
						break;
					}
					keyIndex = 0;
				}
				if ((0x20 & textByte) == 0) {
					keyIndex++;
				} else {
					keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
				}
				if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (length <= ++charsDecoded) {
						break;
					}
					keyIndex = 0;
				}
				if ((0x10 & textByte) == 0) {
					keyIndex++;
				} else {
					keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
				}
				if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (length <= ++charsDecoded) {
						break;
					}
	
					keyIndex = 0;
				}
				if ((0x8 & textByte) != 0) {
					keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
				} else {
					keyIndex++;
				}
				if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (++charsDecoded >= length) {
						break;
					}
					keyIndex = 0;
				}
				if ((0x4 & textByte) == 0) {
					keyIndex++;
				} else {
					keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
				}
				if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (length <= ++charsDecoded) {
						break;
					}
					keyIndex = 0;
				}
				if ((textByte & 0x2) != 0) {
					keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
				} else {
					keyIndex++;
				}
				if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (length <= ++charsDecoded) {
						break;
					}
					keyIndex = 0;
				}
				if ((textByte & 0x1) != 0) {
					keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
				} else {
					keyIndex++;
				}
				if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (++charsDecoded >= length) {
						break;
					}
					keyIndex = 0;
				}
			}
			return sb.toString();
		} catch (Exception e) {
		}
		return "Error";
	}*/
	
	public static int encryptPlayerChat(byte[] arg2, int arg0, int arg3, int arg1, byte[] arg4) {
    	try {
    	    arg1 += arg0;
    	    int i = 0;
    	    int i_0_ = arg3 << 1395298339;
    	    for (/**/; (arg0 ^ 0xffffffff) > (arg1 ^ 0xffffffff); arg0++) {
    		int i_1_ = 0xff & arg4[arg0];
    		int i_2_ = aByteArray630[i_1_];
    		int i_3_ = anIntArray626[i_1_];
    		if (i_2_ == 0)
    		    throw new RuntimeException("No codeword for data value " + i_1_);
    		int i_4_ = 0x7 & i_0_;
    		int i_5_ = i_0_ >> 1011774787;
    		int i_6_ = (i_2_ + (i_4_ - 1) >> 1678525667) + i_5_;
    		i &= -i_4_ >> 2047085535;
    		i_0_ += i_2_;
    		i_4_ += 24;
    		arg2[i_5_] = (byte) (i = method1273(i, i_3_ >>> i_4_));
    		if ((i_6_ ^ 0xffffffff) < (i_5_ ^ 0xffffffff)) {
    		    i_4_ -= 8;
    		    i_5_++;
    		    arg2[i_5_] = (byte) (i = i_3_ >>> i_4_);
    		    if (i_6_ > i_5_) {
    			i_5_++;
    			i_4_ -= 8;
    			arg2[i_5_] = (byte) (i = i_3_ >>> i_4_);
    			if (i_6_ > i_5_) {
    			    i_4_ -= 8;
    			    i_5_++;
    			    arg2[i_5_] = (byte) (i = i_3_ >>> i_4_);
    			    if ((i_5_ ^ 0xffffffff) > (i_6_ ^ 0xffffffff)) {
    				i_4_ -= 8;
    				i_5_++;
    				arg2[i_5_] = (byte) (i = i_3_ << -i_4_);
    			    }
    			}
    		    }
    		}
    	    }
    	    return (i_0_ + 7 >> -2058412413) +arg3;
    	} catch (RuntimeException runtimeexception) {
    	}
		return 0;
        }
	
	/*public static void encryptPlayerChat(byte packedData[], int bit, int offset, String text) {
		if (text.length() > 80) {
			text = text.substring(0, 80);
		}
		int length = text.length();
		int key = 0;
		int bitPosition = bit << 3;
		int srcOffset = offset;
		for (; length > srcOffset; srcOffset++) {
			int textByte = 0xff & text.getBytes()[srcOffset];
			int mask = CHAT_MASKS[textByte];
			int size = CHAT_BIT_SIZES[textByte];
			if (size == 0)
				return;
			int destOffset = bitPosition >> 3;
			int bitOffset = bitPosition & 0x7;
			key &= -bitOffset >> 31;
			bitPosition += size;
			int byteSize = (((bitOffset + size) - 1) >> 3) + destOffset;
			bitOffset += 24;
			packedData[destOffset] = (byte) (key = (key | (mask >>> bitOffset)));
			if (byteSize < destOffset) {
				destOffset++;
				bitOffset -= 8;
				packedData[destOffset] = (byte) (key = mask >>> bitOffset);
				if (byteSize > destOffset) {
					destOffset++;
					bitOffset -= 8;
					packedData[destOffset] = (byte) (key = mask >>> bitOffset);
					if (byteSize > destOffset) {
						bitOffset -= 8;
						destOffset++;
						packedData[destOffset] = (byte) (key = mask >>> bitOffset);
						if (destOffset < byteSize) {
							bitOffset -= 8;
							destOffset++;
							packedData[destOffset] = (byte) (key = mask << -bitOffset);
						}
					}
				}
			}
		}
	}
	
	public static void encryptPlayerchat(byte packedData[], String text) {
		if (text.length() > 80) {
			text = text.substring(0, 80);
		}
		int length = text.length();
		int key = 0;
		int bitPosition = 1 << 3;
		int srcOffset = 0;
		packedData[0] = (byte) length;
		for (; length > srcOffset; srcOffset++) {
			int textByte = 0xff & text.getBytes()[srcOffset];
			int mask = CHAT_MASKS[textByte];
			int size = CHAT_BIT_SIZES[textByte];
			int destOffset = bitPosition >> 3;
			int bitOffset = bitPosition & 0x7;
			key &= (-bitOffset >> 31);
			bitPosition += size;
			int byteSize = (((bitOffset + size) - 1) >> 3) + destOffset;
			bitOffset += 24;
			packedData[destOffset] = (byte) (key = (key | (mask >>> bitOffset)));
			if (byteSize > destOffset) {
				destOffset++;
				bitOffset -= 8;
				packedData[destOffset] = (byte) (key = mask >>> bitOffset);
				if (byteSize > destOffset) {
					destOffset++;
					bitOffset -= 8;
					packedData[destOffset] = (byte) (key = mask >>> bitOffset);
					if (byteSize > destOffset) {
						bitOffset -= 8;
						destOffset++;
						packedData[destOffset] = (byte) (key = mask >>> bitOffset);
						if (destOffset < byteSize) {
							bitOffset -= 8;
							destOffset++;
							packedData[destOffset] = (byte) (key = mask << -bitOffset);
						}
					}
				}
			}
		}
	}*/

	public static int method251(byte[] arg2, int arg0, int arg3, int arg1, byte[] arg4) {
    	try {
    	    arg1 += arg0;
    	    int i = 0;
    	    int i_0_ = arg3 << 1395298339;
    	    for (/**/; (arg0 ^ 0xffffffff) > (arg1 ^ 0xffffffff); arg0++) {
    		int i_1_ = 0xff & arg4[arg0];
    		int i_2_ = aByteArray630[i_1_];
    		int i_3_ = anIntArray626[i_1_];
    		if (i_2_ == 0)
    		    throw new RuntimeException("No codeword for data value " + i_1_);
    		int i_4_ = 0x7 & i_0_;
    		int i_5_ = i_0_ >> 1011774787;
    		int i_6_ = (i_2_ + (i_4_ - 1) >> 1678525667) + i_5_;
    		i &= -i_4_ >> 2047085535;
    		i_0_ += i_2_;
    		i_4_ += 24;
    		arg2[i_5_] = (byte) (i = method1273(i, i_3_ >>> i_4_));
    		if ((i_6_ ^ 0xffffffff) < (i_5_ ^ 0xffffffff)) {
    		    i_4_ -= 8;
    		    i_5_++;
    		    arg2[i_5_] = (byte) (i = i_3_ >>> i_4_);
    		    if (i_6_ > i_5_) {
    			i_5_++;
    			i_4_ -= 8;
    			arg2[i_5_] = (byte) (i = i_3_ >>> i_4_);
    			if (i_6_ > i_5_) {
    			    i_4_ -= 8;
    			    i_5_++;
    			    arg2[i_5_] = (byte) (i = i_3_ >>> i_4_);
    			    if ((i_5_ ^ 0xffffffff) > (i_6_ ^ 0xffffffff)) {
    				i_4_ -= 8;
    				i_5_++;
    				arg2[i_5_] = (byte) (i = i_3_ << -i_4_);
    			    }
    			}
    		    }
    		}
    	    }
    	    return (i_0_ + 7 >> -2058412413) +arg3;
    	} catch (RuntimeException runtimeexception) {
    	}
		return 0;
        }
    
    public static int method1273(int arg0, int arg1) {
    	return arg0 | arg1;
   }
    
    public int decryptChat(byte[] arg0, byte arg1, byte[] arg2, int arg3, int arg4, int arg5) {
    	try {
    	    if (arg4 == 0)
    		return 0;
    	    arg4 += arg5;
    	    int i = 0;
    	    int i_22_ = arg3;
    	    for (;;) {
    		byte i_23_ = arg0[i_22_];
    		if ((i_23_ ^ 0xffffffff) <= -1)
    		    i++;
    		else
    		    i = anIntArray627[i];
    		int i_24_;
    		if ((i_24_ = anIntArray627[i]) < 0) {
    		    arg2[arg5++] = (byte) (i_24_ ^ 0xffffffff);
    		    if ((arg4 ^ 0xffffffff) >= (arg5 ^ 0xffffffff))
    			break;
    		    i = 0;
    		}
    		if ((i_23_ & 0x40) != 0)
    		    i = anIntArray627[i];
    		else
    		    i++;
    		if (((i_24_ = anIntArray627[i]) ^ 0xffffffff) > -1) {
    		    arg2[arg5++] = (byte) (i_24_ ^ 0xffffffff);
    		    if (arg5 >= arg4)
    			break;
    		    i = 0;
    		}
    		if ((i_23_ & 0x20) == 0)
    		    i++;
    		else
    		    i = anIntArray627[i];
    		if (((i_24_ = anIntArray627[i]) ^ 0xffffffff) > -1) {
    		    arg2[arg5++] = (byte) (i_24_ ^ 0xffffffff);
    		    if ((arg5 ^ 0xffffffff) <= (arg4 ^ 0xffffffff))
    			break;
    		    i = 0;
    		}
    		if ((0x10 & i_23_) == 0)
    		    i++;
    		else
    		    i = anIntArray627[i];
    		if ((i_24_ = anIntArray627[i]) < 0) {
    		    arg2[arg5++] = (byte) (i_24_ ^ 0xffffffff);
    		    if ((arg4 ^ 0xffffffff) >= (arg5 ^ 0xffffffff))
    			break;
    		    i = 0;
    		}
    		if ((i_23_ & 0x8) == 0)
    		    i++;
    		else
    		    i = anIntArray627[i];
    		if ((i_24_ = anIntArray627[i]) < 0) {
    		    arg2[arg5++] = (byte) (i_24_ ^ 0xffffffff);
    		    if ((arg5 ^ 0xffffffff) <= (arg4 ^ 0xffffffff))
    			break;
    		    i = 0;
    		}
    		if ((i_23_ & 0x4) == 0)
    		    i++;
    		else
    		    i = anIntArray627[i];
    		if ((i_24_ = anIntArray627[i]) < 0) {
    		    arg2[arg5++] = (byte) (i_24_ ^ 0xffffffff);
    		    if (arg4 <= arg5)
    			break;
    		    i = 0;
    		}
    		if ((i_23_ & 0x2) == 0)
    		    i++;
    		else
    		    i = anIntArray627[i];
    		if ((i_24_ = anIntArray627[i]) < 0) {
    		    arg2[arg5++] = (byte) (i_24_ ^ 0xffffffff);
    		    if ((arg4 ^ 0xffffffff) >= (arg5 ^ 0xffffffff))
    			break;
    		    i = 0;
    		}
    		if ((i_23_ & 0x1) == 0)
    		    i++;
    		else
    		    i = anIntArray627[i];
    		if ((i_24_ = anIntArray627[i]) < 0) {
    		    arg2[arg5++] = (byte) (i_24_ ^ 0xffffffff);
    		    if (arg5 >= arg4)
    			break;
    		    i = 0;
    		}
    		i_22_++;
    	    }
    	    return i_22_ + (1 + -arg3);
    	} catch (RuntimeException runtimeexception) {
    	}
    	return arg5;
       }
    
    private static final int[] aByteArray630 = {
    	22, 22, 22, 22, 22, 22, 21, 22, 22, 20, 22, 22, 22, 21, 
    	22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 
    	22, 22, 22, 22, 3, 8, 22, 16, 22, 16, 17, 7, 13, 13, 13, 
    	16, 7, 10, 6, 16, 10, 11, 12, 12, 12, 12, 13, 13, 14, 14, 
    	11, 14, 19, 15, 17, 8, 11, 9, 10, 10, 10, 10, 11, 10, 9, 
    	7, 12, 11, 10, 10, 9, 10, 10, 12, 10, 9, 8, 12, 12, 9, 14, 
    	8, 12, 17, 16, 17, 22, 13, 21, 4, 7, 6, 5, 3, 6, 6, 5, 4, 
    	10, 7, 5, 6, 4, 4, 6, 10, 5, 4, 4, 5, 7, 6, 10, 6, 10, 22, 
    	19, 22, 14, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
		22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
		22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 
		22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
		22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 
		22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
		22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 
		22, 22, 22, 22, 22, 22, 21, 22, 21, 22, 22, 22, 21, 22, 22, 
	};
    
    private static final int[] anIntArray626 = {
    	0, 1024, 2048, 3072, 4096, 5120, 6144, 8192, 
    	9216, 12288, 10240, 11264, 16384, 18432, 17408, 
    	20480, 21504, 22528, 23552, 24576, 25600, 26624, 
    	27648, 28672, 29696, 30720, 31744, 32768, 33792, 
    	34816, 35840, 36864, 536870912, 16777216, 37888, 
    	65536, 38912, 131072, 196608, 33554432, 524288, 
    	1048576, 1572864, 262144, 67108864, 4194304, 134217728, 
    	327680, 8388608, 2097152, 12582912, 13631488, 14680064, 
    	15728640, 100663296, 101187584, 101711872, 101974016, 
    	102760448, 102236160, 40960, 393216, 229376, 117440512, 
    	104857600, 109051904, 201326592, 205520896, 209715200, 
    	213909504, 106954752, 218103808, 226492416, 234881024, 
    	222298112, 224395264, 268435456, 272629760, 276824064, 
    	285212672, 289406976, 223346688, 293601280, 301989888, 
    	318767104, 297795584, 298844160, 310378496, 102498304, 
    	335544320, 299892736, 300941312, 301006848, 300974080, 
    	39936, 301465600, 49152, 1073741824, 369098752, 402653184, 
    	1342177280, 1610612736, 469762048, 1476395008, -2147483648, 
    	-1879048192, 352321536, 1543503872, -2013265920, -1610612736, 
    	-1342177280, -1073741824, -1543503872, 356515840, -1476395008, 
    	-805306368, -536870912, -268435456, 1577058304, -134217728, 
    	360710144, -67108864, 364904448, 51200, 57344, 52224, 301203456, 
    	53248, 54272, 55296, 56320, 301072384, 301073408, 301074432, 
    	301075456, 301076480, 301077504, 301078528, 301079552, 301080576, 
    	301081600, 301082624, 301083648, 301084672, 301085696, 301086720, 
    	301087744, 301088768, 301089792, 301090816, 301091840, 301092864, 
    	301093888, 301094912, 301095936, 301096960, 301097984, 301099008,
    	301100032, 301101056, 301102080, 301103104, 301104128, 301105152,
    	301106176, 301107200, 301108224, 301109248, 301110272, 301111296, 
    	301112320, 301113344, 301114368, 301115392, 301116416, 301117440, 
    	301118464, 301119488, 301120512, 301121536, 301122560, 301123584, 
    	301124608, 301125632, 301126656, 301127680, 301128704, 301129728,
    	301130752, 301131776, 301132800, 301133824, 301134848, 301135872, 
    	301136896, 301137920, 301138944, 301139968, 301140992, 301142016, 
    	301143040, 301144064, 301145088, 301146112, 301147136, 301148160, 
    	301149184, 301150208, 301151232, 301152256, 301153280, 301154304,
    	301155328, 301156352, 301157376, 301158400, 301159424, 301160448, 
    	301161472, 301162496, 301163520, 301164544, 301165568, 301166592, 
    	301167616, 301168640, 301169664, 301170688, 301171712, 301172736, 
    	301173760, 301174784, 301175808, 301176832, 301177856, 301178880, 
    	301179904, 301180928, 301181952, 301182976, 301184000, 301185024, 
    	301186048, 301187072, 301188096, 301189120, 301190144, 301191168, 
    	301193216, 301195264, 301194240, 301197312, 301198336, 301199360, 
    	301201408, 301202432, 
    };
    
    private static final int[] anIntArray627 = {
    	215, 203, 83, 158, 104, 101, 93, 84, 107, 103, 109, 95,
    	94, 98, 89, 86, 70, 41, 32, 27, 24, 23, -1, -2, 26, -3, 
    	-4, 31, 30, -5, -6, -7, 37, 38, 36, -8, -9, -10, 40, -11, 
    	-12, 55, 48, 46, 47, -13, -14, -15, 52, 51, -16, -17, 54, 
    	-18, -19, 63, 60, 59, -20, -21, 62, -22, -23, 67, 66, -24, 
    	-25, 69, -26, -27, 199, 132, 80, 77, 76, -28, -29, 79, -30, 
    	-31, 87, 85, -32, -33, -34, -35, -36, 197, -37, 91, -38, 134, 
    	-39, -40, -41, 97, -42, -43, 133, 106, -44, 117, -45, -46, 139, 
    	-47, -48, 110, -49, -50, 114, 113, -51, -52, 116, -53, -54, 
    	135, 138, 136, 129, 125, 124, -55, -56, 130, 128, -57, -58, -59, 
    	183, -60, -61, -62, -63, -64, 148, -65, -66, 153, 149, 145, 144, 
    	-67, -68, 147, -69, -70, -71, 152, 154, -72, -73, -74, 157, 171, 
    	-75, -76, 207, 184, 174, 167, 166, 165, -77, -78, -79, 172, 170, 
    	-80, -81, -82, 178, -83, 177, 182, -84, -85, 187, 181, -86, -87, 
    	-88, -89, 206, 221, -90, 189, -91, 198, 254, 262, 195, 196, -92, 
    	-93, -94, -95, -96, 252, 255, 250, -97, 211, 209, -98, -99, 212, 
    	-100, 213, -101, -102, -103, 224, -104, 232, 227, 220, 226, -105, 
    	-106, 246, 236, -107, 243, -108, -109, 231, 237, 235, -110, -111, 
    	239, 238, -112, -113, -114, -115, -116, 241, -117, 244, -118, -119, 
    	248, -120, 249, -121, -122, -123, 253, -124, -125, -126, -127, 259,
    	258, -128, -129, 261, -130, -131, 390, 327, 296, 281, 274, 271, 270, 
    	-132, -133, 273, -134, -135, 278, 277, -136, -137, 280, -138, -139, 
    	289, 286, 285, -140, -141, 288, -142, -143, 293, 292, -144, -145, 
    	295, -146, -147, 312, 305, 302, 301, -148, -149, 304, -150, -151, 
    	309, 308, -152, -153, 311, -154, -155, 320, 317, 316, -156, -157,
    	319, -158, -159, 324, 323, -160, -161, 326, -162, -163, 359, 344, 337, 
    	334, 333, -164, -165, 336, -166, -167, 341, 340, -168, -169, 343, -170, 
    	-171, 352, 349, 348, -172, -173, 351, -174, -175, 356, 355, -176, -177, 
    	358, -178, -179, 375, 368, 365, 364, -180, -181, 367, -182, -183, 372, 
    	371, -184, -185, 374, -186, -187, 383, 380, 379, -188, -189, 382, -190, 
    	-191, 387, 386, -192, -193, 389, -194, -195, 454, 423, 408, 401, 398, 
    	397, -196, -197, 400, -198, -199, 405, 404, -200, -201, 407, -202, -203, 
    	416, 413, 412, -204, -205, 415, -206, -207, 420, 419, -208, -209, 422, 
    	-210, -211, 439, 432, 429, 428, -212, -213, 431, -214, -215, 436, 435, 
    	-216, -217, 438, -218, -219, 447, 444, 443, -220, -221, 446, -222, -223, 
    	451, 450, -224, -225, 453, -226, -227, 486, 471, 464, 461, 460, -228, 
    	-229, 463, -230, -231, 468, 467, -232, -233, 470, -234, -235, 479, 476, 
    	475, -236, -237, 478, -238, -239, 483, 482, -240, -241, 485, -242, -243, 
    	499, 495, 492, 491, -244, -245, 494, -246, -247, 497, -248, 502, -249, 506, 
    	503, -250, -251, 505, -252, -253, 508, -254, 510, -255, -256, 0, 
    };
	
	public static final byte[] DIRECTION_DELTA_X = new byte[] {-1, 0, 1, -1, 1, -1, 0, 1};
	public static final byte[] DIRECTION_DELTA_Y = new byte[] {1, 1, 1, 0, 0, -1, -1, -1};

	/**
	 * Convert a String to a Long.
	 * @param s The string to convert to a long.
	 * @return Returns the string as a long.
	 */
	public static long stringToLong(String s) {
		long l = 0L;
		for (int i = 0; i < s.length() && i < 12; i++) {
			char c = s.charAt(i);
			l *= 37L;
			if (c >= 'A' && c <= 'Z')
				l += (1 + c) - 65;
			else if (c >= 'a' && c <= 'z')
				l += (1 + c) - 97;
			else if (c >= '0' && c <= '9')
				l += (27 + c) - 48;
		}
		for (; l % 37L == 0L && l != 0L; l /= 37L);
		return l;
	}

	public static int direction(int dx, int dy) {
		if (dx < 0) {
			if (dy < 0)
				return 5;
			else if (dy > 0)
				return 0;
			else
				return 3;
		} else if (dx > 0) {
			if (dy < 0)
				return 7;
			else if (dy > 0)
				return 2;
			else
				return 4;
		} else {
			if (dy < 0)
				return 6;
			else if (dy > 0)
				return 1;
			else
				return -1;
		}
	}
	
	// gets the direction between the two given points
	// valid directions are N:0, NE:2, E:4, SE:6, S:8, SW:10, W:12, NW:14
	// the invalid (inbetween) direction are 1,3,5,7,9,11,13,15 i.e. odd
	// integers
	// returns -1, if src and dest are the same
	public static int direction(int srcX, int srcY, int destX, int destY) {
		int dx = destX - srcX, dy = destY - srcY;
		// a lot of cases that have to be considered here ... is there a more
		// sophisticated (and quick!) way?
		if (dx < 0) {
			if (dy < 0) {
				if (dx < dy)
					return 11;
				else if (dx > dy)
					return 9;
				else
					return 10; // dx == dy
			} else if (dy > 0) {
				if (-dx < dy)
					return 15;
				else if (-dx > dy)
					return 13;
				else
					return 14; // -dx == dy
			} else { // dy == 0
				return 12;
			}
		} else if (dx > 0) {
			if (dy < 0) {
				if (dx < -dy)
					return 7;
				else if (dx > -dy)
					return 5;
				else
					return 6; // dx == -dy
			} else if (dy > 0) {
				if (dx < dy)
					return 1;
				else if (dx > dy)
					return 3;
				else
					return 2; // dx == dy
			} else { // dy == 0
				return 4;
			}
		} else { // dx == 0
			if (dy < 0) {
				return 8;
			} else if (dy > 0) {
				return 0;
			} else { // dy == 0
				return -1; // src and dest are the same
			}
		}
	}

	public static final String[] SKILL_NAME = {
		"Attack", "Defence", "Strength", "Hitpoints", "Range", "Prayer",
		"Magic", "Cooking", "Woodcutting",  "Fletching", "Fishing", "Firemaking",
		"Crafting", "Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer",
		"Farming", "Runecrafting", "Construction", "Hunter", "Summoning",
	};
	
	public static byte[] CHAT_BIT_SIZES = { 22, 22, 22, 22, 22, 22, 21, 22, 22,
		20, 22, 22, 22, 21, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
		22, 22, 22, 22, 22, 22, 3, 8, 22, 16, 22, 16, 17, 7, 13, 13, 13,
		16, 7, 10, 6, 16, 10, 11, 12, 12, 12, 12, 13, 13, 14, 14, 11, 14,
		19, 15, 17, 8, 11, 9, 10, 10, 10, 10, 11, 10, 9, 7, 12, 11, 10, 10,
		9, 10, 10, 12, 10, 9, 8, 12, 12, 9, 14, 8, 12, 17, 16, 17, 22, 13,
		21, 4, 7, 6, 5, 3, 6, 6, 5, 4, 10, 7, 5, 6, 4, 4, 6, 10, 5, 4, 4,
		5, 7, 6, 10, 6, 10, 22, 19, 22, 14, 22, 22, 22, 22, 22, 22, 22, 22,
		22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
		22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
		22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
		22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
		22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
		22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
		22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 21, 22, 21, 22, 22, 22, 21,
		22, 22 };

public static int[] CHAT_MASKS = { 0, 1024, 2048, 3072, 4096, 5120, 6144,
		8192, 9216, 12288, 10240, 11264, 16384, 18432, 17408, 20480, 21504,
		22528, 23552, 24576, 25600, 26624, 27648, 28672, 29696, 30720,
		31744, 32768, 33792, 34816, 35840, 36864, 536870912, 16777216,
		37888, 65536, 38912, 131072, 196608, 33554432, 524288, 1048576,
		1572864, 262144, 67108864, 4194304, 134217728, 327680, 8388608,
		2097152, 12582912, 13631488, 14680064, 15728640, 100663296,
		101187584, 101711872, 101974016, 102760448, 102236160, 40960,
		393216, 229376, 117440512, 104857600, 109051904, 201326592,
		205520896, 209715200, 213909504, 106954752, 218103808, 226492416,
		234881024, 222298112, 224395264, 268435456, 272629760, 276824064,
		285212672, 289406976, 223346688, 293601280, 301989888, 318767104,
		297795584, 298844160, 310378496, 102498304, 335544320, 299892736,
		300941312, 301006848, 300974080, 39936, 301465600, 49152,
		1073741824, 369098752, 402653184, 1342177280, 1610612736,
		469762048, 1476395008, -2147483648, -1879048192, 352321536,
		1543503872, -2013265920, -1610612736, -1342177280, -1073741824,
		-1543503872, 356515840, -1476395008, -805306368, -536870912,
		-268435456, 1577058304, -134217728, 360710144, -67108864,
		364904448, 51200, 57344, 52224, 301203456, 53248, 54272, 55296,
		56320, 301072384, 301073408, 301074432, 301075456, 301076480,
		301077504, 301078528, 301079552, 301080576, 301081600, 301082624,
		301083648, 301084672, 301085696, 301086720, 301087744, 301088768,
		301089792, 301090816, 301091840, 301092864, 301093888, 301094912,
		301095936, 301096960, 301097984, 301099008, 301100032, 301101056,
		301102080, 301103104, 301104128, 301105152, 301106176, 301107200,
		301108224, 301109248, 301110272, 301111296, 301112320, 301113344,
		301114368, 301115392, 301116416, 301117440, 301118464, 301119488,
		301120512, 301121536, 301122560, 301123584, 301124608, 301125632,
		301126656, 301127680, 301128704, 301129728, 301130752, 301131776,
		301132800, 301133824, 301134848, 301135872, 301136896, 301137920,
		301138944, 301139968, 301140992, 301142016, 301143040, 301144064,
		301145088, 301146112, 301147136, 301148160, 301149184, 301150208,
		301151232, 301152256, 301153280, 301154304, 301155328, 301156352,
		301157376, 301158400, 301159424, 301160448, 301161472, 301162496,
		301163520, 301164544, 301165568, 301166592, 301167616, 301168640,
		301169664, 301170688, 301171712, 301172736, 301173760, 301174784,
		301175808, 301176832, 301177856, 301178880, 301179904, 301180928,
		301181952, 301182976, 301184000, 301185024, 301186048, 301187072,
		301188096, 301189120, 301190144, 301191168, 301193216, 301195264,
		301194240, 301197312, 301198336, 301199360, 301201408, 301202432 };

private static int[] CHAT_DECRYPT_KEYS = { 215, 203, 83, 158, 104, 101, 93,
		84, 107, 103, 109, 95, 94, 98, 89, 86, 70, 41, 32, 27, 24, 23, -1,
		-2, 26, -3, -4, 31, 30, -5, -6, -7, 37, 38, 36, -8, -9, -10, 40,
		-11, -12, 55, 48, 46, 47, -13, -14, -15, 52, 51, -16, -17, 54, -18,
		-19, 63, 60, 59, -20, -21, 62, -22, -23, 67, 66, -24, -25, 69, -26,
		-27, 199, 132, 80, 77, 76, -28, -29, 79, -30, -31, 87, 85, -32,
		-33, -34, -35, -36, 197, -37, 91, -38, 134, -39, -40, -41, 97, -42,
		-43, 133, 106, -44, 117, -45, -46, 139, -47, -48, 110, -49, -50,
		114, 113, -51, -52, 116, -53, -54, 135, 138, 136, 129, 125, 124,
		-55, -56, 130, 128, -57, -58, -59, 183, -60, -61, -62, -63, -64,
		148, -65, -66, 153, 149, 145, 144, -67, -68, 147, -69, -70, -71,
		152, 154, -72, -73, -74, 157, 171, -75, -76, 207, 184, 174, 167,
		166, 165, -77, -78, -79, 172, 170, -80, -81, -82, 178, -83, 177,
		182, -84, -85, 187, 181, -86, -87, -88, -89, 206, 221, -90, 189,
		-91, 198, 254, 262, 195, 196, -92, -93, -94, -95, -96, 252, 255,
		250, -97, 211, 209, -98, -99, 212, -100, 213, -101, -102, -103,
		224, -104, 232, 227, 220, 226, -105, -106, 246, 236, -107, 243,
		-108, -109, 231, 237, 235, -110, -111, 239, 238, -112, -113, -114,
		-115, -116, 241, -117, 244, -118, -119, 248, -120, 249, -121, -122,
		-123, 253, -124, -125, -126, -127, 259, 258, -128, -129, 261, -130,
		-131, 390, 327, 296, 281, 274, 271, 270, -132, -133, 273, -134,
		-135, 278, 277, -136, -137, 280, -138, -139, 289, 286, 285, -140,
		-141, 288, -142, -143, 293, 292, -144, -145, 295, -146, -147, 312,
		305, 302, 301, -148, -149, 304, -150, -151, 309, 308, -152, -153,
		311, -154, -155, 320, 317, 316, -156, -157, 319, -158, -159, 324,
		323, -160, -161, 326, -162, -163, 359, 344, 337, 334, 333, -164,
		-165, 336, -166, -167, 341, 340, -168, -169, 343, -170, -171, 352,
		349, 348, -172, -173, 351, -174, -175, 356, 355, -176, -177, 358,
		-178, -179, 375, 368, 365, 364, -180, -181, 367, -182, -183, 372,
		371, -184, -185, 374, -186, -187, 383, 380, 379, -188, -189, 382,
		-190, -191, 387, 386, -192, -193, 389, -194, -195, 454, 423, 408,
		401, 398, 397, -196, -197, 400, -198, -199, 405, 404, -200, -201,
		407, -202, -203, 416, 413, 412, -204, -205, 415, -206, -207, 420,
		419, -208, -209, 422, -210, -211, 439, 432, 429, 428, -212, -213,
		431, -214, -215, 436, 435, -216, -217, 438, -218, -219, 447, 444,
		443, -220, -221, 446, -222, -223, 451, 450, -224, -225, 453, -226,
		-227, 486, 471, 464, 461, 460, -228, -229, 463, -230, -231, 468,
		467, -232, -233, 470, -234, -235, 479, 476, 475, -236, -237, 478,
		-238, -239, 483, 482, -240, -241, 485, -242, -243, 499, 495, 492,
		491, -244, -245, 494, -246, -247, 497, -248, 502, -249, 506, 503,
		-250, -251, 505, -252, -253, 508, -254, 510, -255, -256, 0, };

public static String textUnpack(byte packedData[], int off, int size) {
	if (size == 0)
		return "";
	int offset = 1;
	int length = packedData[0] & 0xff;
	try {
		int charsDecoded = 0;
		int keyIndex = 0;
		StringBuilder sb = new StringBuilder();
		for (;;) {
			byte textByte = packedData[offset++];
			if (textByte >= 0) {
				keyIndex++;
			} else {
				keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
			}
			int charId;
			if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
				sb.append((char) (byte) (charId ^ 0xffffffff));
				if (length <= ++charsDecoded) {
					break;
				}
				keyIndex = 0;
			}
			if ((textByte & 0x40) != 0) {
				keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
			} else {
				keyIndex++;
			}
			if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
				sb.append((char) (byte) (charId ^ 0xffffffff));
				if (++charsDecoded >= length) {
					break;
				}
				keyIndex = 0;
			}
			if ((0x20 & textByte) == 0) {
				keyIndex++;
			} else {
				keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
			}
			if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
				sb.append((char) (byte) (charId ^ 0xffffffff));
				if (length <= ++charsDecoded) {
					break;
				}
				keyIndex = 0;
			}
			if ((0x10 & textByte) == 0) {
				keyIndex++;
			} else {
				keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
			}
			if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
				sb.append((char) (byte) (charId ^ 0xffffffff));
				if (length <= ++charsDecoded) {
					break;
				}

				keyIndex = 0;
			}
			if ((0x8 & textByte) != 0) {
				keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
			} else {
				keyIndex++;
			}
			if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
				sb.append((char) (byte) (charId ^ 0xffffffff));
				if (++charsDecoded >= length) {
					break;
				}
				keyIndex = 0;
			}
			if ((0x4 & textByte) == 0) {
				keyIndex++;
			} else {
				keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
			}
			if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
				sb.append((char) (byte) (charId ^ 0xffffffff));
				if (length <= ++charsDecoded) {
					break;
				}
				keyIndex = 0;
			}
			if ((textByte & 0x2) != 0) {
				keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
			} else {
				keyIndex++;
			}
			if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
				sb.append((char) (byte) (charId ^ 0xffffffff));
				if (length <= ++charsDecoded) {
					break;
				}
				keyIndex = 0;
			}
			if ((textByte & 0x1) != 0) {
				keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
			} else {
				keyIndex++;
			}
			if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
				sb.append((char) (byte) (charId ^ 0xffffffff));
				if (++charsDecoded >= length) {
					break;
				}
				keyIndex = 0;
			}
		}
		return sb.toString();
	} catch (Exception e) {
	}
	return "Error";
}

/**
 * Unpacks text.
 * 
 * @param packedData
 *            The packet text.
 * @param size
 *            The length.
 * @return The string.
 */
public static String textUnpack(byte packedData[], int size) {
	if (size == 0)
		return "";
	int offset = 1;
	int length = packedData[0] & 0xff;
	try {
		int charsDecoded = 0;
		int keyIndex = 0;
		StringBuilder sb = new StringBuilder();
		for (;;) {
			byte textByte = packedData[offset++];
			if (textByte >= 0) {
				keyIndex++;
			} else {
				keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
			}
			int charId;
			if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
				sb.append((char) (byte) (charId ^ 0xffffffff));
				if (length <= ++charsDecoded) {
					break;
				}
				keyIndex = 0;
			}
			if ((textByte & 0x40) != 0) {
				keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
			} else {
				keyIndex++;
			}
			if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
				sb.append((char) (byte) (charId ^ 0xffffffff));
				if (++charsDecoded >= length) {
					break;
				}
				keyIndex = 0;
			}
			if ((0x20 & textByte) == 0) {
				keyIndex++;
			} else {
				keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
			}
			if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
				sb.append((char) (byte) (charId ^ 0xffffffff));
				if (length <= ++charsDecoded) {
					break;
				}
				keyIndex = 0;
			}
			if ((0x10 & textByte) == 0) {
				keyIndex++;
			} else {
				keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
			}
			if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
				sb.append((char) (byte) (charId ^ 0xffffffff));
				if (length <= ++charsDecoded) {
					break;
				}

				keyIndex = 0;
			}
			if ((0x8 & textByte) != 0) {
				keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
			} else {
				keyIndex++;
			}
			if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
				sb.append((char) (byte) (charId ^ 0xffffffff));
				if (++charsDecoded >= length) {
					break;
				}
				keyIndex = 0;
			}
			if ((0x4 & textByte) == 0) {
				keyIndex++;
			} else {
				keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
			}
			if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
				sb.append((char) (byte) (charId ^ 0xffffffff));
				if (length <= ++charsDecoded) {
					break;
				}
				keyIndex = 0;
			}
			if ((textByte & 0x2) != 0) {
				keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
			} else {
				keyIndex++;
			}
			if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
				sb.append((char) (byte) (charId ^ 0xffffffff));
				if (length <= ++charsDecoded) {
					break;
				}
				keyIndex = 0;
			}
			if ((textByte & 0x1) != 0) {
				keyIndex = CHAT_DECRYPT_KEYS[keyIndex];
			} else {
				keyIndex++;
			}
			if ((charId = CHAT_DECRYPT_KEYS[keyIndex]) < 0) {
				sb.append((char) (byte) (charId ^ 0xffffffff));
				if (++charsDecoded >= length) {
					break;
				}
				keyIndex = 0;
			}
		}
		return sb.toString();
	} catch (Exception e) {
	}
	return "Error";
}

public static void textPack(byte packedData[], int bit, int offset,
		String text) {
	if (text.length() > 80) {
		text = text.substring(0, 80);
	}
	int length = text.length();
	int key = 0;
	int bitPosition = bit << 3;
	int srcOffset = offset;
	for (; length > srcOffset; srcOffset++) {
		int textByte = 0xff & text.getBytes()[srcOffset];
		int mask = CHAT_MASKS[textByte];
		int size = CHAT_BIT_SIZES[textByte];
		if (size == 0)
			return;
		int destOffset = bitPosition >> 3;
		int bitOffset = bitPosition & 0x7;
		key &= -bitOffset >> 31;
		bitPosition += size;
		int byteSize = (((bitOffset + size) - 1) >> 3) + destOffset;
		bitOffset += 24;
		packedData[destOffset] = (byte) (key = (key | (mask >>> bitOffset)));
		if (byteSize < destOffset) {
			destOffset++;
			bitOffset -= 8;
			packedData[destOffset] = (byte) (key = mask >>> bitOffset);
			if (byteSize > destOffset) {
				destOffset++;
				bitOffset -= 8;
				packedData[destOffset] = (byte) (key = mask >>> bitOffset);
				if (byteSize > destOffset) {
					bitOffset -= 8;
					destOffset++;
					packedData[destOffset] = (byte) (key = mask >>> bitOffset);
					if (destOffset < byteSize) {
						bitOffset -= 8;
						destOffset++;
						packedData[destOffset] = (byte) (key = mask << -bitOffset);
					}
				}
			}
		}
	}
}

public static void textPack(byte packedData[], String text) {
	if (text.length() > 80) {
		text = text.substring(0, 80);
	}
	int length = text.length();
	int key = 0;
	int bitPosition = 1 << 3;
	int srcOffset = 0;
	packedData[0] = (byte) length;
	for (; length > srcOffset; srcOffset++) {
		int textByte = 0xff & text.getBytes()[srcOffset];
		int mask = CHAT_MASKS[textByte];
		int size = CHAT_BIT_SIZES[textByte];
		int destOffset = bitPosition >> 3;
		int bitOffset = bitPosition & 0x7;
		key &= (-bitOffset >> 31);
		bitPosition += size;
		int byteSize = (((bitOffset + size) - 1) >> 3) + destOffset;
		bitOffset += 24;
		packedData[destOffset] = (byte) (key = (key | (mask >>> bitOffset)));
		if (byteSize > destOffset) {
			destOffset++;
			bitOffset -= 8;
			packedData[destOffset] = (byte) (key = mask >>> bitOffset);
			if (byteSize > destOffset) {
				destOffset++;
				bitOffset -= 8;
				packedData[destOffset] = (byte) (key = mask >>> bitOffset);
				if (byteSize > destOffset) {
					bitOffset -= 8;
					destOffset++;
					packedData[destOffset] = (byte) (key = mask >>> bitOffset);
					if (destOffset < byteSize) {
						bitOffset -= 8;
						destOffset++;
						packedData[destOffset] = (byte) (key = mask << -bitOffset);
					}
				}
			}
		}
	}
}
}
