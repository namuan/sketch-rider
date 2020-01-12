package dev.deskriders.sketchrider.oauth;

import io.micronaut.core.util.CollectionUtils;
import io.micronaut.security.authentication.UserDetails;
import io.micronaut.security.oauth2.endpoint.token.response.OauthUserDetailsMapper;
import io.micronaut.security.oauth2.endpoint.token.response.TokenResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.reactivestreams.Publisher;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.Collections;

@Named("github")
@Singleton
public class GithubUserDetailsMapper implements OauthUserDetailsMapper {
    public static final String BEARER_PREFIX = "bearer ";
    public static final String CLAIM = "claim";

    private final GithubApiClient githubApiClient;

    public GithubUserDetailsMapper(GithubApiClient githubApiClient) {
        this.githubApiClient = githubApiClient;
    }

    @Override
    public Publisher<UserDetails> createUserDetails(TokenResponse tokenResponse) {
        return githubApiClient.getUser(BEARER_PREFIX + tokenResponse.getAccessToken())
                .map(githubUser -> new UserDetails(
                        githubUser.getLogin(),
                        Collections.emptyList(),
                        CollectionUtils.mapOf(
                                "id", sha(githubUser.getId()),
                                "name", githubUser.getName()
                        )
                ));
    }

    private String sha(String source) {
        return DigestUtils.sha256Hex(source);
    }
}
