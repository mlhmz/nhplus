package model;

import model.groups.AccountantGroup;
import model.groups.AdminGroup;
import model.groups.CaregiverGroup;

/**
 * Singleton-Factory for Groups
 */
public class GroupFactory {
    private static GroupFactory instance;

    // groups are final because they only get instanced once
    private final Group adminGroup, caregiverGroup, accountantGroup;

    private GroupFactory() {
        adminGroup = new AdminGroup();
        caregiverGroup = new CaregiverGroup();
        accountantGroup = new AccountantGroup();
    }

    /**
     * instances GroupFactory and all Groups that exist
     */
    public static GroupFactory getInstance() {
        if (instance == null) {
            instance = new GroupFactory();
        }
         return instance;
    }

    /**
     * gets groups by string
     */
    public Group getGroup(String type) {
        // using if-else because switch-case requires constant expressions
        if (adminGroup.getIdentifier().equals(type)) {
            return adminGroup;
        } else if (caregiverGroup.getIdentifier().equals(type)) {
            return caregiverGroup;
        } else if (accountantGroup.getIdentifier().equals(type)) {
            return accountantGroup;
        }
        throw new IllegalArgumentException(String.format("There is no group called '%s'", type));
    }

    /**
     * gets all groups
     */
    public Group[] getAllGroups() {
        return new Group[] {
            adminGroup,
            caregiverGroup,
            accountantGroup,
        };
    }
}
