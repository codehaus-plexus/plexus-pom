package org.codehaus.plexus.security.authorization.rbac.store.jdo;

/*
 * Copyright 2005 The Codehaus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.codehaus.plexus.PlexusTestCase;
import org.codehaus.plexus.jdo.ConfigurableJdoFactory;
import org.codehaus.plexus.jdo.DefaultConfigurableJdoFactory;
import org.codehaus.plexus.jdo.JdoFactory;
import org.codehaus.plexus.security.authorization.rbac.jdo.JdoRole;
import org.codehaus.plexus.security.authorization.rbac.store.RbacStore;
import org.codehaus.plexus.security.rbac.Role;
import org.jpox.SchemaTool;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * JdoRbacStoreTest:
 *
 * @author Jesse McConnell <jmcconnell@apache.org>
 * @version $Id:$
 */
public class JdoRbacStoreTest
    extends PlexusTestCase
{
    private RbacStore store = null;

    public RbacStore getStore()
    {
        return store;
    }

    public void setStore( RbacStore store )
    {
        this.store = store;
    }

    /**
     * Creates a new RbacStore which contains no data.
     */
    protected void setUp()
        throws Exception
    {
        super.setUp();

        ConfigurableJdoFactory jdoFactory = (ConfigurableJdoFactory) lookup( JdoFactory.ROLE );
        assertEquals( DefaultConfigurableJdoFactory.class.getName(), jdoFactory.getClass().getName() );

        jdoFactory.setPersistenceManagerFactoryClass( "org.jpox.PersistenceManagerFactoryImpl" ); //$NON-NLS-1$

        jdoFactory.setDriverName( "org.hsqldb.jdbcDriver" ); //$NON-NLS-1$

        jdoFactory.setUrl( "jdbc:hsqldb:mem:" + getName() ); //$NON-NLS-1$

        jdoFactory.setUserName( "sa" ); //$NON-NLS-1$

        jdoFactory.setPassword( "" ); //$NON-NLS-1$

        jdoFactory.setProperty( "org.jpox.transactionIsolation", "READ_UNCOMMITTED" ); //$NON-NLS-1$ //$NON-NLS-2$

        jdoFactory.setProperty( "org.jpox.poid.transactionIsolation", "READ_UNCOMMITTED" ); //$NON-NLS-1$ //$NON-NLS-2$

        jdoFactory.setProperty( "org.jpox.autoCreateSchema", "true" ); //$NON-NLS-1$ //$NON-NLS-2$

        Properties properties = jdoFactory.getProperties();

        for ( Iterator it = properties.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry entry = (Map.Entry) it.next();

            System.setProperty( (String) entry.getKey(), (String) entry.getValue() );
        }

        SchemaTool.createSchemaTables( new URL[] { getClass()
            .getResource( "/org/codehaus/plexus/security/authorization/rbac/jdo/package.jdo" ) }, null, false ); //$NON-NLS-1$

        PersistenceManagerFactory pmf = jdoFactory.getPersistenceManagerFactory();

        assertNotNull( pmf );

        PersistenceManager pm = pmf.getPersistenceManager();

        pm.close();

        setStore( (JdoRbacStore) lookup( RbacStore.ROLE, "jdo" ) );
    }

    public void testStoreInitialization()
        throws Exception
    {
        assertNotNull( getStore() );

        Role role = new JdoRole();

        getStore().addRole( role );

        assertEquals( 1, getStore().getAllRoles().size() );
        
        getStore().removeRole( role );
    }
}