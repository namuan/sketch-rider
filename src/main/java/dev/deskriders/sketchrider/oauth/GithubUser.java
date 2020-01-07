package dev.deskriders.sketchrider.oauth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubUser {
    String login;
    String name;
    String email;
}
