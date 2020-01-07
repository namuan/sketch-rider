package dev.deskriders.sketchrider.oauth;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Flowable;

@Header(name = "User-Agent", value = "sketch-rider")
@Header(name = "Accept", value = "application/vnd.github.v3+json, application/json")
@Client(id = "githubv3")
public interface GithubApiClient {
    @Get("/user")
    Flowable<GithubUser> getUser(@Header(HttpHeaders.AUTHORIZATION) String authorization);
}
