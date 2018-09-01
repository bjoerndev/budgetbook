package de.bjrn.budgetbook.view.swing.open;

import java.awt.Component;
import java.awt.GridLayout;
import java.io.File;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileFilter;

import de.bjrn.budgetbook.BudgetBookSwing;
import de.bjrn.budgetbook.logic.BBConfig;
import de.bjrn.budgetbook.model.DemoMode;
import de.bjrn.budgetbook.view.i18.I18;
import de.bjrn.budgetbook.view.swing.BBViewMain;
import de.bjrn.budgetbook.view.swing.ShutdownListener;
import de.bjrn.budgetbook.view.swing.Splash;

public class OpenDialog extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private final static String PATH_DEFAULT = "~/bb.h2";

	private static final String DB_SUFFIX = ".mv.db";
	
	JPasswordField password = new JPasswordField();
	JButton bChoose;
	JProgressBar progressDecrypt = new JProgressBar();
	JPanel main;
	File file;
	Component loginArea;

	public OpenDialog() {
		file = new File(PATH_DEFAULT);
		main = new JPanel(new GridLayout(0, 1));
		main.add(new Splash());
		loginArea = createLoginArea();
		main.add(loginArea);
		add(main);
		progressDecrypt.setIndeterminate(true);
		setTitle(I18.tLabel("Splash.Title"));
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		password.requestFocus();
		new Thread(new Runnable() {
			@Override
			public void run() {
				lazyInit();
			}
		}).start();
	}
	
	/** Lazy part of boot to speedup */
	protected void lazyInit() {
		URL url = ClassLoader.getSystemResource(BBViewMain.ICON);
		if (url != null) {
			setIconImage(getToolkit().getImage(url));
		}
		addWindowListener(new ShutdownListener(null));
		progressDecrypt.setString(I18.tLabel("Decrypting"));
		progressDecrypt.setStringPainted(true);
	}

	private void cancel() {
		setVisible(false);
		System.exit(0);
	}

	private Component createLoginArea() {
		JPanel p = new JPanel(new GridLayout(0, 2));
		p.add(new JLabel(I18.tLabel("Project")));
		p.add(createFileChooser());
		p.add(new JLabel(I18.tLabel("Password")));
		p.add(password);
		password.addActionListener(e -> open());
		p.add(new JLabel(I18.tLabel("DemoMode")));
		final JCheckBox cbDemoMode = new JCheckBox(I18.tLabel("DemoModeDesc"));
		cbDemoMode.addChangeListener(e -> DemoMode.on = cbDemoMode.isSelected());
		p.add(cbDemoMode);
		// Cancel
		JButton b = new JButton(I18.tLabel("Cancel"));
		b.addActionListener(e -> cancel());
		p.add(b);
		// Open
		b = new JButton(I18.tLabel("Open"));
		b.addActionListener(e -> open());
		p.add(b);
		return p;
	}

	private Component createFileChooser() {
		bChoose = new JButton();
		setFile(file);
		bChoose.addActionListener(e -> selectFile(true));
		return bChoose;
	}

	private void selectFile(boolean open) {
		final JFileChooser fileDialog = new JFileChooser();
		fileDialog.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "H2 DB";
			}

			@Override
			public boolean accept(File f) {
				return f.getName().endsWith(DB_SUFFIX) || f.isDirectory();
			}
		});
		int returnVal = open ? fileDialog.showOpenDialog(this) : fileDialog.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	setFile(fileDialog.getSelectedFile());
        }
	}

	private void setFile(File myFile) {
		if (myFile.getName().endsWith(DB_SUFFIX)) {
			String name = myFile.getName();
			myFile = new File(myFile.getParentFile(), name.substring(0, name.length() - DB_SUFFIX.length()));
		}
		file = myFile;
		bChoose.setText(file.getName());
		bChoose.setToolTipText(file.getAbsolutePath());
	}

	private void open() {
		showOpening(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				BBConfig.INSTANCE().setFile(file);
				BBConfig.INSTANCE().setPwd(password.getPassword());
				try {
					BudgetBookSwing.main(null);
					setVisible(false);
				} catch (Exception e) {
					showOpening(false);
					JOptionPane.showMessageDialog(OpenDialog.this, e.getMessage(), I18.tLabel("Error"),
							JOptionPane.WARNING_MESSAGE);
					password.requestFocus();
				}
			}
		}).start();
	}

	private void showOpening(boolean opening) {
		if (opening) {
			main.remove(loginArea);
			main.add(progressDecrypt);
		} else {
			main.remove(progressDecrypt);
			main.add(loginArea);
		}
		main.updateUI();
	}
}
