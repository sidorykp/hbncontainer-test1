package com.sidorykp.sandbox.vaadin.hbncontainer;

import com.sidorykp.sandbox.vaadin.hbncontainer.domain.Person;

/**
 * Created with IntelliJ IDEA.
 * User: pawel
 * Date: 4/2/12
 * Time: 1:15 AM
 * To change this template use File | Settings | File Templates.
 */
public interface SampleDataProvider {
    public void prepareSampleData();

    public Long createPerson();

    public Person updatePerson(Person person);

    public void deletePerson(Person person);
}
