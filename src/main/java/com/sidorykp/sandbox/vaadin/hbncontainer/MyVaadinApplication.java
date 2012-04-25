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

    protected static final Action CREATE_HBN = new Action("Create in HBN");

    protected static final Action UPDATE = new Action("Update");

    protected static final Action UPDATE_HBN = new Action("Update in HBN");

    protected static final Action DELETE = new Action("Delete");

    protected static final Action DELETE_HBN = new Action("Delete in HBN");

    protected static final Action[] ACTIONS = new Action[] { CREATE, CREATE_HBN, UPDATE, UPDATE_HBN, DELETE, DELETE_HBN };

    private Window window;

    protected HbnContainer<Person> c;

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

        c = new HbnContainer<Person>(Person.class, this);
        c.addListener(new Container.ItemSetChangeListener() {
            @Override
            public void containerItemSetChange(Container.ItemSetChangeEvent itemSetChangeEvent) {
                log.debug("containerItemSetChange");
            }
        });
        table = new Table();
        table.setImmediate(true);
        table.setSelectable(true);
        table.setContainerDataSource(c);
        String[] personTableColumns = {"firstName", "lastName", "phoneNumber"};
        table.setVisibleColumns(personTableColumns);
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
        if (action == UPDATE_HBN) {
            Person p = c.getItem(target).getPojo();
            log.debug("update in HBN Person: " + p);
            p.setFirstName(p.getFirstName() + "1");
            sampleDataProvider.updatePerson(p);
            table.refreshRowCache();
        } else if (action == UPDATE) {
            Person p = c.getItem(target).getPojo();
            log.debug("update Person: " + p);
            p.setFirstName(p.getFirstName() + "1");
            try {
                // TODO does not work because HbnContainer should perform merge first and it does not
                c.updateEntity(p);
            } catch (Exception e) {
                log.warn("exception in updateEntity for " + p.getId(), e);
            }
        } else if (action == CREATE_HBN) {
            // TODO the entity is created but it is not shown in the table
            Person p = sampleDataProvider.createPerson();
            log.debug("create Person: " + p);
        } else if (action == CREATE) {
            // TODO the entity has only ID and it is shown as a very thin row in the table
            Long pId = (Long) c.addItem();
            log.debug("create Person: " + pId);
        } else if (action == DELETE_HBN) {
            Person p = c.getItem(target).getPojo();
            log.debug("delete in HBN Person: " + p);
            sampleDataProvider.deletePerson(p);
            // TODO does not work since the Table tries to reload the entity AFTER its removal
            table.refreshRowCache();
        } else if (action == DELETE) {
            Person p = c.getItem(target).getPojo();
            log.debug("delete Person: " + p);
            // TODO does not work since the Table tries to reload the entity AFTER its removal
            c.removeItem(p.getId());
        }
    }
}
