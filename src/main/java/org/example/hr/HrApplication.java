package org.example.hr; // 你的基础包名

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.example.hr.mapper") // 扫描MyBatis Mapper接口所在的包
public class HrApplication {

	public static void main(String[] args) {
		SpringApplication.run(HrApplication.class, args);
		System.out.println("(づ｡◕‿‿◕｡)づ HR Management System started successfully!"); // 启动成功提示
	}

}