package ru.ezhov.ssh.utils.core;

public class SshUtilException extends Exception {
    public SshUtilException() {
    }

    public SshUtilException(String message) {
        super(message);
    }

    public SshUtilException(String message, Throwable cause) {
        super(message, cause);
    }

    public SshUtilException(Throwable cause) {
        super(cause);
    }
}
