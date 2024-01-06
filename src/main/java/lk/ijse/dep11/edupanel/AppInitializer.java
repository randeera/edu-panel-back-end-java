package lk.ijse.dep11.edupanel;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import lk.ijse.dep11.edupanel.converter.LecturerTypeConverter;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
public class AppInitializer implements WebMvcConfigurer {

    public static void main(String[] args) {
        System.setProperty("application.title", "EduPanel");
        System.setProperty("application.version", "v1.0.0");
        SpringApplication.run(AppInitializer.class, args);
    }

    @Bean
    public ModelMapper mapper(){
        return new ModelMapper();
    }

    @Bean
    public Bucket defaultBucket() throws IOException {
        InputStream serviceAccount =
                new ClassPathResource("/firebase.json").getInputStream();

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("edupanel-efb2b.appspot.com")
                .build();

        if (FirebaseApp.getApps().isEmpty()) FirebaseApp.initializeApp(options);
        return StorageClient.getInstance().bucket();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new LecturerTypeConverter());
    }
}
