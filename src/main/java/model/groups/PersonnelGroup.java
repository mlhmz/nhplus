package model.groups;

import enums.PermissionKey;
import model.Group;

/**
 * PersonnelGroup, contains every Permission related to the Employees of the Company
 */
public class PersonnelGroup implements Group {
    @Override
    public String getIdentifier() {
        return "PERSONNEL";
    }

    @Override
    public String getGuiRepresentation() {
        return "Personalverwaltung";
    }

    @Override
    public PermissionKey[] getPermissions() {
        return new PermissionKey[] {
                PermissionKey.SHOW_HOMEPAGE,
                PermissionKey.CHANGE_PASSWORD,
                PermissionKey.CREATE_CAREGIVER,
                PermissionKey.EDIT_CAREGIVER,
                PermissionKey.DELETE_CAREGIVER,
                PermissionKey.SHOW_ALL_CAREGIVERS
        };
    }
}
