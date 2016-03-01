package net.bootsfaces.component.tree;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorHolder;

import net.bootsfaces.C;
import net.bootsfaces.component.AttributeMapWrapper;
import net.bootsfaces.component.tree.event.TreeNodeEventListener;
import net.bootsfaces.component.tree.model.Node;

/** This class holds the attributes of &lt;b:dataTable /&gt;. */
@ResourceDependencies({ @ResourceDependency(library = "bsf", name = "css/core.css", target = "head"),
		@ResourceDependency(library = "bsf", name = "css/bsf.css", target = "head"),
		@ResourceDependency(library = "javax.faces", name = "jsf.js", target = "head"),
		@ResourceDependency(library = "bsf", name = "js/bootstrap-treeview.min.js", target = "body"),
		@ResourceDependency(library = "bsf", name = "css/bootstrap-treeview.min.css", target = "head") })
@FacesComponent("net.bootsfaces.component.tree.Tree")
public class Tree extends UIComponentBase implements ClientBehaviorHolder {
	public static final String COMPONENT_TYPE = "net.bootsfaces.component.tree.Tree";
	public static final String COMPONENT_FAMILY = C.BSFCOMPONENT;
	public static final String DEFAULT_RENDERER = "net.bootsfaces.component.tree.Tree";

	private Map<String, Object> attributes;
	private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList("click"));

	protected enum PropertyKeys {
		value, 
		nodeSelectionListener, 
		showTags, 
		showIcon, 
		showCheckbox, 
		enableLinks, 
		collapseIcon, 
		expandIcon, 
		color, 
		update, 
		renderRoot
		;

		String toString;

		PropertyKeys(String toString) {
			this.toString = toString;
		}

		PropertyKeys() {
		}

		public String toString() {
			return ((this.toString != null) ? this.toString : super.toString());
		}
	}

	public Tree() {
		setRendererType(DEFAULT_RENDERER);
	}


	@Override
	public Map<String, Object> getAttributes() {
		if (attributes == null)
			attributes = new AttributeMapWrapper(this, super.getAttributes());
		return attributes;
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public TreeNodeEventListener getNodeSelectionListener() {
		return (TreeNodeEventListener) this.getStateHelper().eval(PropertyKeys.nodeSelectionListener);
	}

	public void setNodeSelectionListener(final TreeNodeEventListener nodeSelectionListener) {
		this.updateStateHelper(PropertyKeys.nodeSelectionListener.toString(), nodeSelectionListener);
	}

	public Collection<String> getEventNames() {
		return EVENT_NAMES;
	}

	public String getDefaultEventName() {
		return "click";
	}

	public Node getValue() {
		return (Node) this.getStateHelper().eval(PropertyKeys.value);
	}

	public void setValue(final Node _value) {
		this.updateStateHelper(PropertyKeys.value.toString(), _value);
	}
	
	public boolean isRenderRoot() {
		Boolean value = (Boolean) getStateHelper().eval(PropertyKeys.renderRoot, true);
		return (boolean) value;
	}

	public void setRenderRoot(boolean _renderRoot) {
		getStateHelper().put(PropertyKeys.renderRoot, _renderRoot);
	}

	public boolean isShowIcon() {
		Boolean value = (Boolean) getStateHelper().eval(PropertyKeys.showIcon, true);
		return (boolean) value;
	}

	public void setShowIcon(boolean _showIcon) {
		getStateHelper().put(PropertyKeys.showIcon, _showIcon);
	}

	public boolean isShowCheckbox() {
		Boolean value = (Boolean) getStateHelper().eval(PropertyKeys.showCheckbox, false);
		return (boolean) value;
	}

	public void setShowCheckbox(boolean _showCheckbox) {
		getStateHelper().put(PropertyKeys.showCheckbox, _showCheckbox);
	}

	public boolean isShowTags() {
		Boolean value = (Boolean) getStateHelper().eval(PropertyKeys.showTags, false);
		return (boolean) value;
	}

	public void setShowTags(boolean _showTags) {
		getStateHelper().put(PropertyKeys.showTags, _showTags);
	}

	public boolean isEnableLinks() {
		Boolean value = (Boolean) getStateHelper().eval(PropertyKeys.enableLinks, false);
		return (boolean) value;
	}

	public void setEnableLinks(boolean _enableLinks) {
		getStateHelper().put(PropertyKeys.enableLinks, _enableLinks);
	}

	public String getCollapseIcon() {
		String value = (String) getStateHelper().eval(PropertyKeys.collapseIcon);
		return value;
	}

	public void setCollapseIcon(String _collapseIcon) {
		getStateHelper().put(PropertyKeys.collapseIcon, _collapseIcon);
	}

	public String getExpandIcon() {
		String value = (String) getStateHelper().eval(PropertyKeys.expandIcon);
		return value;
	}

	public void setExpandIcon(String _expandIcon) {
		getStateHelper().put(PropertyKeys.expandIcon, _expandIcon);
	}

	public String getColor() {
		String value = (String) getStateHelper().eval(PropertyKeys.color);
		return value;
	}

	public void setColor(String _color) {
		getStateHelper().put(PropertyKeys.color, _color);
	}

	public String getUpdate() {
		String value = (String) getStateHelper().eval(PropertyKeys.update);
		return value;
	}

	public void setUpdate(String _update) {
		getStateHelper().put(PropertyKeys.update, _update);
	}

	private void updateStateHelper(final String propertyName, final Object value) {
		this.getStateHelper().put(propertyName, value);

		final ValueExpression ve = this.getValueExpression(propertyName);

		if (ve != null) {
			ve.setValue(this.getFacesContext().getELContext(), value);
		}
	}
}
