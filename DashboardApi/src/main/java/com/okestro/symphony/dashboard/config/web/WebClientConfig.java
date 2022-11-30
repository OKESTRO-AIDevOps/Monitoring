package com.okestro.symphony.dashboard.config.web;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.function.ThrowingConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        // in-memory buffer 설정 (Spring WebFlux default : 256KB)
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024*1024*50))
                .build();

        // Logging 설정
//        exchangeStrategies
//                .messageWriters().stream()
//                .filter(LoggingCodecSupport.class::isInstance)
//                .forEach(writer -> ((LoggingCodecSupport)writer).setEnableLoggingRequestDetails(true));

        return WebClient.builder()
                // HttpClient 변경 및 timeout 등 설정
//                .clientConnector(
//                        new ReactorClientHttpConnector(
//                                HttpClient
//                                        .create()
//                                        // HTTPS 인증서를 검증하지 않고 바로 접속하는 설정 (ThrowingConsumer : junit-jupiter-api.jar 필요)
//                                        .secure(
//                                                ThrowingConsumer.unchecked(
//                                                        sslContextSpec -> sslContextSpec.sslContext(
//                                                                SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()
//                                                        )
//                                                )
//                                        )
//                                        // 연결시 ConnectionTimeOut , ReadTimeOut , WriteTimeOut
//                                        .tcpConfiguration(
//                                                client -> client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 120_000)
//                                                        .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(180))
//                                                                .addHandlerLast(new WriteTimeoutHandler(180))
//                                                        )
//                                        )
//                        )
//                )
//                .exchangeStrategies(exchangeStrategies)
//                // request 시 데이터 조작
//                .filter(ExchangeFilterFunction.ofRequestProcessor(
//                        clientRequest -> {
//                            log.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
//                            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.debug("{} : {}", name, value)));
//                            return Mono.just(clientRequest);
//                        }
//                ))
//                 response 시 데이터 조작
                .filter(ExchangeFilterFunction.ofResponseProcessor(
                        clientResponse -> {
                            clientResponse.headers().asHttpHeaders().forEach((name, values) -> values.forEach(value -> log.debug("{} : {}", name, value)));
                            return Mono.just(clientResponse);
                        }
                ))
//                 default header
//                .defaultHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.87 Safari/537.3")
                .defaultHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.87 Safari/537.3")
                .build();
    }
}
