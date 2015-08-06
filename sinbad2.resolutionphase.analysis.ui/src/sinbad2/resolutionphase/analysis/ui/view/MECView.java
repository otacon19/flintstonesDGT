package sinbad2.resolutionphase.analysis.ui.view;


import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import sinbad2.element.ProblemElementsManager;
import sinbad2.element.ProblemElementsSet;
import sinbad2.element.alternative.listener.AlternativesChangeEvent;
import sinbad2.element.alternative.listener.IAlternativesChangeListener;
import sinbad2.element.criterion.listener.CriteriaChangeEvent;
import sinbad2.element.criterion.listener.ICriteriaChangeListener;
import sinbad2.resolutionphase.analysis.ui.provider.MECContentProvider;
import sinbad2.resolutionphase.analysis.ui.provider.MECIdLabelProvider;

import org.eclipse.swt.widgets.Table;


public class MECView extends ViewPart implements IAlternativesChangeListener, ICriteriaChangeListener {
	
	public static final String ID = "flintstones.resolutionphase.analysis.ui.view.mec"; //$NON-NLS-1$
	
	private ProblemElementsManager _elementManager;
	private ProblemElementsSet _elementSet;
	
	private MECContentProvider _provider;
	
	private Composite _container;
	private CLabel _formulaText;
	private Text _idText;
	private Button _addFormulaButton;
	private TableViewer _tableViewer;
	
	private String _id;
	
	private ControlDecoration _idControlDecoration;
	
	public MECView() {
		_elementManager = ProblemElementsManager.getInstance();
		_elementSet = _elementManager.getActiveElementSet();
		
		_elementSet.registerAlternativesChangesListener(this);
		_elementSet.registerCriteriaChangesListener(this);
	}
	
	@Override
	public void createPartControl(Composite parent) {
		_container = new Composite(parent, SWT.NONE);
		
		GridLayout layout = new GridLayout(1, false);
		layout.horizontalSpacing = 0;
		layout.marginBottom = 0;
		layout.marginHeight = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.marginTop = 10;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		_container.setLayout(layout);
		_container.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		Composite mecFormulaComposite = new Composite(_container, SWT.NONE);
		layout = new GridLayout(1, false);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2);
		mecFormulaComposite.setLayoutData(gridData);
		mecFormulaComposite.setLayout(layout);
		mecFormulaComposite.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		Label formulaLabel = new Label(mecFormulaComposite, SWT.NULL);
		gridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 2);
		formulaLabel.setLayoutData(gridData);
		formulaLabel.setText("MEC formula");
		formulaLabel.setFont(SWTResourceManager.getFont("Cantarell", 10, SWT.BOLD)); //$NON-NLS-1$
		formulaLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		_formulaText = new CLabel(mecFormulaComposite, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY);
		gridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 4);
		gridData.heightHint = 75;
		_formulaText.setLayoutData(gridData);
		_formulaText.setEnabled(false);
		_formulaText.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		Composite addButtonAndIdComposite = new Composite(mecFormulaComposite, SWT.NONE);
		layout = new GridLayout(3, false);
		gridData = new GridData(SWT.RIGHT, SWT.RIGHT, true, false, 1, 2);
		addButtonAndIdComposite.setLayoutData(gridData);
		addButtonAndIdComposite.setLayout(layout);
		addButtonAndIdComposite.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		Label idLabel = new Label(addButtonAndIdComposite, SWT.NONE);
		gridData = new GridData(SWT.RIGHT, SWT.RIGHT, false, false, 1, 1);
		gridData.verticalIndent = 12;
		idLabel.setLayoutData(gridData);
		idLabel.setText("MEC id:");
		idLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		_idText = new Text(addButtonAndIdComposite, SWT.BORDER);
		gridData = new GridData(SWT.RIGHT, SWT.RIGHT, false, false, 1, 1);
		gridData.heightHint = 19;
		gridData.widthHint = 150;
		gridData.verticalIndent = 1;
		_idText.setLayoutData(gridData);
		
		_idText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				_id = ((Text) e.getSource()).getText().trim();
				validate();
			}
		});
		
		_idControlDecoration = createNotificationDecorator(_idText);
		_idControlDecoration.show();
		
		_addFormulaButton = new Button(addButtonAndIdComposite, SWT.NULL);
		_addFormulaButton.setLayoutData(new GridData(SWT.RIGHT, SWT.RIGHT, false, false, 1, 1));
		_addFormulaButton.setText("Add");
		_addFormulaButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD).createImage());
		_addFormulaButton.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		_addFormulaButton.setEnabled(false);
		
		Composite tableMECComposite = new Composite(_container, SWT.NONE);
		layout = new GridLayout(1, false);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gridData.heightHint = 275;
		tableMECComposite.setLayoutData(gridData);
		tableMECComposite.setLayout(layout);
		tableMECComposite.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		Label tableLabel = new Label(tableMECComposite, SWT.NULL);
		gridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 2);
		tableLabel.setLayoutData(gridData);
		tableLabel.setText("MECs");
		tableLabel.setFont(SWTResourceManager.getFont("Cantarell", 10, SWT.BOLD)); //$NON-NLS-1$
		tableLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		_tableViewer = new TableViewer(tableMECComposite, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION | SWT.SCROLL_LINE);
		Table table = _tableViewer.getTable();
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		table.setLayoutData(gd_table);
		_tableViewer.getTable().setHeaderVisible(true);
		_provider = new MECContentProvider(_tableViewer);
		_tableViewer.setContentProvider(_provider);
		
		_tableViewer.getTable().addListener(SWT.MeasureItem, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				event.height = 25;
				
			}
		});
		
		_tableViewer.setInput(_provider.getInput());
		getSite().setSelectionProvider(_tableViewer);
		
		addColumns();
		
	}

	private void addColumns() {
		TableViewerColumn tvc = new TableViewerColumn(_tableViewer, SWT.NONE);
		tvc.setLabelProvider(new MECIdLabelProvider());
		TableColumn tc = tvc.getColumn();
		tc.setText("Id");
		tc.setResizable(false);
		tc.pack();
		tvc = new TableViewerColumn(_tableViewer, SWT.NONE);
		tvc.setLabelProvider(new MECIdLabelProvider());
		tc = tvc.getColumn();
		tc.setText("Formula");
		tc.setResizable(false);
		tc.pack();
	}

	@Override
	public void setFocus() {
		_tableViewer.getControl().setFocus();
		
	}

	@Override
	public void notifyAlternativesChange(AlternativesChangeEvent event) {
		switch(event.getChange()) {
			case ADD_CONTEXT:
				String contextId = (String) event.getNewValue();
				if(_formulaText.getText() != null) {
					_formulaText.setText(_formulaText.getText() + " / " + contextId);
				} else {
					_formulaText.setText(contextId);
				}
				break;
			case REMOVE_CONTEXT:
				String removeContextId = (String) event.getOldValue();
				String ns = _formulaText.getText();
				_formulaText.setText(ns.replace(removeContextId, ""));
			default: 
				break;
		
		}
	}
	
	@Override
	public void notifyCriteriaChange(CriteriaChangeEvent event) {
		switch(event.getChange()) {
		case ADD_INDEX:
			String indexId = (String) event.getNewValue();
			if(_formulaText.getText() != null) {
				_formulaText.setText(_formulaText.getText() + " / " + indexId);
			} else {
				_formulaText.setText(indexId);
			}
			break;
		case REMOVE_INDEX:
			String removeContextId = (String) event.getOldValue();
			String ns = _formulaText.getText();
			_formulaText.setText(ns.replace(removeContextId, ""));
		default: 
			break;
		}
		
	}
		
	private ControlDecoration createNotificationDecorator(Text text) {
		ControlDecoration controlDecoration = new ControlDecoration(text, SWT.LEFT | SWT.TOP);
		FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);
		controlDecoration.setImage(fieldDecoration.getImage());
		validate(controlDecoration, ""); //$NON-NLS-1$
		
		return controlDecoration;
	}
	
	private boolean validate(ControlDecoration controlDecoration, String text) {
		controlDecoration.setDescriptionText(text);
		if(text.isEmpty()) {
			controlDecoration.hide();
			return true;
		} else {
			controlDecoration.show();
			return false;
			
		}
	}
	
	private void validate() {		
		boolean validId;
		String message = ""; //$NON-NLS-1$
				
		if(!_id.isEmpty()) {
			/*if(_ids.contains(_id)) {
				message = "Duplicated id";
			}*/
		} else {
				message = "Empty value";
		}
				
		validId = validate(_idControlDecoration, message);
					
		_addFormulaButton.setEnabled(validId);			
	}

}

