package org.example.hr;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@MapperScan("org.example.hr.mapper")
public class HrApplication {

	private static final Logger logger = LoggerFactory.getLogger(HrApplication.class);

	// 通过构造器注入 Environment
	private final Environment environment;

	@Autowired // Spring会自动注入Environment的实例
	public HrApplication(Environment environment) {
		this.environment = environment;
	}

	public static void main(String[] args) {
		SpringApplication.run(HrApplication.class, args);
	}

	/**
	 * 监听 ApplicationReadyEvent 事件，在应用启动完成后执行
	 * @param event ApplicationReadyEvent (现在只有一个参数)
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady(ApplicationReadyEvent event) { // 注意：现在只有一个参数
		// 使用类成员变量 environment
		String serverPort = this.environment.getProperty("server.port", "8080");
		String contextPath = this.environment.getProperty("server.servlet.context-path", "");

		if (contextPath != null && !contextPath.isEmpty() && !contextPath.startsWith("/")) {
			contextPath = "/" + contextPath;
		}
		if (contextPath != null && contextPath.endsWith("/")) {
			contextPath = contextPath.substring(0, contextPath.length() - 1);
		}

		String hostAddress = "localhost";
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			logger.warn("Could not determine local host address, using 'localhost'.", e);
		}

		String pathSuffix = (contextPath.isEmpty() || contextPath.equals("/")) ? "/" : contextPath + "/";
		if (contextPath.equals("/")) {
			pathSuffix = "/";
		}

		String appUrlHttp = String.format("http://localhost:%s%s", serverPort, pathSuffix);
		String appUrlNetwork = String.format("http://%s:%s%s", hostAddress, serverPort, pathSuffix);

		appUrlHttp = appUrlHttp.replaceAll("//+", "/").replaceFirst(":/", "://");
		appUrlNetwork = appUrlNetwork.replaceAll("//+", "/").replaceFirst(":/", "://");

		logger.info("----------------------------------------------------------");
		logger.info("HR Management System is running!");
		logger.info("Local: \t\t{}", appUrlHttp);
		logger.info("External: \t{}", appUrlNetwork);
		logger.info("----------------------------------------------------------");
		logger.info("(づ｡◕‿‿◕｡)づ HR Management System started successfully!");
	}
}