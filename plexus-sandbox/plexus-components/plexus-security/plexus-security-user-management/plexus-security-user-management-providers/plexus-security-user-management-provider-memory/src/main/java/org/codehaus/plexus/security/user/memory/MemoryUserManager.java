package org.codehaus.plexus.security.user.memory;

import org.codehaus.plexus.security.user.UserManager;
import org.codehaus.plexus.security.user.User;
import org.codehaus.plexus.security.user.UserNotFoundException;

import java.util.Map;
import java.util.HashMap;

/**
 * @plexus.component
 *   role="org.codehaus.plexus.security.user.UserManager"
 *   role-hint="memory"
 */
public class MemoryUserManager
    implements UserManager
{
    private Map users;

    public MemoryUserManager()
    {
        users = new HashMap();
    }

    public User addUser( User user )
    {
        users.put( user.getPrincipal(), user );

        return user;
    }

    public User updateUser( User user )
    {
        return addUser( user );
    }

    public User findUser( Object principal )
        throws UserNotFoundException
    {
        User user = (User) users.get( principal );

        if ( user == null )
        {
            throw new UserNotFoundException( "Cannot find the user with the principal '" + principal + "'." );
        }

        return user;
    }

    public void deleteUser( Object principal )
    {
        users.remove( principal );
    }
}