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
            sf.getCurrentSession().persist(p);
        }
    }
}