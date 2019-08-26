package ru.ezhov.ssh.utils.client.infrastructure.configuration.repository.xml;

public class VersionedXmlReaderException extends Exception {
    public VersionedXmlReaderException() {
    }

    public VersionedXmlReaderException(String message) {
        super(message);
    }

    public VersionedXmlReaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public VersionedXmlReaderException(Throwable cause) {
        super(cause);
    }

    public VersionedXmlReaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
