package com.zsl.swing.redis.desktop.utils;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * 
 * @author 张帅令
 * @description  有关icon的一些操作
 *
 */
public class IconUtils {

	private IconUtils() {}
	
	public static ImageIcon getScaleImageIcon(String file,int width,int height) {
		URL resource = IconUtils.class.getResource(file);
		ImageIcon imageIcon = new ImageIcon(resource);
		Image image = imageIcon.getImage();
		Image targetImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(targetImage);
	}
	
	public static Image getScaleImage(String imgPath,int width,int height) {
		ImageIcon imgIcon = new ImageIcon(IconUtils.class.getResource(imgPath));
		Image image = imgIcon.getImage();
		return image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	}
}
