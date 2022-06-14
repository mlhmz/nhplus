package model.groups;

import enums.PermissionKey;
import model.Group;

/**
 * the administration group, it has every permission of the application
 */
public class AdminGroup implements Group {
    @Override
    public String getIdentifier() {
        return "ADMIN";
    }

    @Override
    public String getGuiRepresentation() {
        return "Administration";
    }

    @Override
    public PermissionKey[] getPermissions() {
        // Has all Permissions
        return PermissionKey.values();
    }
}
