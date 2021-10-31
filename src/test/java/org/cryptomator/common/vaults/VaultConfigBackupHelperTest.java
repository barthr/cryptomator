package org.cryptomator.common.vaults;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import static org.cryptomator.common.Constants.VAULTCONFIG_FILENAME;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VaultConfigBackupHelperTest {

	@Test
	public void testCreatingBackup(@TempDir Path vaultPath) throws IOException {
		final Path vaultConfigFile = vaultPath.resolve(VAULTCONFIG_FILENAME);
		Files.write(vaultConfigFile, "test".getBytes());

		final VaultConfigBackupHelper vaultConfigBackupHelper = new VaultConfigBackupHelper(vaultPath);
		final Path configBackupPath = vaultConfigBackupHelper.createBackup();

		// Ensure that after the copy the files contain the same content
		MatcherAssert.assertThat(Files.mismatch(vaultConfigFile, configBackupPath), Matchers.is(-1L));
	}

	@Test
	public void testCreatingBackupWithNoVaultConfig(@TempDir Path vaultPath) {
		final VaultConfigBackupHelper vaultConfigBackupHelper = new VaultConfigBackupHelper(vaultPath);
		assertThrows(IOException.class, vaultConfigBackupHelper::createBackup);
	}

	@Test
	public void testCreatingBackupWithExistingConfig(@TempDir Path vaultPath) throws IOException {
		final Path vaultConfigFile = vaultPath.resolve(VAULTCONFIG_FILENAME);
		Files.write(vaultConfigFile, "test".getBytes());

		final VaultConfigBackupHelper vaultConfigBackupHelper = new VaultConfigBackupHelper(vaultPath);

		final BasicFileAttributes firstAttr = Files.readAttributes(vaultConfigBackupHelper.createBackup(), BasicFileAttributes.class);
		final BasicFileAttributes secondAttr = Files.readAttributes(vaultConfigBackupHelper.createBackup(), BasicFileAttributes.class);

		MatcherAssert.assertThat(secondAttr.lastModifiedTime().toInstant().isAfter(firstAttr.lastModifiedTime().toInstant()), Matchers.is(true));
	}
}