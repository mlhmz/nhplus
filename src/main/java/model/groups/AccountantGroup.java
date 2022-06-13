package model.groups;

import enums.PermissionKey;
import model.Group;

/**
 * the accountant group, it has permissions to read about the patients and their treatments
 */
public class AccountantGroup implements Group {
    @Override
    public String getIdentifier() {
        return "ACCOUNTANT";
    }

    @Override
    public String getGuiRepresentation() {
        return "Rechnungswesen";
    }

    @Override
    public PermissionKey[] getPermissions() {
        return new PermissionKey[] {
                PermissionKey.SHOW_HOMEPAGE,
                PermissionKey.SHOW_ALL_TREATMENTS,
                PermissionKey.SHOW_ALL_PATIENTS
        };
    }
}
