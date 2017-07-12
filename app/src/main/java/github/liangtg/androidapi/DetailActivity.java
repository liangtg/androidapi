package github.liangtg.androidapi;

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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.github.liangtg.base.BaseRecyclerViewHolder;
import com.github.liangtg.base.BaseViewHolder;

import java.util.ArrayList;

import github.liangtg.androidapi.db.DataManager;
import github.liangtg.androidapi.db.TitleItem;

public class DetailActivity extends IActivity {
    private ViewHolder viewHolder;
    private ArrayList<TitleItem> menuList = new ArrayList<>();
    private MenuAdapter menuAdapter = new MenuAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        viewHolder = new ViewHolder(findViewById(R.id.view_holder));
        new DataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onBackPressed() {
        if (viewHolder.drawer.isDrawerOpen(viewHolder.navigationView)) {
            viewHolder.drawer.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    private class ViewHolder extends BaseViewHolder implements NavigationView.OnNavigationItemSelectedListener {
        private final DrawerLayout drawer;
        private final ConstraintLayout constraintLayout;
        private NavigationView navigationView;
        private WebView cnWeb, enWeb;
        private View handler, handlerLine;
        private ConstraintSet constraintSet = new ConstraintSet();

        public ViewHolder(View view) {
            super(view);
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
                        int cy = constraintLayout.getHeight() / 2;
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
            return false;
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
            viewHolder.navigationView.getMenu().clear();
            for (int i = 0; i < menuList.size(); i++) {
                viewHolder.navigationView.getMenu().add(0, i, i, menuList.get(i).cnName);
            }
        }
    }

}
