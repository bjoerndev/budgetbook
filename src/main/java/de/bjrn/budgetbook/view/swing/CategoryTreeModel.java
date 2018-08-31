package de.bjrn.budgetbook.view.swing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import de.bjrn.budgetbook.model.BBModel;
import de.bjrn.budgetbook.model.Category;
import de.bjrn.budgetbook.view.i18.I18;

public class CategoryTreeModel implements TreeModel {

	BBModel model;
	DefaultMutableTreeNode root;
	Object rootObject;
	Map<Object, List<Object>> mapContent;
	
	public CategoryTreeModel(BBModel model) {
		this.model = model;
		rootObject = I18.tLabel("Categories");
		root = new DefaultMutableTreeNode(rootObject);
		initMap();
	}

	private void initMap() {
		mapContent = new HashMap<Object, List<Object>>();
		List<Category> cats = model.getCategories(null);
		List<Object> objs = new Vector<Object>();
		objs.addAll(cats);
		mapContent.put(rootObject, objs);
		for (Category cat : cats) {
			initMap(cat);
		}
	}

	private void initMap(Category cat) {
		List<Object> content = new Vector<Object>();
		for (Category sub : model.getCategories(cat)) {
			initMap(sub);
			content.add(sub);
		}
		mapContent.put(cat, content);
	}

	@Override
	public Object getRoot() {
		return root;
	}

	@Override
	public Object getChild(Object parent, int index) {
		return mapContent.get(unwrap(parent)).get(index);
	}

	@Override
	public int getChildCount(Object parent) {
		return mapContent.get(unwrap(parent)).size();
	}

	@Override
	public boolean isLeaf(Object node) {
		node = unwrap(node);
		return mapContent.get(node) == null || mapContent.get(node).isEmpty();
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		return mapContent.get(unwrap(parent)).indexOf(unwrap(child));
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
	}
	
	private Object unwrap(Object o) {
		if (o instanceof DefaultMutableTreeNode) {
			return ((DefaultMutableTreeNode) o).getUserObject();
		}
		return o;
	}

}
