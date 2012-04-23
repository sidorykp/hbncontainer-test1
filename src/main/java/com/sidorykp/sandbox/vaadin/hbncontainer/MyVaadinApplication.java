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
import com.vaadin.data.Container;
import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.event.Action;
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
public class MyVaadinApplication extends Application implements HbnContainer.SessionManager, Action.Handler
{
    protected static final Action CREATE = new Action("Create");

    protected static final Action UPDATE = new Action("Update");

    protected static final Action DELETE = new Action("Delete");

    protected static final Action[] ACTIONS = new Action[] { CREATE, UPDATE, DELETE };

    private Window window;

    protected HbnContainer c;

    protected Table table;

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

        c = new HbnContainer(Person.class, this);
        c.addListener(new Container.ItemSetChangeListener() {
            @Override
            public void containerItemSetChange(Container.ItemSetChangeEvent itemSetChangeEvent) {
                log.debug("containerItemSetChange");
            }
        });
        table = new Table();
        table.setImmediate(true);
        table.setContainerDataSource(c);
        table.addActionHandler(this);
        window.addComponent(table);
    }

    @Override
    public Session getSession() {
        return sf.getCurrentSession();
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        return ACTIONS;
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
        if (action == UPDATE) {
            Person p = (Person) c.getItem(target).getPojo();
            log.debug("update Person: " + p);
            sampleDataProvider.updatePerson(p);
            table.refreshRowCache();
        }
    }
}
