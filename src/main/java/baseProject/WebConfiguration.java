package baseProject;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import baseProject.controller.ApiHandlerInterceptor;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
	@Bean
	public FilterRegistrationBean<Filter> filterRegistrationBean() {
		FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setForceEncoding(true);
		characterEncodingFilter.setEncoding("UTF-8");
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add("/*");// 拦截路径，可以添加多个
		registrationBean.setUrlPatterns(urlPatterns);
		registrationBean.setFilter(characterEncodingFilter);
		return registrationBean;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new ApiHandlerInterceptor()).addPathPatterns("/**");
	}
}
