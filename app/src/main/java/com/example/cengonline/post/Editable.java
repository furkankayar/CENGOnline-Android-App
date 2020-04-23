package com.example.cengonline.post;

import com.example.cengonline.model.User;

public interface Editable {

    public String getEditedAt();
    public void setEditedAt(String editedAt);
    public String getEditedBy();
    public void setEditedBy(String editedBy);
}
