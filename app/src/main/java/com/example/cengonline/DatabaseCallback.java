package com.example.cengonline;

import com.example.cengonline.model.User;

public interface DatabaseCallback {
    void onSuccess(Object result);
    void onFailed(String message);
}
