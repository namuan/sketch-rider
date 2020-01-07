package dev.deskriders.sketchrider.api;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.View;


import java.util.Map;

@Controller
public class HomeController {

    @View(value = "index")
    @Get(produces = MediaType.TEXT_HTML)
    public HttpResponse<Map> index() {
        return HttpResponse.ok();
    }
}
