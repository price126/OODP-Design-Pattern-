package user;/*
 * user.Party.java
 *
 * Version:
 *   $Id$
 *
 * Revisions:
 *   $Log: user.Party.java,v $
 *   Revision 1.3  2003/02/09 21:21:31  ???
 *   Added lots of comments
 *
 *   Revision 1.2  2003/01/12 22:23:32  ???
 *   *** empty log message ***
 *
 *   Revision 1.1  2003/01/12 19:09:12  ???
 *   Adding user.Party, bowling.Lane, Bowler, and bowling.Alley.
 *
 */

/**
 * Container that holds bowlers
 */

import java.util.Vector;

public class Party {

    /**
     * Vector of bowlers in this party
     */
    private final Vector myBowlers;

    /**
     * Constructor for a user.Party
     *
     * @param bowlers Vector of bowlers that are in this party
     */

    public Party(Vector bowlers) {
        myBowlers = new Vector(bowlers);
    }

    /**
     * Accessor for members in this party
     *
     * @return A vector of the bowlers in this party
     */

    public Vector getMembers() {
        return myBowlers;
    }

}
