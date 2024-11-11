package ai.cloudshelf.exponursdk;

import com.nordicid.nurapi.NurTag;
import com.nordicid.nurapi.NurTagStorage;

public class Helper {
    private static Helper instance;

    private Helper() {
    }

    public static Helper getInstance() {
        if (instance == null)
            instance = new Helper();
        return instance;
    }

    private NurTagStorage mTagStorage = new NurTagStorage();

    public void add() {
        NurTag tag = new NurTag();
        mTagStorage.addTag(tag);
    }

    public int size() {
        return mTagStorage.size();
    }
}
