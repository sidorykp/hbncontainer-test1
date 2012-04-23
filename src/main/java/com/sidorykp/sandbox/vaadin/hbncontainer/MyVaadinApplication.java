/*
 * Copyright 2009 IT Mill Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.sidorykp.sandbox.vaadin.hbncontainer;

import com.sidorykp.sandbox.vaadin.hbncontainer.domain.Person;
import com.vaadin.Application;
import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.ui.Window;
import com.vaadin.ui.Table;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
@Configurable
public class MyVaadinApplication extends Application implements HbnContainer.SessionManager
{
    private Window window;

    @Autowired
    protected SessionFactory sf;

    @Autowired
    protected SampleDataProvider sampleDataProvider;

    protected static final Logger log = LoggerFactory.getLogger(MyVaadinApplication.class);

    @Override
    public void init()
    {
        log.debug("Application init");
        window = new Window("My Vaadin Application");
        setMainWindow(window);

        sampleDataProvider.prepareSampleData();

        HbnContainer c = new HbnContainer(Person.class, this);
        Table table = new Table();
        table.setImmediate(true);
        table.setContainerDataSource(c);
        window.addComponent(table);
    }

    @Override
    public Session getSession() {
        return sf.getCurrentSession();
    }
}
