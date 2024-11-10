build.gradle

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.apache.struts:struts2-core:2.5.26'
    implementation 'org.springframework.security:spring-security-web'
    implementation 'org.springframework.security:spring-security-config'
}

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/login", "/resources/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()
                .permitAll();
    }
}

struts.xml
<struts>
    <package name="default" extends="struts-default">
        <action name="login" class="com.example.LoginAction">
            <result name="input">/login.jsp</result>
            <result name="success" type="redirect">/home</result>
        </action>

        <action name="home" class="com.example.HomeAction">
            <result name="success">/home.jsp</result>
        </action>
    </package>
</struts>


package com.example;

import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport {
    private String username;
    private String password;

    // Getters and setters for username and password

    @Override
    public String execute() {
        // Authentication logic can be handled by Spring Security
        return SUCCESS;
    }
}

<web-app>
    <filter>
        <filter-name>springSecurityFilterChain</filter-name>
        <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>springSecurityFilterChain</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    <filter>
        <filter-name>struts2</filter-name>
        <filter-class>org.apache.struts2.dispatcher.filter.StrutsPrepareAndExecuteFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>struts2</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
</web-app>