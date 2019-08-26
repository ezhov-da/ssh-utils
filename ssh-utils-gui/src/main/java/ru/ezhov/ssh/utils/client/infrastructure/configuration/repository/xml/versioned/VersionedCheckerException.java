package ru.ezhov.ssh.utils.client.infrastructure.configuration.repository.xml.versioned;

public class VersionedCheckerException extends Exception {
    public VersionedCheckerException() {
    }

    public VersionedCheckerException(String message) {
        super(message);
    }

    public VersionedCheckerException(String message, Throwable cause) {
        super(message, cause);
    }

    public VersionedCheckerException(Throwable cause) {
        super(cause);
    }

    public VersionedCheckerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
