package com.sidorykp.sandbox.vaadin.hbncontainer;

import com.sidorykp.sandbox.vaadin.hbncontainer.domain.AddressEntity;
import com.sidorykp.sandbox.vaadin.hbncontainer.domain.Person;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: pawel
 * Date: 4/23/12
 * Time: 9:21 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class SampleDataProviderImpl implements SampleDataProvider {
    @Autowired
    protected SessionFactory sf;

    @Override
    @Transactional (propagation = Propagation.REQUIRED)
    public void prepareSampleData() {
        Person b = new Person();
        b.setFirstName("Big");
        b.setLastName("Boss");
        sf.getCurrentSession().persist(b);

        for (int i = 0; i < 20; ++ i) {
            Person p = new Person();
            p.setFirstName("Foo" + i);
            p.setLastName("Bar" + i);
            AddressEntity a = new AddressEntity();
            a.setCity("City" + i);
            Set<AddressEntity> addresses = new HashSet<AddressEntity>();
            addresses.add(a);
            a = new AddressEntity();
            a.setCity("Village" + i);
            addresses.add(a);
            p.setAddresses(addresses);
            p.setBoss(b);
            sf.getCurrentSession().persist(p);
        }
    }

    @Override
    @Transactional (propagation = Propagation.REQUIRED)
    public Long createPerson() {
        Person person = new Person();
        return (Long) sf.getCurrentSession().save(person);
    }

    @Override
    @Transactional (propagation = Propagation.REQUIRED)
    public void updatePerson(Person person) {
        sf.getCurrentSession().merge(person);
    }

    @Override
    @Transactional (propagation = Propagation.REQUIRED)
    public void deletePerson(Person person) {
        Person pH = (Person) sf.getCurrentSession().load(Person.class, person.getId());
        sf.getCurrentSession().delete(pH);
    }
}