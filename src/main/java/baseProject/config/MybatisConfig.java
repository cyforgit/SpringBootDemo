package baseProject.config;

import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

@Configuration
@MapperScan("baseProject")
public class MybatisConfig {

	/**
	 * 初始化DataSource
	 * 
	 * @return
	 */
	@Bean
	@ConfigurationProperties("spring.datasource.druid")
	public DataSource dataSource() {
		return DruidDataSourceBuilder.create().build();
	}
	
	/**
	 * 多数据源配置
	 * @return
	 */
	@Primary
	@Bean
	@ConfigurationProperties("spring.datasource.druid.one")
	public DataSource dataSourceOne(){
	    return DruidDataSourceBuilder.create().build();
	}
	@Bean
	@ConfigurationProperties("spring.datasource.druid.two")
	public DataSource dataSourceTwo(){
	    return DruidDataSourceBuilder.create().build();
	}

}
