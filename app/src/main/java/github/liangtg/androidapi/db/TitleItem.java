package github.liangtg.androidapi.db;

/**
 * Created by liangtg on 17-7-10.
 */

public class TitleItem {
    public long id, pid;
    public int page, pageCount, depth;
    public String cnName, enName;

    @Override
    public String toString() {
        return "TitleItem{" + "id=" + id + ", pid=" + pid + ", page=" + page + ", pageCount=" + pageCount + ", depth=" + depth + ", cnName='" + cnName + '\'' + ", enName='" + enName + '\'' + '}';
    }
}
