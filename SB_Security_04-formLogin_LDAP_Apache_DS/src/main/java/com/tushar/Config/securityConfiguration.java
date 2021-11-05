package com.tushar.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;


@Configuration
@EnableWebSecurity
public class securityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userAuthService;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		System.out.println("securityConfiguration.configure(auth)");
	
		auth.ldapAuthentication().contextSource().url("ldap://localhost:10389/o=Tushar")
		.managerDn("uid=admin,ou=system").managerPassword("secret")//connectivity
		.and()
		.userSearchBase("ou=users").userSearchFilter("(uid={0})")//Authentication
		.groupSearchBase("ou=roles").groupSearchFilter("(uniqueMember={0})")
		.groupRoleAttribute("cn").rolePrefix("ROLE_");//authorization
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
		.antMatchers("/addStudent").hasAnyRole("INCHARGE","ADMIN")
		.antMatchers("/allStudent").hasAnyRole("INCHARGE","ADMIN","STUD")
		.antMatchers("/updateStudent").hasAnyRole("INCHARGE","ADMIN")
		.antMatchers("/removeStudent").hasRole("ADMIN")
		.antMatchers("/searchStudent").hasAnyRole("INCHARGE","ADMIN")
		.antMatchers("/sections").authenticated() //Authenticatio
		.antMatchers("/uploadStudentFiles").hasAnyRole("INCHARGE","ADMIN")
		.antMatchers("/download").hasAnyRole("INCHARGE","ADMIN","STUD")
		.antMatchers("/UploadStudentData").hasAnyRole("INCHARGE","ADMIN")
		.anyRequest().permitAll()

		//Authentication mode
		.and().formLogin()
		.defaultSuccessUrl("/home", true)
		.loginPage("/Index")
		.loginProcessingUrl("/login")
		.failureUrl("/Index?error")
		
		//remember me on this machine
		.and().rememberMe()
		
		//logout
		.and().logout().logoutSuccessUrl("/Index?logout").logoutRequestMatcher(new AntPathRequestMatcher("/signOut"))
		
		//error page
		.and().exceptionHandling().accessDeniedPage("/accessDenied");
		
	}
	
	
}
