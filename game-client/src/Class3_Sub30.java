import java.math.BigInteger;

class Class3_Sub30 extends Class3 {

	byte[] aByteArray2590;
	static int[] anIntArray2591;
	static int anInt2592;
	private static RSString aClass94_2593 = Class3_Sub4.method108("Loaded interfaces", (byte)-117);
	static RSString aClass94_2594 = Class3_Sub4.method108("Polices charg-Bes", (byte)-126);
	int anInt2595;
	static RSString[] aClass94Array2596 = null;
	static RSString aClass94_2597 = aClass94_2593;
	static RSString aClass94_2598 = Class3_Sub4.method108("<br>", (byte)-124);
	static RSString aClass94_2599 = Class3_Sub4.method108("d-Broulement:", (byte)-119);
	static Class3_Sub28_Sub3 aClass3_Sub28_Sub3_2600;
	static Class151_Sub1[] aClass151_Sub1Array2601 = new Class151_Sub1[28];


	final int method737(int var1) {
		try {
			if(var1 != 1) {
				this.method790(-57, 26);
			}

			this.anInt2595 += 2;
			return (this.aByteArray2590[-2 + this.anInt2595] << 8 & '\uff00') - -(this.aByteArray2590[-1 + this.anInt2595] & 255);
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.EB(" + var1 + ')');
		}
	}

	final void method738(int var1, int var2) {
		try {
			if(var1 > -119) {
				aClass94_2593 = (RSString)null;
			}

			this.aByteArray2590[this.anInt2595++] = (byte)(var2 >> 24);
			this.aByteArray2590[this.anInt2595++] = (byte)(var2 >> 16);
			this.aByteArray2590[this.anInt2595++] = (byte)(var2 >> 8);
			this.aByteArray2590[this.anInt2595++] = (byte)var2;
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "wa.UC(" + var1 + ',' + var2 + ')');
		}
	}

	final void method739(int var1, int var2, long var3) {
		try {
			--var2;
			if(~var2 <= -1 && -8 <= ~var2) {
				if(var1 == 0) {
					for(int var5 = var2 * 8; 0 <= var5; var5 -= 8) {
						this.aByteArray2590[this.anInt2595++] = (byte)((int)(var3 >> var5));
					}

				}
			} else {
				throw new IllegalArgumentException();
			}
		} catch (RuntimeException var6) {
			throw Class44.method1067(var6, "wa.OB(" + var1 + ',' + var2 + ',' + var3 + ')');
		}
	}

	final void method740(long var1, int var3) {
		try {
			this.aByteArray2590[this.anInt2595++] = (byte)((int)(var1 >> 56));
			this.aByteArray2590[this.anInt2595++] = (byte)((int)(var1 >> 48));
			if(var3 == -2037491440) {
				this.aByteArray2590[this.anInt2595++] = (byte)((int)(var1 >> 40));
				this.aByteArray2590[this.anInt2595++] = (byte)((int)(var1 >> 32));
				this.aByteArray2590[this.anInt2595++] = (byte)((int)(var1 >> 24));
				this.aByteArray2590[this.anInt2595++] = (byte)((int)(var1 >> 16));
				this.aByteArray2590[this.anInt2595++] = (byte)((int)(var1 >> 8));
				this.aByteArray2590[this.anInt2595++] = (byte)((int)var1);
			}
		} catch (RuntimeException var5) {
			throw Class44.method1067(var5, "wa.AC(" + var1 + ',' + var3 + ')');
		}
	}

	final int method741(byte var1) {
		try {
			byte var2 = this.aByteArray2590[this.anInt2595++];
			if(var1 < 112) {
				aClass94_2599 = (RSString)null;
			}

			int var3;
			for(var3 = 0; 0 > var2; var2 = this.aByteArray2590[this.anInt2595++]) {
				var3 = (127 & var2 | var3) << 7;
			}

			return var2 | var3;
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "wa.WA(" + var1 + ')');
		}
	}

	final void method742(int var1, int var2) {
		try {
			this.aByteArray2590[-4 + this.anInt2595 + -var2] = (byte)(var2 >> 24);
			this.aByteArray2590[-var2 + this.anInt2595 - 3] = (byte)(var2 >> 16);
			this.aByteArray2590[-2 + this.anInt2595 + -var2] = (byte)(var2 >> 8);
			if(var1 < 78) {
				this.method771(-102, 37);
			}

			this.aByteArray2590[-var2 + this.anInt2595 + -1] = (byte)var2;
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "wa.VB(" + var1 + ',' + var2 + ')');
		}
	}

	final void method743(int var1, int var2) {
		try {
			this.aByteArray2590[this.anInt2595++] = (byte)(-var2 + 128);
			if(var1 != 10213) {
				this.method759(-121, -23);
			}

		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "wa.PC(" + var1 + ',' + var2 + ')');
		}
	}

	static final void method744(boolean var0) {
		try {
			if(!var0) {
				method784(-10, -32, -21);
			}

			++Class148.anInt1908;
		} catch (RuntimeException var2) {
			throw Class44.method1067(var2, "wa.HA(" + var0 + ')');
		}
	}

	final void method745(int var1, RSString var2) {
		try {
			this.anInt2595 += var2.method1580(true, this.aByteArray2590, this.anInt2595, var1, var2.method1540(-65));
			this.aByteArray2590[this.anInt2595++] = 0;
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "wa.LA(" + var1 + ',' + (var2 != null?"{...}":"null") + ')');
		}
	}

	static final void method746(byte var0) {
		try {
			Class67.aClass93_1013.method1524(3);
			if(var0 == -29) {
				;
			}
		} catch (RuntimeException var2) {
			throw Class44.method1067(var2, "wa.VC(" + var0 + ')');
		}
	}

	final int method747(int var1) {
		try {
			this.anInt2595 += 2;
			int var2 = (this.aByteArray2590[-2 + this.anInt2595] << 8 & '\uff00') - -(-128 + this.aByteArray2590[this.anInt2595 + -1] & 255);
			if(var1 != -58) {
				this.method760(true);
			}

			if(var2 > 32767) {
				var2 -= 65536;
			}

			return var2;
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.NA(" + var1 + ')');
		}
	}

	final int method748(int var1) {
		try {
			this.anInt2595 += 4;
			return var1 != -502942936?104:((255 & this.aByteArray2590[this.anInt2595 - 4]) << 24) + (16711680 & this.aByteArray2590[this.anInt2595 + -3] << 16) + (((255 & this.aByteArray2590[this.anInt2595 - 2]) << 8) - -(this.aByteArray2590[this.anInt2595 - 1] & 255));
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.KA(" + var1 + ')');
		}
	}

	final byte method749(boolean var1) {
		try {
			return var1?-79:(byte)(-this.aByteArray2590[this.anInt2595++] + 128);
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.JB(" + var1 + ')');
		}
	}

	final RSString method750(byte var1) {
		try {
			if(var1 != 78) {
				return (RSString)null;
			} else if(this.aByteArray2590[this.anInt2595] != 0) {
				return this.method776(true);
			} else {
				++this.anInt2595;
				return null;
			}
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.IB(" + var1 + ')');
		}
	}

	final int method751(byte var1) {
		try {
			int var2 = 122 / ((30 - var1) / 63);
			return 255 & this.aByteArray2590[this.anInt2595++] - 128;
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.DC(" + var1 + ')');
		}
	}

	final void method752(byte var1, int var2) {
		try {
			this.aByteArray2590[this.anInt2595++] = (byte)var2;
			if(var1 >= -5) {
				this.method757(-77, -126);
			}

		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "wa.RA(" + var1 + ',' + var2 + ')');
		}
	}

	final void method753(byte[] var1, int var2, int var3, int var4) {
		try {
			int var5 = var2;
			if(var4 >= 37) {
				while(~var5 > ~(var2 + var3)) {
					this.aByteArray2590[this.anInt2595++] = var1[var5];
					++var5;
				}

			}
		} catch (RuntimeException var6) {
			throw Class44.method1067(var6, "wa.QC(" + (var1 != null?"{...}":"null") + ',' + var2 + ',' + var3 + ',' + var4 + ')');
		}
	}

	final int method754(boolean var1) {
		try {
			if(!var1) {
				this.anInt2595 = 61;
			}

			return -this.aByteArray2590[this.anInt2595++] + 128 & 255;
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.PB(" + var1 + ')');
		}
	}

	final int method755(byte var1) {
		try {
			this.anInt2595 += 3;
			int var2 = 40 % ((-7 - var1) / 47);
			return ((this.aByteArray2590[this.anInt2595 + -2] & 255) << 8) + ((this.aByteArray2590[-1 + this.anInt2595] & 255) << 16) + (this.aByteArray2590[-3 + this.anInt2595] & 255);
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.BA(" + var1 + ')');
		}
	}

	final long method756(int var1) {
		try {
			if(var1 > -75) {
				return -1L;
			} else {
				long var2 = (long)this.method748(-502942936) & 4294967295L;
				long var4 = 4294967295L & (long)this.method748(-502942936);
				return var4 + (var2 << 32);
			}
		} catch (RuntimeException var6) {
			throw Class44.method1067(var6, "wa.GA(" + var1 + ')');
		}
	}

	final void method757(int var1, int var2) {
		try {
			this.aByteArray2590[this.anInt2595++] = (byte)var1;
			if(var2 < 54) {
				this.method749(false);
			}

			this.aByteArray2590[this.anInt2595++] = (byte)(var1 >> 8);
			this.aByteArray2590[this.anInt2595++] = (byte)(var1 >> 16);
			this.aByteArray2590[this.anInt2595++] = (byte)(var1 >> 24);
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "wa.AB(" + var1 + ',' + var2 + ')');
		}
	}

	final int method758(int var1) {
		try {
			this.anInt2595 += 2;
			int var2 = -90 / ((var1 - -58) / 43);
			return (this.aByteArray2590[-1 + this.anInt2595] - 128 & 255) + ('\uff00' & this.aByteArray2590[-2 + this.anInt2595] << 8);
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.LC(" + var1 + ')');
		}
	}

	final void method759(int var1, int var2) {
		try {
			this.aByteArray2590[this.anInt2595++] = (byte)(var2 >> 16);
			this.aByteArray2590[this.anInt2595++] = (byte)(var2 >> 24);
			if(var1 >= -56) {
				this.method768(82, 22);
			}

			this.aByteArray2590[this.anInt2595++] = (byte)var2;
			this.aByteArray2590[this.anInt2595++] = (byte)(var2 >> 8);
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "wa.MC(" + var1 + ',' + var2 + ')');
		}
	}

	Class3_Sub30(int var1) {
		try {
			this.aByteArray2590 = Class134.method1807(66, var1);
			this.anInt2595 = 0;
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.<init>(" + var1 + ')');
		}
	}

	final byte method760(boolean var1) {
		try {
			return var1?-13:this.aByteArray2590[this.anInt2595++];
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.TA(" + var1 + ')');
		}
	}

	final RSString method761(int var1) {
		try {
			byte var2 = this.aByteArray2590[this.anInt2595++];
			if(var1 < 50) {
				this.aByteArray2590 = (byte[])null;
			}

			if(0 != var2) {
				throw new IllegalStateException("Bad version number in gjstr2");
			} else {
				int var3 = this.anInt2595;

				while(-1 != ~this.aByteArray2590[this.anInt2595++]) {
					;
				}

				return Class3_Sub13_Sub3.method178(this.aByteArray2590, -4114, this.anInt2595 - (var3 - -1), var3);
			}
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "wa.DA(" + var1 + ')');
		}
	}

	final void method762(float var1, byte var2) {
		try {
			int var3 = Float.floatToRawIntBits(var1);
			this.aByteArray2590[this.anInt2595++] = (byte)var3;
			if(var2 > 63) {
				this.aByteArray2590[this.anInt2595++] = (byte)(var3 >> 8);
				this.aByteArray2590[this.anInt2595++] = (byte)(var3 >> 16);
				this.aByteArray2590[this.anInt2595++] = (byte)(var3 >> 24);
			}
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "wa.CC(" + var1 + ',' + var2 + ')');
		}
	}

	final byte method763(byte var1) {
		try {
			return var1 < 98?95:(byte)(-this.aByteArray2590[this.anInt2595++] + 0);
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.BC(" + var1 + ')');
		}
	}

	final void method764(int var1, int var2, byte[] var3, byte var4) {
		try {
			if(var4 != 93) {
				method802(122, true);
			}

			for(int var5 = var1; var1 + var2 > var5; ++var5) {
				var3[var5] = this.aByteArray2590[this.anInt2595++];
			}

		} catch (RuntimeException var6) {
			throw Class44.method1067(var6, "wa.SB(" + var1 + ',' + var2 + ',' + (var3 != null?"{...}":"null") + ',' + var4 + ')');
		}
	}

	final void method765(int var1, byte var2) {
		try {
			this.aByteArray2590[this.anInt2595++] = (byte)(var1 + 128);
			if(var2 == 3) {
				this.aByteArray2590[this.anInt2595++] = (byte)(var1 >> 8);
			}
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "wa.MA(" + var1 + ',' + var2 + ')');
		}
	}

	final int method766(int var1) {
		try {
			this.anInt2595 += 2;
			return var1 >= -54?-17:(255 & this.aByteArray2590[this.anInt2595 - 2]) + ('\uff00' & this.aByteArray2590[this.anInt2595 - 1] << 8);
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.BB(" + var1 + ')');
		}
	}

	public static void method767(int var0) {
		try {
			aClass94_2599 = null;
			anIntArray2591 = null;
			aClass94_2597 = null;
			aClass3_Sub28_Sub3_2600 = null;
			aClass94_2594 = null;
			aClass94Array2596 = null;
			aClass94_2593 = null;
			aClass151_Sub1Array2601 = null;
			aClass94_2598 = null;
			if(var0 != 0) {
				anIntArray2591 = (int[])null;
			}

		} catch (RuntimeException var2) {
			throw Class44.method1067(var2, "wa.IC(" + var0 + ')');
		}
	}

	final void method768(int var1, int var2) {
		try {
			if(var1 != -32769) {
				this.method738(97, -9);
			}

			if(0 <= var2 && ~var2 > -129) {
				this.method752((byte)-10, var2);
			} else if(0 <= var2 && ~var2 > -32769) {
				this.method804(-20037, '\u8000' + var2);
			} else {
				throw new IllegalArgumentException();
			}
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "wa.UA(" + var1 + ',' + var2 + ')');
		}
	}

	final void method769(byte var1, int var2) {
		try {
			this.aByteArray2590[-1 + -var2 + this.anInt2595] = (byte)var2;
			int var3 = 120 % ((-78 - var1) / 48);
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "wa.MB(" + var1 + ',' + var2 + ')');
		}
	}

	final void method770(int[] var1, int var2, int var3, int var4) {
		try {
			int var5 = this.anInt2595;
			this.anInt2595 = var3;
			int var6 = (-var3 + var4) / 8;

			for(int var7 = 0; var6 > var7; ++var7) {
				int var10 = -957401312;
				int var8 = this.method748(-502942936);
				int var9 = this.method748(-502942936);
				int var12 = 32;

				for(int var11 = -1640531527; var12-- > 0; var8 -= (var9 >>> 5 ^ var9 << 4) + var9 ^ var1[var10 & 3] + var10) {
					var9 -= var1[(6754 & var10) >>> 11] + var10 ^ var8 + (var8 >>> 5 ^ var8 << 4);
					var10 -= var11;
				}

				this.anInt2595 -= 8;
				this.method738(-122, var8);
				this.method738(-121, var9);
			}

			if(var2 <= 102) {
				anIntArray2591 = (int[])null;
			}

			this.anInt2595 = var5;
		} catch (RuntimeException var13) {
			throw Class44.method1067(var13, "wa.SC(" + (var1 != null?"{...}":"null") + ',' + var2 + ',' + var3 + ',' + var4 + ')');
		}
	}

	final void method771(int var1, int var2) {
		try {
			if(~(-128 & var2) != -1) {
				if(-1 != ~(-16384 & var2)) {
					if((var2 & -2097152) != 0) {
						if(0 != (-268435456 & var2)) {
							this.method752((byte)-64, var2 >>> 28 | 128);
						}

						this.method752((byte)-80, 128 | var2 >>> 21);
					}

					this.method752((byte)-100, 128 | var2 >>> 14);
				}

				this.method752((byte)-21, var2 >>> 7 | 128);
			}

			this.method752((byte)-46, var2 & 127);
			if(var1 != 17038) {
				aClass94_2594 = (RSString)null;
			}

		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "wa.DB(" + var1 + ',' + var2 + ')');
		}
	}

	final long method772(int var1, int var2) {
		try {
			--var1;
			if(var2 <= var1 && ~var1 >= -8) {
				long var4 = 0L;

				for(int var3 = var1 * 8; ~var3 <= -1; var3 -= 8) {
					var4 |= ((long)this.aByteArray2590[this.anInt2595++] & 255L) << var3;
				}

				return var4;
			} else {
				throw new IllegalArgumentException();
			}
		} catch (RuntimeException var6) {
			throw Class44.method1067(var6, "wa.VA(" + var1 + ',' + var2 + ')');
		}
	}

	final int method773(byte var1) {
		try {
			if(var1 >= -120) {
				return 3;
			} else {
				int var3 = this.method778(true);

				int var2;
				for(var2 = 0; 32767 == var3; var2 += 32767) {
					var3 = this.method778(true);
				}

				var2 += var3;
				return var2;
			}
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "wa.UB(" + var1 + ')');
		}
	}

	final void method774(int var1, int var2, byte[] var3, int var4) {
		try {
			if(var1 == 2) {
				for(int var5 = var4 - (-var2 - -1); ~var4 >= ~var5; --var5) {
					var3[var5] = this.aByteArray2590[this.anInt2595++];
				}

			}
		} catch (RuntimeException var6) {
			throw Class44.method1067(var6, "wa.HC(" + var1 + ',' + var2 + ',' + (var3 != null?"{...}":"null") + ',' + var4 + ')');
		}
	}

	final void method775(int var1, int var2) {
		try {
			if(var2 != 1437452424) {
				this.method798((byte)113);
			}

			this.aByteArray2590[this.anInt2595++] = (byte)(var1 >> 8);
			this.aByteArray2590[this.anInt2595++] = (byte)var1;
			this.aByteArray2590[this.anInt2595++] = (byte)(var1 >> 24);
			this.aByteArray2590[this.anInt2595++] = (byte)(var1 >> 16);
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "wa.GC(" + var1 + ',' + var2 + ')');
		}
	}

	final RSString method776(boolean var1) {
		try {
			if(!var1) {
				aClass3_Sub28_Sub3_2600 = (Class3_Sub28_Sub3)null;
			}

			int var2 = this.anInt2595;

			while(0 != this.aByteArray2590[this.anInt2595++]) {
				;
			}

			return Class3_Sub13_Sub3.method178(this.aByteArray2590, -4114, -var2 + (this.anInt2595 - 1), var2);
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.FC(" + var1 + ')');
		}
	}

	static final void method777(Class91[] var0, boolean var1, int var2, int var3, int var4, int var5, int var6, byte[] var7) {
		try {
			int var10;
			int var11;
			if(!var1) {
				for(int var9 = 0; 4 > var9; ++var9) {
					for(var10 = 0; -65 < ~var10; ++var10) {
						for(var11 = 0; ~var11 > -65; ++var11) {
							if(-1 > ~(var5 - -var10) && var10 + var5 < 103 && ~(var3 + var11) < -1 && ~(var11 + var3) > -104) {
								var0[var9].anIntArrayArray1304[var10 + var5][var3 - -var11] = Class3_Sub28_Sub15.method633(var0[var9].anIntArrayArray1304[var10 + var5][var3 - -var11], -16777217);
							}
						}
					}
				}
			}

			Class3_Sub30 var20 = new Class3_Sub30(var7);
			byte var8;
			if(!var1) {
				var8 = 4;
			} else {
				var8 = 1;
			}

			int var12;
			for(var10 = 0; var8 > var10; ++var10) {
				for(var11 = 0; ~var11 > -65; ++var11) {
					for(var12 = 0; 64 > var12; ++var12) {
						Class167.method2267(var2, var6, var1, var20, var12 - -var3, var5 + var11, (byte)91, 0, var10);
					}
				}
			}

			int var14;
			int var15;
			int var17;
			boolean var21;
			int var24;
			for(var21 = false; var20.anInt2595 < var20.aByteArray2590.length; var21 = true) {
				var11 = var20.method803((byte)-120);
				if(var11 != 129) {
					--var20.anInt2595;
					break;
				}

				for(var12 = 0; var12 < 4; ++var12) {
					byte var13 = var20.method760(false);
					if(0 != var13) {
						if(1 != var13) {
							if(var13 == 2 && -1 > ~var12) {
								var15 = var5 + 64;
								var24 = var3;
								var17 = var3 + 64;
								if(~var15 > -1) {
									var15 = 0;
								} else if(104 <= var15) {
									var15 = 104;
								}

								if(~var3 <= -1) {
									if(-105 >= ~var3) {
										var24 = 104;
									}
								} else {
									var24 = 0;
								}

								if(-1 >= ~var17) {
									if(~var17 <= -105) {
										var17 = 104;
									}
								} else {
									var17 = 0;
								}

								var14 = var5;
								if(var5 >= 0) {
									if(104 <= var5) {
										var14 = 104;
									}
								} else {
									var14 = 0;
								}

								while(var15 > var14) {
									while(~var24 > ~var17) {
										Class136.aByteArrayArrayArray1774[var12][var14][var24] = Class136.aByteArrayArrayArray1774[var12 + -1][var14][var24];
										++var24;
									}

									++var14;
								}
							}
						} else {
							for(var14 = 0; ~var14 > -65; var14 += 4) {
								for(var15 = 0; var15 < 64; var15 += 4) {
									byte var16 = var20.method760(false);

									for(var17 = var14 + var5; 4 + var5 + var14 > var17; ++var17) {
										for(int var18 = var3 + var15; ~var18 > ~(4 + var3 + var15); ++var18) {
											if(var17 >= 0 && ~var17 > -105 && 0 <= var18 && -105 < ~var18) {
												Class136.aByteArrayArrayArray1774[var12][var17][var18] = var16;
											}
										}
									}
								}
							}
						}
					} else {
						var14 = var5;
						if(var5 >= 0) {
							if(~var5 <= -105) {
								var14 = 104;
							}
						} else {
							var14 = 0;
						}

						var24 = var3;
						if(-1 < ~var3) {
							var24 = 0;
						} else if(~var3 <= -105) {
							var24 = 104;
						}

						var15 = 64 + var5;
						var17 = var3 + 64;
						if(-1 >= ~var17) {
							if(var17 >= 104) {
								var17 = 104;
							}
						} else {
							var17 = 0;
						}

						if(-1 < ~var15) {
							var15 = 0;
						} else if(var15 >= 104) {
							var15 = 104;
						}

						while(~var14 > ~var15) {
							while(var24 < var17) {
								Class136.aByteArrayArrayArray1774[var12][var14][var24] = 0;
								++var24;
							}

							++var14;
						}
					}
				}
			}

			if(var4 == 4) {
				int var23;
				if(Class138.aBoolean1807 && !var1) {
					Class86 var22 = null;

					while(~var20.anInt2595 > ~var20.aByteArray2590.length) {
						var12 = var20.method803((byte)-100);
						if(var12 != 0) {
							if(~var12 != -2) {
								throw new IllegalStateException();
							}

							var23 = var20.method803((byte)-114);
							if(0 < var23) {
								for(var14 = 0; var23 > var14; ++var14) {
									Class43 var25 = new Class43(var20);
									if(-32 == ~var25.anInt705) {
										Class57 var26 = Class81.method1401(1001, var20.method737(1));
										var25.method1060((byte)-67, var26.anInt896, var26.anInt908, var26.anInt899, var26.anInt907);
									}

									var25.anInt708 += var3 << 7;
									var25.anInt703 += var5 << 7;
									var17 = var25.anInt708 >> 7;
									var24 = var25.anInt703 >> 7;
									if(~var24 <= -1 && 0 <= var17 && ~var24 > -105 && ~var17 > -105) {
										var25.aBoolean696 = 0 != (Class9.aByteArrayArrayArray113[1][var24][var17] & 2);
										var25.anInt697 = Class44.anIntArrayArrayArray723[var25.anInt704][var24][var17] + -var25.anInt697;
										Class68.method1264(var25);
									}
								}
							}
						} else {
							var22 = new Class86(var20);
						}
					}

					if(var22 == null) {
						var22 = new Class86();
					}

					for(var12 = 0; -9 < ~var12; ++var12) {
						for(var23 = 0; -9 < ~var23; ++var23) {
							var14 = var12 + (var5 >> 3);
							var15 = (var3 >> 3) + var23;
							if(0 <= var14 && var14 < 13 && -1 >= ~var15 && ~var15 > -14) {
								Class115.aClass86ArrayArray1581[var14][var15] = var22;
							}
						}
					}
				}

				if(!var21) {
					for(var11 = 0; var11 < 4; ++var11) {
						for(var12 = 0; 16 > var12; ++var12) {
							for(var23 = 0; var23 < 16; ++var23) {
								var14 = (var5 >> 2) - -var12;
								var15 = var23 + (var3 >> 2);
								if(0 <= var14 && 26 > var14 && 0 <= var15 && var15 < 26) {
									Class136.aByteArrayArrayArray1774[var11][var14][var15] = 0;
								}
							}
						}
					}
				}

			}
		} catch (RuntimeException var19) {
			throw Class44.method1067(var19, "wa.OA(" + (var0 != null?"{...}":"null") + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + (var7 != null?"{...}":"null") + ')');
		}
	}

	final int method778(boolean var1) {
		try {
			if(!var1) {
				aClass94_2593 = (RSString)null;
			}

			int var2 = this.aByteArray2590[this.anInt2595] & 255;
			return 128 <= var2?-32768 + this.method737(1):this.method803((byte)-74);
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.JA(" + var1 + ')');
		}
	}

	final void method779(int var1, int var2) {
		try {
			this.aByteArray2590[this.anInt2595++] = (byte)(var1 >> 16);
			this.aByteArray2590[this.anInt2595++] = (byte)(var1 >> 8);
			if(var2 != 6517) {
				aClass94_2598 = (RSString)null;
			}

			this.aByteArray2590[this.anInt2595++] = (byte)var1;
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "wa.FB(" + var1 + ',' + var2 + ')');
		}
	}

	final int method780(int var1) {
		try {
			this.anInt2595 += 4;
			if(var1 != -1) {
				this.method769((byte)7, -47);
			}

			return ((this.aByteArray2590[this.anInt2595 - 2] & 255) << 24) + ((255 & this.aByteArray2590[this.anInt2595 - 1]) << 16) + ('\uff00' & this.aByteArray2590[-4 + this.anInt2595] << 8) - -(this.aByteArray2590[this.anInt2595 + -3] & 255);
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.NB(" + var1 + ')');
		}
	}

	final int method781(byte var1) {
		try {
			this.anInt2595 += 2;
			if(var1 > -84) {
				this.method749(false);
			}

			return (this.aByteArray2590[-1 + this.anInt2595] << 322035176 & '\uff00') - -(255 & -128 + this.aByteArray2590[this.anInt2595 + -2]);
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.TB(" + var1 + ')');
		}
	}

	final int method782(int var1) {
		try {
			this.anInt2595 += 4;
			if(var1 >= -42) {
				this.method763((byte)-14);
			}

			return (255 & this.aByteArray2590[-4 + this.anInt2595]) + (16711680 & this.aByteArray2590[this.anInt2595 - 2] << 1572599856) + ((255 & this.aByteArray2590[this.anInt2595 + -1]) << -34836040) + ((this.aByteArray2590[-3 + this.anInt2595] & 255) << 481963272);
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.EA(" + var1 + ')');
		}
	}

	final void method783(int var1, int var2) {
		try {
			if(var2 == -268435456) {
				this.aByteArray2590[this.anInt2595++] = (byte)(var1 >> -702824440);
				this.aByteArray2590[this.anInt2595++] = (byte)(128 + var1);
			}
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "wa.LB(" + var1 + ',' + var2 + ')');
		}
	}

	static final Class12 method784(int var0, int var1, int var2) {
		Class3_Sub2 var3 = Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2];
		return var3 != null && var3.aClass12_2230 != null?var3.aClass12_2230:null;
	}

	Class3_Sub30(byte[] var1) {
		try {
			this.anInt2595 = 0;
			this.aByteArray2590 = var1;
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.<init>(" + (var1 != null?"{...}":"null") + ')');
		}
	}

	final void method785(int var1, byte var2) {
		try {
			this.aByteArray2590[this.anInt2595++] = (byte)var1;
			this.aByteArray2590[this.anInt2595++] = (byte)(var1 >> -1080682200);
			if(var2 <= -118) {
				this.aByteArray2590[this.anInt2595++] = (byte)(var1 >> 16);
				this.aByteArray2590[this.anInt2595++] = (byte)(var1 >> 12970328);
			}
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "wa.IA(" + var1 + ',' + var2 + ')');
		}
	}

	final int method786(boolean var1) {
		try {
			if(!var1) {
				this.anInt2595 = 46;
			}

			return 255 & 0 + -this.aByteArray2590[this.anInt2595++];
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.CB(" + var1 + ')');
		}
	}

	final int method787(byte var1) {
		try {
			this.anInt2595 += 2;
			int var2 = (this.aByteArray2590[-1 + this.anInt2595] & 255) + ((255 & this.aByteArray2590[this.anInt2595 + -2]) << -2034851416);
			if(var1 < 4) {
				return -83;
			} else {
				if(~var2 < -32768) {
					var2 -= 65536;
				}

				return var2;
			}
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.TC(" + var1 + ')');
		}
	}

	final int method788(int var1) {
		try {
			this.anInt2595 += 2;
			int var2 = ((this.aByteArray2590[this.anInt2595 - 1] & 255) << 1510012168) - -(this.aByteArray2590[-2 + this.anInt2595] - 128 & 255);
			if(var1 != -1741292848) {
				this.method800((BigInteger)null, (BigInteger)null, 11);
			}

			if(32767 < var2) {
				var2 -= 65536;
			}

			return var2;
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.EC(" + var1 + ')');
		}
	}

	final byte method789(int var1) {
		try {
			return var1 != 0?-51:(byte)(-128 + this.aByteArray2590[this.anInt2595++]);
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.CA(" + var1 + ')');
		}
	}

	final void method790(int var1, int var2) {
		try {
			if(var2 != -13071) {
				this.method797(120);
			}

			this.aByteArray2590[this.anInt2595++] = (byte)(128 + var1);
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "wa.KC(" + var1 + ',' + var2 + ')');
		}
	}

	final int method791(byte var1) {
		try {
			this.anInt2595 += 2;
			if(var1 != 10) {
				this.method751((byte)109);
			}

			int var2 = (this.aByteArray2590[-2 + this.anInt2595] & 255) + ('\uff00' & this.aByteArray2590[this.anInt2595 + -1] << 50972264);
			if(var2 > 32767) {
				var2 -= 65536;
			}

			return var2;
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.SA(" + var1 + ')');
		}
	}

	static final void method792(int var0) {
		try {
			if(var0 == 9179409) {
				int var1 = Class137.method1817((byte)70);
				if(0 == var1) {
					Class158.aByteArrayArrayArray2008 = (byte[][][])null;
					Class136.method1816(0, -7);
				} else if(~var1 == -2) {
					Class3_Sub5.method112((byte)0, (byte)55);
					Class136.method1816(512, -7);
					Class3_Sub13_Sub18.method257((byte)125);
				} else {
					Class3_Sub5.method112((byte)(-4 + Class79.anInt1127 & 255), (byte)55);
					Class136.method1816(2, -7);
				}

			}
		} catch (RuntimeException var2) {
			throw Class44.method1067(var2, "wa.AA(" + var0 + ')');
		}
	}

	final int method793(byte var1, int var2) {
		try {
			if(var1 < 1) {
				return 65;
			} else {
				int var3 = Class99.method1599(var2, this.anInt2595, this.aByteArray2590, (byte)-49);
				this.method738(-120, var3);
				return var3;
			}
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "wa.QB(" + var1 + ',' + var2 + ')');
		}
	}

	final int method794(byte var1) {
		try {
			if(var1 < 76) {
				this.method766(53);
			}

			this.anInt2595 += 3;
			return (16711680 & this.aByteArray2590[this.anInt2595 + -3] << -2022440336) + (('\uff00' & this.aByteArray2590[-2 + this.anInt2595] << -54462168) - -(this.aByteArray2590[this.anInt2595 + -1] & 255));
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.GB(" + var1 + ')');
		}
	}

	static final void method795(byte var0, int var1) {
		try {
			if(var0 != 14) {
				anIntArray2591 = (int[])null;
			}

			Class3_Sub16.aClass93_2450.method1522(var0 ^ -114, var1);
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.OC(" + var0 + ',' + var1 + ')');
		}
	}

	final void method796(int var1, int var2) {
		try {
			if(var1 != -1) {
				aClass94Array2596 = (RSString[])null;
			}

			this.aByteArray2590[this.anInt2595++] = (byte)var2;
			this.aByteArray2590[this.anInt2595++] = (byte)(var2 >> 203327944);
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "wa.PA(" + var1 + ',' + var2 + ')');
		}
	}

	final int method797(int var1) {
		try {
			int var2 = this.aByteArray2590[this.anInt2595] & 255;
			if(var1 != -21208) {
				aClass94_2593 = (RSString)null;
			}

			return ~var2 > -129?-64 + this.method803((byte)-82):this.method737(var1 + 21209) - '\uc000';
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.WB(" + var1 + ')');
		}
	}

	final int method798(byte var1) {
		try {
			int var2 = -83 / ((16 - var1) / 50);
			this.anInt2595 += 4;
			return ((this.aByteArray2590[-3 + this.anInt2595] & 255) << -1597905000) - -(16711680 & this.aByteArray2590[this.anInt2595 + -4] << 861399376) + (((this.aByteArray2590[this.anInt2595 + -1] & 255) << 979767016) - -(255 & this.aByteArray2590[this.anInt2595 + -2]));
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.NC(" + var1 + ')');
		}
	}

	static final void method799(int var0, int var1, int var2, int var3, int var4) {
		try {
			if(~Class140_Sub3.anInt2737 > -101) {
				Class3_Sub28_Sub14.method626(64);
			}

			if(Class138.aBoolean1807) {
				Class22.method935(var0, var2, var0 + var4, var3 + var2);
			} else {
				Class74.method1324(var0, var2, var0 + var4, var3 + var2);
			}

			int var6;
			int var7;
			if(~Class140_Sub3.anInt2737 <= -101) {
				Class17.anInt410 = (int)((float)(var3 * 2) / Class44.aFloat727);
				Class60.anInt930 = Class3_Sub28_Sub1.anInt3536 + -((int)((float)var4 / Class44.aFloat727));
				int var15 = -((int)((float)var4 / Class44.aFloat727)) + Class3_Sub28_Sub1.anInt3536;
				var6 = Class3_Sub4.anInt2251 - (int)((float)var3 / Class44.aFloat727);
				Class60.anInt934 = Class3_Sub4.anInt2251 + -((int)((float)var3 / Class44.aFloat727));
				int var8 = Class3_Sub4.anInt2251 + (int)((float)var3 / Class44.aFloat727);
				var7 = (int)((float)var4 / Class44.aFloat727) + Class3_Sub28_Sub1.anInt3536;
				Class49.anInt817 = (int)((float)(var4 * 2) / Class44.aFloat727);
				if(var1 != 64) {
					aClass94Array2596 = (RSString[])null;
				}

				if(Class138.aBoolean1807) {
					if(Class3_Sub13_Sub19.aClass3_Sub28_Sub16_Sub2_3221 == null || ~Class3_Sub13_Sub19.aClass3_Sub28_Sub16_Sub2_3221.anInt3707 != ~var4 || ~Class3_Sub13_Sub19.aClass3_Sub28_Sub16_Sub2_3221.anInt3696 != ~var3) {
						Class3_Sub13_Sub19.aClass3_Sub28_Sub16_Sub2_3221 = null;
						Class3_Sub13_Sub19.aClass3_Sub28_Sub16_Sub2_3221 = new Class3_Sub28_Sub16_Sub2(var4, var3);
					}

					Class74.method1319(Class3_Sub13_Sub19.aClass3_Sub28_Sub16_Sub2_3221.anIntArray4081, var4, var3);
					Class3_Sub28.method523(var4, 0, 0, var7, var6, 0, var8, var3, var15);
					Class23.method938(var4, 0, var7, var8, var3, 0, 1, var15, var6);
					Class3_Sub5.method111((byte)-54, 0, 0, var15, var4, var8, var6, var7, var3);
					Class22.method926(Class3_Sub13_Sub19.aClass3_Sub28_Sub16_Sub2_3221.anIntArray4081, var0, var2, var4, var3);
					Class74.anIntArray1100 = null;
				} else {
					Class3_Sub28.method523(var4 + var0, var2, 0, var7, var6, var0, var8, var2 - -var3, var15);
					Class23.method938(var0 + var4, var0, var7, var8, var3 + var2, var2, 1, var15, var6);
					Class3_Sub5.method111((byte)-100, var0, var2, var15, var0 - -var4, var8, var6, var7, var3 + var2);
				}

				if(0 < Class3_Sub28_Sub16.anInt3704) {
					--Class3_Sub28_Sub8.anInt3611;
					if(-1 == ~Class3_Sub28_Sub8.anInt3611) {
						Class3_Sub28_Sub8.anInt3611 = 20;
						--Class3_Sub28_Sub16.anInt3704;
					}
				}

				if(Class20.aBoolean438) {
					int var10 = -8 + var2 - -var3;
					int var9 = -5 + (var0 - -var4);
					Class126.aClass3_Sub28_Sub17_1669.method688(Class16.method903(new RSString[]{Class65.aClass94_985, Class72.method1298((byte)9, Class142.anInt1862)}, (byte)-62), var9, var10, 16776960, -1);
					Runtime var11 = Runtime.getRuntime();
					int var12 = (int)((var11.totalMemory() - var11.freeMemory()) / 1024L);
					int var13 = 16776960;
					var10 -= 15;
					if(~var12 < -65537) {
						var13 = 16711680;
					}

					Class126.aClass3_Sub28_Sub17_1669.method688(Class16.method903(new RSString[]{Class119.aClass94_1630, Class72.method1298((byte)9, var12), Class3_Sub13_Sub4.aClass94_3055}, (byte)-108), var9, var10, var13, -1);
					var10 -= 15;
				}

			} else {
				byte var5 = 20;
				var6 = var0 - -(var4 / 2);
				var7 = var3 / 2 + (var2 - 18) + -var5;
				if(Class138.aBoolean1807) {
					Class22.method934(var0, var2, var4, var3, 0);
					Class22.method927(var6 - 152, var7, 304, 34, 9179409);
					Class22.method927(var6 + -151, var7 + 1, 302, 32, 0);
					Class22.method934(-150 + var6, var7 + 2, 3 * Class140_Sub3.anInt2737, 30, 9179409);
					Class22.method934(-150 + var6 + Class140_Sub3.anInt2737 * 3, var7 - -2, 300 + -(3 * Class140_Sub3.anInt2737), 30, 0);
				} else {
					Class74.method1323(var0, var2, var4, var3, 0);
					Class74.method1311(var6 + -152, var7, 304, 34, 9179409);
					Class74.method1311(var6 + -151, 1 + var7, 302, 32, 0);
					Class74.method1323(var6 + -150, var7 + 2, Class140_Sub3.anInt2737 * 3, 30, 9179409);
					Class74.method1323(3 * Class140_Sub3.anInt2737 + -150 + var6, var7 - -2, -(Class140_Sub3.anInt2737 * 3) + 300, 30, 0);
				}

				Class168.aClass3_Sub28_Sub17_2096.method699(Class24.aClass94_462, var6, var5 + var7, 16777215, -1);
			}
		} catch (RuntimeException var14) {
			throw Class44.method1067(var14, "wa.FA(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ')');
		}
	}

	final void method800(BigInteger var1, BigInteger var2, int var3) {
		try {
			int var4 = this.anInt2595;
			this.anInt2595 = 0;
			byte[] var5 = new byte[var4];
			this.method764(0, var4, var5, (byte)93);
			BigInteger var6 = new BigInteger(var5);
			BigInteger var7 = null;
			if (ClientLoader.useRsa)
				var7 = var6.modPow(var1, var2);
			else
				var7 = var6; 
			byte[] var8 = var7.toByteArray();
			this.anInt2595 = 0;
			this.method752((byte)-23, var8.length);
			this.method753(var8, 0, var8.length, var3 + 348);
		} catch (RuntimeException var9) {
			throw Class44.method1067(var9, "wa.KB(" + (var1 != null?"{...}":"null") + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
		}
	}

	final void method801(int var1, float var2) {
		try {
			int var3 = Float.floatToRawIntBits(var2);
			this.aByteArray2590[this.anInt2595++] = (byte)(var3 >> -1164789608);
			this.aByteArray2590[this.anInt2595++] = (byte)(var3 >> -259929904);
			this.aByteArray2590[this.anInt2595++] = (byte)(var3 >> 1414718216);
			if(var1 == 881) {
				this.aByteArray2590[this.anInt2595++] = (byte)var3;
			}
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "wa.QA(" + var1 + ',' + var2 + ')');
		}
	}

	static final RSString method802(int var0, boolean var1) {
		try {
			if(!var1) {
				method746((byte)-33);
			}

			return -1 > ~Class163_Sub2_Sub1.aClass94Array4016[var0].method1540(-26)?Class16.method903(new RSString[]{Class140_Sub7.aClass94Array2935[var0], InputStream_Sub1.aClass94_43, Class163_Sub2_Sub1.aClass94Array4016[var0]}, (byte)-86):Class140_Sub7.aClass94Array2935[var0];
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.RB(" + var0 + ',' + var1 + ')');
		}
	}

	final int method803(byte var1) {
		try {
			if(var1 > -22) {
				this.method780(16);
			}

			return this.aByteArray2590[this.anInt2595++] & 255;
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "wa.RC(" + var1 + ')');
		}
	}

	final void method804(int var1, int var2) {
		try {
			if(var1 != -20037) {
				this.method786(true);
			}

			this.aByteArray2590[this.anInt2595++] = (byte)(var2 >> 2124857032);
			this.aByteArray2590[this.anInt2595++] = (byte)var2;
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "wa.JC(" + var1 + ',' + var2 + ')');
		}
	}

}
