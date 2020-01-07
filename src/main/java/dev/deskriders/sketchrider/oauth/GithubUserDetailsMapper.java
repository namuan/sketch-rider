package dev.deskriders.sketchrider.oauth;

import io.micronaut.security.authentication.UserDetails;
import io.micronaut.security.oauth2.endpoint.token.response.OauthUserDetailsMapper;
import io.micronaut.security.oauth2.endpoint.token.response.TokenResponse;
import org.reactivestreams.Publisher;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Collections;

@Named("github")
@Singleton
public class GithubUserDetailsMapper implements OauthUserDetailsMapper {
    public static final String TOKEN_PREFIX = "token ";
    public static final String CLAIM = "claim";

    private final GithubApiClient githubApiClient;

    public GithubUserDetailsMapper(GithubApiClient githubApiClient) {
        this.githubApiClient = githubApiClient;
    }

    @Override
    public Publisher<UserDetails> createUserDetails(TokenResponse tokenResponse) {
        return githubApiClient.getUser(TOKEN_PREFIX + tokenResponse.getAccessToken())
                .map(githubUser -> new UserDetails(
                        githubUser.getLogin(),
                        Collections.singletonList(CLAIM),
                        Collections.emptyMap()
                ));
    }
}
