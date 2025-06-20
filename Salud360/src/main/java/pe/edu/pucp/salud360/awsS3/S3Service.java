package pe.edu.pucp.salud360.awsS3;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.pucp.salud360.awsS3.S3UrlGenerator;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Service
public class S3Service {

    @Value("${aws.access-key}")
    private String accessKey;

    @Value("${aws.secret-key}")
    private String secretKey;

    @Value("${aws.session-token}")
    private String sessionToken;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public String subirArchivo(String nombreArchivo, MultipartFile archivo) {
        try {
            S3Client s3 = S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(
                            StaticCredentialsProvider.create(
                                    AwsSessionCredentials.create(accessKey, secretKey, sessionToken)
                            )
                    )
                    .build();

            s3.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(nombreArchivo)
                            .build(),
                    RequestBody.fromInputStream(archivo.getInputStream(), archivo.getSize())
            );

            // Devuelve la URL pública del archivo
            return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + nombreArchivo;

        } catch (IOException e) {
            throw new RuntimeException("Error al subir archivo a S3", e);
        }
    }

    public Resource obtenerArchivo(String nombreArchivo) {
        S3Client s3 = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsSessionCredentials.create(accessKey, secretKey, sessionToken)
                        )
                )
                .build();

        ResponseInputStream<GetObjectResponse> inputStream = s3.getObject(
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(nombreArchivo)
                        .build()
        );

        return new InputStreamResource(inputStream);
    }
}
