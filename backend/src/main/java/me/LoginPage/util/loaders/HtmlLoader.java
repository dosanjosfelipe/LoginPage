package me.LoginPage.util.loaders;

import org.springframework.core.io.ClassPathResource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class HtmlLoader {

    // Transforma o arquivo html que contém o corpo do email em uma String
    public static String RegisterEmailFormatter(String userName, String emailPath) throws IOException {
        ClassPathResource resource = new ClassPathResource(emailPath);

        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String emailBody = reader.lines().collect(Collectors.joining("\n"));
            return emailBody.replace("${nome}", userName);
        }
    }

    public static String ResetTokenEmailFormatter( String emailPath, String token) throws IOException {
        ClassPathResource resource = new ClassPathResource(emailPath);


        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String emailBody = reader.lines().collect(Collectors.joining("\n"));
            return emailBody.replace("${token}", token);
        }
    }
}
