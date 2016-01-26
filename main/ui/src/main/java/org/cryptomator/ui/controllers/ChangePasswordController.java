package org.cryptomator.ui.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cryptomator.ui.controls.SecPasswordField;
import org.cryptomator.ui.model.Vault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Text;

@Singleton
public class ChangePasswordController extends AbstractFXMLViewController {

	private static final Logger LOG = LoggerFactory.getLogger(ChangePasswordController.class);

	private ChangePasswordListener listener;
	private Vault vault;

	@FXML
	private SecPasswordField oldPasswordField;

	@FXML
	private SecPasswordField newPasswordField;

	@FXML
	private SecPasswordField retypePasswordField;

	@FXML
	private Button changePasswordButton;

	@FXML
	private Text messageText;

	@FXML
	private Hyperlink downloadsPageLink;

	private final Application app;

	@Inject
	public ChangePasswordController(Application app) {
		super();
		this.app = app;
	}

	@Override
	protected URL getFxmlResourceUrl() {
		return getClass().getResource("/fxml/change_password.fxml");
	}

	@Override
	protected ResourceBundle getFxmlResourceBundle() {
		return ResourceBundle.getBundle("localization");
	}

	@Override
	public void initialize() {
		oldPasswordField.textProperty().addListener(this::passwordFieldsDidChange);
		newPasswordField.textProperty().addListener(this::passwordFieldsDidChange);
		retypePasswordField.textProperty().addListener(this::passwordFieldsDidChange);
	}

	// ****************************************
	// Password fields
	// ****************************************

	private void passwordFieldsDidChange(ObservableValue<? extends String> property, String oldValue, String newValue) {
		boolean oldPasswordIsEmpty = oldPasswordField.getText().isEmpty();
		boolean newPasswordIsEmpty = newPasswordField.getText().isEmpty();
		boolean passwordsAreEqual = newPasswordField.getText().equals(retypePasswordField.getText());
		changePasswordButton.setDisable(oldPasswordIsEmpty || newPasswordIsEmpty || !passwordsAreEqual);
	}

	// ****************************************
	// Downloads link
	// ****************************************

	@FXML
	public void didClickDownloadsLink(ActionEvent event) {
		app.getHostServices().showDocument("https://cryptomator.org/downloads/");
	}

	// ****************************************
	// Change password button
	// ****************************************

	@FXML
	private void didClickChangePasswordButton(ActionEvent event) {
		throw new UnsupportedOperationException("TODO");
		/*
		 * downloadsPageLink.setVisible(false);
		 * final Path masterKeyPath = vault.getPath().resolve(Vault.VAULT_MASTERKEY_FILE);
		 * final Path masterKeyBackupPath = vault.getPath().resolve(Vault.VAULT_MASTERKEY_BACKUP_FILE);
		 * 
		 * // decrypt with old password:
		 * final CharSequence oldPassword = oldPasswordField.getCharacters();
		 * try (final InputStream masterKeyInputStream = Files.newInputStream(masterKeyPath, StandardOpenOption.READ)) {
		 * vault.getCryptor().decryptMasterKey(masterKeyInputStream, oldPassword);
		 * Files.copy(masterKeyPath, masterKeyBackupPath, StandardCopyOption.REPLACE_EXISTING);
		 * } catch (IOException ex) {
		 * messageText.setText(resourceBundle.getString("changePassword.errorMessage.decryptionFailed"));
		 * LOG.error("Decryption failed for technical reasons.", ex);
		 * newPasswordField.swipe();
		 * retypePasswordField.swipe();
		 * return;
		 * } catch (WrongPasswordException e) {
		 * messageText.setText(resourceBundle.getString("changePassword.errorMessage.wrongPassword"));
		 * newPasswordField.swipe();
		 * retypePasswordField.swipe();
		 * Platform.runLater(oldPasswordField::requestFocus);
		 * return;
		 * } catch (UnsupportedKeyLengthException ex) {
		 * messageText.setText(resourceBundle.getString("changePassword.errorMessage.unsupportedKeyLengthInstallJCE"));
		 * LOG.warn("Unsupported Key-Length. Please install Oracle Java Cryptography Extension (JCE).", ex);
		 * newPasswordField.swipe();
		 * retypePasswordField.swipe();
		 * return;
		 * } catch (UnsupportedVaultException e) {
		 * downloadsPageLink.setVisible(true);
		 * if (e.isVaultOlderThanSoftware()) {
		 * messageText.setText(resourceBundle.getString("changePassword.errorMessage.unsupportedVersion.vaultOlderThanSoftware") + " ");
		 * } else if (e.isSoftwareOlderThanVault()) {
		 * messageText.setText(resourceBundle.getString("changePassword.errorMessage.unsupportedVersion.softwareOlderThanVault") + " ");
		 * }
		 * newPasswordField.swipe();
		 * retypePasswordField.swipe();
		 * return;
		 * } finally {
		 * oldPasswordField.swipe();
		 * }
		 * 
		 * // when we reach this line, decryption was successful.
		 * 
		 * // encrypt with new password:
		 * final CharSequence newPassword = newPasswordField.getCharacters();
		 * try (final OutputStream masterKeyOutputStream = Files.newOutputStream(masterKeyPath, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.SYNC)) {
		 * vault.getCryptor().encryptMasterKey(masterKeyOutputStream, newPassword);
		 * messageText.setText(resourceBundle.getString("changePassword.infoMessage.success"));
		 * Platform.runLater(this::didChangePassword);
		 * // At this point the backup is still using the old password.
		 * // It will be changed as soon as the user unlocks the vault the next time.
		 * // This way he can still restore the old password, if he doesn't remember the new one.
		 * } catch (IOException ex) {
		 * LOG.error("Re-encryption failed for technical reasons. Restoring Backup.", ex);
		 * this.restoreBackupQuietly();
		 * } finally {
		 * newPasswordField.swipe();
		 * retypePasswordField.swipe();
		 * }
		 */
	}

	private void restoreBackupQuietly() {
		/*
		 * final Path masterKeyPath = vault.getPath().resolve(Vault.VAULT_MASTERKEY_FILE);
		 * final Path masterKeyBackupPath = vault.getPath().resolve(Vault.VAULT_MASTERKEY_BACKUP_FILE);
		 * try {
		 * Files.copy(masterKeyBackupPath, masterKeyPath, StandardCopyOption.REPLACE_EXISTING);
		 * } catch (IOException ex) {
		 * LOG.error("Restoring Backup failed.", ex);
		 * }
		 */
	}

	private void didChangePassword() {
		if (listener != null) {
			listener.didChangePassword(this);
		}
	}

	/* Getter/Setter */

	public Vault getVault() {
		return vault;
	}

	public void setVault(Vault vault) {
		this.vault = vault;
	}

	public ChangePasswordListener getListener() {
		return listener;
	}

	public void setListener(ChangePasswordListener listener) {
		this.listener = listener;
	}

	/* callback */

	interface ChangePasswordListener {
		void didChangePassword(ChangePasswordController ctrl);
	}

}
