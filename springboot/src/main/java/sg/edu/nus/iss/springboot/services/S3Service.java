package sg.edu.nus.iss.springboot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service {
    
    @Autowired
    private AmazonS3 amazonS3;

    @Value("${do.storage.bucket}")
    private String bucketName;

    @Value("${do.storage.endpoint}")
    private String endPoint;


    public String uploadPfpToDigitalOcean(MultipartFile imageFile, String username) throws Exception {
        
        System.out.println("Attempting to save profile picture to digital ocean");

        String originalFileName = imageFile.getOriginalFilename();
        String fileExtension = ".jpg";  // default file extension
        fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        String finalFilePath = "profiles/" + username + fileExtension;

        ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(imageFile.getContentType());
            objectMetadata.setContentLength(imageFile.getSize());

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, finalFilePath, imageFile.getInputStream(), objectMetadata);
            putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putObjectRequest);

            System.out.println("Success saving profile picture to digital ocean");

            String imgUrl = String.format("https://%s.%s/%s", bucketName, endPoint, finalFilePath);

            return imgUrl;

        } catch (Exception e) {
            System.out.println("Error saving profile picture to digital ocean: " + e.getMessage());
            throw new Exception();
        }
    }


    public String uploadRecipeImageToDigitalOcean(MultipartFile imageFile, Integer recipeId) throws Exception{
        
        System.out.println("Attempting to save recipe picture to digital ocean");

        String originalFileName = imageFile.getOriginalFilename();
        String fileExtension = ".jpg";
        fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        String finalFilePath = "recipes/" + recipeId + fileExtension;

        ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(imageFile.getContentType());
            objectMetadata.setContentLength(imageFile.getSize());

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, finalFilePath, imageFile.getInputStream(), objectMetadata);
            putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putObjectRequest);
    
            System.out.println("Success saving profile picture to digital ocean");
    
            String imgUrl = String.format("https://%s.%s/%s", bucketName, endPoint, finalFilePath);
    
            return imgUrl;
    
        } catch (Exception e) {
            System.out.println("Error saving profile picture to digital ocean: " + e.getMessage());
            throw new Exception();
        }

    }
}
