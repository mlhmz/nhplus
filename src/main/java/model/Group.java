package model;

import enums.PermissionKey;

/**
 * interface for the user groups
 */
public interface Group {
    /**
     * identifier for database
     */
    String getIdentifier();

    /**
     * representation for gui
     */
    String getGuiRepresentation();

    /**
     * list of permissions
     */
    PermissionKey[] getPermissions();
}
