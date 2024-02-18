package com.app.PipelineConfig.config;

import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.app.PipelineConfig.service.CustomeOAuth2UserService;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Value("${github.oauth.token}")
    private String githubOAuthToken;

    @Bean
    public GitHub gitHub() throws IOException {
        return GitHub.connectUsingOAuth(githubOAuthToken);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /*
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/**login**").permitAll()
                        .anyRequest().authenticated()
                )
                /*.exceptionHandling(e -> e
                    .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                )
                .csrf(c -> c
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .logout(l -> l
                        .logoutSuccessUrl("/login").permitAll()
                )
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(userService)
                .and()
                .defaultSuccessUrl("/");
*/

        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/registration").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true))
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(userService)
                .and()
                .defaultSuccessUrl("/");
                /*
                .oauth2Login()
                        .loginPage("/login")
                        .userInfoEndpoint()
                        .userService(userService);
                */
        return http.build();
    }
    @Autowired
    private CustomeOAuth2UserService userService;
}

/*
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
class SecurityConfiguration {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    @Autowired
    SecurityFilterChain getSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        /*
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/registration").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true))
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(userService)
                .and()
                .defaultSuccessUrl("/");

        httpSecurity.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/registration").permitAll()
                // .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true))
                // Disable CSRF (not required for this demo)
                .csrf().disable();

        // authorize all requests coming through, and ensure that they are
        // authenticated
        httpSecurity.authorizeHttpRequests().anyRequest().authenticated();

        // enable oauth2 login, and forward successful logins to the `/welcome` route
        // Here `true` ensures that the user is forwarded to `/welcome` irrespective of
        // the original route that they entered through our application
        httpSecurity.oauth2Login()
                .defaultSuccessUrl("/", true);

        return httpSecurity.build();
    }


    // enabling oauth2 login will not work without defining a
    // `ClientRegistrationRepository` bean. FOr simplicity, we're defining it in the same class
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {

        // Create a new client registration
        return new InMemoryClientRegistrationRepository(
                // spring security provides us pre-populated builders for popular
                // oauth2 providers, like Github, Google, or Facebook.
                // The `CommonOAuth2Provider` contains the definitions for these
                CommonOAuth2Provider.GITHUB.getBuilder("github")
                        // In this case, most of the common configuration, like the token and user endpoints
                        // are pre-populated. We only need to supply our client ID and secret
                        .clientId("1708aacd6f7de758a8cf")
                        .clientSecret("bde7ceace2af0cfb72be82a9552f43e5f5824934")
                        // ^ these credentials are fake, so you'll have to use your own :)
                        .build());
    }
}
*/


