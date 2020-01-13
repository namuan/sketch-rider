package dev.deskriders.sketchrider.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.sourceforge.plantuml.code.TranscoderSmart;
import org.apache.commons.codec.binary.Base32InputStream;
import org.apache.commons.codec.binary.Base32OutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@UtilityClass
public class DocumentCursor {

    @SneakyThrows
    public String en(String source) {
        TranscoderSmart ts = new TranscoderSmart();
        return ts.encode(source);
    }

    @SneakyThrows
    public String de(String source) {
        TranscoderSmart ts = new TranscoderSmart();
        return ts.decode(source);
    }

    @SneakyThrows
    public String compress(String source) {
        try (
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                GZIPOutputStream gos = new GZIPOutputStream(baos);
        ) {
            gos.write(source.getBytes(Charset.defaultCharset()));
            gos.flush();
            gos.finish();
            return Base64.getUrlEncoder().encodeToString(baos.toByteArray());
        }
    }


    @SneakyThrows
    public String deCompress(String compressedSource) {
        byte[] decodedSource = Base64.getUrlDecoder().decode(compressedSource);
        try (
                ByteArrayInputStream bais = new ByteArrayInputStream(decodedSource);
                GZIPInputStream gis = new GZIPInputStream(bais);
        ) {
            byte[] bytes = gis.readAllBytes();
            return new String(bytes, Charset.defaultCharset());
        }
    }
    
}
