import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class ZipperApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZipperApplication.class, args);
    }

  
    public static class ZipController {

      
        public ResponseEntity<ByteArrayResource> zipRequest(
                @RequestPart("pdfFile") MultipartFile pdfFile,
                @RequestPart("json") String json) throws IOException {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

                
                zipOutputStream.putNextEntry(new ZipEntry(pdfFile.getOriginalFilename()));
                zipOutputStream.write(pdfFile.getBytes());
                zipOutputStream.closeEntry();

                zipOutputStream.putNextEntry(new ZipEntry("content.json"));
                zipOutputStream.write(json.getBytes());
                zipOutputStream.closeEntry();
            }

            
            ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

         
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=compressed_response.zip");

          
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(byteArrayOutputStream.size())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        }
    }
}
