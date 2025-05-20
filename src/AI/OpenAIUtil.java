package AI;

import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;

public class OpenAIUtil {
    private static final String API_KEY = "sk-xxx";
    private static final String ENDPOINT = "https://api.openai.com/v1/chat/completions";

    public static String goiYDeThiTuChuDe(String chuDe, String loai) throws IOException {
        OkHttpClient client = new OkHttpClient();

        String prompt;
        if (loai.equalsIgnoreCase("nghe")) {
            prompt = "Tạo 1 câu hỏi trắc nghiệm tiếng Nhật thuộc dạng hội thoại 2 người hoặc mô tả về chủ đề: " + chuDe +
                    ". Trả về theo JSON sau: {cauHoi:..., dung:..., sai1:..., sai2:..., sai3:..., audio:...}. Gợi ý một câu ngắn phù hợp với âm thanh.";
        } else {
            prompt = "Tạo 1 câu hỏi trắc nghiệm tiếng Nhật với chủ đề: " + chuDe +
                    ". Trả về theo JSON sau: {cauHoi:..., dung:..., sai1:..., sai2:..., sai3:...}";
        }

        String json = "{\n" +
                "  \"model\": \"gpt-4o-mini\",\n" +
                "  \"messages\": [\n" +
                "    {\"role\": \"system\", \"content\": \"Bạn là chuyên gia tạo câu hỏi trắc nghiệm tiếng Nhật.\"},\n" +
                "    {\"role\": \"user\", \"content\": \"" + prompt.replace("\"", "\\\"") + "\"}\n" +
                "  ]\n" +
                "}";

        Request request = new Request.Builder()
                .url(ENDPOINT)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String bodyString = response.body().string();

                JSONObject full = new JSONObject(bodyString);
                String content = full.getJSONArray("choices")
                                     .getJSONObject(0)
                                     .getJSONObject("message")
                                     .getString("content");

                return content.trim();
            } else {
                throw new IOException("Gọi API thất bại: " + response.code() + " - " + response.body().string());
            }
        }
    }
    public static File taoFileAmThanhTuVanBan(String vanBan, String tenFile) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        JSONObject json = new JSONObject();
        json.put("model", "tts-1");
        json.put("input", vanBan);
        json.put("voice", "nova"); // hoặc alloy, echo, fable, onyx, shimmer

        RequestBody body = RequestBody.create(mediaType, json.toString());

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/audio/speech")
                .addHeader("Authorization", "Bearer YOUR_API_KEY")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Lỗi khi gọi API TTS: " + response);
        }
        
        File file = new File("tts_output/" + tenFile + ".mp3");
        try (InputStream in = response.body().byteStream();
             FileOutputStream out = new FileOutputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }

        return file;
    }
}
