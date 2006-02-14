/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jface.internal.databinding.api.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.eclipse.jface.internal.databinding.api.IDataBindingContext;
import org.eclipse.jface.internal.databinding.api.IObservableFactory;
import org.eclipse.jface.internal.databinding.api.Property;
import org.eclipse.jface.internal.databinding.api.observable.IObservable;
import org.eclipse.jface.internal.databinding.api.observable.value.IObservableValue;
import org.eclipse.jface.internal.databinding.nonapi.beans.JavaBeanObservableValue;

/**
 * This is an optional IUpdatableFactory that forces all JavaBeans updatables
 * that it creates to be an IUpdatableValue, even if the actual type of
 * the property is a collection.
 * 
 * @since 1.0
 */
public class JavaBeansScalarObservableValueFactory extends Object implements
		IObservableFactory {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.internal.databinding.api.IObservableFactory#createObservable(org.eclipse.jface.internal.databinding.api.IDataBindingContext, java.lang.Object)
	 */
	public IObservable createObservable(IDataBindingContext bindingContext, Object description) {
        if (! (description instanceof Property)) {
           return null;
        }
        Property property = (Property) description;
        Object collectionContainer = property.getObject();
        String propertyName = (String) property.getPropertyID();
        
        BeanInfo beanInfo = null;
        try {
           beanInfo = Introspector.getBeanInfo(collectionContainer.getClass());
        } catch (IntrospectionException e) {
           return null;
        }

        boolean found = false;
        int position = 0;
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        while (!found && position < pds.length) {
           if (pds[position].getName().equals(propertyName)) {
              found = true;
              break;
           }
           ++position;
        }
        if (!found) {
           return null;
        }
        IObservableValue updatable = new JavaBeanObservableValue(collectionContainer, pds[position]);
        return updatable;
     }

}
