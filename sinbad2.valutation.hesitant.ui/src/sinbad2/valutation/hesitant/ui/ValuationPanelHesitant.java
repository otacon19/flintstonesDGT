package sinbad2.valutation.hesitant.ui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import sinbad2.domain.linguistic.fuzzy.FuzzySet;
import sinbad2.domain.linguistic.fuzzy.label.LabelLinguisticDomain;
import sinbad2.valuation.Valuation;
import sinbad2.valuation.hesitant.EUnaryRelationType;
import sinbad2.valuation.hesitant.HesitantValuation;
import sinbad2.valuation.ui.valuationpanel.ValuationPanel;

public class ValuationPanelHesitant extends ValuationPanel {
	
	private Button _primaryButton;
	private Button _compositeButton;
	
	private Button _unaryRelationshipButton;
	private Button _binaryRelationshipButton;
	
	private LabelLinguisticDomain _label;
	
	private Combo _hesitantEvaluationCombo1;
	private Combo _hesitantEvaluationCombo2;
	
	private Label _betweenLabel;
	private Label _andLabel;

	private Composite _hesitantValueComposite;
	private Composite _hesitantRelationshipComposite;
	
	private ModifyListener _hesitantEvaluationCombo1ModifyListener;
	private ModifyListener _hesitantEvaluationCombo2ModifyListener;
	
	private List<Integer> _selectIndexes;
	private Map<String, List<Integer>> _binaryIndexes;
	
	protected void createControls() {		
		GridLayout layout = new GridLayout(4, false);
		layout.verticalSpacing = 5;
		_valuationPart.setLayout(layout);
		
		Label label = new Label(_valuationPart, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("Cantarell", 11, SWT.BOLD));
		label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		label.setText("Hesitant evaluation");
		
		Composite buttonsComposite = new Composite(_valuationPart, SWT.NONE);
		buttonsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1));
		buttonsComposite.setLayout(new GridLayout(2, true));
		
		Composite hesitantButtonsComposite = new Composite(buttonsComposite, SWT.NONE);
		hesitantButtonsComposite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1));
		hesitantButtonsComposite.setLayout(new GridLayout(1,  false));
		
		_primaryButton = new Button(hesitantButtonsComposite, SWT.RADIO);
		_primaryButton.setText("Primary");
		_primaryButton.setSelection(true);
		
		_compositeButton = new Button(hesitantButtonsComposite, SWT.RADIO);
		_compositeButton.setText("Composite");
		
		_hesitantRelationshipComposite = new Composite(buttonsComposite, SWT.NONE);
		_hesitantRelationshipComposite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1));
		_hesitantRelationshipComposite.setLayout(new GridLayout(1,  false));
		
		_unaryRelationshipButton = new Button(_hesitantRelationshipComposite, SWT.RADIO);
		_unaryRelationshipButton.setText("Unary");
		
		_binaryRelationshipButton = new Button(_hesitantRelationshipComposite, SWT.RADIO);
		_binaryRelationshipButton.setText("Binary");
		
		_selectIndexes = new LinkedList<Integer>();
		_binaryIndexes = new HashMap<String, List<Integer>>();
		
		initControls();
		
		_primaryButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(_hesitantRelationshipComposite.getEnabled()) {
					_hesitantRelationshipComposite.setEnabled(false);
					_unaryRelationshipButton.setEnabled(false);
					_binaryRelationshipButton.setEnabled(false);
				}
				
				checkHesitantValues(false, false, false, true, false);
				selectionChange();
			}
		});
		
		_compositeButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(!_hesitantRelationshipComposite.getEnabled()) {
					_hesitantRelationshipComposite.setEnabled(true);
					_unaryRelationshipButton.setEnabled(true);
					_binaryRelationshipButton.setEnabled(true);
					if(_binaryRelationshipButton.getSelection()) {
						checkHesitantValues(true, true, true, true, false);
					} else {
						checkHesitantValues(false, true, false, true, false);
					}
				}
				
				selectionChange();
			}
		});
		
		_unaryRelationshipButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(_hesitantRelationshipComposite.getEnabled()) {
					if(_compositeButton.getSelection()) {
						checkHesitantValues(false, true, false, true, false);
					} else {
						checkHesitantValues(false, false, false, true, false);
					}
				}
				
				selectionChange();
			}
		});
		
		_binaryRelationshipButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(_hesitantRelationshipComposite.getEnabled()) {
					if(_compositeButton.getSelection()) {
						checkHesitantValues(true, true, true, true, false);
					} else {
						checkHesitantValues(false, false, false, true, false);
					}
				}
				selectionChange();
			}
		});
	}

	public Object getSelection() {
		
		if(!(_selectIndexes.size() > 0)) {
			return _hesitantEvaluationCombo2.getSelectionIndex();
		} else {
			return _selectIndexes;
		}
	}
	
	public boolean differentValue() {
		
		if(_valuation == null) {
			return true;
		} else {
			return ((HesitantValuation) _valuation).getLabel().getName().equals(_label);
		}
	}

	@Override
	public Valuation getNewValuation() {
		
		HesitantValuation result = null;
		
		if (_valuation == null) {
			result = (HesitantValuation) _valuationsManager.copyValuation(HesitantValuation.ID);
			result.setDomain(_domain);
		} else {
			result = (HesitantValuation) _valuation.clone();
		}
		
		if(_binaryIndexes.containsKey("Binary")) {
			result.setBinaryRelation(_binaryIndexes.get("Binary").get(0), _binaryIndexes.get("Binary").get(1));
		} else {
			result.setLabel(_label);
		}
		
		return result;
	}

	protected void initControls() {
		
		setHesitantForm();
		selectionChange();
	}
	
	private void checkHesitantValues(boolean between, boolean hesitantCombo1, boolean and, boolean hesitantCombo2, boolean first) {
		
		_binaryIndexes.clear();
		_selectIndexes.clear();
		
		int fields = 0;
		
		if(between) {
			fields++;
		}
		
		if(hesitantCombo1) {
			fields++;
		}
		
		if(and) {
			fields++;
		}
		
		if(hesitantCombo2) {
			fields++;
		}
		
		if(_hesitantValueComposite != null) {
			_hesitantValueComposite.dispose();
		}
		
		_hesitantValueComposite = new Composite(_valuationPart, SWT.NONE);
		_hesitantValueComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 4, 1));
		GridLayout layout = new GridLayout(fields, false);
		_hesitantValueComposite.setLayout(layout);
		
		if(between) {
			if(_betweenLabel != null) {
				_betweenLabel.dispose();
			}
			_betweenLabel = new Label(_hesitantValueComposite, SWT.NONE);
			_betweenLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
			_betweenLabel.setText("Between");
		}
		
		if(hesitantCombo1) {
			if(_hesitantEvaluationCombo1 != null) {
				_hesitantEvaluationCombo1.dispose();
			}
			_hesitantEvaluationCombo1 = new Combo(_hesitantValueComposite, SWT.BORDER);
			_hesitantEvaluationCombo1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			_hesitantEvaluationCombo1.addSelectionListener(new SelectionAdapter() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					selectionChange();
				}
			});
		}
		
		if(and) {
			if(_andLabel != null) {
				_andLabel.dispose();
			}
			_andLabel = new Label(_hesitantValueComposite, SWT.NONE);
			_andLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
			_andLabel.setText("and");
		}
		
		if(hesitantCombo2) {
			if(_hesitantEvaluationCombo2 != null) {
				_hesitantEvaluationCombo2.dispose();
			}
			_hesitantEvaluationCombo2 = new Combo(_hesitantValueComposite, SWT.BORDER);
			_hesitantEvaluationCombo2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			_hesitantEvaluationCombo2.addSelectionListener(new SelectionAdapter() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					selectionChange();
				}
			});
		}
		
		String[] items = new String[((FuzzySet) _domain).getLabelSet().getCardinality()];
		for(int i = 0; i < items.length; ++i) {
			items[i] = ((FuzzySet) _domain).getLabelSet().getLabel(i).getName();
		}
		
		boolean composite = false;
		if(_hesitantEvaluationCombo1 != null) {
			if(!_hesitantEvaluationCombo1.isDisposed()) {
				composite = true;
			}
		}
		
		if(composite) {
			boolean binary = false;
			if(_andLabel != null) {
				if(!_andLabel.isDisposed()) {
					binary = true;
					int length = items.length;
					String auxItems1[] = new String[1];
					String auxItems2[] = new String[length - 1];
					for(int i = 0; i < length; ++i) {
						if(i == 0) {
							auxItems1[i] = items[i];
						} else {
							auxItems2[i - 1] = items[i];
						}
					}
					_hesitantEvaluationCombo1.setItems(auxItems1);
					_hesitantEvaluationCombo1.select(0);
					_hesitantEvaluationCombo1.pack();
					_hesitantEvaluationCombo2.setItems(auxItems2);
					_hesitantEvaluationCombo2.select(0);
					_hesitantEvaluationCombo2.pack();
				}
			}
			
			if(!binary) {
				String[] unaryTypes = new String[EUnaryRelationType.values().length];
				for(int i = 0; i < EUnaryRelationType.values().length; ++i) {
					unaryTypes[i] = EUnaryRelationType.values()[i].toString();
				}
				_hesitantEvaluationCombo1.setItems(unaryTypes);
				_hesitantEvaluationCombo1.select(0);
				_hesitantEvaluationCombo1.pack();
				_hesitantEvaluationCombo2.setItems(items);
				_hesitantEvaluationCombo2.select(0);
				_hesitantEvaluationCombo2.pack();		
			}
		} else {
			_hesitantEvaluationCombo2.setItems(items);
			_hesitantEvaluationCombo2.select(0);
			_hesitantEvaluationCombo2.pack();
		}
		
		if(first) {
			if(_valuation != null) {
				int pos[] = ((HesitantValuation) _valuation).getEnvelopeIndex();
				if(((HesitantValuation) _valuation).isPrimary()) {
					_hesitantEvaluationCombo2.select(pos[1]);
				} else if(((HesitantValuation) _valuation).isUnary()) {
					switch(((HesitantValuation) _valuation).getUnaryRelation()) {
					case AtLeast:
						_hesitantEvaluationCombo1.select(1);
						_hesitantEvaluationCombo2.select(pos[0]);
						break;
					case AtMost:
						_hesitantEvaluationCombo1.select(0);
						_hesitantEvaluationCombo2.select(pos[1]);
						break;
					case GreaterThan:
						_hesitantEvaluationCombo1.select(3);
						_hesitantEvaluationCombo2.select(pos[0] - 1);
						break;
					case LowerThan:
						_hesitantEvaluationCombo1.select(2);
						_hesitantEvaluationCombo2.select(pos[1] + 1);
						break;
					default:
						break;
					}
				} else {
					int length = items.length;
					String[] auxItems1 = new String[pos[1]];
					String[] auxItems2 = new String[length - (pos[0] + 1)];
					
					int j = 0;
					for(int i = 0; i < length; ++i) {
						if(i < pos[1]) {
							auxItems1[i] = items[i];
						}
						
						if(i > pos[0]) {
							auxItems2[j] = items[i];
							++j;
						}
					}
					
					_hesitantEvaluationCombo1.setItems(auxItems1);
					_hesitantEvaluationCombo1.select(pos[0]);
					_hesitantEvaluationCombo2.setItems(auxItems2);
					_hesitantEvaluationCombo2.select(pos[1] - (pos[0] + 1));
				}
			}
		}
		
		if(_hesitantEvaluationCombo1 != null) {
			if(!_hesitantEvaluationCombo1.isDisposed()) {
				_hesitantEvaluationCombo1ModifyListener = new ModifyListener() {
					
					@Override
					public void modifyText(ModifyEvent e) {
						boolean binary = false;
						
						_selectIndexes.clear();
						
						if(_andLabel != null) {
							if(!_andLabel.isDisposed()) {
								binary = true;
								String items1[] = _hesitantEvaluationCombo1.getItems();
								String items2[] = _hesitantEvaluationCombo2.getItems();
								
								int i = 0;
								boolean find = false;
								String pivotItem = items1[items1.length - 1];
								
								do {
									if(items2[i].equals(pivotItem)) {
										find = true;
									} else {
										++i;
									}
								} while((!find) && (i < items2.length));
								
								int pos1 = _hesitantEvaluationCombo1.getSelectionIndex();
								int pos2 = items1.length + _hesitantEvaluationCombo2.getSelectionIndex();
								
								if(find) {
									pos2 -= i + 1;
								}
								_selectIndexes.add(pos1);
								_selectIndexes.add(pos2);
								_binaryIndexes.put("Binary", _selectIndexes);
								
								_hesitantEvaluationCombo1.removeModifyListener(_hesitantEvaluationCombo1ModifyListener);
								_hesitantEvaluationCombo2.removeModifyListener(_hesitantEvaluationCombo2ModifyListener);
								
								int length = items2.length + items1.length;
								if(find) {
									length -= i + 1;
								}
								String[] items = new String[length];
								int j = 0;
								for(int pos = 0; pos < length; ++pos) {
									if(pos < items1.length) {
										items[pos] = items1[pos];
									} else {
										if(find) {
											items[pos] = items2[j + (i + 1)];
										} else {
											items[pos] = items2[j];
										}
										++j;
									}
								}
								String[] auxItems1 = new String[pos2];
								String[] auxItems2 = new String[length - (pos1 + 1)];
								j = 0;
								for(int pos = 0; pos < length; ++pos) {
									if(pos < pos2) {
										auxItems1[pos] = items[pos];
									}
									if(pos > pos1) {
										auxItems2[j] = items[pos];
										++j;
									}
								}
								
								_hesitantEvaluationCombo1.setItems(auxItems1);
								_hesitantEvaluationCombo1.select(pos1);
								_hesitantEvaluationCombo2.setItems(auxItems2);
								_hesitantEvaluationCombo2.select(pos2 - (pos1 + 1));
								
								_hesitantEvaluationCombo1.addModifyListener(_hesitantEvaluationCombo1ModifyListener);
								_hesitantEvaluationCombo2.addModifyListener(_hesitantEvaluationCombo2ModifyListener);
							}
						}
						
						_selectIndexes.clear();
						
						if(!binary) {
							String value = _hesitantEvaluationCombo1.getItems()[_hesitantEvaluationCombo1.getSelectionIndex()];
							if (EUnaryRelationType.GreaterThan.toString().equals(value)) {
								_selectIndexes.add(_hesitantEvaluationCombo2.getSelectionIndex() + 1);
								_selectIndexes.add(((FuzzySet) _domain).getLabelSet().getCardinality() - 1);
							} else if(EUnaryRelationType.LowerThan.toString().equals(value)) {
								_selectIndexes.add(0);
								_selectIndexes.add(_hesitantEvaluationCombo2.getSelectionIndex() - 1);
							} else if(EUnaryRelationType.AtLeast.toString().equals(value)) {
								_selectIndexes.add(_hesitantEvaluationCombo2.getSelectionIndex());
								_selectIndexes.add(((FuzzySet) _domain).getLabelSet().getCardinality() - 1);
							} else if(EUnaryRelationType.AtMost.toString().equals(value)) {
								_selectIndexes.add(0);
								_selectIndexes.add(_hesitantEvaluationCombo2.getSelectionIndex());
							}
						}	
					}
				};
				
				_hesitantEvaluationCombo1.addModifyListener(_hesitantEvaluationCombo1ModifyListener);
			}
		}
		
		_hesitantEvaluationCombo2ModifyListener = new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				
				_selectIndexes.clear();
				
				boolean binary = false;
				if(_andLabel != null) {
					if(!_andLabel.isDisposed()) {
						binary = true;
						String items1[] = _hesitantEvaluationCombo1.getItems();
						String items2[] = _hesitantEvaluationCombo2.getItems();
						
						int i = 0;
						boolean find = false;
						String pivotItem = items1[items1.length - 1];
						
						do {
							if (items2[i].equals(pivotItem)) {
								find = true;
							} else {
								i++;
							}
						} while((!find) && (i < items2.length));
						
						int pos1 = _hesitantEvaluationCombo1.getSelectionIndex();
						int pos2 = items1.length + _hesitantEvaluationCombo2.getSelectionIndex();
						
						if (find) {
							pos2 -= i + 1;
						}
						
						_selectIndexes.add(pos1);
						_selectIndexes.add(pos2);
						_binaryIndexes.put("Binary", _selectIndexes);
						
						_hesitantEvaluationCombo1.removeModifyListener(_hesitantEvaluationCombo1ModifyListener);
						_hesitantEvaluationCombo2.removeModifyListener(_hesitantEvaluationCombo2ModifyListener);
						int length = items2.length + items1.length;

						if (find) {
							length -= i + 1;
						}
						String[] items = new String[length];
						int j = 0;
						for (int pos = 0; pos < length; pos++) {
							if (pos < items1.length) {
								items[pos] = items1[pos];
							} else {
								if (find) {
									items[pos] = items2[j + (i + 1)];
								} else {
									items[pos] = items2[j];
								}
								++j;
							}
						}
						String auxItems1[] = new String[pos2];
						String auxItems2[] = new String[length - (pos1 + 1)];
						j = 0;
						for (int pos = 0; pos < length; pos++) {
							if (pos < pos2) {
								auxItems1[pos] = items[pos];
							}
							if (pos > pos1) {
								auxItems2[j] = items[pos];
								j++;
							}
						}
						_hesitantEvaluationCombo1.setItems(auxItems1);
						_hesitantEvaluationCombo1.select(pos1);
						_hesitantEvaluationCombo2.setItems(auxItems2);
						_hesitantEvaluationCombo2.select(pos2 - (pos1 + 1));
						
						_hesitantEvaluationCombo1.addModifyListener(_hesitantEvaluationCombo1ModifyListener);
						_hesitantEvaluationCombo2.addModifyListener(_hesitantEvaluationCombo2ModifyListener);
					}
				}
				
				if (!binary) {
					boolean unary = false;
					if (_hesitantEvaluationCombo1 != null) {
						if (!_hesitantEvaluationCombo1.isDisposed()) {
							unary = true;
							
							_selectIndexes.clear();
							
							String value = _hesitantEvaluationCombo1.getItems()[_hesitantEvaluationCombo1.getSelectionIndex()];
							if (EUnaryRelationType.GreaterThan.toString().equals(value)) {
								_selectIndexes.add(_hesitantEvaluationCombo2.getSelectionIndex() + 1);
								_selectIndexes.add(((FuzzySet) _domain).getLabelSet().getCardinality() - 1);
							} else if(EUnaryRelationType.LowerThan.toString().equals(value)) {
								_selectIndexes.add(0);
								_selectIndexes.add(_hesitantEvaluationCombo2.getSelectionIndex() - 1);
							} else if(EUnaryRelationType.AtLeast.toString().equals(value)) {
								_selectIndexes.add(_hesitantEvaluationCombo2.getSelectionIndex());
								_selectIndexes.add(((FuzzySet) _domain).getLabelSet().getCardinality() - 1);
							} else if(EUnaryRelationType.AtMost.toString().equals(value)) {
								_selectIndexes.add(0);
								_selectIndexes.add(_hesitantEvaluationCombo2.getSelectionIndex());
							}
						}
					}
					
					if (!unary) {
						_selectIndexes.add(_hesitantEvaluationCombo2.getSelectionIndex());
					}
					
				}
			}
		};
		_hesitantEvaluationCombo2.addModifyListener(_hesitantEvaluationCombo2ModifyListener);
		_hesitantValueComposite.layout();
		_valuationPart.layout();
		
	}
	
	private void setHesitantForm() {
		
		if(_valuation == null) {
			checkHesitantValues(false, false, false, true, true);	
			_hesitantRelationshipComposite.setEnabled(false);
			_unaryRelationshipButton.setSelection(true);
			_unaryRelationshipButton.setEnabled(false);
			_binaryRelationshipButton.setEnabled(false);
			_primaryButton.setSelection(true);
		}else {
			if(((HesitantValuation) _valuation).isPrimary()) {
				checkHesitantValues(false, false, false, true, true);
				_hesitantRelationshipComposite.setEnabled(false);
				_unaryRelationshipButton.setSelection(true);
				_unaryRelationshipButton.setEnabled(false);
				_binaryRelationshipButton.setEnabled(false);
				_primaryButton.setSelection(true);
			} else if(((HesitantValuation) _valuation).isUnary()) {
				checkHesitantValues(false, true, false, true, true);
				_hesitantRelationshipComposite.setEnabled(true);
				_compositeButton.setSelection(true);
				_unaryRelationshipButton.setSelection(true);
				_unaryRelationshipButton.setEnabled(true);
				_binaryRelationshipButton.setEnabled(true);
			} else {
				checkHesitantValues(true, true, true, true, true);
				_hesitantRelationshipComposite.setEnabled(true);
				_compositeButton.setSelection(true);
				_binaryRelationshipButton.setSelection(true);
				_unaryRelationshipButton.setEnabled(true);
				_binaryRelationshipButton.setEnabled(true);
			}
		}
		
		if(((FuzzySet) _domain).getLabelSet().getCardinality() == 1) {
			_compositeButton.setEnabled(false);
		} else {
			_compositeButton.setEnabled(true);
		}
	}
	
}
