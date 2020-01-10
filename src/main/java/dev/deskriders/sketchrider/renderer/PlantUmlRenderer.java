package dev.deskriders.sketchrider.renderer;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.code.TranscoderSmart;

import javax.inject.Singleton;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@Singleton
public class PlantUmlRenderer {

    TranscoderSmart transcoder = new TranscoderSmart();

    public byte[] renderDiagram(String encodedDiagramSource) throws IOException {
        String deflatedDiagramSource = transcoder.decode(encodedDiagramSource);
        SourceStringReader sourceStringReader = new SourceStringReader(deflatedDiagramSource);
        ByteArrayOutputStream plantUmlDiagramStream = new ByteArrayOutputStream();
        sourceStringReader.outputImage(plantUmlDiagramStream);
        return plantUmlDiagramStream.toByteArray();
    }

}
