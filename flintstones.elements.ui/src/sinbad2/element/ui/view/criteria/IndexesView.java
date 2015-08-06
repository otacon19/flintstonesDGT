package sinbad2.element.ui.view.criteria;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.part.ViewPart;


import sinbad2.element.ui.view.criteria.provider.CriteriaContentProvider;
import sinbad2.element.ui.view.criteria.provider.CriterionIdLabelProvider;

public class IndexesView extends ViewPart {

	public static final String ID = "flintstones.element.ui.view.indexes"; //$NON-NLS-1$
	public static final String CONTEXT_ID = "flintstones.element.ui.view.indexes.indexes_view"; //$NON-NLS-1$

	private static final IContextService _contextService = (IContextService) PlatformUI
			.getWorkbench().getService(IContextService.class);

	private TableViewer _tableViewer;

	private CriteriaContentProvider _provider;

	public IndexesView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		_tableViewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);

		_tableViewer.getTable().addListener(SWT.MeasureItem, new Listener() {

			@Override
			public void handleEvent(Event event) {
				event.height = 25;

			}
		});

		_provider = new CriteriaContentProvider(_tableViewer);
		_tableViewer.setContentProvider(_provider);

		addColumns();
		hookContextMenu();
		hookFocusListener();

		_tableViewer.setInput(_provider.getInput());
		getSite().setSelectionProvider(_tableViewer);
	}

	private void addColumns() {
		TableViewerColumn tvc = new TableViewerColumn(_tableViewer, SWT.NONE);
		tvc.setLabelProvider(new CriterionIdLabelProvider());
		TableColumn tc = tvc.getColumn();
		tc.setResizable(false);
		tc.setWidth(50);
	}

	private void hookContextMenu() {
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(_tableViewer.getTable());
		_tableViewer.getTable().setMenu(menu);
		getSite().registerContextMenu(menuManager, _tableViewer);
	}

	private void hookFocusListener() {
		_tableViewer.getControl().addFocusListener(new FocusListener() {

			private IContextActivation activation = null;

			@Override
			public void focusLost(FocusEvent e) {
				_contextService.deactivateContext(activation);
			}

			@Override
			public void focusGained(FocusEvent e) {
				activation = _contextService.activateContext(CONTEXT_ID);
			}
		});

	}

	@Override
	public void setFocus() {
		_tableViewer.getControl().setFocus();

	}

}
