package com.xcyo.baselib.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DataUtils {

	private static final String ecoding = "UTF-8";

	public static final String getEcoding() {
		return ecoding;
	}

	private static int[] key_space = new int[] { 0x420c5591, 0xe1114919,
			0x11f7604f, 0x25b9fc88, 0x8fb7e697, 0xc03966eb, 0x7a940aa7,
			0x7625ec16, 0xaa27064c, 0xa9ba465d, 0xed3f48ce, 0x86042063,
			0xde4a7724, 0x35a18162, 0xb4ea22c4, 0xf6ba26d1, 0xb44c0f9b,
			0x29941d26, 0x210098cf, 0x5532eb38, 0xb700e00f, 0xdffbf8a5,
			0x93424f1f, 0x9ccfbb31, 0xc27228fd, 0x54609158, 0xac3d2204,
			0x129e5cc2, 0x6befc158, 0x99f67bfd, 0x7e049a65, 0x7af7af8f,
			0x35934c47, 0x820be029, 0xf8a49f4b, 0x46b3507f, 0x5fd8318d,
			0x3f482d7a, 0x14f9b09c, 0xd148025e, 0xa77edca1, 0x822ae076,
			0x3f067eb3, 0xb4124bf1, 0x903996ad, 0x361338b3, 0x448f65b9,
			0x84e064c5, 0xb3847c82, 0x50b30408, 0xe224f808, 0x2c464dba,
			0x2379c239, 0xb942ae99, 0xda475dc2, 0x288bdb58, 0xa8601989,
			0xb2542010, 0xee6c96ee, 0x1136d5d0, 0x3cc6606e, 0xe8b9935b,
			0xa86ccf03, 0xbdbb5bfe, 0x4d96e990, 0x518acf78, 0x8a608ef7,
			0xfe1b91fa, 0x38a1e931, 0xaee57f32, 0xf884ba5d, 0xd47328fe,
			0x795fca6a, 0x69f17029, 0x735f60da, 0xb70e65bc, 0xd00613bc,
			0x6401ac85, 0x484a27f1, 0x5a2a2049, 0x18008116, 0x367ede96,
			0x392dc774, 0x121f7c63, 0xe51f90d5, 0x373f18b9, 0x3ba27067,
			0x66f9d415, 0xb094e438, 0x16b04d77, 0xb17843fa, 0xb6586d66,
			0xb6f317fe, 0x623f0880, 0xa6ede877, 0x1c7a7ac5, 0xb857fed4,
			0xe2226572, 0x8d5d5db4, 0xfdd6c2c8, 0xeefde3be, 0xab5b8cf5,
			0x4f427892, 0x43bf65f7, 0xad482acb, 0x9441300d, 0xb2bf5395,
			0x2bc1c73d, 0xd98c87bf, 0xd36cd3c4, 0xb3ffa927, 0x6452ec1f,
			0x9b7a964c, 0x209af5f8, 0x1d010095, 0x54911583, 0x6dbeb953,
			0xf05d841f, 0x3d54eab1, 0x424a544d, 0x9d144e7c, 0x6c8e7d3e,
			0x4c55ada2, 0xe5f362c9, 0x3f37a465, 0x6b687f31, 0x52a437ce,
			0x931a5585, 0xc62218ee, 0x17abc102, 0x1113c873, 0xe1be7b8d,
			0x66d556c2, 0x53067948, 0xc5759098, 0x479a962a, 0x9b2a6805,
			0xcd7c58a7, 0xf5d0890c, 0x5fd4145c, 0xfd588748, 0x965f6cae,
			0xf3fabd8a, 0x207c55b9, 0x7e55ddd0, 0x66a0c99e, 0xc2e2324a,
			0xf437e0f6, 0x50d2896e, 0x59ae8e1d, 0xfdd71f4b, 0x1dfcdba4,
			0xdc031ed6, 0x6bb659dd, 0x2c9b57a1, 0x8a70f5e2, 0x9b7708c8,
			0x90cc2536, 0x2eaaaab5, 0xe8449431, 0x905203a9, 0xd080a04c,
			0xaeda9ccb, 0x41230a14, 0x7230bc21, 0x7460ecb1, 0xa58e7fd9,
			0x97923121, 0x6736f7b0, 0xcfb7f013, 0x4bca4ebd, 0x64bceb6a,
			0x54453865, 0x99f521e6, 0x3b62be11, 0x91330e6f, 0x5ecdd1cf,
			0x6378a755, 0xf5641d9f, 0x78129607, 0x9e130328, 0xc2159098,
			0x9469599b, 0x992bed8a, 0x1dd955c4, 0xc5621b55, 0x3314b0fa,
			0x2d25e3da, 0xe7b06bd4, 0xdc55c5ac, 0xcf17b300, 0xda65e315,
			0x5b408f73, 0x25db327c, 0xdcb281b4, 0x69b30f29, 0xc83f12c6,
			0x4efbffff, 0xa5aa39e3, 0xd71d0013, 0x74c6ae0d, 0xbf76c179,
			0xb10f03ab, 0x479600aa, 0x51573779, 0xa0ff71e0, 0x6d45cb4f,
			0xd16ff4aa, 0x67d3fcb9, 0xb7bb88fe, 0x7a5b6347, 0xfc95702e,
			0xf07d396b, 0x26a1e409, 0xb62e2acd, 0xb026a4c8, 0x33adbb64,
			0x613f57a6, 0xb4d8ea06, 0x2d8653af, 0xd29cc351, 0xe006e58e,
			0xd88a95a7, 0x7f37c0e6, 0xbf80579b, 0x97c30abe, 0xb8afadac,
			0xb0fb6f39, 0xabd21a69, 0x9e200240, 0xde7a7ef9, 0x2e319236,
			0x75535bb8, 0x2266a653, 0x9dba8379, 0xfee10a19, 0xa3c631ef,
			0x4e4cd5a2, 0xcc6408aa, 0x84f81e56, 0xf86f304a, 0x38aa921e,
			0xf8415e4d, 0xc8a70eb2, 0xf4f46926, 0xf50ad237, 0x934c1c4c,
			0x36a2e057, 0x47df6869, 0x7e43e45a, 0xd8423d8a, 0x700e98a9,
			0x8aca0fd9, 0xa61d9b68, 0xd8c51f3f, 0xadab2b3e, };

	private static int[] key_index = new int[] { 20, 21, 14, 15, 1, 2, 1, 2 };

	public static byte[] getKeyCode() {
		ByteArrayOutputStream op = new ByteArrayOutputStream();
		try {
			for (int key : key_index) {
				int c = key_space[key];
				op.write(int2byte(c));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if(op != null)
                    op.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return op.toByteArray();
	}

	/* 将int 转为 byte[] */
	public static byte[] int2byte(int number) {
		int temp = number;
		byte[] b = new byte[4];
		for (int i = 0; i < b.length; i++) {
			b[i] = new Long(temp & 0xff).byteValue();// 将最低位保存在最低位
			temp = temp >> 8; // 向右移8位
		}
		return b;
	}

	/* zlib压缩 */
	public static byte[] compress(byte[] data) {
		if (data == null)
			return null;
		byte[] output = new byte[0];
		java.util.zip.Deflater compresser = new java.util.zip.Deflater();
		compresser.reset();
		compresser.setInput(data);
		compresser.finish();

		ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[1024];
			while (!compresser.finished()) {
				int i = compresser.deflate(buf);
				bos.write(buf, 0, i);
			}
			output = bos.toByteArray();
		} catch (Exception e) {
			output = data;
			e.printStackTrace();
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		compresser.end();
		return output;
	}
	/*zlib解压缩*/
	public static byte[] decompress(byte[] data) {
		byte[] output = new byte[0];

		Inflater decompresser = new Inflater();
		decompresser.reset();
		decompresser.setInput(data);

		ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[1024];
			while (!decompresser.finished()) {
				int i = decompresser.inflate(buf);
				o.write(buf, 0, i);
			}
			output = o.toByteArray();
		} catch (Exception e) {
			output = data;
			e.printStackTrace();
		} finally {
			try {
				o.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		decompresser.end();
		return output;
	}

	/* zip压缩 */
	public static byte[] zipCompress(String content) {
		if (content == null)
			return null;
		ByteArrayOutputStream bts = null;
		GZIPOutputStream zps = null;
		try {
			bts = new ByteArrayOutputStream();
			zps = new GZIPOutputStream(bts);
			zps.write(content.getBytes());
			zps.close();
			// return bts.toString("UTF-8");
			return bts.toByteArray();
		} catch (IOException e) {
		} finally {
			try {
				if (bts != null) {
					bts.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/* zip解压缩 */
	public static String zipUnCompress(String content) {
		if (content == null)
			return null;
		ByteArrayInputStream byString = null;
		GZIPInputStream gzip = null;
		ByteArrayOutputStream bops = null;
		try {
			bops = new ByteArrayOutputStream();
			byString = new ByteArrayInputStream(content.getBytes("ISO-8859-1"));
			gzip = new GZIPInputStream(byString);
			byte[] bt = new byte[256];
			int count = -1;
			while ((count = gzip.read(bt)) != -1) {
				bops.write(bt, 0, count);
			}
			return bops.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (byString != null) {
					byString.close();
				}
				if (gzip != null) {
					gzip.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	/* AES加密 */
	public final static byte[] encrypt(byte[] byteContent) {
		if (byteContent == null) {
			return null;
		}
		try {
			SecretKeySpec key = new SecretKeySpec(getKeyCode(), 0, 32, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");// 创建密码器


			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			md.update(byteContent);
			byte tmp[] = md.digest();
			IvParameterSpec iv = new IvParameterSpec(tmp);


			cipher.init(Cipher.ENCRYPT_MODE, key, iv);// 初始化


			int blockSize = cipher.getBlockSize();

			int plaintextLength = byteContent.length;
			plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
//			if (plaintextLength % blockSize != 0) {
//			}

			byte[] plaintext = new byte[plaintextLength];
			System.arraycopy(byteContent, 0, plaintext, 0, byteContent.length);

			int size = plaintextLength - byteContent.length;
			for(int i=0;i<size;i++){
				plaintext[byteContent.length+i] = new Integer(size).byteValue();
			}


			byte[] result = cipher.doFinal(plaintext);

			byte[] a = new byte[result.length+tmp.length];
			System.arraycopy(tmp, 0, a, 0, tmp.length);
			System.arraycopy(result, 0, a, tmp.length, result.length);

			return a;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	/* AES解密 */
	public final static byte[] decrypt(byte[] byteContent) {
		if (byteContent == null) {
			return null;
		}
		try {
			SecretKeySpec key = new SecretKeySpec(getKeyCode(), 0, 32, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");// 创建密码器

			byte tmp[] = new byte[16];
			System.arraycopy(byteContent, 0, tmp, 0, 16);
			IvParameterSpec iv = new IvParameterSpec(tmp);
			cipher.init(Cipher.DECRYPT_MODE, key, iv);// 初始化

			byte[] plaintext = new byte[byteContent.length-16];
			System.arraycopy(byteContent, 16, plaintext, 0, byteContent.length-16);

			byte[] result = cipher.doFinal(plaintext);

			byte lastByte = result[result.length-1];
			int size = 0;
			for(int i = result.length -1;i>=0;i--){
				if(result[i]==lastByte){
					size++;
				}else {
					break;
				}
			}
			byte[] des = new byte[result.length-size];
			System.arraycopy(result, 0,des, 0,result.length-size);

			return des; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/* base64生成字符串 */
	public static String getBase64(byte[] byteContent) {
		String s = null;
		if (byteContent != null) {
			s = new String(android.util.Base64.encode(byteContent, android.util.Base64.DEFAULT));
		}
		return s;
	}

	/* base64生成byte[] */
	public static byte[] getFromBase64(String s) {
		byte[] byteContent = null;
		if (s != null) {
			try {
				byteContent = android.util.Base64.decode(s.getBytes(), android.util.Base64.DEFAULT);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return byteContent;
	}

}
