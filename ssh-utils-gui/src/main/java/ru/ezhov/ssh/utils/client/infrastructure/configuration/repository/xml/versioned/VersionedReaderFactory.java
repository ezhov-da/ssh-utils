package ru.ezhov.ssh.utils.client.infrastructure.configuration.repository.xml.versioned;

import ru.ezhov.ssh.utils.client.infrastructure.configuration.repository.xml.UnsupportedVersionedXmlReader;
import ru.ezhov.ssh.utils.client.infrastructure.configuration.repository.xml.VersionedXmlReader;

import java.io.File;

public abstract class VersionedReaderFactory {
    private VersionedReaderFactory() {
        //фабрика
    }

    public static VersionedXmlReader byVersion(File file) throws VersionedCheckerException, UnsupportedVersionedXmlReader {
        VersionedChecker versionedChecker = new VersionedChecker();
        SupportVersion check = versionedChecker.check(file);
        switch (check) {
            case OLD:
                return new OldVersionedXmlReader(file);
            case ONE:
                return new FirstVersionedXmlReader(file);
            default:
                throw new UnsupportedVersionedXmlReader("Неподдерживаемая версия конфигурации");

        }
    }
}
