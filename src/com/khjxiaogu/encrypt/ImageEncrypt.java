package com.khjxiaogu.encrypt;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageEncrypt {
	Random rnd = new SecureRandom();
	static JTextField je;
	static JLabel l;

	private strictfp static void encrypt() throws IOException {
		JFileChooser jfc = new JFileChooser(new File("./"));
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.setDialogTitle("选择图片文件");
		FileNameExtensionFilter restrict = new FileNameExtensionFilter("图片文件", "jpg", "jpeg", "png", "bmp", "tiff");
		jfc.addChoosableFileFilter(restrict);
		if (jfc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
			return;
		ImageEncrypt.l.setText("读取中...");
		BufferedImage bi = ImageIO.read(jfc.getSelectedFile());
		int w = bi.getWidth();
		int h = bi.getHeight();
		BufferedImage bo = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		ByteBuffer keybuff = ByteBuffer.allocateDirect(16);
		int ivl = 0;
		int ivc = 0;
		int iva = 0;
		ImageEncrypt.l.setText("加密中...");
		PerlinNoise pn = new PerlinNoise(ImageEncrypt.je.getText());
		int[] ivt = new int[w];
		for (int i = 0; i < h; i++) {
			int rgb = 0;
			for (int j = 0; j < w; j++) {
				double xd = j;
				double yd = i;
				int clr;
				clr = bi.getRGB(j, i);
				keybuff.clear();
				keybuff.put((byte) 0);
				keybuff.put(pn.at(xd, yd, 1.1));
				keybuff.put(pn.at(xd, yd, 2.2));
				keybuff.put(pn.at(xd, yd, 3.3));
				keybuff.put((byte) 0);
				keybuff.put(pn.at(xd, yd, 4.4));
				keybuff.put(pn.at(xd, yd, 5.5));
				keybuff.put(pn.at(xd, yd, 6.6));
				int key2 = keybuff.getInt(1);
				keybuff.put((byte) 0);
				keybuff.put(pn.at(xd, yd, 7.7));
				keybuff.put(pn.at(xd, yd, 8.8));
				keybuff.put(pn.at(xd, yd, 9.9));
				int key = keybuff.getInt(0);
				int key3 = keybuff.getInt(2);
				rgb = clr ^ key ^ key2 ^ key3 ^ ivl ^ ivc ^ iva ^ ivt[j];
				ivt[j] = rgb;
				ivl = rgb;
				iva = (int) (((long) iva + (long) rgb) % 0xffffffff);
				bo.setRGB(j, i, rgb);

			}
			iva = 0;
			ivc = (int) (((long) ivc + (long) rgb) % 0xffffffff);
		}
		ImageEncrypt.l.setText("完成！");
		JFileChooser jfc2 = new JFileChooser(new File("./"));
		jfc2.setAcceptAllFileFilterUsed(false);
		jfc2.setDialogTitle("保存加密的图片文件");
		FileNameExtensionFilter restrict2 = new FileNameExtensionFilter("图片文件", "png", "bmp", "tiff");
		jfc2.addChoosableFileFilter(restrict2);
		if (jfc2.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
			return;
		File f = jfc2.getSelectedFile();
		int i = f.getName().lastIndexOf('.');
		String extension = "";
		if (i > 0) {
			extension = f.getName().substring(i + 1);
		}
		ImageIO.write(bo, extension, f);
	}

	private strictfp static void decrypt() throws IOException {
		JFileChooser jfc = new JFileChooser(new File("./"));
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.setDialogTitle("选择图片文件");
		FileNameExtensionFilter restrict = new FileNameExtensionFilter("图片文件", "jpg", "jpeg", "png", "bmp", "tiff");
		jfc.addChoosableFileFilter(restrict);
		if (jfc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
			return;
		ImageEncrypt.l.setText("读取中...");
		BufferedImage bi = ImageIO.read(jfc.getSelectedFile());
		int w = bi.getWidth();
		int h = bi.getHeight();
		BufferedImage bo = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		ByteBuffer keybuff = ByteBuffer.allocateDirect(16);
		int ivl = 0,ivc = 0,iva = 0;//行向量，行首向量，总向量
		ImageEncrypt.l.setText("加密中...");
		PerlinNoise pn = new PerlinNoise(ImageEncrypt.je.getText());
		int[] ivt = new int[w];//列向量
		for (int i = 0; i < h; i++) {
			int clr = 0;
			for (int j = 0; j < w; j++) {
				double xd = j;
				double yd = i;
				clr = bi.getRGB(j, i);
				keybuff.clear();
				keybuff.put((byte) 0);
				keybuff.put(pn.at(xd, yd, 1.1));
				keybuff.put(pn.at(xd, yd, 2.2));
				keybuff.put(pn.at(xd, yd, 3.3));
				keybuff.put((byte) 0);
				keybuff.put(pn.at(xd, yd, 4.4));
				keybuff.put(pn.at(xd, yd, 5.5));
				keybuff.put(pn.at(xd, yd, 6.6));
				int key2 = keybuff.getInt(1);
				keybuff.put((byte) 0);
				keybuff.put(pn.at(xd, yd, 7.7));
				keybuff.put(pn.at(xd, yd, 8.8));
				keybuff.put(pn.at(xd, yd, 9.9));
				int key = keybuff.getInt(0);
				int key3 = keybuff.getInt(2);
				int rgb = clr ^ key ^ key2 ^ key3 ^ ivl ^ ivc ^ iva ^ ivt[j];
				ivt[j] = clr;
				ivl = clr;
				iva = (int) (((long) iva + (long) clr) % 0xffffffff);
				bo.setRGB(j, i, rgb);
			}
			iva = 0;
			ivc = (int) (((long) ivc + (long) clr) % 0xffffffff);
		}
		ImageEncrypt.l.setText("完成！");
		JFileChooser jfc2 = new JFileChooser(new File("./"));
		jfc2.setAcceptAllFileFilterUsed(false);
		jfc2.setDialogTitle("保存加密的图片文件");
		FileNameExtensionFilter restrict2 = new FileNameExtensionFilter("图片文件", "jpg", "jpeg", "png", "bmp", "tiff");
		jfc2.addChoosableFileFilter(restrict2);
		if (jfc2.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
			return;
		File f = jfc2.getSelectedFile();
		int i = f.getName().lastIndexOf('.');
		String extension = "";
		if (i > 0) {
			extension = f.getName().substring(i + 1);
		}
		ImageIO.write(bo, extension, f);
	}

	public strictfp static void main(String[] args) throws IOException {
		JFrame f = new JFrame("简易图片加密");
		f.setLayout(new BoxLayout(f.getContentPane(),BoxLayout.Y_AXIS));
		f.setSize(400, 400);
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setResizable(false);
		JButton button1 = new JButton("加密图片");
		JButton button2 = new JButton("解密图片");
		ImageEncrypt.je = new JTextField(30);
		ImageEncrypt.l = new JLabel("请选择操作！");

		button1.addActionListener(ev -> {
			try {
				ImageEncrypt.encrypt();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		button2.addActionListener(ev -> {
			try {
				ImageEncrypt.decrypt();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		JPanel p = new JPanel();
		JPanel p2 = new JPanel();
		JPanel text=new JPanel();
		p.add(button1);
		p.add(button2);
		p2.add(new JLabel("密码："));
		p2.add(ImageEncrypt.je);
		p2.setSize(400, p2.getWidth());
		f.add(p);
		f.add(p2);
		l.setMaximumSize(new Dimension(Integer.MAX_VALUE, l.getMinimumSize().height));
		f.add(ImageEncrypt.l);
		f.add(Box.createVerticalGlue());
		text.add(new JLabel("提示：仅供娱乐，不保证加密安全。请勿用于生产用途。"));
		text.add(new JLabel("加密后的图片请务必原图发送，否则解密会出现偏差。"));
		text.add(new JLabel("作者：khj小骨(Github @khjxiaogu)，联系方式：khjxiaogu@qq.com"));
		text.add(new JLabel("Copyright (C) 2020 khjxiaogu,all rights reserved."));
		text.setMaximumSize(new Dimension(Integer.MAX_VALUE, text.getMaximumSize().height));
		text.setMinimumSize(new Dimension(text.getMinimumSize().width, text.getMaximumSize().height));
		f.add(text);
		f.setVisible(true);
		// System.out.println("finished");
	}

}

class PerlinNoise {
	Random noiseRandom;

	public PerlinNoise() {
		noiseRandom = new Random();
		PerlinNoise.shuffleArray(permutation, noiseRandom);
		for (int i = 0; i < 256; i++) {
			p[256 + i] = p[i] = permutation[i];
		}
		;
	}

	public PerlinNoise(long seed) {
		noiseRandom = new Random(seed);
		PerlinNoise.shuffleArray(permutation, noiseRandom);
		for (int i = 0; i < 256; i++) {
			p[256 + i] = p[i] = permutation[i];
		}
		;
	}

	public PerlinNoise(String seed) {
		noiseRandom = new Random(PerlinNoise.stringToSeed(seed));
		PerlinNoise.shuffleArray(permutation, noiseRandom);
		for (int i = 0; i < 256; i++) {
			p[256 + i] = p[i] = permutation[i];
		}
		;
	}

	static void shuffleArray(int[] ar, Random rnd) {
		// If running on Java 6 or older, use `new Random()` on RHS here
		for (int i = ar.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			// Simple swap
			int a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}

	static long stringToSeed(String s) {
		if (s == null)
			return 0;
		long hash = 0;
		for (char c : s.toCharArray()) {
			hash = 31L * hash + c;
		}
		return hash;
	}

	strictfp public byte at(double x, double y, double z) {
		int X = (int) Math.floor(x) & 255, // FIND UNIT CUBE THAT
				Y = (int) Math.floor(y) & 255, // CONTAINS POINT.
				Z = (int) Math.floor(z) & 255;
		x -= Math.floor(x); // FIND RELATIVE X,Y,Z
		y -= Math.floor(y); // OF POINT IN CUBE.
		z -= Math.floor(z);
		double u = PerlinNoise.fade(x), // COMPUTE FADE CURVES
				v = PerlinNoise.fade(y), // FOR EACH OF X,Y,Z.
				w = PerlinNoise.fade(z);
		int A = p[X] + Y, AA = p[A] + Z, AB = p[A + 1] + Z, // HASH COORDINATES OF
				B = p[X + 1] + Y, BA = p[B] + Z, BB = p[B + 1] + Z; // THE 8 CUBE CORNERS,
		long result = Double.doubleToRawLongBits(PerlinNoise.lerp(w,
				PerlinNoise.lerp(v, PerlinNoise.lerp(u, PerlinNoise.grad(p[AA], x, y, z), // AND ADD
						PerlinNoise.grad(p[BA], x - 1, y, z)), // BLENDED
						PerlinNoise.lerp(u, PerlinNoise.grad(p[AB], x, y - 1, z), // RESULTS
								PerlinNoise.grad(p[BB], x - 1, y - 1, z))), // FROM 8
				PerlinNoise.lerp(v, PerlinNoise.lerp(u, PerlinNoise.grad(p[AA + 1], x, y, z - 1), // CORNERS
						PerlinNoise.grad(p[BA + 1], x - 1, y, z - 1)), // OF CUBE
						PerlinNoise.lerp(u, PerlinNoise.grad(p[AB + 1], x, y - 1, z - 1),
								PerlinNoise.grad(p[BB + 1], x - 1, y - 1, z - 1)))));
		return (byte) (result >> 0x46 & 0xff ^ result >> 0x52 & 0xff);
	}

	static strictfp double fade(double t) {
		return t * t * t * (t * (t * 6 - 15) + 10);
	}

	static strictfp double lerp(double t, double a, double b) {
		return a + t * (b - a);
	}

	static strictfp double grad(int hash, double x, double y, double z) {
		int h = hash & 15; // CONVERT LO 4 BITS OF HASH CODE
		double u = h < 8 ? x : y, // INTO 12 GRADIENT DIRECTIONS.
				v = h < 4 ? y : h == 12 || h == 14 ? x : z;
		return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
	}

	final int p[] = new int[512], permutation[] = { 151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7,
			225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62,
			94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175,
			74, 165, 71, 134, 139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92,
			41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18,
			169, 200, 196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124,
			123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223,
			183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253,
			19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210,
			144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184,
			84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141,
			128, 195, 78, 66, 215, 61, 156, 180 };
}
