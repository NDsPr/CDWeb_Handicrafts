package com.handicrafts.util;

import org.apache.tika.Tika;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;

public class DetectTypeFileUtil {
    public static boolean isImage(Part part) {
        InputStream inputStream = null;
        Tika tika = new Tika();
        try {
            inputStream = part.getInputStream();
            String fileType = tika.detect(inputStream);
            return fileType.startsWith("image");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
