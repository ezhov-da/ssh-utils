package ru.ezhov.ssh.utils.gui.repositories;

public class ConfigRepositoryException extends Exception {
    public ConfigRepositoryException() {
    }

    public ConfigRepositoryException(String message) {
        super(message);
    }

    public ConfigRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigRepositoryException(Throwable cause) {
        super(cause);
    }
}
