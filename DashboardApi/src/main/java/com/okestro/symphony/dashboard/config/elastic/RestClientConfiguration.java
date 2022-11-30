/*
 * Developed by bhhan@okestro.com on 2021-02-03
 * Last Modified 2020-02-10 11:14:27
 */

package com.okestro.symphony.dashboard.config.elastic;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

import java.time.Duration;

@Configuration
public class RestClientConfiguration extends AbstractElasticsearchConfiguration {
	@Value("${spring.elasticsearch.rest.uris}")
	private String hostAndPort;



	@Override
	@Bean
	public RestHighLevelClient elasticsearchClient() {

		//@ TODO-useSsl, withBasicAuth
		final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
				.connectedTo(hostAndPort)
				.withSocketTimeout(Duration.ofSeconds(30))
				.withBasicAuth("elastic","okestro2018")
				.build();

		return RestClients.create(clientConfiguration).rest();
	}
}