package dev.deskriders.sketchrider.api;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class HomeControllerTest {

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Test
    @DisplayName("should render index page")
    void testRenderIndexPage() {
        // given
        MutableHttpRequest<Object> getIndex = HttpRequest.GET("/");

        // when
        HttpResponse<Object> httpResponse = httpClient.toBlocking().exchange(getIndex);

        // then
        assertThat(httpResponse).isNotNull();
    }
}