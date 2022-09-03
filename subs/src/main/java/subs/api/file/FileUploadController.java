package subs.api.file;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Controller
public class FileUploadController {

    @GetMapping("")
    public String index() {
        return "uploader";
    }

    @PostMapping("/upload")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) {
        String s;

        String fileName = file.getOriginalFilename();

        try {
            ProcessBuilder builder = new ProcessBuilder("D:\\Education\\Practice\\Test\\venv\\Scripts\\python.exe",
                    "C:\\Users\\stepa\\OneDrive\\Рабочий стол\\subs\\subs\\src\\main\\python\\process.py", fileName);
            Process process = builder.start();

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(process.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(process.getErrorStream()));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok("File processed successfully");
    }
}
