
class Class3_Sub28 extends Class3 {

   static RSString[] aClass94Array2566 = new RSString[200];
   static int anInt2567 = -1;
   private static RSString aClass94_2568 = Class3_Sub4.method108("Started 3d Library", (byte)-128);
   long aLong2569;
   Class3_Sub28 aClass3_Sub28_2570;
   static int anInt2571;
   static int anInt2572;
   static Class153 aClass153_2573;
   static int[] anIntArray2574 = new int[14];
   static int anInt2575;
   static RSString aClass94_2576 = aClass94_2568;
   static int anInt2577 = 0;
   Class3_Sub28 aClass3_Sub28_2578;


   static final void method518(Class140_Sub4_Sub1 var0, int var1) {
      try {
         Class3_Sub9 var2 = (Class3_Sub9)Class3_Sub28_Sub7_Sub1.aClass130_4046.method1780(var0.aClass94_3967.method1578(-121), 0);
         if(var1 >= -85) {
            method523(40, -17, -52, -32, 9, -51, -85, -84, -19);
         }

         if(var2 != null) {
            var2.method134(1);
         } else {
            Class70.method1286(var0.anIntArray2755[0], false, (Class111)null, 0, (Class140_Sub4_Sub2)null, var0.anIntArray2767[0], Class26.anInt501, var0);
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "rg.UA(" + (var0 != null?"{...}":"null") + ',' + var1 + ')');
      }
   }

   static final int method519(int var0, boolean var1, int var2, int var3) {
      try {
         var0 &= 3;
         if(!var1) {
            method520((byte)-89);
         }

         return 0 != var0?(~var0 != -2?(~var0 == -3?-var3 + 7:-var2 + 7):var2):var3;
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "rg.RA(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   static final Class3_Sub28_Sub3 method520(byte var0) {
      try {
         int var1 = -122 % ((var0 - -48) / 33);
         return Class3_Sub30.aClass3_Sub28_Sub3_2600;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "rg.OA(" + var0 + ')');
      }
   }

   public static void method521(int var0) {
      try {
         aClass153_2573 = null;
         if(var0 == -3) {
            aClass94Array2566 = null;
            aClass94_2568 = null;
            anIntArray2574 = null;
            aClass94_2576 = null;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "rg.QA(" + var0 + ')');
      }
   }

   static final Class90 method522(int var0, int var1) {
      try {
         Class90 var2 = (Class90)Class3_Sub28_Sub7_Sub1.aClass93_4043.method1526((long)var0, (byte)121);
         if(null == var2) {
            byte[] var3 = Class29.aClass153_557.method2133(Class38_Sub1.method1031(var0, 2), (byte)-122, Canvas_Sub1.method54(var0, false));
            var2 = new Class90();
            if(var1 != 27112) {
               anInt2572 = -67;
            }

            var2.anInt1284 = var0;
            if(null != var3) {
               var2.method1478(new Class3_Sub30(var3), 74);
            }

            var2.method1481(98);
            Class3_Sub28_Sub7_Sub1.aClass93_4043.method1515((byte)-95, var2, (long)var0);
            return var2;
         } else {
            return var2;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "rg.PA(" + var0 + ',' + var1 + ')');
      }
   }

   static final void method523(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      try {
         int var9 = var3 - var8;
         int var11 = (-var5 + var0 << 16) / var9;
         int var10 = -var4 + var6;
         int var12 = (var7 + -var1 << 16) / var10;
         Class83.method1410(var1, 0, var6, var4, var3, var5, var8, var12, var11, var2, -12541);
      } catch (RuntimeException var13) {
         throw Class44.method1067(var13, "rg.SA(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ')');
      }
   }

   final void method524(byte var1) {
      try {
         if(this.aClass3_Sub28_2570 != null) {
            this.aClass3_Sub28_2570.aClass3_Sub28_2578 = this.aClass3_Sub28_2578;
            this.aClass3_Sub28_2578.aClass3_Sub28_2570 = this.aClass3_Sub28_2570;
            this.aClass3_Sub28_2578 = null;
            this.aClass3_Sub28_2570 = null;
            if(var1 != -107) {
               this.aClass3_Sub28_2578 = (Class3_Sub28)null;
            }

         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "rg.TA(" + var1 + ')');
      }
   }

}
