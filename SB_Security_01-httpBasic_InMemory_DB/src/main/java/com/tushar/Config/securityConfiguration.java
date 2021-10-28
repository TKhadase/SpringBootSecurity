package com.tushar.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
public class securityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		System.out.println("securityConfiguration.configure(auth)");
		auth.inMemoryAuthentication().withUser("Tushar").password("{noop}Tushar").roles("STUD","ICH");
		auth.inMemoryAuthentication().withUser("STUD").password("{noop}STUD").roles("STUD");
		auth.inMemoryAuthentication().withUser("ADMIN").password("{noop}ADMIN").roles("ADMIN");
		
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		System.out.println("securityConfiguration.configure(http)");
		http.authorizeRequests().antMatchers("/").permitAll() //No Authorization, Authentication
		.antMatchers("/Index").permitAll()
		.antMatchers("/register").permitAll()
		.antMatchers("/home").authenticated() //Authentication
		.antMatchers("/header").authenticated() 
		.antMatchers("/footer").authenticated() 
		.antMatchers("/addStudent").hasAnyRole("ICH","ADMIN")
		.antMatchers("/allStudent").hasAnyRole("ICH","ADMIN","STUD")
		.antMatchers("/updateStudent").hasAnyRole("ICH","ADMIN")
		.antMatchers("/removeStudent").hasRole("ADMIN")
		.antMatchers("/searchStudent").hasAnyRole("ICH","ADMIN")
		.antMatchers("/sections").authenticated() //Authenticatio
		.antMatchers("/uploadStudentFiles").hasAnyRole("ICH","ADMIN")
		.antMatchers("/download").hasAnyRole("ICH","ADMIN","STUD")
		.antMatchers("/UploadStudentData").hasAnyRole("ICH","ADMIN")
		
		//Authentication mode
		.and().httpBasic()
		
		
		//error page
		.and().exceptionHandling().accessDeniedPage("/accessDenied");
		
	}
	
	
}
