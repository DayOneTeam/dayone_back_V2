package dayone.dayone.demoday.entity;

import java.util.List;

public class DemoDayUsers {

    private final List<DemoDayUser> demoDayUsers;

    public DemoDayUsers(final List<DemoDayUser> demoDayUsers) {
        this.demoDayUsers = demoDayUsers;
    }

    public boolean isAlreadyApply(final Long userId) {
        return demoDayUsers.stream()
            .anyMatch(demoDayUser -> demoDayUser.getUserId().equals(userId));
    }
}
