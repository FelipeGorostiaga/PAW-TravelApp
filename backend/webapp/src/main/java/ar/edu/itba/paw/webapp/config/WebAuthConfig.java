package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.TravelUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.concurrent.TimeUnit;

@EnableWebSecurity
@Configuration
@ComponentScan("ar.edu.itba.paw.webapp.auth")
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Value("classpath:rememberme.key")
    private Resource key;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    private TravelUserDetailsService userDetailsService;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.userDetailsService(userDetailsService)
                .sessionManagement()
                //.invalidSessionUrl("/")
                .and().authorizeRequests()
                .antMatchers("/","/signin", "/signup").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/home/**").authenticated()
                .and().formLogin()
                .loginPage("/signin")
                .defaultSuccessUrl("/home/1", true)
                .usernameParameter("username")
                .passwordParameter("password")
                .failureUrl("/signin?error=true")
                .and().rememberMe()
                .rememberMeParameter("rememberme")
                .userDetailsService(userDetailsService)
                .key(getRememberMeKey())
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(27))
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and().exceptionHandling()
                .accessDeniedPage("/403")
                .and().csrf().disable();
    }

    private String getRememberMeKey() {
        final StringWriter writer = new StringWriter();
        try (Reader reader = new InputStreamReader(key.getInputStream())) {
            char[] data = new char[1024];
            int len;
            while ((len = reader.read(data)) != -1) {
                writer.write(data,0,len);
            }
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
        return writer.toString();
    }

    @Bean
    public DaoAuthenticationProvider getDaoAuth() {
        DaoAuthenticationProvider ap = new DaoAuthenticationProvider();
        ap.setUserDetailsService(userDetailsService);
        ap.setPasswordEncoder(passwordEncoder);
        return ap;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder); OTRA FORMA
        auth.authenticationProvider(getDaoAuth());
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico","/icons/**","/webjars/**");
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}



