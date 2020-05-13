package com.example.testapplication.shared.callback;

public interface DeleteCallback {
    void onDelete(int position);
    void onUndo();
    void onSnackbarDismissed(int event);
}
