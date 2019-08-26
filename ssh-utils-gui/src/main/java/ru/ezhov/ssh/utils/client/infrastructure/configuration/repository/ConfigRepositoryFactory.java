package ru.ezhov.ssh.utils.client.infrastructure.configuration.repository;

import ru.ezhov.ssh.utils.client.infrastructure.configuration.repository.xml.XmlConfigRepository;
import ru.ezhov.ssh.utils.client.model.configuration.repository.ConfigRepository;
import ru.ezhov.ssh.utils.client.model.configuration.repository.ConfigRepositoryException;

import java.io.File;

public abstract class ConfigRepositoryFactory {
    public static ConfigRepository createFromFile(File file) throws ConfigRepositoryException {
        return new XmlConfigRepository(file.getAbsoluteFile());
    }
}
