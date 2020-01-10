package dev.deskriders.sketchrider.api.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotNull;


@Introspected
public class CreateUserDiagramRequest {

    @NotNull
    String documentCode;

    @JsonCreator
    public CreateUserDiagramRequest(String documentCode) {
        this.documentCode = documentCode;
    }
}
