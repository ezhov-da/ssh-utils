package ru.ezhov.ssh.utils.client.gui.tab.panel.model;

import ru.ezhov.ssh.utils.client.model.configuration.domain.SshDownloadFile;
import ru.ezhov.ssh.utils.client.gui.tab.panel.domain.SshDownloadFileGui;
import ru.ezhov.ssh.utils.client.model.configuration.repository.ConfigRepository;
import ru.ezhov.ssh.utils.client.model.configuration.repository.ConfigRepositoryException;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.stream.Collectors;

public class SshFileTableModel extends AbstractTableModel {

    private ConfigRepository configRepository;
    private List<SshDownloadFileGui> all;

    public SshFileTableModel(ConfigRepository configRepository) throws ConfigRepositoryException {
        this.configRepository = configRepository;
        load();
    }

    private void load() throws ConfigRepositoryException {
        this.all = configRepository
                .all()
                .stream()
                .map(SshDownloadFileGui::from)
                .collect(Collectors.toList());

    }

    public SshDownloadFileGui getBy(int rowIndex) {
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
        SshDownloadFileGui downloadFile = all.get(rowIndex);
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
            load();
            fireTableDataChanged();
        } catch (ConfigRepositoryException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка обновления");
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (aValue == null) {
            removeRow(aValue, rowIndex, columnIndex);
        } else if (aValue instanceof SshDownloadFileGui) {
            addRow(aValue, rowIndex, columnIndex);
        } else {
            setValue(aValue, rowIndex, columnIndex);
        }
        fireTableDataChanged();
    }

    private void addRow(Object aValue, int rowIndex, int columnIndex) {
        SshDownloadFileGui sshDownloadFile = (SshDownloadFileGui) aValue;
        try {
            configRepository.add(sshDownloadFile.getSshDownloadFile());
            load();
        } catch (ConfigRepositoryException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка добавления");
        }
    }

    private void removeRow(Object aValue, int rowIndex, int columnIndex) {
        if (all.size() > rowIndex) {
            try {
                all.remove(rowIndex);
                List<SshDownloadFile> collect = all
                        .stream()
                        .map(SshDownloadFileGui::getSshDownloadFile)
                        .collect(Collectors.toList());
                configRepository.addAll(collect);
                load();
            } catch (ConfigRepositoryException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Ошибка удаления");
            }
        }
    }

    private void setValue(Object aValue, int rowIndex, int columnIndex) {
        SshDownloadFileGui downloadFile = all.get(rowIndex);
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

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }


    public void save() {
        try {
            List<SshDownloadFile> collect = all
                    .stream()
                    .map(SshDownloadFileGui::getSshDownloadFile)
                    .collect(Collectors.toList());
            configRepository.addAll(collect);
            load();
            fireTableDataChanged();
        } catch (ConfigRepositoryException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка сохранения");
        }
    }
}
