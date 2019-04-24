package ru.ezhov.ssh.utils.gui.repositories;

import java.io.File;

public abstract class ConfigRepositoryFactory {
    public static ConfigRepository createFromFile(File file) throws ConfigRepositoryException {
        return new XmlConfigRepository(file.getAbsoluteFile());
    }
}
