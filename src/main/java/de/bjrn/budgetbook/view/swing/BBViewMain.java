package de.bjrn.budgetbook.view.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.kordamp.ikonli.swing.FontIcon;
import org.kordamp.ikonli.websymbols.Websymbols;

import de.bjrn.budgetbook.logic.BBLogic;
import de.bjrn.budgetbook.model.BBModel;
import de.bjrn.budgetbook.view.i18.I18;
import de.bjrn.budgetbook.view.swing.helper.ContextUpdater;
import de.bjrn.budgetbook.view.swing.helper.JNaviButton;

public class BBViewMain extends JFrame implements ContextUpdater {
	private static final long serialVersionUID = 1L;
	
	private static final int NAVI_ICON_SIZE = 20;
	public static final String ICON = "de/bjrn/budgetbook/images/icon.png";
	
	private BBLogic logic;
	private JComponent context;
	private List<JNaviButton> buttons;

	public BBViewMain(BBLogic logic) {
		super(I18.tLabel("Splash.Title"));
		this.logic = logic;
		initUI();
	}

	public void showMain() {
		setSize(1000, 800);
		setLocationRelativeTo(null); // Center on screen
        setVisible(true);
	}

	private void initUI() {
		addWindowListener(new ShutdownListener(logic));
		setLayout(new BorderLayout());
		context = new JPanel(new BorderLayout());
		BBViewAbstract view = new BBViewAccesses(this);
		view.showUI();
		setContext(view);
		getContentPane().add(createNavigation(), BorderLayout.NORTH);
		getContentPane().add(context, BorderLayout.CENTER);
		
		URL url = ClassLoader.getSystemResource(ICON);
		if (url != null) {
			setIconImage(getToolkit().getImage(url));
		}
	}

	private Component createNavigation() {
		JPanel navi = new JPanel(new GridLayout(1, 0));
		for (JNaviButton button : getNaviButtons()) {
			navi.add(button);
		}
		return navi;
	}
	
	public BBLogic getLogic() {
		return logic;
	}
	
	public BBModel getModel() {
		return logic.getModel();
	}

	private List<JNaviButton> getNaviButtons() {
		if (buttons == null) {
			buttons = new Vector<JNaviButton>();
			buttons.add(new JNaviButton(I18.tLabel("Accesses"), FontIcon.of(Websymbols.USER, NAVI_ICON_SIZE, Color.BLACK), this, new BBViewAccesses(this)));
			buttons.add(new JNaviButton(I18.tLabel("Transactions"), FontIcon.of(Websymbols.BOXEDLIST, NAVI_ICON_SIZE, Color.GRAY), this, new BBViewTransactions(this)));
			buttons.add(new JNaviButton(I18.tLabel("Level"), FontIcon.of(Websymbols.RATING, NAVI_ICON_SIZE, Color.ORANGE), this, new BBViewLevel(this)));
			buttons.add(new JNaviButton(I18.tLabel("Categories"), FontIcon.of(Websymbols.LIST, NAVI_ICON_SIZE, Color.BLACK), this, new BBViewCategories(this)));
			buttons.add(new JNaviButton(I18.tLabel("Analysis"), FontIcon.of(Websymbols.TABLE, NAVI_ICON_SIZE, Color.BLUE), this, new BBViewEvaluations(this)));
		}
		return buttons;
	}

	@Override
	public void setContext(JComponent ctx) {
		unselectNavi();
		context.removeAll();
		context.add(ctx, BorderLayout.CENTER);
		context.updateUI();
		context.setBackground(Color.gray);

	}

	private void unselectNavi() {
		for (JNaviButton button : getNaviButtons()) {
			button.unselect();
		}
	}

}
