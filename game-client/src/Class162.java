
final class Class162 {

   static int anInt2036;
   static int anInt2037;
   static int anInt2038 = 0;
   static int[][] anIntArrayArray2039 = new int[][]{{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, {12, 8, 4, 0, 13, 9, 5, 1, 14, 10, 6, 2, 15, 11, 7, 3}, {15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0}, {3, 7, 11, 15, 2, 6, 10, 14, 1, 5, 9, 13, 0, 4, 8, 12}};
   static byte[] aByteArray2040 = new byte[520];


   static final void method2203(Class140_Sub4_Sub1 var0, int var1) {
      try {
         Class3_Sub9 var2 = (Class3_Sub9)Class3_Sub28_Sub7_Sub1.aClass130_4046.method1780(var0.aClass94_3967.method1578(var1 + -126), 0);
         if(var1 != 8) {
            method2204((Class3_Sub30)null, -44);
         }

         if(null != var2) {
            if(var2.aClass3_Sub24_Sub1_2312 != null) {
               Class3_Sub26.aClass3_Sub24_Sub2_2563.method461(var2.aClass3_Sub24_Sub1_2312);
               var2.aClass3_Sub24_Sub1_2312 = null;
            }

            var2.method86(var1 ^ -1016);
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "wc.B(" + (var0 != null?"{...}":"null") + ',' + var1 + ')');
      }
   }

   static final void method2204(Class3_Sub30 var0, int var1) {
      try {
         if(null != Class69.aClass30_1039) {
            try {
               Class69.aClass30_1039.method984(-117, 0L);
               Class69.aClass30_1039.method983(var0.aByteArray2590, var0.anInt2595, -903171152, 24);
            } catch (Exception var3) {
               ;
            }
         }

         if(var1 >= 45) {
            var0.anInt2595 += 24;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "wc.E(" + (var0 != null?"{...}":"null") + ',' + var1 + ')');
      }
   }

   public static void method2205(int var0) {
      try {
         anIntArrayArray2039 = (int[][])null;
         aByteArray2040 = null;
         if(var0 != -17413) {
            anInt2036 = 77;
         }

      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "wc.C(" + var0 + ')');
      }
   }

   static final void method2206(boolean var0, int var1) {
      try {
         Class3_Sub28_Sub6 var2 = Class3_Sub24_Sub3.method466(4, 4, var1);
         var2.a(var0);
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "wc.A(" + var0 + ',' + var1 + ')');
      }
   }

   static final Class111 method2207(int var0, int var1) {
      try {
         if(var0 != 4) {
            method2205(95);
         }

         Class111 var2 = (Class111)Canvas_Sub1.aClass93_21.method1526((long)var1, (byte)121);
         if(var2 == null) {
            byte[] var3 = Class85.aClass153_1171.method2133(Class3_Sub13_Sub36.method340(var1, -51), (byte)-122, Class15.method893(var1, (byte)110));
            var2 = new Class111();
            var2.anInt1527 = var1;
            if(null != var3) {
               var2.method1692(6219, new Class3_Sub30(var3));
            }

            var2.method1689(var0 + -2120);
            if(!Class14.aBoolean337 && var2.aBoolean1491) {
               var2.aClass94Array1499 = null;
            }

            if(var2.aBoolean1498) {
               var2.anInt1538 = 0;
               var2.aBoolean1486 = false;
            }

            Canvas_Sub1.aClass93_21.method1515((byte)-114, var2, (long)var1);
            return var2;
         } else {
            return var2;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "wc.D(" + var0 + ',' + var1 + ')');
      }
   }

}
