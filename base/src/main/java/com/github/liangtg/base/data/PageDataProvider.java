package com.github.liangtg.base.data;

/**
 * Created by liangtg on 16-7-14.
 */
public abstract class PageDataProvider {

    private boolean working = false;
    private boolean haveMoreData = true;
    private boolean onceWorked = false;

    private int page = 1;

    public void refresh() {
        if (working) {
            return;
        }
        reset();
        nextPage();
    }

    public void reset() {
        page = 1;
        haveMoreData = true;
        onResetData();
    }

    public void nextPage() {
        if (working || !haveMoreData) {
            return;
        }
        working = true;
        doWork(page);
    }

    public void endPage(boolean success, boolean more) {
        onceWorked = true;
        working = false;
        if (success) {
            page++;
            haveMoreData = more;
        }
    }

    public boolean isWorking() {
        return working;
    }

    public boolean haveMoreData() {
        return haveMoreData;
    }

    public boolean onceWorked() {
        return onceWorked;
    }

    /**
     * @param page start from 1
     */
    public abstract void doWork(int page);

    public abstract void onResetData();

}
