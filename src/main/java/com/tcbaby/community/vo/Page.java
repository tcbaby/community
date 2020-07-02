package com.tcbaby.community.vo;

/**
 * @author tcbaby
 * @date 20/05/04 16:52
 */
public class Page {

    /** 当前页 */
    private int page = 1;
    /** 显示记录数 */
    private int size = 10;

    /** 总记录数 */
    private long total;
    /** 总页数 */
    private int totalPage;

    /** 起始页 */
    private int from;
    /** 结束页 */
    private int to;

    /** 请求资源路径 便于复用分页组件 */
    private String path;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        if (page > 0) {
            this.page = page;
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        if (size > 0 || size <= 20) {
            this.size = size;
        }
    }

    public void setTotal(long total) {
        this.total = total;
        // 设置totalPage值
        totalPage = (int) (total % size == 0 ? total / size : total / size + 1);
        // 设置from、to   每页显示5个页码按钮
        from = page - 2;
        to = page + 2;
        if (from <= 0) {
            from = 1;
            to = 5;
        }
        if (to > totalPage) {
            to = totalPage;
            from = (to - 4 > 0) ? to - 4 : 1;
        }
    }

    public long getTotal() {
        return total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
