/**
 *  Copyright 2014-16 by Riccardo Massera (TheCoder4.Eu) and Stephan Rauh (http://www.beyondjava.net).
 *
 *  This file is part of BootsFaces.
 *
 *  BootsFaces is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  BootsFaces is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with BootsFaces. If not, see <http://www.gnu.org/licenses/>.
 */

package net.bootsfaces.component.dataTable;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import net.bootsfaces.component.ajax.AJAXRenderer;
import net.bootsfaces.render.CoreRenderer;
import net.bootsfaces.render.Responsive;
import net.bootsfaces.render.Tooltip;
import net.bootsfaces.utils.BsfUtils;

/** This class generates the HTML code of &lt;b:dataTable /&gt;. */
@FacesRenderer(componentFamily = "net.bootsfaces.component", rendererType = "net.bootsfaces.component.dataTable.DataTable")
public class DataTableRenderer extends CoreRenderer {

//	@Override
//	public void decode(FacesContext context, UIComponent component) {
//		super.decode(context, component);
//		DataTable dataTable = (DataTable) component;
//	}


	/**
	 * This methods generates the HTML code of the current b:dataTable.
	 * <code>encodeBegin</code> generates the start of the component. After the,
	 * the JSF framework calls <code>encodeChildren()</code> to generate the
	 * HTML code between the beginning and the end of the component. For
	 * instance, in the case of a panel component the content of the panel is
	 * generated by <code>encodeChildren()</code>. After that,
	 * <code>encodeEnd()</code> is called to generate the rest of the HTML code.
	 *
	 * @param context
	 *            the FacesContext.
	 * @param component
	 *            the current b:dataTable.
	 * @throws IOException
	 *             thrown if something goes wrong when writing the HTML code.
	 */
	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {

		if (!component.isRendered()) {
			return;
		}
		DataTable dataTable = (DataTable) component;

		ResponseWriter rw = context.getResponseWriter();
		String clientId = dataTable.getClientId();

		// put custom code here
		// Simple demo widget that simply renders every attribute value
		rw.startElement("table", dataTable);
		rw.writeAttribute("id", clientId, "id");

		String styleClass = "table table-striped table-bordered";
		if (dataTable.isRowHighlight()) styleClass += " table-hover ";
		if (dataTable.getStyleClass() != null)
			styleClass += " " + dataTable.getStyleClass();
		styleClass += Responsive.getResponsiveStyleClass(dataTable, false);
		styleClass += " " + clientId.replace(":", "") + "Table";
		rw.writeAttribute("class", styleClass, "class");
		Tooltip.generateTooltip(context, dataTable, rw);
		rw.writeAttribute("cellspacing", "0", "cellspacing");
		rw.writeAttribute("style", dataTable.getStyle(), "style");
		AJAXRenderer.generateBootsFacesAJAXAndJavaScript(context, dataTable, rw);

		generateHeader(context, dataTable, rw);
		generateBody(context, dataTable, rw);
		generateFooter(context, dataTable, rw);
	}

	private void generateFooter(FacesContext context, DataTable dataTable, ResponseWriter rw) throws IOException {
		if(dataTable.isMultiColumnSearch()) {
			rw.startElement( "tfoot", dataTable );
			rw.startElement( "tr", dataTable );
			List<UIComponent> columns = dataTable.getChildren();
			for ( UIComponent column : columns ) {
				if (!column.isRendered()) {
					continue;
				}
				rw.startElement( "th", dataTable );
				if ( column.getFacet( "header" ) != null ) {
					UIComponent facet = column.getFacet( "header" );
					facet.encodeAll( context );
				}
				rw.endElement( "th" );
			}
			rw.endElement( "tr" );
			rw.endElement( "tfoot" );
		}
	}

	private void generateBody(FacesContext context, DataTable dataTable, ResponseWriter rw) throws IOException {
		rw.startElement("tbody", dataTable);
		int rows = dataTable.getRowCount();
		dataTable.setRowIndex(-1);
		for (int row = 0; row < rows; row++) {
			dataTable.setRowIndex(row);
			if (dataTable.isRowAvailable()) {
				rw.startElement("tr", dataTable);
				List<UIComponent> columns = dataTable.getChildren();
				for (UIComponent column : columns) {
				    if (!column.isRendered()) {
				        continue;
				    }
					rw.startElement("td", dataTable);
					column.encodeChildren(context);
					rw.endElement("td");
				}
				rw.endElement("tr");
			}
		}
		rw.endElement("tbody");
		dataTable.setRowIndex(-1);
	}

	private void generateHeader(FacesContext context, DataTable dataTable, ResponseWriter rw) throws IOException {
		rw.startElement("thead", dataTable);
		rw.startElement("tr", dataTable);
		int index = 0;
		List<UIComponent> columns = dataTable.getChildren();
		for (UIComponent column : columns) {
		    if (!column.isRendered()) {
		        continue;
		    }
			rw.startElement("th", dataTable);
			if (column.getFacet("header") != null) {
				UIComponent facet = column.getFacet("header");
				facet.encodeAll(context);
			} else if (column.getAttributes().get("label") != null) {
				rw.writeText(column.getAttributes().get("label"), null);
			} else {
				boolean labelHasBeenRendered = false;
				for (UIComponent c : column.getChildren()) {
					if (c.getAttributes().get("label") != null) {
						rw.writeText(c.getAttributes().get("label"), null);
						labelHasBeenRendered = true;
						break;
					}
				}
				if (!labelHasBeenRendered) {
					for (UIComponent c : column.getChildren()) {
						if (c.getAttributes().get("value") != null) {
							rw.writeText(c.getAttributes().get("value"), null);
							labelHasBeenRendered = true;
							break;
						}
					}

				}
				if (!labelHasBeenRendered) {
					rw.writeText("Column #" + index, null);
				}
			}
			if (column.getFacet("order") != null) {
				Map<Integer, String> columnSortOrder;
				if (dataTable.getColumnSortOrderMap() == null) {
					dataTable.initColumnSortOrderMap();
				}
				columnSortOrder = dataTable.getColumnSortOrderMap();
				UIComponent facet = column.getFacet("order");
				String order = facet.toString();
				columnSortOrder.put(index, order);
			}
			rw.endElement("th");
			index++;
		}
		rw.endElement("tr");
		rw.endElement("thead");
	}

	/**
	 * This methods generates the HTML code of the current b:dataTable.
	 * <code>encodeBegin</code> generates the start of the component. After the,
	 * the JSF framework calls <code>encodeChildren()</code> to generate the
	 * HTML code between the beginning and the end of the component. For
	 * instance, in the case of a panel component the content of the panel is
	 * generated by <code>encodeChildren()</code>. After that,
	 * <code>encodeEnd()</code> is called to generate the rest of the HTML code.
	 *
	 * @param context
	 *            the FacesContext.
	 * @param component
	 *            the current b:dataTable.
	 * @throws IOException
	 *             thrown if something goes wrong when writing the HTML code.
	 */
	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		DataTable dataTable = (DataTable) component;
		Map<Integer, String> columnSortOrder = dataTable.getColumnSortOrderMap();
		Integer page = 0;
		Integer pageLength = dataTable.getPageLength();
		String searchTerm = "''";
		String orderString = "[]";
		if ( columnSortOrder != null ) {
			StringBuilder sb = new StringBuilder();
			int i = 0;
			for ( Map.Entry<Integer, String> entry : columnSortOrder.entrySet() ) {
				String separator = ( i > 0 ) ? "," : "";
				sb.append( separator )
				  .append( "[" )
				  .append( entry.getKey() )
				  .append( "," )
				  .append( "'" )
				  .append( entry.getValue() )
				  .append( "'" )
				  .append( "]" );
				i++;
			}
			orderString = sb.toString();
		}
		ResponseWriter rw = context.getResponseWriter();
		String clientIdRaw = dataTable.getClientId();
		String clientId = clientIdRaw.replace(":", "");
		String widgetVar = dataTable.getWidgetVar();
		if (null == widgetVar) {
			widgetVar = clientId+"Widget";
		}
		String lang = determineLanguage(context, dataTable);
		rw.endElement("table");
		Tooltip.activateTooltips(context, dataTable);
		rw.startElement("script", component);
		//# Start enclosure
		rw.writeText("$(document).ready(function() {", null);
		//# Enclosure-scoped variable initialization
		rw.writeText(widgetVar + " = $('." + clientId + "Table" + "');" +
					 //# Get instance of wrapper, and replace it with the unwrapped table.
					 "var wrapper = $('#" + clientIdRaw.replace( ":","\\\\:" ) + "_wrapper');" +
					 "wrapper.replaceWith(" + widgetVar +");" +
					 "var table = " + widgetVar +".DataTable({" +
					 "	fixedHeader: " + dataTable.isFixedHeader() + "," +
					 "	responsive: " + dataTable.isResponsive() + ", " +
					 "	paging: " + dataTable.isPaginated() + ", " +
					 "	pageLength: " + pageLength + ", " +
					 "	lengthMenu: " + dataTable.getPageLengthMenu() + ", " +
					 "	searching: " + dataTable.isSearching() + ", " +
					 "	order: " + orderString + ", " +
					 "  stateSave: " + dataTable.isSaveState() + ", " +
					 (dataTable.getScrollSize() > 0 ? " scrollY: " + dataTable.getScrollSize() + ", scrollCollapse: " + dataTable.isScrollCollapse() + "," : "") +
					 (BsfUtils.isStringValued(lang) ? "  language: { url: '" + lang + "' } " : "") +
					 "});" +
					 "var workInProgressErrorMessage = 'Multiple DataTables on the same page are not yet supported when using " +
					 "dataTableProperties attribute; Could not save state';", null);

		if(dataTable.isMultiColumnSearch())	{
			//# Footer stuff: https://datatables.net/examples/api/multi_filter.html
			//# Convert footer column text to input textfields
			rw.writeText( widgetVar + ".find('tfoot th').each(function() {" +
						  "var title = $(this).text();" +
						  "$(this).html('<input class=\"input-sm\" type=\"text\" placeholder=\"Search ' + title + '\" />');" +
						  "});", null );
			//# Add event listeners for each input
			rw.writeText( "table.columns().every( function () {" +
						  "var that = this;" +
						  "$( 'input', this.footer() ).on( 'keyup change', function () {" +
						  "    if ( that.search() !== this.value ) {" +
						  "        that.search( this.value ).draw('page');" +
						  "    }" +
						  "} );" +
						  "} );", null );
		}
		//# End enclosure
		rw.writeText("} );",null );
		rw.endElement("script");
	}

	/**
	 * Determine if the user specify a lang
	 * Otherwise return null to avoid language settings.
	 *
	 * @param fc
	 * @param dataTable
	 * @return
	 */
	private String determineLanguage(FacesContext fc, DataTable dataTable) {
		final Set<String> availableLanguages = new HashSet<String>(Arrays.asList(
		     new String[] {"de", "en", "es", "fr", "hu", "it", "pl", "ru"}
		));
		if(BsfUtils.isStringValued(dataTable.getCustomLangUrl())) {
			return dataTable.getCustomLangUrl();
		} else if(BsfUtils.isStringValued(dataTable.getLang())) {
			String lang = dataTable.getLang();
			if(availableLanguages.contains(lang)) return determineLanguageUrl(fc, lang);
		}
		return null;
	}

	/**
	 * Determine the locale to set-up to dataTable component.
	 * The locale is determined in this order:
	 * - if customLangUrl is specified, it is the value set up
	 * - otherwise, the system check if locale is explicit specified
	 * - otherwise it takes from the ViewRoot
	 *
	 * @param fc
	 * @param dataTable
	 * @return
	 */
	private String determineLanguageUrl(FacesContext fc, String lang) {
		// Build resource url
		return fc.getApplication().getResourceHandler().createResource("jq/ui/i18n/dt/datatable-" + lang + ".json", "bsf").getRequestPath();
	}

	@Override
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		// Children are already rendered in encodeBegin()
	}

}
