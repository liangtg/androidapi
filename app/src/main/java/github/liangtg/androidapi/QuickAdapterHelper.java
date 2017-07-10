package github.liangtg.androidapi;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by liangtg on 17-7-10.
 */

public class QuickAdapterHelper<VH extends RecyclerView.ViewHolder, T extends RecyclerView.Adapter<VH> & QuickAdapterHelper.QuickHelper<VH>> {
    public static final int TYPE_ITEM = 0xFF;

    private QuickHelper<VH> quickHelper;

    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return quickHelper.onCreateQuickViewHolder(null);
        } else {
            return quickHelper.onCreateItemViewHolder(parent, viewType);
        }
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public void onBindViewHolder(VH holder, int position) {
        if (TYPE_ITEM == holder.getItemViewType()) {
            quickHelper.onBindItemViewHolder(holder, position);
        } else {
        }
    }


    public interface QuickHelper<VH extends RecyclerView.ViewHolder> {
        VH onCreateQuickViewHolder(View quickView);

        VH onCreateItemViewHolder(ViewGroup parent, int viewType);

        void onBindItemViewHolder(VH holder, int position);

    }
}
