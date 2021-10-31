package com.tushar.Config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class securityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource ds;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		System.out.println("securityConfiguration.configure(auth)");
		/*auth.inMemoryAuthentication().passwordEncoder(new  BCryptPasswordEncoder()).withUser("Tushar").password("$2a$10$Z0Kb9dYXknrmC4FVI864b.LrZ9QrmhW6GM1TmJ.BdFPQieIC.1OXe").roles("STUD","ICH");
		auth.inMemoryAuthentication().withUser("STUD").password("$2a$10$.IsQaL7n13XraHWn.wPauOMdNBmCvWE.kR/wjjG3dNBM.WBwFtzaK").roles("STUD");
		auth.inMemoryAuthentication().withUser("ADMIN").password("$2a$10$SJGBfwHgytYfEzXB7diyauVdQ6ou4c5hND0czh7EG44/m7.XCfRHq").roles("ADMIN");*/

		/*
		drop table USERS_ROLES;
drop table USERS;

create table USERS(uname varchar2(20) not null enable, paswrd varchar2(200), status number(1,0), CONSTRAINT SEC_USERS_pk PRIMARY KEY (uname) enable);

create table USERS_ROLES( ROLE varchar2(20), uname varchar2(20), CONSTRAINT USERS_ROLES_Fk  FOREIGN KEY (uname) REFERENCES USERS(uname));

INSERT INTO USERS VALUES('Tushar', '$2a$10$Z0Kb9dYXknrmC4FVI864b.LrZ9QrmhW6GM1TmJ.BdFPQieIC.1OXe', 1);
INSERT INTO USERS VALUES('STUD', '$2a$10$.IsQaL7n13XraHWn.wPauOMdNBmCvWE.kR/wjjG3dNBM.WBwFtzaK', 1);
INSERT INTO USERS VALUES('ADMIN', '$2a$10$SJGBfwHgytYfEzXB7diyauVdQ6ou4c5hND0czh7EG44/m7.XCfRHq', 1);

INSERT INTO USERS_ROLES VALUES('STUD', 'STUD');
INSERT INTO USERS_ROLES VALUES('ICH', 'Tushar');
INSERT INTO USERS_ROLES VALUES('STUD','ADMIN');
INSERT INTO USERS_ROLES VALUES('ADMIN', 'ADMIN');
INSERT INTO USERS_ROLES VALUES('ICH','ADMIN');
COMMIT;
*/
		
		auth.jdbcAuthentication().dataSource(ds).passwordEncoder(new BCryptPasswordEncoder()).
		usersByUsernameQuery("SELECT  UNAME, PASWRD, STATUS FROM USERS WHERE UNAME=?").//for authentication
		authoritiesByUsernameQuery("SELECT  UNAME, ROLE FROM USERS_ROLES WHERE UNAME=?");//for authorization
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		System.out.println("securityConfiguration.configure(http)");
		http.authorizeRequests().antMatchers("/").permitAll() //No Authorization, Authentication
		.antMatchers("/Index").permitAll()
		.antMatchers("/register").permitAll()
		.antMatchers("/home").permitAll() //Authentication
		.antMatchers("/header").authenticated() 
		.antMatchers("/footer").authenticated() 
		.antMatchers("/addStudent").hasAnyAuthority("ICH","ADMIN")
		.antMatchers("/allStudent").hasAnyAuthority("ICH","ADMIN","STUD")
		.antMatchers("/updateStudent").hasAnyAuthority("ICH","ADMIN")
		.antMatchers("/removeStudent").hasAuthority("ADMIN")
		.antMatchers("/searchStudent").hasAnyAuthority("ICH","ADMIN")
		.antMatchers("/sections").authenticated() //Authenticatio
		.antMatchers("/uploadStudentFiles").hasAnyAuthority("ICH","ADMIN")
		.antMatchers("/download").hasAnyAuthority("ICH","ADMIN","STUD")
		.antMatchers("/UploadStudentData").hasAnyAuthority("ICH","ADMIN")

		//Authentication mode
		.and().formLogin()
		
		//remember me on this machine
		.and().rememberMe()
		
		//logout
		.and().logout().logoutSuccessUrl("/Index")
		
		//error page
		.and().exceptionHandling().accessDeniedPage("/accessDenied");
		
	}
	
	
}
