package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//パスワードハッシュ化

public class PasswordUtil {

	public static String hash(String rawPassword) {
		try {
			// "SHA-256"アルゴリズムの MessageDigest オブジェクトを取得
			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			// パスワードをバイト配列に変換し、ハッシュを計算
			byte[] hashedBytes = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));

			// バイト配列を16進数の文字列に変換
			StringBuilder sb = new StringBuilder();
			for (byte b : hashedBytes) {
				// %02x は、バイトを2桁の16進数（0埋め）でフォーマットする指定
				sb.append(String.format("%02x", b));
			}

			return sb.toString();

		} catch (NoSuchAlgorithmException e) {
			// SHA-256アルゴリズムが見つからない
			e.printStackTrace();
			throw new RuntimeException("ハッシュ化アルゴリズムが見つかりません", e);
		}
	}
}