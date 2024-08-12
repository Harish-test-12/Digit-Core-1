package com.example.gateway;
import co.elastic.apm.attach.ElasticApmAttacher;
import com.example.gateway.config.ApplicationProperties;
import org.egov.common.utils.MultiStateInstanceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
@PropertySource({"${spring.routes.filepath}"})
public class GatewayApplication {

	@Autowired
	private ApplicationProperties applicationProperties;

    @Value("${elastic.apm.service-name}")
    private String serviceName;

    @Value("${elastic.apm.server-url}")
    private String serviceUrl;

    @Value("${elastic.apm.application-packages}")
    private String applicationPackages;

    @Value("${elastic.apm.environment}")
    private String environment;

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

    @PostConstruct
    public void initElasticApm() {
        Map<String, String> apmConfig = new HashMap<>();
        apmConfig.put("service_name", serviceName);
        apmConfig.put("server_urls", serviceUrl);
        apmConfig.put("secret_token", "");
        apmConfig.put("application_packages", applicationPackages);
        apmConfig.put("environment", environment);
        ElasticApmAttacher.attach(apmConfig);
    }

	@Value("${egov.user-info-header}")
	private String userInfoHeader;
	private List<String> encryptedUrlSet;
	private List<String> openEndpointsWhitelist;
	private List<String> mixedModeEndpointsWhitelist;

	@Value("${egov.encrypted-endpoints-list}")
	public void setEncrytpedUrlListValues(List<String> EcryptedListFromProperties) {
		this.encryptedUrlSet = Collections.unmodifiableList(EcryptedListFromProperties);
	}

	@Value("${egov.open-endpoints-whitelist}")
	public void setOpenEndpointsWhitelistValues(List<String> openUrlListFromProperties) {
		this.openEndpointsWhitelist = Collections.unmodifiableList(openUrlListFromProperties);
	}

	@Value("${egov.mixed-mode-endpoints-whitelist}")
	public void setMixModeEndpointListVaaues(List<String> mixModeUrlListFromProperties) {
		this.mixedModeEndpointsWhitelist = Collections.unmodifiableList(mixModeUrlListFromProperties);
	}

	@Value("${egov.auth-service-host}")
	private String authServiceHost;

	@Value("${egov.auth-service-uri}")
	private String authServiceUri;

	@Value("${egov.authorize.access.control.host}${egov.authorize.access.control.uri}")
	private String authorizationUrl;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public MultiStateInstanceUtil centralInstanceUtil() {
		return new MultiStateInstanceUtil();
	}

	/**
	 * This to create a default RedisRateLimiter
	 * @return
	 */
	@Bean
	public RedisRateLimiter redisRateLimiter() {
		return new RedisRateLimiter(applicationProperties.getDefaultReplenishRate(), applicationProperties.getDefaultBurstCapacity());
	}

}
