package github.liangtg.androidapi;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
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

import github.liangtg.androidapi.db.DataManager;
import github.liangtg.androidapi.db.TitleItem;

public class MainActivity extends IActivity {
    private ViewHolder viewHolder;
    private ArrayList<TitleItem> data = new ArrayList<>();
    private int lastPosition = -1;
    private int addPosition = -1;
    private long addPID = 0;
    private Dialog addDialog = null;
    private Dialog editDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initAppBar();
        viewHolder = new ViewHolder(findViewById(R.id.view_holder));
        startTask();
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
            addPID = 0;
            addPosition = data.size();
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
            public void onClick(DialogInterface d, int which) {
                TextInputLayout inputLayout = (TextInputLayout) addDialog.getWindow().findViewById(R.id.input_cn);
                String cn = inputLayout.getEditText().getText().toString();
                inputLayout = (TextInputLayout) addDialog.getWindow().findViewById(R.id.input_en);
                String en = inputLayout.getEditText().getText().toString();
                addTitle(cn, en);
            }
        });
        addDialog = builder.show();
    }

    private void showEditTitleDialog(final TitleItem item) {
        createEditTitleDialog(item);
        TextInputLayout input = (TextInputLayout) editDialog.getWindow().findViewById(R.id.input_cn);
        input.getEditText().setText(item.cnName);
        input = (TextInputLayout) editDialog.getWindow().findViewById(R.id.input_en);
        input.getEditText().setText(item.enName);
    }

    private void createEditTitleDialog(final TitleItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("编辑");
        builder.setView(R.layout.dialog_add_title);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextInputLayout input = (TextInputLayout) editDialog.getWindow().findViewById(R.id.input_cn);
                if (input.getEditText().getText().length() > 0) {
                    item.cnName = input.getEditText().getText().toString();
                }
                input = (TextInputLayout) editDialog.getWindow().findViewById(R.id.input_en);
                if (input.getEditText().getText().length() > 0) {
                    item.enName = input.getEditText().getText().toString();
                }
                editTitle(item);
            }
        });
        editDialog = builder.show();
    }

    private void editTitle(TitleItem item) {
        DataManager.instance().editTitle(item.id, item.cnName, item.enName);
        viewHolder.recyclerView.getAdapter().notifyItemChanged(lastPosition);
    }

    private void addTitle(String cn, String en) {
        if (cn.length() > 0 || en.length() > 0) {
            TitleItem addTitle = DataManager.instance().addTitle(addPID, cn, en);
            data.add(addPosition, addTitle);
            for (int i = addPosition - 1; i >= 0; i--) {
                if (data.get(i).depth < addTitle.depth) {
                    data.get(i).pageCount++;
                    break;
                }
            }
            viewHolder.recyclerView.getAdapter().notifyItemInserted(addPosition);
        }
    }

    @Override
    protected boolean displayUp() {
        return false;
    }

    private void startTask() {
        new DataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class ViewHolder extends BaseViewHolder {
        private RecyclerView recyclerView;

        public ViewHolder(View view) {
            super(view);
            recyclerView = get(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new DataAdapter());
            DefaultItemAnimator animator = new DefaultItemAnimator();
            animator.setSupportsChangeAnimations(true);
            animator.setAddDuration(500);
            animator.setChangeDuration(500);
            recyclerView.setItemAnimator(animator);
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }
    }

    private class DataAdapter extends RecyclerView.Adapter<AdapterViewHolder> {
        @Override
        public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AdapterViewHolder(getLayoutInflater().inflate(R.layout.item_title, parent, false));
        }

        @Override
        public void onBindViewHolder(AdapterViewHolder holder, int position) {
            StringBuilder sb = new StringBuilder();
            TitleItem item = data.get(position);
            String sym = item.depth % 2 == 0 ? "◇" : "◆";
            for (int i = 0; i < item.depth - 1; i++) {
                sb.append("-");
            }
            if (item.depth > 0) sb.append(">");
            sb.append(item.cnName);
            sb.append("/");
            sb.append(item.enName);
            ViewCompat.setBackground(holder.itemView, AppCompatResources.getDrawable(holder.getContext(), R.drawable.title_item_bg));
            holder.itemView.setOnClickListener(holder);
            holder.itemView.setSelected(lastPosition == position);
            holder.setText(R.id.title, sb.toString()).setOnClickListener(R.id.add, holder);
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
                if (lastPosition >= 0) viewHolder.recyclerView.getAdapter().notifyItemChanged(lastPosition);
                lastPosition = getAdapterPosition();
                viewHolder.recyclerView.getAdapter().notifyItemChanged(lastPosition);
            } else if (R.id.item_view_holder == id) {
                gotoActivity(Main2Activity.class);
            }
        }

        private void showPopupMenu() {
            PopupMenu menu = new PopupMenu(getContext(), get(R.id.add));
            menu.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
            menu.inflate(R.menu.item_title);
            menu.setOnMenuItemClickListener(this);
            menu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            if (R.id.add_sub == id) {
                tryAddTitle();
            } else if (R.id.edit_title == id) {
                tryEditTitle();
            } else if (R.id.del_title == id) {
                tryDelTitle();
            }
            return true;
        }

        private void tryDelTitle() {
            TitleItem item = data.get(lastPosition);
            if (item.pageCount > 0) {
                showToast("标题有子标题, 不能删除");
            } else {
                data.remove(lastPosition);
                for (int i = lastPosition - 1; i >= 0; i--) {
                    if (data.get(i).depth < item.depth) {
                        data.get(i).pageCount--;
                        break;
                    }
                }
                viewHolder.recyclerView.getAdapter().notifyItemRemoved(lastPosition);
                DataManager.instance().deleteTitle(item.id);
                lastPosition = -1;
            }
        }

        private void tryEditTitle() {
            showEditTitleDialog(data.get(lastPosition));
        }

        private void tryAddTitle() {
            addPID = data.get(lastPosition).id;
            findAddSubPosition();
            showAddTitleDialog();
        }

        private void findAddSubPosition() {
            TitleItem item = data.get(lastPosition);
            int position = lastPosition + 1;
            for (; position < data.size(); position++) {
                if (data.get(position).depth <= item.depth) break;
            }
            addPosition = position;
        }
    }

    private class DataTask extends AsyncTask<Void, Void, ArrayList<TitleItem>> {

        @Override
        protected ArrayList<TitleItem> doInBackground(Void... params) {
            return DataManager.instance().getTitleList();
        }

        @Override
        protected void onPostExecute(ArrayList<TitleItem> titleItems) {
            boolean change = !data.isEmpty();
            data.clear();
            data.addAll(titleItems);
            if (change) {
                viewHolder.recyclerView.getAdapter().notifyDataSetChanged();
            } else {
                viewHolder.recyclerView.getAdapter().notifyItemRangeInserted(0, data.size());
            }
        }
    }

}
