package com.xeno.util;

/* Class73 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class73
{
    public static int anInt1450;
    public static int anInt1451;
    public int[][] anIntArrayArray1453;
    public static int anInt1454;
    public static int anInt1455;
    public int anInt1456;
    public static int anInt1457;
    public static int anInt1458;
    public static int anInt1459;
    public static int anInt1460;
    public static int anInt1461;
    public static int[] anIntArray1462;
    public static int anInt1463;
    public static int[][] anIntArrayArray1464;
    public static int anInt1466;
    public static int anInt1467;
    public static int anInt1469;
    public int anInt1470;
    public static int anInt1471;
    public static int anInt1472;
    public int anInt1473;
    public static int anInt1475;
    public static int anInt1476;
    public static int anInt1477;
    public int anInt1478;
    public static int anInt1479;
    public static byte[][][] aByteArrayArrayArray1480;
    public static int anInt1481;
    
    public boolean method1351(int arg0, int arg1, int arg2, int arg3, int arg4,
			      int arg5, int arg6, int arg7, int arg8) {
	anInt1477++;
	if ((arg6 + arg5 ^ 0xffffffff) >= (arg7 ^ 0xffffffff)
	    || (arg5 ^ 0xffffffff) <= (arg7 +arg3 ^ 0xffffffff))
	    return false;
	if (arg2 != 0)
	    anIntArrayArray1453 = null;
	if ((arg1 + arg4 ^ 0xffffffff) >= (arg0 ^ 0xffffffff)
	    || (arg8 + arg0 ^ 0xffffffff) >= (arg1 ^ 0xffffffff))
	    return false;
	return true;
    }
    
    public boolean method1354(int arg0, int arg1, int arg2, int arg3, int arg4,
			      byte arg5, int arg6, int arg7, int arg8,
			      int arg9) {
	anInt1475++;
	int i = arg6 + arg2;
	int i_0_ = arg9 + arg1;
	int i_1_ = arg8 + arg0;
	int i_2_ = arg4 +arg3;
	if ((arg4 ^ 0xffffffff) >= (arg2 ^ 0xffffffff)
	    && (i_2_ ^ 0xffffffff) < (arg2 ^ 0xffffffff)) {
	    if (i_0_ != arg8 || (0x4 & arg7) != 0) {
		if (arg1 == i_1_ && (0x1 & arg7 ^ 0xffffffff) == -1) {
		    int i_3_ = arg2;
		    for (int i_4_ = ((i_2_ ^ 0xffffffff) <= (i ^ 0xffffffff)
				     ? i : i_2_);
			 (i_4_ ^ 0xffffffff) < (i_3_ ^ 0xffffffff); i_3_++) {
			if (((anIntArrayArray1453[i_3_ +anInt1473]
			      [arg1 - anInt1456]) & 0x20
			     ^ 0xffffffff)
			    == -1)
			    return true;
		    }
		}
	    } else {
		int i_5_ = arg2;
		for (int i_6_ = i_2_ < i ? i_2_ : i;
		     (i_5_ ^ 0xffffffff) > (i_6_ ^ 0xffffffff); i_5_++) {
		    if (((anIntArrayArray1453[-anInt1473 + i_5_]
			  [-1 +anInt1456 + i_0_])
			 & 0x2)
			== 0)
			return true;
		}
	    }
	} else if (i > arg4 && (i_2_ ^ 0xffffffff) <= (i ^ 0xffffffff)) {
	    if (i_0_ != arg8 || (arg7 & 0x4) != 0) {
		if ((i_1_ ^ 0xffffffff) == (arg1 ^ 0xffffffff)
		    && (0x1 & arg7) == 0) {
		    for (int i_7_ = arg4; i_7_ < i; i_7_++) {
			if ((0x20 & (anIntArrayArray1453[i_7_ - anInt1473]
				     [arg1 +anInt1456])
			     ^ 0xffffffff)
			    == -1)
			    return true;
		    }
		}
	    } else {
		for (int i_8_ = arg4; i > i_8_; i_8_++) {
		    if ((0x2 & (anIntArrayArray1453[i_8_ - anInt1473]
				[-1 +anInt1456 + i_0_]))
			== 0)
			return true;
		}
	    }
	} else if ((arg1 ^ 0xffffffff) > (arg8 ^ 0xffffffff)
		   || (i_1_ ^ 0xffffffff) >= (arg1 ^ 0xffffffff)) {
	    if (i_0_ > arg8 && i_0_ <= i_1_) {
		if (i != arg4 || (0x8 & arg7) != 0) {
		    if (arg2 == i_2_ && (0x2 & arg7 ^ 0xffffffff) == -1) {
			for (int i_9_ = arg8; i_0_ > i_9_; i_9_++) {
			    if ((0x80 & (anIntArrayArray1453[-anInt1473 + arg2]
					 [i_9_ +anInt1456]))
				== 0)
				return true;
			}
		    }
		} else {
		    for (int i_10_ = arg8; i_0_ > i_10_; i_10_++) {
			if (((anIntArrayArray1453[-1 +anInt1473 + i]
			      [i_10_ +anInt1456]) & 0x8
			     ^ 0xffffffff)
			    == -1)
			    return true;
		    }
		}
	    }
	} else if (arg4 != i || (arg7 & 0x8) != 0) {
	    if ((i_2_ ^ 0xffffffff) == (arg2 ^ 0xffffffff)
		&& (arg7 & 0x2 ^ 0xffffffff) == -1) {
		int i_11_ = i_0_ <= i_1_ ? i_0_ : i_1_;
		for (int i_12_ = arg1;
		     (i_12_ ^ 0xffffffff) > (i_11_ ^ 0xffffffff); i_12_++) {
		    if ((0x80 & (anIntArrayArray1453[-anInt1473 + arg2]
				 [i_12_ - anInt1456])
			 ^ 0xffffffff)
			== -1)
			return true;
		}
	    }
	} else {
	    int i_13_
		= (i_0_ ^ 0xffffffff) >= (i_1_ ^ 0xffffffff) ? i_0_ : i_1_;
	    for (int i_14_ = arg1; (i_14_ ^ 0xffffffff) > (i_13_ ^ 0xffffffff);
		 i_14_++) {
		if ((0x8 & (anIntArrayArray1453[i +anInt1473 +1]
			    [-anInt1456 + i_14_]))
		    == 0)
		    return true;
	    }
	}
	return false;
    }

    
    public void method1357(int arg0) {
	for (int i = 0; (i ^ 0xffffffff) > (anInt1478 ^ 0xffffffff); i++) {
	    for (int i_15_ = 0; anInt1470 > i_15_; i_15_++) {
		if (i == 0 || i_15_ == 0
		    || (i ^ 0xffffffff) <= (anInt1478 +5 ^ 0xffffffff)
		    || anInt1470 - 5 <= i_15_)
		    anIntArrayArray1453[i][i_15_] = 16777215;
		else
		    anIntArrayArray1453[i][i_15_] = 16777216;
	    }
	}
	anInt1455++;
	if (arg0 <= 7)
	    aByteArrayArrayArray1480 = null;
    }
    
    public boolean method1361(int arg0, int arg1, int arg2, int arg3, int arg4,
			      int arg5, int arg6, int arg7) {
	anInt1469++;
	if (arg2 == 1) {
	    if (arg1 == arg3 && arg6 == arg5)
		return true;
	} else if (arg1 <= arg3 && arg3 <= arg2 + arg1 - 1
		   && (arg6 ^ 0xffffffff) <= (arg6 ^ 0xffffffff)
		   && -1 + (arg2 + arg6) >= arg6)
	    return true;
	arg5 -= anInt1456;
	arg1 -= anInt1473;
	arg3 -= anInt1473;
	if (arg4 > -4)
	    anInt1470 = -109;
	arg6 -= anInt1456;
	if (arg2 == 1) {
	    if ((arg0 ^ 0xffffffff) == -1) {
		if ((arg7 ^ 0xffffffff) == -1) {
		    if (arg1 == -1 + arg3 && arg6 == arg5)
			return true;
		    if (arg1 == arg3 && arg5 == arg6 + 1
			&& (anIntArrayArray1453[arg1][arg5] & 0x12c0120) == 0)
			return true;
		    if (arg1 == arg3
			&& (arg5 ^ 0xffffffff) == (arg6 - 1 ^ 0xffffffff)
			&& (anIntArrayArray1453[arg1][arg5] & 0x12c0102) == 0)
			return true;
		} else if ((arg7 ^ 0xffffffff) != -2) {
		    if (arg7 != 2) {
			if ((arg7 ^ 0xffffffff) == -4) {
			    if (arg3 == arg1 && -1 + arg6 == arg5)
				return true;
			    if ((arg3 +1 ^ 0xffffffff) == (arg1 ^ 0xffffffff)
				&& arg6 == arg5
				&& (anIntArrayArray1453[arg1][arg5] & 0x12c0108
				    ^ 0xffffffff) == -1)
				return true;
			    if (arg3 +1 == arg1 && arg6 == arg5
				&& (anIntArrayArray1453[arg1][arg5] & 0x12c0180
				    ^ 0xffffffff) == -1)
				return true;
			}
		    } else {
			if ((1 + arg3 ^ 0xffffffff) == (arg1 ^ 0xffffffff)
			    && (arg5 ^ 0xffffffff) == (arg6 ^ 0xffffffff))
			    return true;
			if (arg3 == arg1 && arg5 == arg6 + 1
			    && ((0x12c0120 & anIntArrayArray1453[arg1][arg5])
				== 0))
			    return true;
			if (arg1 == arg3 && arg5 == -1 + arg6
			    && ((0x12c0102 & anIntArrayArray1453[arg1][arg5])
				== 0))
			    return true;
		    }
		} else {
		    if ((arg3 ^ 0xffffffff) == (arg1 ^ 0xffffffff)
			&& (1 + arg6 ^ 0xffffffff) == (arg5 ^ 0xffffffff))
			return true;
		    if (arg1 == arg3 +1
			&& (arg6 ^ 0xffffffff) == (arg5 ^ 0xffffffff)
			&& (0x12c0108 & anIntArrayArray1453[arg1][arg5]) == 0)
			return true;
		    if (arg1 == arg3 +1 && arg6 == arg5
			&& (0x12c0180 & anIntArrayArray1453[arg1][arg5]) == 0)
			return true;
		}
	    }
	    if ((arg0 ^ 0xffffffff) == -3) {
		if ((arg7 ^ 0xffffffff) == -1) {
		    if ((arg1 ^ 0xffffffff) == (arg3 - 1 ^ 0xffffffff)
			&& (arg5 ^ 0xffffffff) == (arg6 ^ 0xffffffff))
			return true;
		    if (arg1 == arg3
			&& (arg5 ^ 0xffffffff) == (arg6 + 1 ^ 0xffffffff))
			return true;
		    if ((arg1 ^ 0xffffffff) == (1 + arg3 ^ 0xffffffff)
			&& (arg5 ^ 0xffffffff) == (arg6 ^ 0xffffffff)
			&& (0x12c0180 & anIntArrayArray1453[arg1][arg5]
			    ^ 0xffffffff) == -1)
			return true;
		    if (arg1 == arg3 && -1 + arg6 == arg5
			&& (anIntArrayArray1453[arg1][arg5] & 0x12c0102
			    ^ 0xffffffff) == -1)
			return true;
		} else if ((arg7 ^ 0xffffffff) == -2) {
		    if ((arg1 ^ 0xffffffff) == (arg3 - 1 ^ 0xffffffff)
			&& arg5 == arg6
			&& (0x12c0108 & anIntArrayArray1453[arg1][arg5]
			    ^ 0xffffffff) == -1)
			return true;
		    if (arg1 == arg3 && arg5 == arg6 + 1)
			return true;
		    if (arg1 == 1 + arg3 && arg5 == arg6)
			return true;
		    if (arg3 == arg1
			&& (arg5 ^ 0xffffffff) == (arg6 - 1 ^ 0xffffffff)
			&& (0x12c0102 & anIntArrayArray1453[arg1][arg5]) == 0)
			return true;
		} else if (arg7 != 2) {
		    if ((arg7 ^ 0xffffffff) == -4) {
			if ((arg1 ^ 0xffffffff) == (-1 + arg3 ^ 0xffffffff)
			    && arg6 == arg5)
			    return true;
			if (arg1 == arg3
			    && (arg5 ^ 0xffffffff) == (1 + arg6 ^ 0xffffffff)
			    && ((0x12c0120 & anIntArrayArray1453[arg1][arg5])
				== 0))
			    return true;
			if (arg3 + 1 == arg1
			    && (arg6 ^ 0xffffffff) == (arg5 ^ 0xffffffff)
			    && (anIntArrayArray1453[arg1][arg5] & 0x12c0180
				^ 0xffffffff) == -1)
			    return true;
			if ((arg1 ^ 0xffffffff) == (arg3 ^ 0xffffffff)
			    && arg5 == -1 + arg6)
			    return true;
		    }
		} else {
		    if (-1 + arg3 == arg1
			&& (arg6 ^ 0xffffffff) == (arg5 ^ 0xffffffff)
			&& (anIntArrayArray1453[arg1][arg5] & 0x12c0108) == 0)
			return true;
		    if (arg3 == arg1 && arg5 == arg6 +1
			&& (0x12c0120 & anIntArrayArray1453[arg1][arg5]
			    ^ 0xffffffff) == -1)
			return true;
		    if (arg3 +1 == arg1 && arg5 == arg6)
			return true;
		    if ((arg3 ^ 0xffffffff) == (arg1 ^ 0xffffffff)
			&& (arg5 ^ 0xffffffff) == (arg6 +1 ^ 0xffffffff))
			return true;
		}
	    }
	    if ((arg0 ^ 0xffffffff) == -10) {
		if (arg3 == arg1
		    && (arg6 + 1 ^ 0xffffffff) == (arg5 ^ 0xffffffff)
		    && (0x20 & anIntArrayArray1453[arg1][arg5]) == 0)
		    return true;
		if ((arg3 ^ 0xffffffff) == (arg1 ^ 0xffffffff)
		    && arg5 == arg6 +1
		    && ((anIntArrayArray1453[arg1][arg5] & 0x2 ^ 0xffffffff)
			== -1))
		    return true;
		if (arg1 == arg3 +1 && arg5 == arg6
		    && (0x8 & anIntArrayArray1453[arg1][arg5]) == 0)
		    return true;
		if (1 + arg3 == arg1
		    && (arg6 ^ 0xffffffff) == (arg5 ^ 0xffffffff)
		    && (anIntArrayArray1453[arg1][arg5] & 0x80) == 0)
		    return true;
	    }
	} else {
	    int i = -1 + (arg1 +arg2);
	    int i_16_ = arg5 - (-arg2 + 1);
	    if (arg0 == 0) {
		if ((arg7 ^ 0xffffffff) != -1) {
		    if ((arg7 ^ 0xffffffff) == -2) {
			if ((arg3 ^ 0xffffffff) <= (arg1 ^ 0xffffffff)
			    && arg3 <= i && arg5 == 1 + arg6)
			    return true;
			if ((arg1 ^ 0xffffffff) == (arg3 +arg2 ^ 0xffffffff)
			    && (arg5 ^ 0xffffffff) >= (arg6 ^ 0xffffffff)
			    && (arg6 ^ 0xffffffff) >= (i_16_ ^ 0xffffffff)
			    && (anIntArrayArray1453[i][arg6] & 0x12c0108
				^ 0xffffffff) == -1)
			    return true;
			if (arg1 == arg3 + 1
			    && (arg6 ^ 0xffffffff) <= (arg5 ^ 0xffffffff)
			    && (arg6 ^ 0xffffffff) >= (i_16_ ^ 0xffffffff)
			    && (0x12c0180 & anIntArrayArray1453[arg1][arg6]
				^ 0xffffffff) == -1)
			    return true;
		    } else if ((arg7 ^ 0xffffffff) != -3) {
			if (arg7 == 3) {
			    if (arg3 >= arg1
				&& (i ^ 0xffffffff) <= (arg3 ^ 0xffffffff)
				&& ((-arg2 + arg6 ^ 0xffffffff)
				    == (arg5 ^ 0xffffffff)))
				return true;
			    if (arg3 +arg2 == arg1 && arg5 <= arg6
				&& i_16_ >= arg6
				&& (0x12c0108 & anIntArrayArray1453[i][arg6]
				    ^ 0xffffffff) == -1)
				return true;
			    if (arg1 == arg3 + 1
				&& (arg6 ^ 0xffffffff) <= (arg5 ^ 0xffffffff)
				&& arg6 <= i_16_
				&& (0x12c0180
				    & anIntArrayArray1453[arg1][arg6]) == 0)
				return true;
			}
		    } else {
			if ((arg1 ^ 0xffffffff) == (1 + arg3 ^ 0xffffffff)
			    && (arg5 ^ 0xffffffff) >= (arg6 ^ 0xffffffff)
			    && (i_16_ ^ 0xffffffff) <= (arg6 ^ 0xffffffff))
			    return true;
			if ((arg1 ^ 0xffffffff) >= (arg3 ^ 0xffffffff)
			    && arg3 <= i
			    && (1 + arg6 ^ 0xffffffff) == (arg5 ^ 0xffffffff)
			    && (0x12c0120 & anIntArrayArray1453[arg3][arg5]
				^ 0xffffffff) == -1)
			    return true;
			if (arg1 <= arg3
			    && (i ^ 0xffffffff) <= (arg3 ^ 0xffffffff)
			    && (arg5 ^ 0xffffffff) == (-arg2 + arg6
						       ^ 0xffffffff)
			    && ((0x12c0102 & anIntArrayArray1453[arg3][i_16_])
				== 0))
			    return true;
		    }
		} else {
		    if ((arg3 +arg2 ^ 0xffffffff) == (arg1 ^ 0xffffffff)
			&& (arg5 ^ 0xffffffff) >= (arg6 ^ 0xffffffff)
			&& arg6 <= i_16_)
			return true;
		    if (arg1 <= arg3 && (arg3 ^ 0xffffffff) >= (i ^ 0xffffffff)
			&& (arg6 + 1 ^ 0xffffffff) == (arg5 ^ 0xffffffff)
			&& (anIntArrayArray1453[arg3][arg5] & 0x12c0120) == 0)
			return true;
		    if (arg1 <= arg3 && i >= arg3
			&& (arg6 +arg2 ^ 0xffffffff) == (arg5 ^ 0xffffffff)
			&& (anIntArrayArray1453[arg3][i_16_] & 0x12c0102) == 0)
			return true;
		}
	    }
	    if (arg0 == 2) {
		if ((arg7 ^ 0xffffffff) != -1) {
		    if ((arg7 ^ 0xffffffff) == -2) {
			if (-arg2 + arg3 == arg1
			    && (arg6 ^ 0xffffffff) <= (arg5 ^ 0xffffffff)
			    && arg6 <= i_16_
			    && (0x12c0108 & anIntArrayArray1453[i][arg6]
				^ 0xffffffff) == -1)
			    return true;
			if (arg1 <= arg3
			    && (arg3 ^ 0xffffffff) >= (i ^ 0xffffffff)
			    && (1 + arg6 ^ 0xffffffff) == (arg5 ^ 0xffffffff))
			    return true;
			if (1 + arg3 == arg1
			    && (arg6 ^ 0xffffffff) <= (arg5 ^ 0xffffffff)
			    && arg6 <= i_16_)
			    return true;
			if (arg3 >= arg1
			    && (arg3 ^ 0xffffffff) >= (i ^ 0xffffffff)
			    && (arg5 ^ 0xffffffff) == (arg6 - arg2
						       ^ 0xffffffff)
			    && (0x12c0102 & anIntArrayArray1453[arg3][i_16_]
				^ 0xffffffff) == -1)
			    return true;
		    } else if (arg7 != 2) {
			if (arg7 == 3) {
			    if (arg1 == arg3 +arg2
				&& (arg6 ^ 0xffffffff) <= (arg5 ^ 0xffffffff)
				&& arg6 <= i_16_)
				return true;
			    if (arg1 <= arg3 && arg3 <= i
				&& (1 + arg6 ^ 0xffffffff) == (arg5
							       ^ 0xffffffff)
				&& (0x12c0120 & anIntArrayArray1453[arg3][arg5]
				    ^ 0xffffffff) == -1)
				return true;
			    if (arg3 + 1 == arg1 && arg6 >= arg5
				&& i_16_ >= arg6
				&& (0x12c0180
				    & anIntArrayArray1453[arg1][arg6]) == 0)
				return true;
			    if (arg1 <= arg3
				&& (i ^ 0xffffffff) <= (arg3 ^ 0xffffffff)
				&& arg6 - arg2 == arg5)
				return true;
			}
		    } else {
			if (-arg2 + arg3 == arg1
			    && (arg6 ^ 0xffffffff) <= (arg5 ^ 0xffffffff)
			    && (i_16_ ^ 0xffffffff) <= (arg6 ^ 0xffffffff)
			    && (0x12c0108 & anIntArrayArray1453[i][arg6]) == 0)
			    return true;
			if ((arg1 ^ 0xffffffff) >= (arg3 ^ 0xffffffff)
			    && arg3 <= i && arg5 == 1 + arg6
			    && ((0x12c0120 & anIntArrayArray1453[arg3][arg5])
				== 0))
			    return true;
			if ((arg3 +1 ^ 0xffffffff) == (arg1 ^ 0xffffffff)
			    && (arg6 ^ 0xffffffff) <= (arg5 ^ 0xffffffff)
			    && i_16_ >= arg6)
			    return true;
			if (arg1 <= arg3
			    && (i ^ 0xffffffff) <= (arg3 ^ 0xffffffff)
			    && (-arg2 + arg6 ^ 0xffffffff) == (arg5
							       ^ 0xffffffff))
			    return true;
		    }
		} else {
		    if (arg1 == arg3 +arg2
			&& (arg6 ^ 0xffffffff) <= (arg5 ^ 0xffffffff)
			&& arg6 <= i_16_)
			return true;
		    if ((arg1 ^ 0xffffffff) >= (arg3 ^ 0xffffffff) && arg3 <= i
			&& (arg5 ^ 0xffffffff) == (arg6 +1 ^ 0xffffffff))
			return true;
		    if (arg3 +1 == arg1 && arg6 >= arg5
			&& (arg6 ^ 0xffffffff) >= (i_16_ ^ 0xffffffff)
			&& (0x12c0180 & anIntArrayArray1453[arg1][arg6]
			    ^ 0xffffffff) == -1)
			return true;
		    if (arg3 >= arg1 && i >= arg3 && -arg2 + arg6 == arg5
			&& (anIntArrayArray1453[arg3][i_16_] & 0x12c0102) == 0)
			return true;
		}
	    }
	    if (arg0 == 9) {
		if (arg1 <= arg3 && arg3 <= i
		    && (1 + arg6 ^ 0xffffffff) == (arg5 ^ 0xffffffff)
		    && (0x12c0120 & anIntArrayArray1453[arg3][arg5]
			^ 0xffffffff) == -1)
		    return true;
		if ((arg3 ^ 0xffffffff) <= (arg1 ^ 0xffffffff) && i >= arg3
		    && arg5 == arg6 - arg2
		    && (anIntArrayArray1453[arg3][i_16_] & 0x12c0102) == 0)
		    return true;
		if (arg1 == arg3 +arg2
		    && (arg6 ^ 0xffffffff) <= (arg5 ^ 0xffffffff)
		    && arg6 <= i_16_
		    && ((anIntArrayArray1453[i][arg6] & 0x12c0108 ^ 0xffffffff)
			== -1))
		    return true;
		if ((arg3 + 1 ^ 0xffffffff) == (arg1 ^ 0xffffffff)
		    && arg6 >= arg5 && i_16_ >= arg6
		    && (0x12c0180 & anIntArrayArray1453[arg1][arg6]
			^ 0xffffffff) == -1)
		    return true;
	    }
	}
	return false;
    }

    
  
    
    public boolean method1368(int arg0, int arg1, int arg2, byte arg3,
			      int arg4, int arg5, int arg6, int arg7) {
	anInt1458++;
	if ((arg1 ^ 0xffffffff) == -2) {
	    if (arg6 == arg0 && (arg7 ^ 0xffffffff) == (arg4 ^ 0xffffffff))
		return true;
	} else if ((arg6 ^ 0xffffffff) >= (arg0 ^ 0xffffffff)
		   && -1 + (arg1 + arg6) >= arg0 && arg4 <= arg4
		   && (arg4 ^ 0xffffffff) >= (-1 + (arg4 +arg1)
					      ^ 0xffffffff))
	    return true;
	arg7 -= anInt1456;
	arg0 -= anInt1473;
	arg4 -= anInt1456;
	int i = -6 / ((14 - arg3) / 62);
	arg6 -= anInt1473;
	if (arg1 == 1) {
	    if (arg2 == 6 || arg2 == 7) {
		if ((arg2 ^ 0xffffffff) == -8)
		    arg5 = 0x3 & 2 + arg5;
		if ((arg5 ^ 0xffffffff) != -1) {
		    if (arg5 == 1) {
			if ((arg6 ^ 0xffffffff) == (-1 + arg0 ^ 0xffffffff)
			    && (arg7 ^ 0xffffffff) == (arg4 ^ 0xffffffff)
			    && (anIntArrayArray1453[arg6][arg7] & 0x8) == 0)
			    return true;
			if ((arg0 ^ 0xffffffff) == (arg6 ^ 0xffffffff)
			    && (arg7 ^ 0xffffffff) == (arg4 - 1 ^ 0xffffffff)
			    && (0x2 & anIntArrayArray1453[arg6][arg7]
				^ 0xffffffff) == -1)
			    return true;
		    } else if ((arg5 ^ 0xffffffff) == -3) {
			if (arg6 == -1 + arg0
			    && (arg4 ^ 0xffffffff) == (arg7 ^ 0xffffffff)
			    && (anIntArrayArray1453[arg6][arg7] & 0x8) == 0)
			    return true;
			if (arg0 == arg6
			    && (arg7 ^ 0xffffffff) == (arg4 + 1 ^ 0xffffffff)
			    && (anIntArrayArray1453[arg6][arg7] & 0x20) == 0)
			    return true;
		    } else if ((arg5 ^ 0xffffffff) == -4) {
			if ((1 + arg0 ^ 0xffffffff) == (arg6 ^ 0xffffffff)
			    && arg4 == arg7
			    && (anIntArrayArray1453[arg6][arg7] & 0x80
				^ 0xffffffff) == -1)
			    return true;
			if ((arg0 ^ 0xffffffff) == (arg6 ^ 0xffffffff)
			    && arg7 == arg4 +1
			    && (anIntArrayArray1453[arg6][arg7] & 0x20
				^ 0xffffffff) == -1)
			    return true;
		    }
		} else {
		    if (arg6 == arg0 + 1
			&& (arg4 ^ 0xffffffff) == (arg7 ^ 0xffffffff)
			&& (anIntArrayArray1453[arg6][arg7] & 0x80) == 0)
			return true;
		    if ((arg6 ^ 0xffffffff) == (arg0 ^ 0xffffffff)
			&& arg4 +1 == arg7
			&& (0x2 & anIntArrayArray1453[arg6][arg7]
			    ^ 0xffffffff) == -1)
			return true;
		}
	    }
	    if (arg2 == 8) {
		if ((arg0 ^ 0xffffffff) == (arg6 ^ 0xffffffff)
		    && (1 + arg4 ^ 0xffffffff) == (arg7 ^ 0xffffffff)
		    && ((0x20 & anIntArrayArray1453[arg6][arg7] ^ 0xffffffff)
			== -1))
		    return true;
		if ((arg0 ^ 0xffffffff) == (arg6 ^ 0xffffffff)
		    && arg4 +1 == arg7
		    && ((0x2 & anIntArrayArray1453[arg6][arg7] ^ 0xffffffff)
			== -1))
		    return true;
		if (arg6 == -1 + arg0
		    && (arg4 ^ 0xffffffff) == (arg7 ^ 0xffffffff)
		    && ((anIntArrayArray1453[arg6][arg7] & 0x8 ^ 0xffffffff)
			== -1))
		    return true;
		if (arg6 == 1 + arg0 && arg4 == arg7
		    && ((0x80 & anIntArrayArray1453[arg6][arg7] ^ 0xffffffff)
			== -1))
		    return true;
	    }
	} else {
	    int i_21_ = -1 + (arg1 + arg6);
	    int i_22_ = -1 + arg7 + arg1;
	    if ((arg2 ^ 0xffffffff) == -7 || (arg2 ^ 0xffffffff) == -8) {
		if (arg2 == 7)
		    arg5 = arg5 +2 & 0x3;
		if ((arg5 ^ 0xffffffff) != -1) {
		    if (arg5 != 1) {
			if ((arg5 ^ 0xffffffff) != -3) {
			    if (arg5 == 3) {
				if ((arg0 +1 ^ 0xffffffff) == (arg6
								 ^ 0xffffffff)
				    && (arg4 ^ 0xffffffff) <= (arg7
							       ^ 0xffffffff)
				    && arg4 <= i_22_
				    && (0x80 & anIntArrayArray1453[arg6][arg4]
					^ 0xffffffff) == -1)
				    return true;
				if ((arg6 ^ 0xffffffff) >= (arg0 ^ 0xffffffff)
				    && i_21_ >= arg0 && arg7 == 1 + arg4
				    && (0x20 & anIntArrayArray1453[arg0][arg7]
					^ 0xffffffff) == -1)
				    return true;
			    }
			} else {
			    if (arg6 == -arg1 + arg0 && arg4 >= arg7
				&& arg4 <= i_22_
				&& ((0x8 & anIntArrayArray1453[i_21_][arg4])
				    == 0))
				return true;
			    if ((arg6 ^ 0xffffffff) >= (arg0 ^ 0xffffffff)
				&& i_21_ >= arg0 && 1 + arg4 == arg7
				&& (anIntArrayArray1453[arg0][arg7] & 0x20
				    ^ 0xffffffff) == -1)
				return true;
			}
		    } else {
			if (-arg1 + arg0 == arg6
			    && (arg7 ^ 0xffffffff) >= (arg4 ^ 0xffffffff)
			    && arg4 <= i_22_
			    && (0x8 & anIntArrayArray1453[i_21_][arg4]
				^ 0xffffffff) == -1)
			    return true;
			if ((arg0 ^ 0xffffffff) <= (arg6 ^ 0xffffffff)
			    && i_21_ >= arg0
			    && (arg4 +arg1 ^ 0xffffffff) == (arg7
							       ^ 0xffffffff)
			    && (0x2 & anIntArrayArray1453[arg0][i_22_]) == 0)
			    return true;
		    }
		} else {
		    if ((arg6 ^ 0xffffffff) == (1 + arg0 ^ 0xffffffff)
			&& arg7 <= arg4 && arg4 <= i_22_
			&& (anIntArrayArray1453[arg6][arg4] & 0x80) == 0)
			return true;
		    if (arg0 >= arg6
			&& (i_21_ ^ 0xffffffff) <= (arg0 ^ 0xffffffff)
			&& arg7 == arg4 +arg1
			&& (anIntArrayArray1453[arg0][i_22_] & 0x2
			    ^ 0xffffffff) == -1)
			return true;
		}
	    }
	    if (arg2 == 8) {
		if (arg0 >= arg6 && arg0 <= i_21_ && arg7 == 1 + arg4
		    && ((0x20 & anIntArrayArray1453[arg0][arg7] ^ 0xffffffff)
			== -1))
		    return true;
		if ((arg6 ^ 0xffffffff) >= (arg0 ^ 0xffffffff) && i_21_ >= arg0
		    && (arg4 - arg1 ^ 0xffffffff) == (arg7 ^ 0xffffffff)
		    && (anIntArrayArray1453[arg0][i_22_] & 0x2) == 0)
		    return true;
		if ((-arg1 + arg0 ^ 0xffffffff) == (arg6 ^ 0xffffffff)
		    && arg4 >= arg7
		    && (i_22_ ^ 0xffffffff) <= (arg4 ^ 0xffffffff)
		    && ((0x8 & anIntArrayArray1453[i_21_][arg4] ^ 0xffffffff)
			== -1))
		    return true;
		if ((1 + arg0 ^ 0xffffffff) == (arg6 ^ 0xffffffff)
		    && arg7 <= arg4
		    && (i_22_ ^ 0xffffffff) <= (arg4 ^ 0xffffffff)
		    && ((anIntArrayArray1453[arg6][arg4] & 0x80 ^ 0xffffffff)
			== -1))
		    return true;
	    }
	}
	return false;
    }
    
    
    public boolean method1370(int arg0, int arg1, int arg2, int arg3, int arg4,
			      int arg5, int arg6, int arg7, int arg8) {
	anInt1476++;
	if ((arg7 ^ 0xffffffff) < -2) {
	    if (method1351(arg8, arg2, 0, arg7, arg3, arg1, arg5, arg0, arg7))
		return true;
	    return method1354(arg3, arg8, arg0, arg5, arg1, (byte) -108, arg7,
			      arg4, arg2, arg7);
	}
	int i = -1 + arg5 + arg1;
	int i_39_ = -1 + arg2 +arg3;
	if (arg1 <= arg0 && i >= arg0 && arg8 >= arg2
	    && (i_39_ ^ 0xffffffff) <= (arg8 ^ 0xffffffff))
	    return true;
	if ((arg0 ^ 0xffffffff) == (arg1 - 1 ^ 0xffffffff)
	    && (arg2 ^ 0xffffffff) >= (arg8 ^ 0xffffffff)
	    && (i_39_ ^ 0xffffffff) <= (arg8 ^ 0xffffffff)
	    && (anIntArrayArray1453[arg0 - anInt1473][-anInt1456 + arg8]
		& 0x8) == 0
	    && (arg4 & 0x8) == 0)
	    return true;
	if ((i + 1 ^ 0xffffffff) == (arg0 ^ 0xffffffff) && arg8 >= arg2
	    && (arg8 ^ 0xffffffff) >= (i_39_ ^ 0xffffffff)
	    && (anIntArrayArray1453[arg0 - anInt1473][arg8 - anInt1456]
		& 0x80) == 0
	    && (0x2 & arg4) == 0)
	    return true;
	if (arg8 == -1 + arg2 && arg0 >= arg1 && i >= arg0
	    && (anIntArrayArray1453[-anInt1473 + arg0][-anInt1456 + arg8] & 0x2
		^ 0xffffffff) == -1
	    && (arg4 & 0x4) == 0)
	    return true;
	if (arg6 != 25417)
	    method1357(112);
	if ((arg8 ^ 0xffffffff) == (1 + i_39_ ^ 0xffffffff)
	    && (arg1 ^ 0xffffffff) >= (arg0 ^ 0xffffffff) && arg0 <= i
	    && ((0x20
		 & anIntArrayArray1453[-anInt1473 + arg0][-anInt1456 + arg8])
		== 0)
	    && (0x1 & arg4) == 0)
	    return true;
	return false;
    }
    
    public Class73(int arg0, int arg1) {
	anInt1478 = arg0;
	anInt1470 = arg1;
	anInt1456 = 0;
	anIntArrayArray1453 = new int[anInt1478][anInt1470];
	anInt1473 = 0;
	method1357(57);
    }
}

