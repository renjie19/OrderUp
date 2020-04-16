package com.example.testapplication.shared.callback;

import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapplication.OrderUp;
import com.example.testapplication.R;
import com.google.android.material.snackbar.Snackbar;

public class SwipeDeleteCallback extends ItemTouchHelper.SimpleCallback {
    private DeleteCallback deleteCallback;
    private View mRootView;
    private Drawable icon;
    private ColorDrawable background;

    public SwipeDeleteCallback(View rootView, DeleteCallback onDeleteDeleteCallback){
        super(0, ItemTouchHelper.RIGHT);
        this.deleteCallback = onDeleteDeleteCallback;
        this.mRootView = rootView;
        this.icon = ContextCompat.getDrawable(OrderUp.getContext(), R.drawable.ic_delete_black_24dp);
        this.background = new ColorDrawable(0xF2F13131);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        deleteCallback.onDelete(viewHolder.getAdapterPosition());
        showSnackUndoBar();
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

    private void showSnackUndoBar() {
        Snackbar.make(mRootView, "Removed Successfully", Snackbar.LENGTH_LONG)
                .setAction("Undo", v -> deleteCallback.onUndo())
                .show();
    }
}
