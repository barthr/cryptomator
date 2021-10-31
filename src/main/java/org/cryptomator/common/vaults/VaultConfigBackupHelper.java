package org.cryptomator.common.vaults;

import org.cryptomator.cryptofs.common.MasterkeyBackupHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.cryptomator.common.Constants.VAULTCONFIG_BACKUP_SUFFIX;
import static org.cryptomator.common.Constants.VAULTCONFIG_FILENAME;

public class VaultConfigBackupHelper {

	private final Path vault;

	VaultConfigBackupHelper(Path vault) {
		this.vault = vault;
	}

	static Path createBackup(Vault vault) throws IOException {
		return new VaultConfigBackupHelper(vault.getPath()).createBackup();
	}

	public Path createBackup() throws IOException {
		final Path vaultConfigFile = vault.resolve(VAULTCONFIG_FILENAME);
		final String backupId = MasterkeyBackupHelper.generateFileIdSuffix(Files.readAllBytes(vaultConfigFile));
		final Path vaultConfigFileBackup = vault.resolve(VAULTCONFIG_FILENAME + backupId + VAULTCONFIG_BACKUP_SUFFIX);
		return Files.copy(vaultConfigFile, vaultConfigFileBackup, StandardCopyOption.REPLACE_EXISTING);
	}
}
