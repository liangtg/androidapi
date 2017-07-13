package github.liangtg.androidapi;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.github.liangtg.base.BaseRecyclerViewHolder;
import com.github.liangtg.base.BaseViewHolder;
import com.google.gson.Gson;

import java.util.ArrayList;

import github.liangtg.androidapi.db.ContentItem;
import github.liangtg.androidapi.db.DataManager;
import github.liangtg.androidapi.db.TitleItem;

public class DetailActivity extends IActivity {
    private static final int REQUEST_EDIT = 0xFF;
    private static final String url = "file:///android_asset/html/empty.html";
    private ViewHolder viewHolder;
    private ArrayList<TitleItem> menuList = new ArrayList<>();
    private MenuAdapter menuAdapter = new MenuAdapter();
    private int lastPosition = 0;
    private ContentItem curContent;

    public static void gotoDetail(int position) {
        Intent intent = new Intent(IApplication.context(), DetailActivity.class);
        intent.putExtra(Intent.EXTRA_INDEX, position);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        IApplication.context().startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        lastPosition = getIntent().getIntExtra(Intent.EXTRA_INDEX, 0);
        viewHolder = new ViewHolder(findViewById(R.id.view_holder));
        new DataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (R.id.edit == id) {
            toEdit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (viewHolder.drawer.isDrawerOpen(viewHolder.navigationView)) {
            viewHolder.drawer.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    private void toEdit() {
        Intent intent = new Intent(IApplication.context(), EditDetailActivity.class);
        intent.setData(Uri.parse(new Gson().toJson(curContent)));
        startActivityForResult(intent, REQUEST_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && requestCode == REQUEST_EDIT) {
            showItem(menuList.get(lastPosition));
        }
    }

    private void showItem(TitleItem c) {
        viewHolder.toolbar.setTitle(String.format("%s/%s", c.cnName, c.enName));
        ContentItem content = DataManager.instance().getContent(c.id);
        curContent = content;
        if (content.tid == -1) {
            content.tid = c.id;
            toEdit();
        }
        if (TextUtils.isEmpty(content.cn)) {
            viewHolder.cnWeb.loadUrl(url);
        } else {
            viewHolder.cnWeb.loadDataWithBaseURL(null, content.cn, null, null, null);
        }
        if (TextUtils.isEmpty(content.en)) {
            viewHolder.enWeb.loadUrl(url);
        } else {
            viewHolder.enWeb.loadDataWithBaseURL(null, content.en, null, null, null);
        }
    }

    private class ViewHolder extends BaseViewHolder implements NavigationView.OnNavigationItemSelectedListener {
        private final DrawerLayout drawer;
        private final ConstraintLayout constraintLayout;
        private Toolbar toolbar;
        private NavigationView navigationView;
        private WebView cnWeb, enWeb;
        private View handler, handlerLine;
        private ConstraintSet constraintSet = new ConstraintSet();

        public ViewHolder(View view) {
            super(view);
            toolbar = get(R.id.toolbar);
            setSupportActionBar(toolbar);
            handler = get(R.id.handler);
            handler.setOnTouchListener(new View.OnTouchListener() {
                float dy = 0;
                int out[] = new int[2];

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEvent.ACTION_DOWN == event.getAction()) {
                        dy = event.getRawY();
                        constraintLayout.getLocationOnScreen(out);
                    } else {
                        float bias = (event.getRawY() - out[1] * 1f) / constraintLayout.getHeight();
                        bias = Math.max(0.25f, Math.min(0.75f, bias));
                        constraintSet.setVerticalBias(R.id.handler_line, bias);
                        constraintSet.applyTo(constraintLayout);
                    }
                    return true;
                }
            });
            handlerLine = get(R.id.handler_line);
            constraintLayout = get(R.id.constraint);
            constraintSet.clone(constraintLayout);
            Toolbar toolbar = get(R.id.toolbar);
            drawer = get(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getBaseActivity(),
                    drawer,
                    toolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            navigationView = get(R.id.menu_view);
            navigationView.setNavigationItemSelectedListener(this);
            cnWeb = get(R.id.cn_web);
            enWeb = get(R.id.en_web);
            cnWeb.loadUrl("http://m.baidu.com/error.jsp");
            enWeb.loadUrl("http://m.baidu.com/error.jsp");
        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            viewHolder.drawer.closeDrawers();
            lastPosition = item.getItemId();
            showItem(menuList.get(item.getItemId()));
            return true;
        }
    }

    private class MenuViewHolder extends BaseRecyclerViewHolder {
        public MenuViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class MenuAdapter extends RecyclerView.Adapter<MenuViewHolder> {
        @Override
        public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(MenuViewHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return menuList.size();
        }
    }

    private class DataTask extends AsyncTask<Void, Void, ArrayList<TitleItem>> {

        @Override
        protected ArrayList<TitleItem> doInBackground(Void... params) {
            return DataManager.instance().getTitleList();
        }

        @Override
        protected void onPostExecute(ArrayList<TitleItem> titleItems) {
            menuList.addAll(titleItems);
            showItem(menuList.get(lastPosition));
            viewHolder.navigationView.getMenu().clear();
            for (int i = 0; i < menuList.size(); i++) {
                viewHolder.navigationView.getMenu().add(0, i, i, menuList.get(i).cnName);
            }
        }
    }

}
