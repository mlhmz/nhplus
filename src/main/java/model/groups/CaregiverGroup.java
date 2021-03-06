package model.groups;

import enums.PermissionKey;
import model.Group;

/**
 * the caregiver group, it has permissions for managing patients and their treatments
 */
public class CaregiverGroup implements Group {
    @Override
    public String getIdentifier() {
        return "CAREGIVER";
    }

    @Override
    public String getGuiRepresentation() {
        return "Pflege";
    }

    @Override
    public PermissionKey[] getPermissions() {
        return new PermissionKey[] {
                PermissionKey.SHOW_HOMEPAGE,
                PermissionKey.CREATE_PATIENT,
                PermissionKey.CREATE_TREATMENT,
                PermissionKey.EDIT_PATIENT,
                PermissionKey.EDIT_TREATMENT,
                PermissionKey.LOCK_AND_UNLOCK_PATIENT,
                PermissionKey.DELETE_TREATMENT,
                PermissionKey.SHOW_ALL_PATIENTS,
                PermissionKey.SHOW_ALL_TREATMENTS,
                PermissionKey.CHANGE_PASSWORD
        };
    }
}
