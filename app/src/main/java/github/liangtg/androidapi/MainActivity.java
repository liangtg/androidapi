package github.liangtg.androidapi;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.github.liangtg.base.BaseRecyclerViewHolder;
import com.github.liangtg.base.BaseViewHolder;

import java.util.ArrayList;

public class MainActivity extends IActivity {
    private ViewHolder viewHolder;
    private ArrayList<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initAppBar();
        viewHolder = new ViewHolder(findViewById(R.id.view_holder));
        for (int i = 0; i < 100; i++) {
            data.add(String.format("item:%d", i));
        }
    }

    @Override
    protected boolean displayUp() {
        return false;
    }

    private class ViewHolder extends BaseViewHolder {
        private RecyclerView recyclerView;

        public ViewHolder(View view) {
            super(view);
            recyclerView = get(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new DataAdapter());
        }
    }

    private class DataAdapter extends RecyclerView.Adapter<AdapterViewHolder> {
        @Override
        public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AdapterViewHolder(getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false));
        }

        @Override
        public void onBindViewHolder(AdapterViewHolder holder, int position) {
            holder.setText(android.R.id.text1, data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private class AdapterViewHolder extends BaseRecyclerViewHolder {
        public AdapterViewHolder(View itemView) {
            super(itemView);
        }
    }


}
