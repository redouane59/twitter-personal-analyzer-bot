package com.socialmediaraiser.twittersocialgraph.impl;

import lombok.Getter;

public enum GroupEnum {

    EX_GAUCHE(1),
    PS(2),
    LREM(3),
    PR(4),
    LR(5),
    EX_DROITE(6),
    JOURNALISTES_DROITE(7),
    JOURNALISTES_GAUCHE(8);

    @Getter
    private int groupId;
    GroupEnum(int groupId) {
        this.groupId = groupId;
    }
}
