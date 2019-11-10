/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.util.Args;

public class SerializableEntity
extends AbstractHttpEntity {
    private byte[] objSer;
    private Serializable objRef;

    public SerializableEntity(Serializable ser, boolean bufferize) throws IOException {
        Args.notNull(ser, "Source object");
        if (bufferize) {
            this.createBytes(ser);
            return;
        }
        this.objRef = ser;
    }

    public SerializableEntity(Serializable ser) {
        Args.notNull(ser, "Source object");
        this.objRef = ser;
    }

    private void createBytes(Serializable ser) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(ser);
        out.flush();
        this.objSer = baos.toByteArray();
    }

    @Override
    public InputStream getContent() throws IOException, IllegalStateException {
        if (this.objSer != null) return new ByteArrayInputStream(this.objSer);
        this.createBytes(this.objRef);
        return new ByteArrayInputStream(this.objSer);
    }

    @Override
    public long getContentLength() {
        if (this.objSer != null) return this.objSer.length;
        return -1L;
    }

    @Override
    public boolean isRepeatable() {
        return true;
    }

    @Override
    public boolean isStreaming() {
        if (this.objSer != null) return false;
        return true;
    }

    @Override
    public void writeTo(OutputStream outstream) throws IOException {
        Args.notNull(outstream, "Output stream");
        if (this.objSer == null) {
            ObjectOutputStream out = new ObjectOutputStream(outstream);
            out.writeObject(this.objRef);
            out.flush();
            return;
        }
        outstream.write(this.objSer);
        outstream.flush();
    }
}

