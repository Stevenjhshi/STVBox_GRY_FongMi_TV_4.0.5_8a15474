package com.stvbox.gry.ui.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewbinding.ViewBinding;

import com.stvbox.gry.databinding.DialogRestoreBinding;
import com.stvbox.gry.db.AppDatabase;
import com.stvbox.gry.impl.Callback;
import com.stvbox.gry.ui.adapter.RestoreAdapter;
import com.stvbox.gry.ui.custom.SpaceItemDecoration;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;

public class RestoreDialog extends BaseDialog implements RestoreAdapter.OnClickListener {

    private DialogRestoreBinding binding;
    private RestoreAdapter adapter;
    private Callback callback;

    public static RestoreDialog create() {
        return new RestoreDialog();
    }

    public void show(FragmentActivity activity, Callback callback) {
        for (Fragment f : activity.getSupportFragmentManager().getFragments()) if (f instanceof BottomSheetDialogFragment) return;
        show(activity.getSupportFragmentManager(), null);
        this.callback = callback;
    }

    @Override
    protected ViewBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return binding = DialogRestoreBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initView() {
        binding.recycler.setItemAnimator(null);
        binding.recycler.setHasFixedSize(false);
        binding.recycler.setAdapter(adapter = new RestoreAdapter(this));
        binding.recycler.addItemDecoration(new SpaceItemDecoration(1, 16));
        binding.recycler.setVisibility(adapter.getItemCount() == 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onItemClick(File item) {
        AppDatabase.restore(item, callback);
        dismiss();
    }

    @Override
    public void onDeleteClick(File item) {
        if (adapter.remove(item) == 0) dismiss();
    }
}
