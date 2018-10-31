package com.texaconnect.texa.interfaces;

import com.texaconnect.texa.model.Element;
import com.texaconnect.texa.model.GroupItem;

public interface GroupClickCallback {
    void onGroupClick(GroupItem groupItem);
    void onElementClick(Element element);
}
