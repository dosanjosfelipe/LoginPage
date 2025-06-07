package me.LoginPage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Importe este para o PasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder; // Importe este para o PasswordEncoder
import java.util.Arrays; // Importe este para Arrays.asList

@Configuration
@EnableWebSecurity // Habilita a configuração de segurança web do Spring
public class SecurityConfig {

    // Adicione o PasswordEncoder aqui, como discutimos anteriormente
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Desabilita CSRF para APIs (cuidado em aplicações com sessões)
            // Para APIs RESTful sem sessões baseadas em cookies, é comum desabilitar o CSRF.
            .csrf(csrf -> csrf.disable())
            // 2. Habilita e configura o CORS no nível do Spring Security
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // 3. Define as regras de autorização para as requisições HTTP
            .authorizeHttpRequests(authorize -> authorize
                // Permite acesso público a estes endpoints (login, registro, etc.)
                // Isso é crucial para que suas rotas de autenticação funcionem.
                .requestMatchers("/login", "/register", "/ResetPassword", "/token", "/static/**", "/css/**", "/js/**", "/images/**", "/fonts/**").permitAll()
                // Permite acesso a qualquer rota que comece com /frontend/templates/
                .requestMatchers("/frontend/templates/**").permitAll()
                // Qualquer outra requisição precisa de autenticação
                .anyRequest().authenticated()
            );
        return http.build();
    }

    // Bean para configurar as regras CORS
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // AQUI: Liste as origens que podem acessar sua API.
        // É importante ser explícito aqui para segurança.
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5500")); // Seu frontend Live Server
        // Se você tiver outras origens no futuro, adicione-as aqui.
        // Ex: Arrays.asList("http://localhost:5500", "https://seuapp.com")

        // Define os métodos HTTP permitidos (GET, POST, PUT, DELETE, OPTIONS são os mais comuns)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Permite todos os cabeçalhos na requisição (e.g., Content-Type, Authorization)
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Se você pretende usar cookies ou sessões (como JWT em cookies),
        // isso é importante para que as credenciais sejam enviadas.
        configuration.setAllowCredentials(true);

        // Define por quanto tempo (em segundos) a resposta do preflight pode ser armazenada em cache pelo navegador.
        // Reduz o número de requisições OPTIONS.
        configuration.setMaxAge(3600L); // 3600 segundos = 1 hora

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica esta configuração CORS a todos os caminhos (endpoints) da sua API.
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
