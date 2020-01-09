package dev.deskriders.sketchrider.api;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.View;

import javax.annotation.Nullable;

@Controller
public class HomeController {

    @Secured(SecurityRule.IS_ANONYMOUS)
    @View(value = "home/index")
    @Get(produces = MediaType.TEXT_HTML)
    public HttpResponse index(@Nullable Authentication authentication) {
        return HttpResponse.ok();
    }
}
