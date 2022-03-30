package tk.felixfab.VoiceBeam.Manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class FileManager {

    public static void createTempFolder() throws IOException {

        File file = Paths.get("WebTemp").toFile();

        deleteFolder(file);

        Files.createDirectory(file.toPath());
    }

    public static void removeTempFolder() throws IOException {
        Files.delete(Paths.get("WebTemp"));
    }

    public static File saveTempAudio(String data, String filePath){

        byte[] bytes = Base64.getDecoder().decode(data);

        File file = new File("WebTemp/" + filePath);

        try (FileOutputStream fos = new FileOutputStream(file.getPath())) {
            fos.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void removeTempAudioFile(String filePath) throws IOException {
        Files.delete(Paths.get("/WebTemp/" + filePath));
    }

    public static void deleteFolder(File file){

        File[] files = file.listFiles();
        if(files!=null) {
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        file.delete();
    }
}
