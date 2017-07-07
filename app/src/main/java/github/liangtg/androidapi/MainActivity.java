package github.liangtg.androidapi;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (R.id.add == id) {
            showAddTitleDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAddTitleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("新建标题");
        builder.setView(R.layout.dialog_add_title);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
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
            return new AdapterViewHolder(getLayoutInflater().inflate(R.layout.item_title, parent, false));
        }

        @Override
        public void onBindViewHolder(AdapterViewHolder holder, int position) {
            holder.setText(R.id.title, data.get(position)).setOnClickListener(R.id.add, holder);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private class AdapterViewHolder extends BaseRecyclerViewHolder implements PopupMenu.OnMenuItemClickListener {
        public AdapterViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onClick(View v, int id) {
            if (R.id.add == id) {
                showPopupMenu();
            }
        }

        private void showPopupMenu() {
            PopupMenu menu = new PopupMenu(getContext(), get(R.id.add));
            menu.setGravity(Gravity.LEFT | Gravity.BOTTOM);
            menu.inflate(R.menu.item_title);
            menu.setOnMenuItemClickListener(this);
            menu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            showToast(item.getTitle().toString());
            return true;
        }
    }


}
