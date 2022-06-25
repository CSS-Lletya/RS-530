package com.xeno.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import com.xeno.model.ItemDefinition;

public class ItemXMLToBinary {

	public static void main(String[] args) throws IOException {
		new ItemXMLToBinary().start();
	}

	@SuppressWarnings("unchecked")
	public void start() throws IOException {
		System.out.println("Loading...");
		List<ItemDefinition> defs = (List<ItemDefinition>) XStreamUtil.getXStream().fromXML(new FileInputStream("data/itemDefinitions.xml"));
		System.out.println("Done.");
		System.gc();
		System.out.println("Saving...");
		RandomAccessFile raf = new RandomAccessFile(new File("data/itemDefinitions.dat"), "rw");
		raf.writeInt(defs.size());
		for(ItemDefinition def : defs) {
			raf.writeShort(def.getId());
			raf.writeShort(def.getEquipId());
			raf.writeBoolean(def.isNoted());
			raf.writeBoolean(def.isStackable());
			raf.writeInt(def.getPrice().getNormalPrice());
			raf.writeInt(def.getPrice().getMinimumPrice());
			raf.writeInt(def.getPrice().getMaximumPrice());
			for(int i = 0; i < 13; i++) {
				raf.writeByte(def.getBonus(i));
			}
			raf.writeBytes(def.getName());
			raf.writeByte(0);
			raf.writeBytes(def.getExamine());
			raf.writeByte(0);
			if(def.getId() % 100 == 0) {
				System.out.println(def.getId() + "/" + defs.size());
			}
		}
		raf.close();
		System.out.println("Done.");
	}

}
