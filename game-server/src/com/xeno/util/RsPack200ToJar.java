package com.xeno.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * This unpacks a jagex made .pack200 file, which they slightly change to give me a headache.
 *
 * @author moparisthebest
 */
public class RsPack200ToJar {

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage: RsPack200ToJar inFile outFile");
            return;
        }
        File inFile = new File(args[0]);
        FileInputStream fis = new FileInputStream(inFile);
        byte abyte0[] = new byte[(int) inFile.length()];
        fis.read(abyte0);
        //jagex strips the gzip header, so tack it back on at the beginning
        byte abyte1[] = new byte[abyte0.length + 2];
        abyte1[0] = 31;
        abyte1[1] = -117;
        System.arraycopy(abyte0, 0, abyte1, 2, abyte0.length);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //unpack the .pack200 to a byte array
        Pack200.newUnpacker().unpack(new GZIPInputStream(new ByteArrayInputStream(abyte1)), new JarOutputStream(bos));
        // write the byte array to a jar file the way jagex unpacks it, I don't exactly know why if you have the above
        // statement dump directly to a file it is not in a valid zip format...
        byte abyte2[] = new byte[1000];
        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(bos.toByteArray()));
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(args[1]));
        do {
            ZipEntry zipentry = zis.getNextEntry();
            if (zipentry == null)
                break;
            zos.putNextEntry(zipentry);
            do {
                int i = zis.read(abyte2, 0, 1000);
                if (~i == 0)
                    break;
                zos.write(abyte2, 0, i);
            } while (true);
        } while (true);
        zis.close();
        zos.close();
    }
}

