import java.io.IOException;
import java.net.Socket;

final class Class3_Sub13_Sub21 extends Class3_Sub13 {

   private int anInt3253 = 0;
   private int anInt3254 = 4096;
   static int anInt3255;
   static int anInt3256;
   private int anInt3257 = 12288;
   private int anInt3258 = 0;
   static int anInt3259;
   static int anInt3260 = -1;
   static boolean aBoolean3261 = false;
   private int anInt3262 = 2048;
   static int anInt3263 = 0;
   static Class3_Sub28_Sub3 aClass3_Sub28_Sub3_3264;
   private int anInt3265 = 2048;
   private int anInt3266 = 8192;


   final void method157(int var1, Class3_Sub30 var2, boolean var3) {
      try {
         if(~var1 == -1) {
            this.anInt3265 = var2.method737(1);
         } else if(var1 == 1) {
            this.anInt3253 = var2.method737(1);
         } else if(-3 == ~var1) {
            this.anInt3258 = var2.method737(1);
         } else if(var1 != 3) {
            if(var1 != 4) {
               if(-6 == ~var1) {
                  this.anInt3254 = var2.method737(1);
               } else if(var1 == 6) {
                  this.anInt3266 = var2.method737(1);
               }
            } else {
               this.anInt3257 = var2.method737(1);
            }
         } else {
            this.anInt3262 = var2.method737(1);
         }

         if(!var3) {
            this.anInt3266 = 85;
         }

      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "mh.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   static final void method267(byte var0) {
      try {
         if(Canvas_Sub1.anInt23 != 0) {
            try {
               if(++Class132.anInt1734 > 2000) {
                  if(null != Class3_Sub15.aClass89_2429) {
                     Class3_Sub15.aClass89_2429.method1468(14821);
                     Class3_Sub15.aClass89_2429 = null;
                  }

                  if(~Class28.anInt548 <= -2) {
                     Class130.anInt1711 = -5;
                     Canvas_Sub1.anInt23 = 0;
                     return;
                  }

                  Canvas_Sub1.anInt23 = 1;
                  Class132.anInt1734 = 0;
                  ++Class28.anInt548;
                  if(Class140_Sub6.anInt2894 != Class162.anInt2036) {
                     Class140_Sub6.anInt2894 = Class162.anInt2036;
                  } else {
                     Class140_Sub6.anInt2894 = Class26.anInt506;
                  }
               }

               if(Canvas_Sub1.anInt23 == 1) {
                  Class3_Sub9.aClass64_2318 = Class38.aClass87_665.method1441((byte)8, Class38_Sub1.aString2611, Class140_Sub6.anInt2894);
                  Canvas_Sub1.anInt23 = 2;
               }

               int var1;
               if(-3 == ~Canvas_Sub1.anInt23) {
                  if(-3 == ~Class3_Sub9.aClass64_2318.anInt978) {
                     throw new IOException();
                  }

                  if(1 != Class3_Sub9.aClass64_2318.anInt978) {
                     return;
                  }

                  Class3_Sub15.aClass89_2429 = new Class89((Socket)Class3_Sub9.aClass64_2318.anObject974, Class38.aClass87_665);
                  Class3_Sub9.aClass64_2318 = null;
                  Class3_Sub15.aClass89_2429.method1464(false, 0, Class3_Sub13_Sub1.aClass3_Sub30_Sub1_3035.aByteArray2590, Class3_Sub13_Sub1.aClass3_Sub30_Sub1_3035.anInt2595);
                  if(Class44_Sub1.aClass155_2627 != null) {
                     Class44_Sub1.aClass155_2627.method2159(63);
                  }

                  if(null != Class3_Sub21.aClass155_2491) {
                     Class3_Sub21.aClass155_2491.method2159(66);
                  }

                  var1 = Class3_Sub15.aClass89_2429.method1462(0);
                  if(Class44_Sub1.aClass155_2627 != null) {
                     Class44_Sub1.aClass155_2627.method2159(64);
                  }

                  if(Class3_Sub21.aClass155_2491 != null) {
                     Class3_Sub21.aClass155_2491.method2159(111);
                  }

                  if(var1 != 21) {
                     Class130.anInt1711 = var1;
                     Canvas_Sub1.anInt23 = 0;
                     Class3_Sub15.aClass89_2429.method1468(14821);
                     Class3_Sub15.aClass89_2429 = null;
                     return;
                  }

                  Canvas_Sub1.anInt23 = 3;
               }

               if(var0 <= 26) {
                  method269(-75, 44);
               }

               if(3 == Canvas_Sub1.anInt23) {
                  if(~Class3_Sub15.aClass89_2429.method1465(-18358) > -2) {
                     return;
                  }

                  Class3_Sub13_Sub33.aClass94Array3391 = new RSString[Class3_Sub15.aClass89_2429.method1462(0)];
                  Canvas_Sub1.anInt23 = 4;
               }

               if(~Canvas_Sub1.anInt23 == -5) {
                  if(~Class3_Sub15.aClass89_2429.method1465(-18358) > ~(8 * Class3_Sub13_Sub33.aClass94Array3391.length)) {
                     return;
                  }

                  Class28.aClass3_Sub30_Sub1_532.anInt2595 = 0;
                  Class3_Sub15.aClass89_2429.method1461(0, 8 * Class3_Sub13_Sub33.aClass94Array3391.length, -18455, Class28.aClass3_Sub30_Sub1_532.aByteArray2590);

                  for(var1 = 0; ~Class3_Sub13_Sub33.aClass94Array3391.length < ~var1; ++var1) {
                     Class3_Sub13_Sub33.aClass94Array3391[var1] = Class41.method1052(-29664, Class28.aClass3_Sub30_Sub1_532.method756(-125));
                  }

                  Class130.anInt1711 = 21;
                  Canvas_Sub1.anInt23 = 0;
                  Class3_Sub15.aClass89_2429.method1468(14821);
                  Class3_Sub15.aClass89_2429 = null;
                  return;
               }
            } catch (IOException var2) {
               if(Class3_Sub15.aClass89_2429 != null) {
                  Class3_Sub15.aClass89_2429.method1468(14821);
                  Class3_Sub15.aClass89_2429 = null;
               }

               if(~Class28.anInt548 > -2) {
                  ++Class28.anInt548;
                  if(Class162.anInt2036 == Class140_Sub6.anInt2894) {
                     Class140_Sub6.anInt2894 = Class26.anInt506;
                  } else {
                     Class140_Sub6.anInt2894 = Class162.anInt2036;
                  }

                  Class132.anInt1734 = 0;
                  Canvas_Sub1.anInt23 = 1;
               } else {
                  Class130.anInt1711 = -4;
                  Canvas_Sub1.anInt23 = 0;
               }
            }

         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "mh.Q(" + var0 + ')');
      }
   }

   public static void method268(byte var0) {
      try {
         if(var0 != -91) {
            method268((byte)7);
         }

         aClass3_Sub28_Sub3_3264 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "mh.R(" + var0 + ')');
      }
   }

   public Class3_Sub13_Sub21() {
      super(0, true);
   }

   static final void method269(int var0, int var1) {
      try {
         if(var0 != -5) {
            anInt3263 = 109;
         }

         Class61.aClass93_939.method1522(-128, var1);
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "mh.O(" + var0 + ',' + var1 + ')');
      }
   }

   final int[] method154(int var1, byte var2) {
      try {
         int var4 = 15 % ((30 - var2) / 36);
         int[] var3 = this.aClass114_2382.method1709(-16409, var1);
         if(this.aClass114_2382.aBoolean1580) {
            int var5 = -2048 + Class163_Sub3.anIntArray2999[var1];

            for(int var6 = 0; var6 < Class113.anInt1559; ++var6) {
               int var9 = var5 + this.anInt3253;
               int var7 = Class102.anIntArray2125[var6] + -2048;
               int var8 = this.anInt3265 + var7;
               var9 = 2047 < ~var9?var9 - -4096:var9;
               var9 = 2048 < var9?-4096 + var9:var9;
               int var10 = var7 + this.anInt3258;
               var8 = var8 < -2048?var8 + 4096:var8;
               var8 = -2049 <= ~var8?var8:-4096 + var8;
               var10 = ~var10 <= 2047?var10:4096 + var10;
               var10 = -2049 > ~var10?var10 - 4096:var10;
               int var11 = var5 - -this.anInt3262;
               var11 = -2048 > var11?var11 + 4096:var11;
               var11 = -2049 > ~var11?var11 - 4096:var11;
               var3[var6] = !this.method271(var8, var9, (byte)113) && !this.method270((byte)-44, var10, var11)?0:4096;
            }
         }

         return var3;
      } catch (RuntimeException var12) {
         throw Class44.method1067(var12, "mh.D(" + var1 + ',' + var2 + ')');
      }
   }

   private final boolean method270(byte var1, int var2, int var3) {
      try {
         int var4 = this.anInt3257 * (var2 + var3) >> 12;
         if(var1 > -17) {
            method267((byte)89);
         }

         int var5 = Class75_Sub2.anIntArray2639[var4 * 255 >> 12 & 255];
         var5 = (var5 << 12) / this.anInt3257;
         var5 = (var5 << 12) / this.anInt3266;
         var5 = var5 * this.anInt3254 >> 12;
         return var5 > -var2 + var3 && ~(-var5) > ~(var3 + -var2);
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "mh.S(" + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   final void method158(int var1) {
      try {
         Class8.method844((byte)-9);
         if(var1 != 16251) {
            method272((byte)-85);
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "mh.P(" + var1 + ')');
      }
   }

   private final boolean method271(int var1, int var2, byte var3) {
      try {
         int var4 = (var2 - var1) * this.anInt3257 >> 12;
         if(var3 != 113) {
            this.method270((byte)-91, -79, -4);
         }

         int var5 = Class75_Sub2.anIntArray2639[(1047948 & var4 * 255) >> 12];
         var5 = (var5 << 12) / this.anInt3257;
         var5 = (var5 << 12) / this.anInt3266;
         var5 = var5 * this.anInt3254 >> 12;
         return ~(var2 + var1) > ~var5 && -var5 < var2 + var1;
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "mh.F(" + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   static final void method272(byte var0) {
      try {
         if(var0 != -124) {
            aClass3_Sub28_Sub3_3264 = (Class3_Sub28_Sub3)null;
         }

         int var1 = Class3_Sub13_Sub26.aByteArrayArray3335.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            if(Class3_Sub13_Sub26.aByteArrayArray3335[var2] != null) {
               int var3 = -1;

               for(int var4 = 0; ~var4 > ~Class3_Sub13_Sub20.anInt3244; ++var4) {
                  if(Class3_Sub13_Sub30.anIntArray3367[var4] == Class3_Sub24_Sub3.anIntArray3494[var2]) {
                     var3 = var4;
                     break;
                  }
               }

               if(0 == ~var3) {
                  Class3_Sub13_Sub30.anIntArray3367[Class3_Sub13_Sub20.anInt3244] = Class3_Sub24_Sub3.anIntArray3494[var2];
                  var3 = Class3_Sub13_Sub20.anInt3244++;
               }

               int var5 = 0;
               Class3_Sub30 var16 = new Class3_Sub30(Class3_Sub13_Sub26.aByteArrayArray3335[var2]);

               while(~var16.anInt2595 > ~Class3_Sub13_Sub26.aByteArrayArray3335[var2].length && 511 > var5) {
                  int var6 = var5++ << 6 | var3;
                  int var7 = var16.method737(var0 ^ -123);
                  int var8 = var7 >> 14;
                  int var9 = 63 & var7 >> 7;
                  int var11 = var9 + 64 * (Class3_Sub24_Sub3.anIntArray3494[var2] >> 8) - Class131.anInt1716;
                  int var10 = var7 & 63;
                  int var12 = var10 + -Class82.anInt1152 + 64 * (255 & Class3_Sub24_Sub3.anIntArray3494[var2]);
                  Class90 var13 = Class3_Sub28.method522(var16.method737(1), 27112);
                  if(Class3_Sub13_Sub24.aClass140_Sub4_Sub2Array3292[var6] == null && (var13.aByte1267 & 1) > 0 && ~var8 == ~Class140_Sub3.anInt2745 && -1 >= ~var11 && 104 > var13.anInt1245 + var11 && ~var12 <= -1 && 104 > var12 - -var13.anInt1245) {
                     Class3_Sub13_Sub24.aClass140_Sub4_Sub2Array3292[var6] = new Class140_Sub4_Sub2();
                     Class140_Sub4_Sub2 var14 = Class3_Sub13_Sub24.aClass140_Sub4_Sub2Array3292[var6];
                     Class15.anIntArray347[Class163.anInt2046++] = var6;
                     var14.anInt2838 = Class44.anInt719;
                     var14.method1987(-1, var13);
                     var14.method1976(var14.aClass90_3976.anInt1245, 2);
                     var14.anInt2806 = var14.anInt2785 = Class27.anIntArray510[var14.aClass90_3976.aByte1268];
                     var14.anInt2779 = var14.aClass90_3976.anInt1274;
                     if(~var14.anInt2779 == -1) {
                        var14.anInt2785 = 0;
                     }

                     var14.anInt2763 = var14.aClass90_3976.anInt1280;
                     var14.method1967(-2, var14.method1977((byte)114), var11, var12, true);
                  }
               }
            }
         }

      } catch (RuntimeException var15) {
         throw Class44.method1067(var15, "mh.E(" + var0 + ')');
      }
   }

}
