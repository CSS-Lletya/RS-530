import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.Method;
import java.net.URL;

public abstract class RSInterface extends Applet implements Runnable, FocusListener, WindowListener {

   private boolean aBoolean1 = false;
   static int anInt2;
   static int anInt3 = 0;

   private static RSString aClass94_5 = Class3_Sub4.method108(" from your ignore list first)3", (byte)-128);
   static boolean aBoolean6 = false;
   static RSString aClass94_7 = Class3_Sub4.method108(" s(West d-Bconnect-B)3", (byte)-123);
   static RSString aClass94_8 = Class3_Sub4.method108("", (byte)-118);
   static RSString aClass94_9 = Class3_Sub4.method108(")3)3)3", (byte)-128);
   static RSString aClass94_10 = Class3_Sub4.method108("::rect_debug", (byte)-128);
   static boolean aBoolean11 = false;
   public static int anInt12;
   public static boolean aBoolean13;
   static RSString aClass94_4 = aClass94_5;

   public final void focusLost(FocusEvent var1) {
      try {
         Class163_Sub2_Sub1.aBoolean4013 = false;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "rc.focusLost(" + (var1 != null?"{...}":"null") + ')');
      }
   }

   abstract void method25(byte var1);

   public final void windowClosing(WindowEvent var1) {
      try {
         this.destroy();
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "rc.windowClosing(" + (var1 != null?"{...}":"null") + ')');
      }
   }

   public final void windowIconified(WindowEvent var1) {}

   public static void method26(int var0) {
      try {
         if(var0 < 15) {
            method27((RSString)null, true);
         }

         aClass94_7 = null;
         aClass94_8 = null;
         aClass94_10 = null;
         aClass94_9 = null;
         aClass94_5 = null;
         aClass94_4 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "rc.W(" + var0 + ')');
      }
   }

   public final void windowDeactivated(WindowEvent var1) {}

   static final RSString method27(RSString var0, boolean var1) {
      try {
         if(!var1) {
            method26(-78);
         }

         int var2 = Class100.method1602(0, var0);
         return var2 != -1?Class119.aClass131_1624.aClass94Array1721[var2].method1560(Class3_Sub13_Sub16.aClass94_3192, true, Class3_Sub28_Sub10_Sub2.aClass94_4066):Class3_Sub28_Sub7_Sub1.aClass94_4049;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "rc.V(" + (var0 != null?"{...}":"null") + ',' + var1 + ')');
      }
   }

   public final AppletContext getAppletContext() {
      try {
         return null != Class3_Sub13_Sub7.aFrame3092?null:(Class38.aClass87_665 != null && this != Class38.aClass87_665.anApplet1219?Class38.aClass87_665.anApplet1219.getAppletContext():super.getAppletContext());
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "rc.getAppletContext()");
      }
   }

   public final void focusGained(FocusEvent var1) {
      try {
         Class163_Sub2_Sub1.aBoolean4013 = true;
         Class3_Sub13_Sub10.aBoolean3116 = true;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "rc.focusGained(" + (var1 != null?"{...}":"null") + ')');
      }
   }

   static final void method28(boolean var0) {
      try {
         Class143.aClass93_1874.method1524(3);
         if(!var0) {
            aBoolean11 = false;
         }

      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "rc.Q(" + var0 + ')');
      }
   }

   public final void windowClosed(WindowEvent var1) {}

   final boolean method29(int var1) {
      /*try {
         String var2 = this.getDocumentBase().getHost().toLowerCase();
         if(!var2.equals("jagex.com") && !var2.endsWith(".jagex.com")) {
            if(!var2.equals("runescape.com") && !var2.endsWith(".runescape.com")) {
               if(var1 != 27496) {
                  aClass94_10 = (Class94)null;
               }

               if(var2.endsWith("127.0.0.1")) {
                  return true;
               } else {
                  while(-1 > ~var2.length() && 48 <= var2.charAt(-1 + var2.length()) && 57 >= var2.charAt(-1 + var2.length())) {
                     var2 = var2.substring(0, -1 + var2.length());
                  }

                  if(!var2.endsWith("192.168.1.")) {
                     this.method31("invalidhost", var1 + -27544);
                     return false;
                  } else {
                     return true;
                  }
               }
            } else {
               return true;
            }
         } else {
            return true;
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "rc.T(" + var1 + ')');
      }*/
	   return true;
   }

   final synchronized void method30(byte var1) {
      try {
         if(Class3_Sub28_Sub12.aCanvas3648 != null) {
            Class3_Sub28_Sub12.aCanvas3648.removeFocusListener(this);
            Class3_Sub28_Sub12.aCanvas3648.getParent().remove(Class3_Sub28_Sub12.aCanvas3648);
         }

         Object var2;
         if(Class3_Sub13_Sub10.aFrame3121 != null) {
            var2 = Class3_Sub13_Sub10.aFrame3121;
         } else if(null != Class3_Sub13_Sub7.aFrame3092) {
            var2 = Class3_Sub13_Sub7.aFrame3092;
         } else {
            var2 = Class38.aClass87_665.anApplet1219;
         }

         ((Container)var2).setLayout((LayoutManager)null);
         Class3_Sub28_Sub12.aCanvas3648 = new Canvas_Sub2(this);
         if(var1 >= 30) {
            ((Container)var2).add(Class3_Sub28_Sub12.aCanvas3648);
            Class3_Sub28_Sub12.aCanvas3648.setSize(Class23.anInt454, Class140_Sub7.anInt2934);
            Class3_Sub28_Sub12.aCanvas3648.setVisible(true);
            if(var2 != Class3_Sub13_Sub7.aFrame3092) {
               Class3_Sub28_Sub12.aCanvas3648.setLocation(Class84.anInt1164, Class106.anInt1442);
            } else {
               Insets var3 = Class3_Sub13_Sub7.aFrame3092.getInsets();
               Class3_Sub28_Sub12.aCanvas3648.setLocation(Class84.anInt1164 + var3.left, var3.top + Class106.anInt1442);
            }

            Class3_Sub28_Sub12.aCanvas3648.addFocusListener(this);
            Class3_Sub28_Sub12.aCanvas3648.requestFocus();
            Class163_Sub2_Sub1.aBoolean4013 = true;
            Class3_Sub13_Sub10.aBoolean3116 = true;
            Class3_Sub13_Sub6.aBoolean3078 = true;
            Class3_Sub28_Sub5.aBoolean3593 = false;
            Class142.aLong1847 = Class5.method830((byte)-55);
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "rc.BA(" + var1 + ')');
      }
   }

   public final void destroy() {
      try {
         if(this == Class3_Sub29.anApplet_Sub1_2588 && !Class29.aBoolean554) {
            Class3_Sub9.aLong2313 = Class5.method830((byte)-55);
            Class3_Sub13_Sub34.method331(5000L, 64);
            Class3_Sub13_Sub10.aClass87_3125 = null;
            this.method35(46, false);
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "rc.destroy()");
      }
   }

   public final void update(Graphics var1) {
      try {
         this.paint(var1);
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "rc.update(" + (var1 != null?"{...}":"null") + ')');
      }
   }

   final void method31(String var1, int var2) {
      try {
         if(!this.aBoolean1) {
            this.aBoolean1 = true;
            System.out.println("error_game_" + var1);

            try {
               if(var2 != -48) {
                  aClass94_4 = (RSString)null;
               }

               this.getAppletContext().showDocument(new URL(this.getCodeBase(), "error_game_" + var1 + ".ws"), "_top");
            } catch (Exception var4) {
               ;
            }

         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "rc.U(" + (var1 != null?"{...}":"null") + ',' + var2 + ')');
      }
   }

   abstract void method32(byte var1);

   abstract void method33(int var1);

   public final URL getDocumentBase() {
      try {
         return null != Class3_Sub13_Sub7.aFrame3092?null:(Class38.aClass87_665 != null && this != Class38.aClass87_665.anApplet1219?Class38.aClass87_665.anApplet1219.getDocumentBase():super.getDocumentBase());
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "rc.getDocumentBase()");
      }
   }

   public final synchronized void paint(Graphics var1) {
      try {
         if(this == Class3_Sub29.anApplet_Sub1_2588 && !Class29.aBoolean554) {
            Class3_Sub13_Sub10.aBoolean3116 = true;
            if(Class137.aBoolean1784 && !Class138.aBoolean1807 && ~(-Class142.aLong1847 + Class5.method830((byte)-55)) < -1001L) {
               Rectangle var2 = var1.getClipBounds();
               if(var2 == null || ~var2.width <= ~Class3_Sub9.anInt2334 && ~Class70.anInt1047 >= ~var2.height) {
                  Class3_Sub28_Sub5.aBoolean3593 = true;
               }
            }

         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "rc.paint(" + (var1 != null?"{...}":"null") + ')');
      }
   }

   public final void windowDeiconified(WindowEvent var1) {}

   static final void method34(int var0) {
      try {
         if(null != Class44_Sub1.aClass155_2627) {
            Class44_Sub1.aClass155_2627.method2163(false);
         }

         if(var0 != -32589) {
            method26(-25);
         }

         if(Class3_Sub21.aClass155_2491 != null) {
            Class3_Sub21.aClass155_2491.method2163(false);
         }

         Class140_Sub3.method1959(256, 2, 22050, Class3_Sub13_Sub15.aBoolean3184);
         Class44_Sub1.aClass155_2627 = Class58.method1195(22050, Class38.aClass87_665, Class3_Sub28_Sub12.aCanvas3648, 0, 14);
         Class44_Sub1.aClass155_2627.method2154(114, Class86.aClass3_Sub24_Sub4_1193);
         Class3_Sub21.aClass155_2491 = Class58.method1195(2048, Class38.aClass87_665, Class3_Sub28_Sub12.aCanvas3648, 1, 14);
         Class3_Sub21.aClass155_2491.method2154(-126, Class3_Sub26.aClass3_Sub24_Sub2_2563);
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "rc.DA(" + var0 + ')');
      }
   }

   private final void method35(int var1, boolean var2) {
      try {
         synchronized(this) {
            if(Class29.aBoolean554) {
               return;
            }

            Class29.aBoolean554 = true;
         }

         if(Class38.aClass87_665.anApplet1219 != null) {
            Class38.aClass87_665.anApplet1219.destroy();
         }

         try {
            this.method32((byte)23);
         } catch (Exception var8) {
            ;
         }

         if(Class3_Sub28_Sub12.aCanvas3648 != null) {
            try {
               Class3_Sub28_Sub12.aCanvas3648.removeFocusListener(this);
               Class3_Sub28_Sub12.aCanvas3648.getParent().remove(Class3_Sub28_Sub12.aCanvas3648);
            } catch (Exception var7) {
               ;
            }
         }

         if(null != Class38.aClass87_665) {
            try {
               Class38.aClass87_665.method1445(0);
            } catch (Exception var6) {
               ;
            }
         }

         this.method33(126);
         if(var1 <= 31) {
            this.method40(78, -50, false, -104, (String)null, -76, -61, 49);
         }

         if(null != Class3_Sub13_Sub7.aFrame3092) {
            try {
               System.exit(0);
            } catch (Throwable var5) {
               ;
            }
         }

         System.out.println("Shutdown complete - clean:" + var2);
      } catch (RuntimeException var10) {
         throw Class44.method1067(var10, "rc.EA(" + var1 + ',' + var2 + ')');
      }
   }

   public final void windowActivated(WindowEvent var1) {}

   private final void method36(boolean var1) {
      try {
         long var2 = Class5.method830((byte)-55);
         long var4 = Class134.aLongArray1766[Class153.anInt1953];
         Class134.aLongArray1766[Class153.anInt1953] = var2;
         Class153.anInt1953 = 31 & Class153.anInt1953 - -1;
         synchronized(this) {
            if(!var1) {
               this.start();
            }

            Class3_Sub13_Sub6.aBoolean3078 = Class163_Sub2_Sub1.aBoolean4013;
         }

         this.method25((byte)107);
         if(0L != var4 && var2 <= var4) {
            ;
         }

      } catch (RuntimeException var9) {
         throw Class44.method1067(var9, "rc.R(" + var1 + ')');
      }
   }

   public static final void providesignlink(Class87 var0) {
      try {
         Class38.aClass87_665 = var0;
         Class3_Sub13_Sub10.aClass87_3125 = var0;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "rc.providesignlink(" + (var0 != null?"{...}":"null") + ')');
      }
   }

   private final void method37(int var1) {
      try {
         long var2 = Class5.method830((byte)-55);
         long var4 = Class163_Sub1.aLongArray2986[Class62.anInt950];
         if(var1 != 0) {
            aClass94_7 = (RSString)null;
         }

         Class163_Sub1.aLongArray2986[Class62.anInt950] = var2;
         Class62.anInt950 = 31 & Class62.anInt950 + 1;
         if(~var4 != -1L && var2 > var4) {
            int var6 = (int)(var2 + -var4);
            Class142.anInt1862 = (32000 + (var6 >> 1)) / var6;
         }

         if(50 < Class3_Sub13_Sub25.anInt3313++) {
            Class3_Sub13_Sub10.aBoolean3116 = true;
            Class3_Sub13_Sub25.anInt3313 -= 50;
            Class3_Sub28_Sub12.aCanvas3648.setSize(Class23.anInt454, Class140_Sub7.anInt2934);
            Class3_Sub28_Sub12.aCanvas3648.setVisible(true);
            if(Class3_Sub13_Sub7.aFrame3092 != null && null == Class3_Sub13_Sub10.aFrame3121) {
               Insets var8 = Class3_Sub13_Sub7.aFrame3092.getInsets();
               Class3_Sub28_Sub12.aCanvas3648.setLocation(var8.left + Class84.anInt1164, Class106.anInt1442 + var8.top);
            } else {
               Class3_Sub28_Sub12.aCanvas3648.setLocation(Class84.anInt1164, Class106.anInt1442);
            }
         }

         this.method38(40);
      } catch (RuntimeException var7) {
         throw Class44.method1067(var7, "rc.AA(" + var1 + ')');
      }
   }

   abstract void method38(int var1);

   public final URL getCodeBase() {
      return Class3_Sub13_Sub7.aFrame3092 == null?(null != Class38.aClass87_665 && this != Class38.aClass87_665.anApplet1219?Class38.aClass87_665.anApplet1219.getCodeBase():super.getCodeBase()):null;
   }

   public final void run() {
      try {
         try {
            if(null != Class87.aString1216) {
               String var1 = Class87.aString1216.toLowerCase();
               if(var1.indexOf("sun") == -1 && -1 == var1.indexOf("apple")) {
                  if(0 != ~var1.indexOf("ibm") && (Class87.aString1196 == null || Class87.aString1196.equals("1.4.2"))) {
                     this.method31("wrongjava", -48);
                     return;
                  }
               } else {
                  String var2 = Class87.aString1196;
                  if(var2.equals("1.1") || var2.startsWith("1.1.") || var2.equals("1.2") || var2.startsWith("1.2.")) {
                     this.method31("wrongjava", -48);
                     return;
                  }

                  Class132.anInt1737 = 5;
               }
            }

            int var7;
            if(null != Class87.aString1196 && Class87.aString1196.startsWith("1.")) {
               var7 = 2;

               int var9;
               for(var9 = 0; ~var7 > ~Class87.aString1196.length(); ++var7) {
                  char var3 = Class87.aString1196.charAt(var7);
                  if(~var3 > -49 || 57 < var3) {
                     break;
                  }

                  var9 = var9 * 10 - (-var3 - -48);
               }

               if(~var9 <= -6) {
                  Class137.aBoolean1784 = true;
               }
            }

            if(null != Class38.aClass87_665.anApplet1219) {
               Method var8 = Class87.aMethod1220;
               if(null != var8) {
                  try {
                     var8.invoke(Class38.aClass87_665.anApplet1219, new Object[]{Boolean.TRUE});
                  } catch (Throwable var4) {
                     ;
                  }
               }
            }

            Class3_Sub28_Sub18.method713(0);
            this.method30((byte)120);
            Class164_Sub1.aClass158_3009 = Class3_Sub13_Sub23_Sub1.method285(Class140_Sub7.anInt2934, Class23.anInt454, true, Class3_Sub28_Sub12.aCanvas3648);
            this.method39(2);
            Class3_Sub25.aClass129_2552 = Class36.method1012((byte)-31);

            while(-1L == ~Class3_Sub9.aLong2313 || Class3_Sub9.aLong2313 > Class5.method830((byte)-55)) {
               Class133.anInt1754 = Class3_Sub25.aClass129_2552.method1767(-1, Class132.anInt1737, Class44_Sub1.anInt2626);

               for(var7 = 0; var7 < Class133.anInt1754; ++var7) {
                  this.method36(true);
               }

               this.method37(0);
               Class81.method1400(Class38.aClass87_665, Class3_Sub28_Sub12.aCanvas3648, -80);
            }
         } catch (Exception var5) {
            Class49.method1125((String)null, var5, (byte)127);
            this.method31("crash", -48);
         }

         this.method35(107, true);
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "rc.run()");
      }
   }

   public final String getParameter(String var1) {
      try {
         return Class3_Sub13_Sub7.aFrame3092 == null?(Class38.aClass87_665 != null && this != Class38.aClass87_665.anApplet1219?Class38.aClass87_665.anApplet1219.getParameter(var1):super.getParameter(var1)):null;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "rc.getParameter(" + (var1 != null?"{...}":"null") + ')');
      }
   }

   abstract void method39(int var1);

   public final void stop() {
      try {
         if(Class3_Sub29.anApplet_Sub1_2588 == this && !Class29.aBoolean554) {
            Class3_Sub9.aLong2313 = 4000L + Class5.method830((byte)-55);
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "rc.stop()");
      }
   }

   public abstract void init();

   final void method40(int var1, int var2, boolean var3, int var4, String var5, int var6, int var7, int var8) {
      try {
         if(var7 != -8057) {
            this.method38(12);
         }

         try {
            Class140_Sub7.anInt2934 = var6;
            Class70.anInt1047 = var6;
            Class84.anInt1164 = 0;
            Class3_Sub13_Sub23_Sub1.anInt4033 = var2;
            Class23.anInt454 = var4;
            Class3_Sub9.anInt2334 = var4;
            Class106.anInt1442 = 0;
            Class3_Sub29.anApplet_Sub1_2588 = this;
            Class3_Sub13_Sub7.aFrame3092 = new Frame();
            Class3_Sub13_Sub7.aFrame3092.setTitle("Jagex");
            Class3_Sub13_Sub7.aFrame3092.setResizable(true);
            Class3_Sub13_Sub7.aFrame3092.addWindowListener(this);
            Class3_Sub13_Sub7.aFrame3092.setVisible(true);
            Class3_Sub13_Sub7.aFrame3092.toFront();
            Insets var9 = Class3_Sub13_Sub7.aFrame3092.getInsets();
            Class3_Sub13_Sub7.aFrame3092.setSize(var9.left + Class3_Sub9.anInt2334 + var9.right, var9.top + Class70.anInt1047 + var9.bottom);
            Class3_Sub13_Sub10.aClass87_3125 = Class38.aClass87_665 = new Class87((Applet)null, var1, var5, var8);
            Class64 var10 = Class38.aClass87_665.method1451(0, 1, this);

            while(0 == var10.anInt978) {
               Class3_Sub13_Sub34.method331(10L, 64);
            }

            Class17.aThread409 = (Thread)var10.anObject974;
         } catch (Exception var11) {
            Class49.method1125((String)null, var11, (byte)115);
         }

      } catch (RuntimeException var12) {
         throw Class44.method1067(var12, "rc.S(" + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + (var5 != null?"{...}":"null") + ',' + var6 + ',' + var7 + ',' + var8 + ')');
      }
   }

   public final void windowOpened(WindowEvent var1) {}

   public final void start() {
      try {
         if(Class3_Sub29.anApplet_Sub1_2588 == this && !Class29.aBoolean554) {
            Class3_Sub9.aLong2313 = 0L;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "rc.start()");
      }
   }

   final void method41(byte var1, int var2, int var3, int var4, int var5) {
      try {
         try {
            if(Class3_Sub29.anApplet_Sub1_2588 != null) {
               ++Class36.anInt639;
               if(~Class36.anInt639 <= -4) {
                  this.method31("alreadyloaded", -48);
                  return;
               }

               this.getAppletContext().showDocument(this.getDocumentBase(), "_self");
               return;
            }

            Class3_Sub29.anApplet_Sub1_2588 = this;
            Class106.anInt1442 = 0;
            Class3_Sub13_Sub23_Sub1.anInt4033 = var4;
            if(var1 >= -23) {
               aClass94_5 = (RSString)null;
            }

            Class23.anInt454 = var2;
            Class3_Sub9.anInt2334 = var2;
            Class84.anInt1164 = 0;
            Class140_Sub7.anInt2934 = var5;
            Class70.anInt1047 = var5;
            String var6 = this.getParameter("openwinjs");
            if(var6 != null && var6.equals("1")) {
               Class3_Sub28_Sub6.aBoolean3594 = true;
            } else {
               Class3_Sub28_Sub6.aBoolean3594 = false;
            }

            if(null == Class38.aClass87_665) {
               Class3_Sub13_Sub10.aClass87_3125 = Class38.aClass87_665 = new Class87(this, var3, (String)null, 0);
            }

            Class64 var7 = Class38.aClass87_665.method1451(0, 1, this);

            while(~var7.anInt978 == -1) {
               Class3_Sub13_Sub34.method331(10L, 64);
            }

            Class17.aThread409 = (Thread)var7.anObject974;
         } catch (Exception var8) {
            Class49.method1125((String)null, var8, (byte)113);
            this.method31("crash", -48);
         }

      } catch (RuntimeException var9) {
         throw Class44.method1067(var9, "rc.CA(" + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ')');
      }
   }

}
