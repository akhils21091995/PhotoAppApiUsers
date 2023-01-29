package com.akhil.photoapp.api.users.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.akhil.photoapp.api.users.service.UsersService;


@Configuration
@EnableWebSecurity
public class WebSecurity {

	private final Environment env;
	private final UsersService usersService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public WebSecurity(UsersService usersService, BCryptPasswordEncoder bCryptPasswordEncoder,Environment env) {
		this.env = env;
		this.usersService = usersService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {

		// Configure AuthenticationManagerBuilder
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(usersService).passwordEncoder(bCryptPasswordEncoder);

		// Get AuthenticationManager
		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
		http.csrf().disable();
		//http.authorizeHttpRequests().requestMatchers("/**").permitAll();
		http.authorizeHttpRequests().requestMatchers("/**").permitAll().and()
				.addFilter(getAuthenticationFilter(authenticationManager)).authenticationManager(authenticationManager);
		http.headers().frameOptions().disable();

		return http.build();
	}

	protected AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager)
			throws Exception {
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, usersService, env);
		authenticationFilter.setFilterProcessesUrl(env.getProperty("login.url.path"));
		return authenticationFilter;
	}

}
