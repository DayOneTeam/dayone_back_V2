package dayone.dayone.user.service;

import dayone.dayone.global.service.FileService;
import org.springframework.web.multipart.MultipartFile;

public class StaticFileService implements FileService {

    @Override
    public String uploadFile(MultipartFile file) {
        return "updatedFile";
    }
}
