
final class Class57 {

   static Class3_Sub28_Sub16 aClass3_Sub28_Sub16_895;
   int anInt896 = 0;
   static RSString aClass94_897 = Class3_Sub4.method108("Liste des mises -9 jour charg-Be", (byte)-121);
   static int[] anIntArray898 = new int[2500];
   int anInt899 = 2048;
   static int anInt900;
   static RSString aClass94_901 = Class3_Sub4.method108("Ihre Ignorieren)2Liste ist voll)1 Sie k-Onnen nur 100 Spieler darauf eintragen)3", (byte)-125);
   static int anInt902 = 100;
   static int anInt903;
   static int[] anIntArray904 = new int[200];
   static RSString aClass94_905 = Class3_Sub4.method108("Sprites charg-Bs", (byte)-118);
   static int anInt906;
   int anInt907 = 0;
   int anInt908 = 2048;


   final void method1190(int var1, Class3_Sub30 var2, int var3) {
      try {
         while(true) {
            int var4 = var2.method803((byte)-88);
            if(var4 == 0) {
               if(var1 != 2) {
                  aClass94_905 = (RSString)null;
               }

               return;
            }

            this.method1191(var4, var2, var3, true);
         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "ic.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   private final void method1191(int var1, Class3_Sub30 var2, int var3, boolean var4) {
      try {
         if(1 != var1) {
            if(var1 == 2) {
               this.anInt908 = var2.method737(1);
            } else if(-4 == ~var1) {
               this.anInt899 = var2.method737(1);
            } else if(4 == var1) {
               this.anInt907 = var2.method787((byte)46);
            }
         } else {
            this.anInt896 = var2.method803((byte)-78);
         }

         if(!var4) {
            method1192((byte)123);
         }

      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "ic.C(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ',' + var4 + ')');
      }
   }

   public static void method1192(byte var0) {
      try {
         aClass94_897 = null;
         anIntArray898 = null;
         aClass94_901 = null;
         anIntArray904 = null;
         aClass94_905 = null;
         if(var0 == -86) {
            aClass3_Sub28_Sub16_895 = null;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "ic.B(" + var0 + ')');
      }
   }

}
