package com.google.zxing.aztec.encoder;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.datamatrix.encoder.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.datamatrix.encoder.SymbolShapeHint;

public class TestBorder {
	public static void main(String[] args) {
		//System.out.println("Hello World!");

		Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
		hints.put(EncodeHintType.DATA_MATRIX_SHAPE, SymbolShapeHint.FORCE_SQUARE);
		//hints.put(EncodeHintType.MARGIN, 50);

		int bigEnough = 200;


		DataMatrixWriter writer = new DataMatrixWriter();
		BitMatrix matrix = writer.encode("\\F010594001099999210ab\\F1718010121GOAOBQFYCWHMK", BarcodeFormat.DATA_MATRIX, bigEnough, bigEnough, hints);
		BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(matrix);
		BufferedImage qr2Image = HighLevelEncoder.whiteBorder(qrImage,300); //metoda din ztec.encoder/HighLevelEncoder

		try {
			ImageIO.write(HighLevelEncoder.whiteBorder(qrImage, 200), "png", new FileOutputStream("/home/alexandru/proiecte/tecnetv4/tmp/image2.png"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
