package com.remote.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class IPResolver implements KeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        //String ip=exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();

        //获取请求地址
        String ip= exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();

        System.out.println("url:"+ip);

        return Mono.just(ip);
    }
}
