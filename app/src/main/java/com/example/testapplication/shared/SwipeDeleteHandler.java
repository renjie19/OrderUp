package com.example.testapplication.shared;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapplication.OrderUp;
import com.example.testapplication.R;
import com.example.testapplication.ui.adapter.Adapter;
import com.google.android.material.snackbar.Snackbar;

public class SwipeDeleteHandler extends ItemTouchHelper.SimpleCallback {
    private Drawable icon;
    private ColorDrawable background;
    private Adapter adapter;
    private Activity mActivity;

    public SwipeDeleteHandler(Adapter adapter, Activity mActivity) {
        super(0, ItemTouchHelper.RIGHT);
        this.adapter = adapter;
        this.mActivity = mActivity;
        this.icon = ContextCompat.getDrawable(mActivity.getApplicationContext(), R.drawable.ic_delete_black_24dp);
        this.background = new ColorDrawable(Color.RED);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.delete(viewHolder.getAdapterPosition());
        showSnackUndoBar();
    }

    private void showSnackUndoBar() {
        Snackbar.make(mActivity.getWindow().getDecorView().getRootView(), "Removed Successfully", Snackbar.LENGTH_LONG)
                .setAction("Undo", v -> undoDelete())
                .show();
    }

    private void undoDelete() {
        adapter.restore();
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX > 0) { // Swiping to the right
            int iconLeft = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            int iconRight = itemView.getLeft() + iconMargin;
            icon.setBounds(iconRight, iconTop, iconLeft, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                    itemView.getBottom());
//            } else if (dX < 0) { // Swiping to the left
//                int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
//                int iconRight = itemView.getRight() - iconMargin;
//                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
//
//                background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
//                        itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
        }

        background.draw(c);
        icon.draw(c);
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
