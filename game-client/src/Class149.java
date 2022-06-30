import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

final class Class149 implements MouseListener, MouseMotionListener, FocusListener {

   static float[] aFloatArray1919 = new float[4];
   static int[] anIntArray1920;
   static int anInt1921 = 0;
   static RSString aClass94_1922 = Class3_Sub4.method108("weiss:", (byte)-118);
   static int anInt1923;
   static int anInt1924 = 0;
   static int anInt1925 = 0;
   static int anInt1926;
   static int anInt1927 = 0;


   public final synchronized void mouseMoved(MouseEvent var1) {
      try {
         if(Class3_Sub28_Sub7_Sub1.aClass149_4047 != null) {
            Class3_Sub28_Sub7_Sub1.anInt4045 = 0;
            Class3_Sub21.anInt2493 = var1.getX();
            Class95.anInt1340 = var1.getY();
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ug.mouseMoved(" + (var1 != null?"{...}":"null") + ')');
      }
   }

   static final void method2087(byte var0) {
      try {
         Class3_Sub4 var1 = (Class3_Sub4)Class3_Sub13_Sub6.aClass61_3075.method1222(-81);

         for(int var2 = -37 / ((-21 - var0) / 48); null != var1; var1 = (Class3_Sub4)Class3_Sub13_Sub6.aClass61_3075.method1221(4)) {
            if(var1.anInt2259 > 0) {
               --var1.anInt2259;
            }

            if(0 != var1.anInt2259) {
               if(~var1.anInt2261 < -1) {
                  --var1.anInt2261;
               }

               if(~var1.anInt2261 == -1 && 1 <= var1.anInt2264 && 1 <= var1.anInt2248 && 102 >= var1.anInt2264 && ~var1.anInt2248 >= -103 && (~var1.anInt2265 > -1 || Class3_Sub28_Sub10.method590((byte)-34, var1.anInt2265, var1.anInt2262))) {
                  Class41.method1048(var1.anInt2265, var1.anInt2264, var1.anInt2250, var1.anInt2256, var1.anInt2248, -65, var1.anInt2262, var1.anInt2263);
                  var1.anInt2261 = -1;
                  if(~var1.anInt2254 == ~var1.anInt2265 && var1.anInt2254 == -1) {
                     var1.method86(-1024);
                  } else if(~var1.anInt2265 == ~var1.anInt2254 && var1.anInt2256 == var1.anInt2257 && ~var1.anInt2253 == ~var1.anInt2262) {
                     var1.method86(-1024);
                  }
               }
            } else if(-1 < ~var1.anInt2254 || Class3_Sub28_Sub10.method590((byte)-66, var1.anInt2254, var1.anInt2253)) {
               Class41.method1048(var1.anInt2254, var1.anInt2264, var1.anInt2250, var1.anInt2257, var1.anInt2248, -71, var1.anInt2253, var1.anInt2263);
               var1.method86(-1024);
            }
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ug.A(" + var0 + ')');
      }
   }

   public static void method2088(boolean var0) {
      try {
         aClass94_1922 = null;
         anIntArray1920 = null;
         if(!var0) {
            anIntArray1920 = (int[])null;
         }

         aFloatArray1919 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "ug.C(" + var0 + ')');
      }
   }

   public final synchronized void focusLost(FocusEvent var1) {
      try {
         if(Class3_Sub28_Sub7_Sub1.aClass149_4047 != null) {
            Class28.anInt549 = 0;
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ug.focusLost(" + (var1 != null?"{...}":"null") + ')');
      }
   }

   static final void method2089(byte var0) {
      try {
         Class158_Sub1.aClass93_2982.method1523((byte)-105);
         if(var0 >= 84) {
            ;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "ug.D(" + var0 + ')');
      }
   }

   private int mouseWheelX;
   private int mouseWheelY;
   private boolean mouseWheelDown;
   
   public final synchronized void mouseDragged(MouseEvent mouseevent) {
      try {
         if(null != Class3_Sub28_Sub7_Sub1.aClass149_4047) {
            Class3_Sub28_Sub7_Sub1.anInt4045 = 0;
            Class3_Sub21.anInt2493 = mouseevent.getX();
            Class95.anInt1340 = mouseevent.getY();
         }
         if (this.mouseWheelDown) {
 			int dx = this.mouseWheelX - mouseevent.getY();
 			int dy = this.mouseWheelY - mouseevent.getX();
 			this.mouseWheelDragged(dx, -dy);
 			this.mouseWheelX = mouseevent.getY();
 			this.mouseWheelY = mouseevent.getX();
 		}
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ug.mouseDragged(" + (mouseevent != null?"{...}":"null") + ')');
      }
   }
   
	private void mouseWheelDragged(int dx, int ndy) {
		Class3_Sub9.anInt2309 += dx ;
		Class28.anInt531 += (ndy);
	}

   public final synchronized void mouseReleased(MouseEvent var1) {
      try {
         if(null != Class3_Sub28_Sub7_Sub1.aClass149_4047) {
            Class3_Sub28_Sub7_Sub1.anInt4045 = 0;
            Class28.anInt549 = 0;
            this.mouseWheelDown = false;
            int var2 = var1.getModifiers();
            if(0 == (16 & var2)) {
               ;
            }

            if(-1 == ~(var2 & 4)) {
               ;
            }

            if(-1 == ~(var2 & 8)) {
               ;
            }
         }

         if(var1.isPopupTrigger()) {
            var1.consume();
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ug.mouseReleased(" + (var1 != null?"{...}":"null") + ')');
      }
   }

   public final void mouseClicked(MouseEvent var1) {
      try {
         if(var1.isPopupTrigger()) {
            var1.consume();
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ug.mouseClicked(" + (var1 != null?"{...}":"null") + ')');
      }
   }

   public final void focusGained(FocusEvent var1) {}

   static final void method2090(int var0) {
      try {
         if(Class3_Sub28_Sub7_Sub1.aClass149_4047 != null) {
            Class149 var1 = Class3_Sub28_Sub7_Sub1.aClass149_4047;
            synchronized(var1) {
               Class3_Sub28_Sub7_Sub1.aClass149_4047 = null;
            }
         }

         if(var0 == 8) {
            ;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ug.F(" + var0 + ')');
      }
   }

   public final synchronized void mousePressed(MouseEvent mouseevent) {
      try {
         if(Class3_Sub28_Sub7_Sub1.aClass149_4047 != null) {
            Class3_Sub28_Sub7_Sub1.anInt4045 = 0;
            Class16.anInt362 = mouseevent.getX();
            Class3_Sub13_Sub32.anInt3389 = mouseevent.getY();
            Class140_Sub6.aLong2926 = Class5.method830((byte)-55);
            if (SwingUtilities.isMiddleMouseButton(mouseevent)) {
				this.mouseWheelDown = true;
				this.mouseWheelX = mouseevent.getX();
				this.mouseWheelY = mouseevent.getY();
				return;
			}
            if(!mouseevent.isMetaDown()) {
               Class140_Sub3.anInt2743 = 1;
               Class28.anInt549 = 1;
            } else {
               Class140_Sub3.anInt2743 = 2;
               Class28.anInt549 = 2;
            }

            int var2 = mouseevent.getModifiers();
            if((var2 & 16) == 0) {
               ;
            }

            if(~(4 & var2) != -1) {
               ;
            }

            if(-1 != ~(var2 & 8)) {
               ;
            }
         }

         if(mouseevent.isPopupTrigger()) {
            mouseevent.consume();
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ug.mousePressed(" + (mouseevent != null?"{...}":"null") + ')');
      }
   }

   public final synchronized void mouseExited(MouseEvent var1) {
      try {
         if(Class3_Sub28_Sub7_Sub1.aClass149_4047 != null) {
            Class3_Sub28_Sub7_Sub1.anInt4045 = 0;
            Class3_Sub21.anInt2493 = -1;
            Class95.anInt1340 = -1;
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ug.mouseExited(" + (var1 != null?"{...}":"null") + ')');
      }
   }

   static final void method2091(int var0, int var1) {
      try {
         if(Class140.anIntArray1838 == null || Class140.anIntArray1838.length < var0) {
            Class140.anIntArray1838 = new int[var0];
         }

         if(var1 != 4) {
            anInt1926 = -75;
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ug.E(" + var0 + ',' + var1 + ')');
      }
   }

   public final synchronized void mouseEntered(MouseEvent var1) {
      try {
         if(Class3_Sub28_Sub7_Sub1.aClass149_4047 != null) {
            Class3_Sub28_Sub7_Sub1.anInt4045 = 0;
            Class3_Sub21.anInt2493 = var1.getX();
            Class95.anInt1340 = var1.getY();
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ug.mouseEntered(" + (var1 != null?"{...}":"null") + ')');
      }
   }

   static final void method2092(int var0, byte var1) {
      try {
         if(var1 != -47) {
            aClass94_1922 = (RSString)null;
         }

         Class3_Sub28_Sub6 var2 = Class3_Sub24_Sub3.method466(4, 9, var0);
         var2.a(true);
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ug.B(" + var0 + ',' + var1 + ')');
      }
   }

}
