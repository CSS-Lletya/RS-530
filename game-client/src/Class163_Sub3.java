
final class Class163_Sub3 extends Class163 {

   static int[] anIntArray2999;
   static RSString aClass94_3000 = Class3_Sub4.method108("; Max)2Age=", (byte)-119);
   static int anInt3001;
   static RSString aClass94_3002 = Class3_Sub4.method108("M", (byte)-121);
   static RSString[] aClass94Array3003 = new RSString[100];
   static boolean aBoolean3004 = true;
   static byte[][] aByteArrayArray3005;
   static RSString aClass94_3006 = Class3_Sub4.method108("<col=ff3000>", (byte)-125);
   static int[] anIntArray3007 = new int[]{-1, -1, 1, 1};


   public static void method2227(byte var0) {
      try {
         aClass94_3006 = null;
         anIntArray3007 = null;
         aClass94Array3003 = null;
         if(var0 == 37) {
            aByteArrayArray3005 = (byte[][])null;
            aClass94_3002 = null;
            aClass94_3000 = null;
            anIntArray2999 = null;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "fb.A(" + var0 + ')');
      }
   }

   static final void method2228(byte var0) {
      try {
         Class66.anInt997 = 0;
         Class139.anInt1829 = 0;
         Class151_Sub1.method2100((byte)81);
         Class140_Sub3.method1964(false);
         Class131.method1786(-59);
         Canvas_Sub2.method58(-102);
         if(var0 <= -69) {
            int var1;
            for(var1 = 0; Class139.anInt1829 > var1; ++var1) {
               int var2 = Class3_Sub7.anIntArray2292[var1];
               if(Class44.anInt719 != Sprites.aClass140_Sub4_Sub1Array3269[var2].anInt2838) {
                  if(0 < Sprites.aClass140_Sub4_Sub1Array3269[var2].anInt3969) {
                     Class162.method2203(Sprites.aClass140_Sub4_Sub1Array3269[var2], 8);
                  }

                  Sprites.aClass140_Sub4_Sub1Array3269[var2] = null;
               }
            }

            if(~Class130.anInt1704 != ~Class28.aClass3_Sub30_Sub1_532.anInt2595) {
               throw new RuntimeException("gpp1 pos:" + Class28.aClass3_Sub30_Sub1_532.anInt2595 + " psize:" + Class130.anInt1704);
            } else {
               for(var1 = 0; var1 < Class159.anInt2022; ++var1) {
                  if(null == Sprites.aClass140_Sub4_Sub1Array3269[Class56.anIntArray887[var1]]) {
                     throw new RuntimeException("gpp2 pos:" + var1 + " size:" + Class159.anInt2022);
                  }
               }

            }
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "fb.B(" + var0 + ')');
      }
   }

   static final void method2229(long var0, byte var2) {
      try {
         if(-1L != ~var0) {
            if((100 > Class8.anInt104 || Class3_Sub13_Sub29.aBoolean3358) && ~Class8.anInt104 > -201) {
               RSString var3 = Class41.method1052(-29664, var0).method1545((byte)-50);
               if(var2 != -91) {
                  method2227((byte)22);
               }

               int var4;
               for(var4 = 0; Class8.anInt104 > var4; ++var4) {
                  if(~Class50.aLongArray826[var4] == ~var0) {
                     Class3_Sub30_Sub1.method805(Class3_Sub28_Sub14.aClass94_3672, 0, Class16.method903(new RSString[]{var3, Class3_Sub28_Sub11.aClass94_3645}, (byte)-93), -1);
                     return;
                  }
               }

               for(var4 = 0; ~Class3_Sub28_Sub5.anInt3591 < ~var4; ++var4) {
                  if(~var0 == ~Class114.aLongArray1574[var4]) {
                     Class3_Sub30_Sub1.method805(Class3_Sub28_Sub14.aClass94_3672, 0, Class16.method903(new RSString[]{Class38.aClass94_662, var3, RSInterface.aClass94_4}, (byte)-66), -1);
                     return;
                  }
               }

               if(var3.method1528((byte)-42, Class102.aClass140_Sub4_Sub1_2141.aClass94_3967)) {
                  Class3_Sub30_Sub1.method805(Class3_Sub28_Sub14.aClass94_3672, 0, Class62.aClass94_957, -1);
               } else {
                  ++Class28.anInt544;
                  Class70.aClass94Array1046[Class8.anInt104] = var3;
                  Class50.aLongArray826[Class8.anInt104] = var0;
                  Class55.anIntArray882[Class8.anInt104] = 0;
                  Class3_Sub28.aClass94Array2566[Class8.anInt104] = Class3_Sub28_Sub14.aClass94_3672;
                  Class57.anIntArray904[Class8.anInt104] = 0;
                  Class3.aBooleanArray73[Class8.anInt104] = false;
                  ++Class8.anInt104;
                  Class110.anInt1472 = Class3_Sub13_Sub17.anInt3213;
                  Class3_Sub13_Sub1.aClass3_Sub30_Sub1_3035.method816(0, 120);
                  Class3_Sub13_Sub1.aClass3_Sub30_Sub1_3035.method740(var0, var2 ^ 2037491381);
               }
            } else {
               Class3_Sub30_Sub1.method805(Class3_Sub28_Sub14.aClass94_3672, 0, Class163_Sub2_Sub1.aClass94_4024, -1);
            }
         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "fb.C(" + var0 + ',' + var2 + ')');
      }
   }

}
