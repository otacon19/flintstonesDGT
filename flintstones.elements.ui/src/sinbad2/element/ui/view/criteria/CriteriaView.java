package sinbad2.element.ui.view.criteria;


import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.services.ISourceProviderService;

import sinbad2.element.criterion.Criterion;
import sinbad2.element.ui.Images;
import sinbad2.element.ui.handler.criterion.modify.ModifyCriterionHandler;
import sinbad2.element.ui.nls.Messages;
import sinbad2.element.ui.sourceprovider.criteria.BrothersCriteriaSelectedSourceProvider;
import sinbad2.element.ui.view.criteria.provider.CriteriaContentProvider;
import sinbad2.element.ui.view.criteria.provider.CriterionIdLabelProvider;

public class CriteriaView extends ViewPart {
	
	public static final String ID = "flintstones.element.ui.view.criteria"; //$NON-NLS-1$
	public static final String CONTEXT_ID = "flintstones.element.ui.view.criteria.criteria_view"; //$NON-NLS-1$
	
	private static final IContextService _contextService = (IContextService) PlatformUI.getWorkbench().getService(IContextService.class);
	
	private TreeViewer _treeViewer;
	
	private CriteriaContentProvider _provider;
	
	public CriteriaView() {}
	
	@Override
	public void createPartControl(Composite parent) {
		_treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		
		_treeViewer.getTree().addListener(SWT.MeasureItem, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				event.height = 25;
				
			}
		});
		
		_provider = new CriteriaContentProvider(_treeViewer);
		_treeViewer.setContentProvider(_provider);
		
		addColumns();
		hookContextMenu();
		hookFocusListener();
		hookSelectionChangedListener();
		hookDoubleClickListener();
		
		_treeViewer.setInput(_provider.getInput());
		getSite().setSelectionProvider(_treeViewer);
	}

	private void addColumns() {
		TreeViewerColumn tvc = new TreeViewerColumn(_treeViewer, SWT.NONE);
		tvc.setLabelProvider(new CriterionIdLabelProvider());
		TreeColumn tc = tvc.getColumn();
		tc.setMoveable(true);
		tc.setResizable(false);
		tc.setText(Messages.CriteriaView_Criteria);
		tc.setImage(Images.Criterion);
		tc.pack();			
	}
	
	private void hookContextMenu() {
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(_treeViewer.getTree());
		_treeViewer.getTree().setMenu(menu);
		getSite().registerContextMenu(menuManager, _treeViewer);
	}
	
	private void hookDoubleClickListener() {
		_treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				Criterion criterion = (Criterion) selection.getFirstElement();
				
				ModifyCriterionHandler modifyCriterionHandler = new ModifyCriterionHandler(criterion);
				
				try {
					modifyCriterionHandler.execute(null);
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	private void hookSelectionChangedListener() {
		_treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			private ISourceProviderService sourceProviderService = (ISourceProviderService) getSite()
					.getService(ISourceProviderService.class);
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				BrothersCriteriaSelectedSourceProvider sourceProvider = (BrothersCriteriaSelectedSourceProvider) sourceProviderService.
						getSourceProvider(BrothersCriteriaSelectedSourceProvider.BROTHERS_CRITERIA_SELECTED);
				
				boolean oldState = BrothersCriteriaSelectedSourceProvider.ENABLED.equals(sourceProvider.getCurrentState().get(
						BrothersCriteriaSelectedSourceProvider.BROTHERS_CRITERIA_SELECTED));
				boolean newState = true;
				
				
				if(newState != oldState) {
					sourceProvider.toogleState();
				}
				
			}
		});
		
	}

	private void hookFocusListener() {
		_treeViewer.getControl().addFocusListener(new FocusListener() {
			
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
		_treeViewer.getControl().setFocus();
		
	}

}
