package github.liangtg.androidapi;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.github.liangtg.base.BaseViewHolder;
import com.google.gson.Gson;

import github.liangtg.androidapi.db.ContentItem;
import github.liangtg.androidapi.db.DataManager;
import github.liangtg.androidapi.db.TitleItem;

public class EditDetailActivity extends IActivity {
    private ContentItem contentItem;
    private ViewHolder viewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_detail);
        String data = getIntent().getDataString();
        if (TextUtils.isEmpty(data)) {
            finish();
            return;
        }
        contentItem = new Gson().fromJson(data, ContentItem.class);
        TitleItem title = DataManager.instance().getTitle(contentItem.tid);
        setTitle(String.format("%s/%s", title.cnName, title.enName));
        initAppBar();
        viewHolder = new ViewHolder(findViewById(R.id.view_holder));
        viewHolder.inputEn.setText(contentItem.en);
        viewHolder.inputCn.setText(contentItem.cn);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (R.id.save == id) {
            trySave();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void trySave() {
        String en = viewHolder.inputEn.getText().toString();
        String cn = viewHolder.inputCn.getText().toString();
        if (!TextUtils.isEmpty(cn) || !TextUtils.isEmpty(en)) {
            DataManager.instance().editContent(contentItem.tid, cn, en);
            setResult(RESULT_OK);
        }
        finish();
    }

    private class ViewHolder extends BaseViewHolder {
        EditText inputCn, inputEn;

        public ViewHolder(View view) {
            super(view);
            inputCn = get(R.id.input_cn);
            inputEn = get(R.id.input_en);
        }

    }


}
