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

    public byte[] renderDocument(String encodedDocumentSource) throws IOException {
        String deflatedDocumentSource = transcoder.decode(encodedDocumentSource);
        SourceStringReader sourceStringReader = new SourceStringReader(deflatedDocumentSource);
        ByteArrayOutputStream plantUmlDocumentStream = new ByteArrayOutputStream();
        sourceStringReader.outputImage(plantUmlDocumentStream);
        return plantUmlDocumentStream.toByteArray();
    }

    public String convertToSource(String encodedDocumentSource) throws IOException {
        return transcoder.decode(encodedDocumentSource);
    }

}
