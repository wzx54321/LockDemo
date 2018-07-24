package com.lib.aliocr.widget.popup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lib.aliocr.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 使用方法：
 * <p>
 * <p>
 * List<XinPopWindow.MenuItem> menuItems=new ArrayList<>();
 * menuItems.add(new MyPopWindow.MenuItem("拍照"));
 * menuItems.add(new MyPopWindow.MenuItem("找下那个"));
 * menuItems.add(new MyPopWindow.MenuItem("拍照"));
 * menuItems.add(new MyPopWindow.MenuItem("拍照"));
 * <p>
 * XinPopWindow myPopWindow = new XinPopWindow(MainActivity.this);
 * myPopWindow.setData(menuItems);
 * myPopWindow.showAtLocation(root, Gravity.BOTTOM, 0, 0);
 * <p>
 * <p>
 * 作者：xin on 2018/7/9 0009 15:03
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */
public class XinPopWindow extends PopupWindow {
    OnItemClickListener mOnItemClickListener;
    Activity mContext;
    private Window mWindow;
    private ListView mItems;
    private MenuAdapter menuAdapter;

    public XinPopWindow(Activity context) {
        mContext = context;
        mWindow = mContext.getWindow();
        backgroundAlpha(1);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //noinspection ConstantConditions
        View mMenuView = inflater.inflate(R.layout.common_popwindow, null, false);

        View btnCancel = mMenuView.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mItems = mMenuView.findViewById(R.id.itemList);
        menuAdapter = new MenuAdapter();
        mItems.setAdapter(menuAdapter);
        mItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                          @Override
                                          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                              if (mOnItemClickListener != null)
                                                  mOnItemClickListener.onItemClick(parent, view, position, id, menuAdapter.getMenuItems().get(position).type);
                                          }
                                      }
        );

        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP || event.getY() < mItems.getTop())
                    XinPopWindow.this.dismiss();
                return true;
            }
        });
        setOutsideTouchable(true);
        this.setFocusable(true);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        this.setContentView(mMenuView);
        setBackgroundDrawable(ContextCompat.getDrawable(mContext.getApplication(), android.R.color.transparent));
        this.setAnimationStyle(R.style.common_PopupAnimation);


        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1);
            }
        });
    }

    public void backgroundAlpha(float bgAlpha) {
        if (mWindow == null) {
            mWindow = mContext.getWindow();
        }
        if (mWindow == null)
            return;
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.alpha = bgAlpha; //0.0-1.0
        mWindow.setAttributes(lp);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        backgroundAlpha(0.6f);
    }

    public void setData(List<MenuItem> menuItems) {
        menuAdapter.setMenuItems(menuItems);
    }

    public void setOnItemClickListener(final OnItemClickListener listener) {

        mOnItemClickListener = listener;

    }

    /////////////////////////////////////事件监听

    public interface OnItemClickListener {
        void onItemClick(AdapterView<?> parent, View view, int position, long id, int type);
    }

    public static class MenuAdapter extends BaseAdapter {

        List<MenuItem> menuItems;

        public MenuAdapter() {

            menuItems = new ArrayList<>();
        }

        public List<MenuItem> getMenuItems() {
            return menuItems;
        }

        public void setMenuItems(List<MenuItem> menuItems) {
            this.menuItems = menuItems;
            notifyDataSetChanged();
        }

        static class ViewHolder {
            TextView btn;
            View line;

        }

        @Override
        public int getCount() {
            return menuItems.size();
        }

        @Override
        public MenuItem getItem(int position) {
            return menuItems.isEmpty() ? null : menuItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_item_popup, null, false);
                holder.btn = convertView.findViewById(R.id.btn_);
                holder.line = convertView.findViewById(R.id.line_);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.btn.setText(getItem(position).name);


            if (position == 0) {
                holder.btn.setBackgroundResource(R.drawable.common_btn_pop_choose_pic_up);
            } else {
                holder.btn.setBackgroundResource(R.drawable.common_btn_pop_choose_pic_mid);
            }


            if (position == getCount() - 1) {
                holder.line.setVisibility(View.GONE);
            } else {
                holder.line.setVisibility(View.VISIBLE);
            }

            return convertView;
        }


    }

    public static class MenuItem {
        public String name;
        public int type;

        public MenuItem(String name, int type) {
            this.name = name;
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MenuItem)) return false;

            MenuItem menuItem = (MenuItem) o;

            return type == menuItem.type && (name != null ? name.equals(menuItem.name) : menuItem.name == null);
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + type;
            return result;
        }
    }


}
