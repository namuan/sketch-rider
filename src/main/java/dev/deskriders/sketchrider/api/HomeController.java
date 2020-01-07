package dev.deskriders.sketchrider.api;

import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.View;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;

@Controller
public class HomeController {

    @Secured(SecurityRule.IS_ANONYMOUS)
    @View(value = "index")
    @Get(produces = MediaType.TEXT_HTML)
    public Map index(@Nullable Authentication authentication) {
        if (authentication == null) {
            return Collections.singletonMap("loggedIn", false);
        }

        System.out.println(authentication.getAttributes());
        return CollectionUtils.mapOf(
                "loggedIn", true,
                "username", authentication.getName(),
                "uid", authentication.getAttributes().get("id")
        );
    }
}
