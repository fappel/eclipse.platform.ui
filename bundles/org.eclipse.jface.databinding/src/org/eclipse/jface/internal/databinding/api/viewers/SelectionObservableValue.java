/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.jface.internal.databinding.api.viewers;

import org.eclipse.jface.internal.databinding.api.observable.value.AbstractObservableValue;
import org.eclipse.jface.internal.databinding.api.observable.value.ValueDiff;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;

/**
 * @since 1.0
 * 
 */
public class SelectionObservableValue extends AbstractObservableValue {

	private StructuredViewer viewer;

	private Object currentSelection = null;

	private ISelectionChangedListener selectionChangedListener = new ISelectionChangedListener() {
		public void selectionChanged(SelectionChangedEvent event) {
			IStructuredSelection selection = (IStructuredSelection) event
					.getSelection();
			Object oldSelection = currentSelection;
			Object newSelection = selection.getFirstElement();
			if (newSelection != oldSelection) {
				currentSelection = newSelection;
				fireValueChange(new ValueDiff(oldSelection, newSelection));
			}
		}
	};

	/**
	 * @param viewer
	 */
	public SelectionObservableValue(StructuredViewer viewer) {
		this.viewer = viewer;
		viewer.addSelectionChangedListener(selectionChangedListener);
		this.currentSelection = ((IStructuredSelection) viewer.getSelection())
				.getFirstElement();
	}

	protected Object doGetValue() {
		return currentSelection;
	}
	
	public void dispose() {
		viewer.removeSelectionChangedListener(selectionChangedListener);
		viewer = null;
		currentSelection = null;
		selectionChangedListener = null;
		super.dispose();
	}

	public Object getValueType() {
		return Object.class;
	}

}
