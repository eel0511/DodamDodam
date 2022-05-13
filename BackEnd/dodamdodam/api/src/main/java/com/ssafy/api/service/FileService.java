package com.ssafy.api.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.ssafy.core.common.FileUtil;
import com.ssafy.core.exception.ErrorCode;
import com.ssafy.core.exception.CustomException;
import lombok.RequiredArgsConstructor;
import marvin.image.MarvinImage;
import org.marvinproject.image.transform.scale.Scale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;


@RequiredArgsConstructor
@Service
public class FileService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String uploadFileV1(String category, MultipartFile multipartFile) {
        validateFileExists(multipartFile);

        String fileName = FileUtil.buildFileName(category, multipartFile.getOriginalFilename());
        checkFileNameExtension(multipartFile);
        MultipartFile resizedFile = resizeImage(fileName, multipartFile);
        System.out.println(multipartFile.getContentType());
        System.out.println(resizedFile.getContentType());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(resizedFile.getContentType());
        objectMetadata.setContentLength(resizedFile.getSize());

        try (InputStream inputStream = resizedFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_SIZE_EXCEED);
        }

        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }

    private void validateFileExists(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new CustomException(ErrorCode.FILE_DOES_NOT_EXIST);
        }
    }

    private void checkFileNameExtension(MultipartFile multipartFile) {
        String originFilename = Objects.requireNonNull(multipartFile.getOriginalFilename()).replaceAll(" ", "");
        String formatName = originFilename.substring(originFilename.lastIndexOf(".") + 1).toLowerCase();
        String[] supportFormat = { "bmp", "jpg", "jpeg", "png" };
        if (!Arrays.asList(supportFormat).contains(formatName)) {
            throw new CustomException(ErrorCode.WRONG_FILE_EXTENSION);
        }
    }

    public byte[] downloadFileV1(String resourcePath) {
        validateFileExistsAtUrl(resourcePath);

        S3Object s3Object = amazonS3Client.getObject(bucketName, resourcePath);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_DOWNLOAD_FAIL);
        }
    }

    private void validateFileExistsAtUrl(String resourcePath) {
        if (!amazonS3Client.doesObjectExist(bucketName, resourcePath)) {
            throw new CustomException(ErrorCode.FILE_DOES_NOT_EXIST);
        }
    }

    //삭제 구현        amazonS3Client.deleteObject();




//    public FileUploadResponse uploadFile(long userId, String category, List<MultipartFile> multipartFiles) {
//        List<String> fileUrls = new ArrayList<>();
//
//        // 파일 업로드 갯수를 정합니다(10개 이하로 정의)
//        for (MultipartFile multipartFile : multipartFiles) {
//            if (fileUrls.size() > 10) {
//                throw new CustomException(FILE_COUNT_EXCEED);
//            }
//
//            String fileName = PlandPMSUtils.buildFileName(userId, category, multipartFile.getOriginalFilename());
//            ObjectMetadata objectMetadata = new ObjectMetadata();
//            objectMetadata.setContentType(multipartFile.getContentType());
//
//            try (InputStream inputStream = multipartFile.getInputStream()) {
//                amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
//                        .withCannedAcl(CannedAccessControlList.PublicRead));
//                fileUrls.add(FILE_URL_PROTOCOL + bucketName + "/" + fileName);
//            } catch (IOException e) {
//                throw new CustomException(FILE_UPLOAD_FAIL);
//            }
//        }
//
//        return new FileUploadResponse(fileUrls);
//    }


    @Async
    public MultipartFile resizeImage(String fileName, MultipartFile file) {
        if (file.getSize() > 1572864) {
            try {
                String fileFormatName = file.getContentType().substring(file.getContentType().lastIndexOf("/") + 1);
                BufferedImage inputImage = ImageIO.read(file.getInputStream());
                int originWidth = inputImage.getWidth();
                int originHeight = inputImage.getHeight();
                MarvinImage imageMarvin = new MarvinImage(inputImage);

                Scale scale = new Scale();
                scale.load();
                scale.setAttribute("newWidth", 712);
                scale.setAttribute("newHeight", 712*originHeight /originWidth);
                scale.process(imageMarvin.clone(), imageMarvin, null, null, false);

                BufferedImage imageNoAlpha = imageMarvin.getBufferedImageNoAlpha();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(imageNoAlpha, fileFormatName, baos);
                baos.flush();
//            return new MockMultipartFile(fileName,baos.toByteArray());
                return new MockMultipartFile(fileName,"","image/"+fileFormatName, baos.toByteArray());

            } catch (Exception e) {
                throw new CustomException(ErrorCode.INVALID_REQUEST);
            }
        } else {
            return file;
        }

    }
}