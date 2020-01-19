package com.socialmediaraiser.twittersocialgraph.impl;

import lombok.Getter;

public enum GroupEnum {

    EX_GAUCHE("red"),
    PS("HotPink"),
    LREM("Gold"),
    PR("DarkRed"),
    LR("DodgerBlue"),
    EX_DROITE("darkblue"),
    JOURNALISTES_DROITE("darkgreen"),
    JOURNALISTES_GAUCHE("green");

    @Getter
    private String color;
    GroupEnum(String color) {
        this.color = color;
    }
}
