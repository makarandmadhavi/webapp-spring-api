package tech.stark.webapp.config;

import org.hibernate.cache.spi.RegionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.SnsClientBuilder;

@Configuration
public class SnsConfig {

    @Value("${aws.profile}")
    private String aws_profile;

    @Value("${aws.region}")
    private String aws_region;

    @Bean
    public SnsClient createSnsClient(){
        return SnsClient.builder()
                .region(Region.of(aws_region))
//                .credentialsProvider(ProfileCredentialsProvider.create(aws_profile))
                .build();
    }
}
