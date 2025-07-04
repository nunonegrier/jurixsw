package br.com.jurix.filemanager.helper;

import br.com.jurix.filemanager.entity.FileMetadata;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public class ResponseFileHelper {

    public static void prepareDownloadResponse(FileMetadata fileMetadata, InputStream file, HttpServletResponse httpServletResponse) {

        try {

            String contentType = MimeTypesHelper.getMimeType(fileMetadata.getExtension());

            httpServletResponse.setContentType(contentType);
            httpServletResponse.setHeader("content-disposition", "attachment; filename=\"" + fileMetadata.getName() + "\"");

            IOUtils.copy(file, httpServletResponse.getOutputStream());
            httpServletResponse.flushBuffer();
        }catch (IOException e){
            throw new RuntimeException("Falha ao carregar resposta de arquivo", e);
        }
    }
}
