package com.example.sweater.domain.util;

import com.example.sweater.domain.User;

public abstract class MessageHelper {
    public static String getAuthorName(User user) {
        return user != null ? user.getUsername() : "<none>";
    }
}
