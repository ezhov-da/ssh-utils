package ru.ezhov.ssh.utils.gui.gui.tab.panel.privateKey;

import ru.ezhov.ssh.utils.gui.domain.SshDownloadFile;
import ru.ezhov.ssh.utils.gui.repositories.ConfigRepository;
import ru.ezhov.ssh.utils.gui.repositories.ConfigRepositoryException;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class SshFileTableModel extends AbstractTableModel {

    private ConfigRepository configRepository;
    private List<SshDownloadFile> all;

    public SshFileTableModel(ConfigRepository configRepository) throws ConfigRepositoryException {
        this.configRepository = configRepository;
        all = configRepository.all();
    }

    public SshDownloadFile getBy(int rowIndex) {
        return all.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return all.size();
    }

    @Override
    public int getColumnCount() {
        return 8;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Название";
            case 1:
                return "Хост";
            case 2:
                return "Порт";
            case 3:
                return "Имя пользователя";
            case 4:
                return "Passphrase";
            case 5:
                return "Путь к ключу";
            case 6:
                return "Файл для скачивания";
            case 7:
                return "Файл для сохранения";
            default:
                return "Не обнаружен столбец";
        }
    }

    @Override
    public String getValueAt(int rowIndex, int columnIndex) {
        SshDownloadFile downloadFile = all.get(rowIndex);
        String text = "";
        switch (columnIndex) {
            case 0:
                text = downloadFile.getDescription();
                break;
            case 1:
                text = downloadFile.getHost();
                break;
            case 2:
                text = downloadFile.getPort();
                break;
            case 3:
                text = downloadFile.getUsername();
                break;
            case 4:
                text = downloadFile.getPassphrase();
                break;
            case 5:
                text = downloadFile.getPathToPrivateKey();
                break;
            case 6:
                text = downloadFile.getFileFrom();
                break;
            case 7:
                text = downloadFile.getFileTo();
                break;
        }

        return text;
    }

    public void reload() {
        try {
            all = configRepository.all();
            fireTableDataChanged();
        } catch (ConfigRepositoryException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка обновления");
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (aValue == null) {
            try {
                all.remove(rowIndex);
                configRepository.addAll(all);
                all = configRepository.all();
                fireTableDataChanged();
            } catch (ConfigRepositoryException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Ошибка удаления");
            }
        } else if (aValue instanceof SshDownloadFile) {
            SshDownloadFile sshDownloadFile = (SshDownloadFile) aValue;
            try {
                configRepository.add(sshDownloadFile);
                all = configRepository.all();
                fireTableDataChanged();
            } catch (ConfigRepositoryException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Ошибка добавления");
            }
        } else {
            SshDownloadFile downloadFile = all.get(rowIndex);
            String value = String.valueOf(aValue);
            switch (columnIndex) {
                case 0:
                    downloadFile.setDescription(value);
                    break;
                case 1:
                    downloadFile.setHost(value);
                    break;
                case 2:
                    downloadFile.setPort(value);
                    break;
                case 3:
                    downloadFile.setUsername(value);
                    break;
                case 4:
                    downloadFile.setPassphrase(value);
                    break;
                case 5:
                    downloadFile.setPathToPrivateKey(value);
                    break;
                case 6:
                    downloadFile.setFileFrom(value);
                    break;
                case 7:
                    downloadFile.setFileTo(value);
                    break;
            }
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }


    public void save() {
        try {
            configRepository.addAll(all);
            all = configRepository.all();
            fireTableDataChanged();
        } catch (ConfigRepositoryException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка сохранения");
        }
    }
}
