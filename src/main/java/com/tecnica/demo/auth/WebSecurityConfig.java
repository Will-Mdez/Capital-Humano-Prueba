package com.tecnica.demo.auth;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <b>WebSecurityConfig.java</b>
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends OncePerRequestFilter {

	/**
	 * Constantes de la clase para validaciones y parametros
	 */

	/* Constant ALLOED_HEADERS of type String */
	private static final String ALLOED_HEADERS = "*";
	/* Constant GET of type String */
	private static final String GET = "GET";
	/* Constant POST of type String */
	private static final String POST = "POST";
	/* Constant PUT of type String */
	private static final String PUT = "PUT";
	/* Constant PATTER_CONFIG of type String */
	private static final String PATTER_CONFIG = "/**";
	/* Constant basePath of type String */
	@Value("${basePath}")
	private String basePath;
	/* Constant urlActuador of type String */
	@Value("${management.endpoints.web.base-path}")
	private String urlActuator;

	public WebSecurityConfig() {
		/**
		 * Constructor por defecto
		 */
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		response.addHeader("Expect-CT", "max-age=3600, enforce");
		filterChain.doFilter(request, response);

	}

	/**
	 * Configuración de los cors de acceso
	 *
	 * @return Configuración de CORS
	 */
	@Bean
	protected CorsConfigurationSource corsConfigurationSource() {

		CorsConfiguration configuration = new CorsConfiguration();
		// Permitir todos los headers
		configuration.setAllowedOriginPatterns(Arrays.asList(ALLOED_HEADERS));
		// Permitir metodos POST y GET
		configuration.setAllowedMethods(Arrays.asList(GET, POST, PUT));
		configuration.setAllowedHeaders(Arrays.asList(ALLOED_HEADERS));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration(PATTER_CONFIG, configuration);
		return source;
	}

	/**
	 * Configuración de requests
	 */
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.headers(h -> h.contentSecurityPolicy(c -> c.policyDirectives("default-src 'self'")));
		http.headers(h -> h.addHeaderWriter(new StaticHeadersWriter("Expect-CT", "max-age=3600, enforce")));
		http.csrf(csrf -> csrf.requireCsrfProtectionMatcher(new OrRequestMatcher(new AntPathRequestMatcher(basePath))));

		http.authorizeHttpRequests(auth -> auth.requestMatchers(basePath).permitAll().requestMatchers(basePath)
				.permitAll().anyRequest().permitAll());
		http.cors(Customizer.withDefaults());

		return http.build();
	}

	/**
	 * WebSecurityCustomizer
	 *
	 * @return WebSecurityCustomizer
	 */
	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {

		return web -> web.ignoring().requestMatchers(HttpMethod.GET, urlActuator);
	}
}
