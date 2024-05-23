package com.spring.contactlist.contactapi;

//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//@Configuration
//public class CorsConfig {
//
//	@Bean
//	public CorsFilter corsFilter() {
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		CorsConfiguration config = new CorsConfiguration();
//		config.addAllowedOrigin("http://localhost:3000");
//		config.addAllowedOrigin("https://splendorous-sundae-37c6f1.netlify.app"); // Remove the trailing slash
//		config.addAllowedMethod("*");
//		config.addAllowedHeader("*");
//		source.registerCorsConfiguration("/**", config);
//		return new CorsFilter(source);
//	}
//}
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();

		// Allow all origins (replace with specific domains if needed)
		config.addAllowedOrigin("*"); // Allow requests from any origin

		// Allow all methods (GET, POST, PUT, DELETE, etc.)
		config.addAllowedMethod("*"); // Allow all HTTP methods

		// Allow all headers
		config.addAllowedHeader("*"); // Allow all headers

		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
}
