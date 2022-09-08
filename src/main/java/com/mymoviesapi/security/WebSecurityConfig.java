package com.mymoviesapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
@EnableWebSecurity
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
public class WebSecurityConfig {

//	@Autowired
//	private DataSource dataSource;
//
//	@Override
//	public void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.jdbcAuthentication().passwordEncoder(new BCryptPasswordEncoder()).dataSource(dataSource)
//				.usersByUsernameQuery("select username, password, enabled from users where username=?")
//				.authoritiesByUsernameQuery("select username, role from users where username=?");
//	}
//
//	@Override
//	public void configure(HttpSecurity httpSecurity) throws Exception {
//		httpSecurity.authorizeRequests().antMatchers("/h2-console/**").permitAll().and().csrf()
//				.ignoringAntMatchers("/h2-console/**").and().headers().frameOptions().sameOrigin().and()
//				.authorizeRequests().anyRequest().authenticated().and().sessionManagement()
//				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf().disable().httpBasic();
//	}

	@Bean
	public UserDetailsManager userDetailsService() {
		UserDetails normalUser = User.withDefaultPasswordEncoder().username("user").password("5678").roles("USER")
				.build();
		UserDetails adminUser = User.withDefaultPasswordEncoder().username("admin").password("5678")
				.roles("USER", "ADMIN").build();
		return new InMemoryUserDetailsManager(normalUser, adminUser);
	}
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) -> authz
                        .antMatchers(HttpMethod.GET).hasAnyRole("USER", "ADMIN")
                        .antMatchers(HttpMethod.PATCH).hasAnyRole("USER", "ADMIN")
                )
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
		
	@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/resources/**");
    }

}