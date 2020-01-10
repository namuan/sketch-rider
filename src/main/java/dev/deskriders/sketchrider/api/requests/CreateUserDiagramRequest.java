package dev.deskriders.sketchrider.api.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;


@Getter
@ToString
@Introspected
public class CreateUserDiagramRequest {

    @NotNull
    @JsonProperty
    String diagramCode;

    @NotNull
    @JsonProperty
    String docId;

}
