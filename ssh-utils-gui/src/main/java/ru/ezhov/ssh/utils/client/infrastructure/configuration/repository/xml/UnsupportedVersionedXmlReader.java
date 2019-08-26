package ru.ezhov.ssh.utils.client.infrastructure.configuration.repository.xml;

public class UnsupportedVersionedXmlReader extends Exception {
    public UnsupportedVersionedXmlReader() {
    }

    public UnsupportedVersionedXmlReader(String message) {
        super(message);
    }

    public UnsupportedVersionedXmlReader(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedVersionedXmlReader(Throwable cause) {
        super(cause);
    }

    public UnsupportedVersionedXmlReader(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
