
final class Class21 {

   static boolean aBoolean440 = false;
   static int[] anIntArray441 = new int[2048];
   static RSString aClass94_442 = Class3_Sub4.method108("Number of player models in cache:", (byte)-121);
   static int anInt443;
   static RSString aClass94_444 = Class3_Sub4.method108("<img=1>", (byte)-125);


   public static void method911(int var0) {
      try {
         if(var0 != 26) {
            anIntArray441 = (int[])null;
         }

         anIntArray441 = null;
         aClass94_444 = null;
         aClass94_442 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "dh.B(" + var0 + ')');
      }
   }

   static final void method912(boolean var0) {
      try {
         Class3_Sub13_Sub1.aClass3_Sub30_Sub1_3035.anInt2595 = 0;
         Class7.anInt2166 = -1;
         Class38_Sub1.aBoolean2615 = var0;
         Class130.anInt1704 = 0;
         Class65.anInt987 = 0;
         Class3_Sub13_Sub34.anInt3415 = 0;
         Class3_Sub29.anInt2582 = -1;
         Class161.anInt2028 = 0;
         Class38_Sub1.anInt2617 = 0;
         Class24.anInt469 = -1;
         Class28.aClass3_Sub30_Sub1_532.anInt2595 = 0;
         Class3_Sub28_Sub16.anInt3699 = 0;
         RSString.anInt2147 = -1;

         int var1;
         for(var1 = 0; Sprites.aClass140_Sub4_Sub1Array3269.length > var1; ++var1) {
            if(null != Sprites.aClass140_Sub4_Sub1Array3269[var1]) {
               Sprites.aClass140_Sub4_Sub1Array3269[var1].anInt2772 = -1;
            }
         }

         for(var1 = 0; ~var1 > ~Class3_Sub13_Sub24.aClass140_Sub4_Sub2Array3292.length; ++var1) {
            if(Class3_Sub13_Sub24.aClass140_Sub4_Sub2Array3292[var1] != null) {
               Class3_Sub13_Sub24.aClass140_Sub4_Sub2Array3292[var1].anInt2772 = -1;
            }
         }

         Class3_Sub28_Sub9.method580((byte)80);
         Class133.anInt1753 = 1;
         Class117.method1719(30, 5);

         for(var1 = 0; var1 < 100; ++var1) {
            Class3_Sub28_Sub14.aBooleanArray3674[var1] = true;
         }

         Class3_Sub13_Sub8.method204(-3);
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "dh.F(" + var0 + ')');
      }
   }

   static final Class118 method913(int var0) {
      try {
         try {
            if(var0 != 31431) {
               method913(123);
            }

            return (Class118)Class.forName("Class118_Sub1").newInstance();
         } catch (Throwable var2) {
            return null;
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "dh.C(" + var0 + ')');
      }
   }

   static final Class3_Sub31 method914(int var0, int var1, int var2, int var3) {
      try {
         Class3_Sub31 var4 = new Class3_Sub31();
         var4.anInt2603 = var3;
         var4.anInt2602 = var1;
         Class3_Sub13_Sub17.aClass130_3208.method1779(1, var4, (long)var2);
         Class3_Sub13_Sub13.method232(var1, 16182);
         Class11 var5 = Class7.method832((byte)122, var2);
         if(var5 != null) {
            Class20.method909(120, var5);
         }

         if(null != Class3_Sub13_Sub7.aClass11_3087) {
            Class20.method909(117, Class3_Sub13_Sub7.aClass11_3087);
            Class3_Sub13_Sub7.aClass11_3087 = null;
         }

         int var6 = Class3_Sub13_Sub34.anInt3415;
         if(var0 != 6422) {
            aClass94_444 = (RSString)null;
         }

         int var7;
         for(var7 = 0; var6 > var7; ++var7) {
            if(Class2.method73(Class3_Sub13_Sub7.aShortArray3095[var7], var0 + -6301)) {
               Class3_Sub25.method509(1, var7);
            }
         }

         if(1 == Class3_Sub13_Sub34.anInt3415) {
            Class38_Sub1.aBoolean2615 = false;
            Class75.method1340(Class109.anInt1462, Class3_Sub28_Sub3.anInt3552, (byte)-40, Class3_Sub13_Sub33.anInt3395, Class3_Sub28_Sub1.anInt3537);
         } else {
            Class75.method1340(Class109.anInt1462, Class3_Sub28_Sub3.anInt3552, (byte)-40, Class3_Sub13_Sub33.anInt3395, Class3_Sub28_Sub1.anInt3537);
            var7 = Class168.aClass3_Sub28_Sub17_2096.method682(Class75_Sub4.aClass94_2667);

            for(int var8 = 0; Class3_Sub13_Sub34.anInt3415 > var8; ++var8) {
               int var9 = Class168.aClass3_Sub28_Sub17_2096.method682(Class3_Sub30.method802(var8, true));
               if(~var9 < ~var7) {
                  var7 = var9;
               }
            }

            Class3_Sub28_Sub3.anInt3552 = 8 + var7;
            Class3_Sub28_Sub1.anInt3537 = 15 * Class3_Sub13_Sub34.anInt3415 + (!Class153.aBoolean1951?22:26);
         }

         if(var5 != null) {
            Class151_Sub1.method2104(var5, false, 55);
         }

         Class3_Sub13_Sub12.method226(var1, 58);
         if(0 != ~Class3_Sub28_Sub12.anInt3655) {
            Class3_Sub8.method124(var0 ^ 6509, 1, Class3_Sub28_Sub12.anInt3655);
         }

         return var4;
      } catch (RuntimeException var10) {
         throw Class44.method1067(var10, "dh.D(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   static final void method915(RSString var0, int var1) {
      try {
         int var2 = Class3_Sub28_Sub8.method576(var0, false);
         if(var1 != var2) {
            Class3_Sub28_Sub7.method565((byte)86, Class119.aClass131_1624.aShortArray1727[var2], Class119.aClass131_1624.aShortArray1718[var2]);
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "dh.A(" + (var0 != null?"{...}":"null") + ',' + var1 + ')');
      }
   }

   static final Class146 method916(byte var0) {
      try {
         if(var0 != 15) {
            aClass94_442 = (RSString)null;
         }

         try {
            return (Class146)Class.forName("Class146_Sub1").newInstance();
         } catch (Throwable var2) {
            return null;
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "dh.E(" + var0 + ')');
      }
   }

}
