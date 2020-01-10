package dev.deskriders.sketchrider.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;

import javax.inject.Singleton;

@Singleton
@Replaces(DynamoDbConfig.class)
@Requires(env = {Environment.DEVELOPMENT, Environment.TEST})
public class LocalDynamoDbConfig extends DbConfig {
    public LocalDynamoDbConfig(AppConfig appConfig) {
        String dynamoEndpoint = appConfig.getDynamo();

        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
                dynamoEndpoint, "eu-west-1"
        );
        this.amazonDynamoDB = AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(endpointConfiguration)
                .build();
        this.dynamoDBMapper = new DynamoDBMapper(this.amazonDynamoDB);
    }
}
