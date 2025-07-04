package br.com.jurix.filemanager.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

public class MimeTypesHelper {

    private static Map<String, String> mimeTypes;

    public static String getMimeType(String extension){

        loadMimeTypeFile();
        return mimeTypes.get(extension);
    }

    private static void loadMimeTypeFile(){

        if(Objects.isNull(mimeTypes)) {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = TypeReference.class.getResourceAsStream("/mimetypes.json");
            try {
                mimeTypes = mapper.readValue(inputStream, new TypeReference<Map<String, String>>(){});
            } catch (IOException e) {
                throw new RuntimeException("Não foi possível ler arquivo de mimetypes");
            }
        }
    }
}
