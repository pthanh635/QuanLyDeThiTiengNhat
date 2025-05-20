package AI;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TextToSpeechUtil {
	
    public static String taoFileAmThanhTuVanBan(String vanBan, String fileLuu) throws IOException {
        String encodedText = URLEncoder.encode(vanBan, "UTF-8");
        String googleTTSUrl = "https://translate.google.com/translate_tts?ie=UTF-8&q=" + encodedText
                + "&tl=ja&client=tw-ob";
        URL url = new URL(googleTTSUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");

        try (InputStream in = conn.getInputStream()) {
            Files.createDirectories(Paths.get(fileLuu).getParent()); // Tạo thư mục nếu chưa có
            try (OutputStream out = new FileOutputStream(fileLuu)) {
                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
        }

        return fileLuu;
    }
}
