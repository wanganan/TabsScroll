package com.laundrylangpickcargo.tabsscroll;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sinaan on 2016/12/8.
 */
public class TabFragment extends Fragment{

    private ArrayList<Cloth> list;
    private ImageView holdCart;
    private RelativeLayout holdRootView;
    private TextView holdChooseNum;
    private int mChooseNum;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab,null);
        String tabel = getArguments().getString("tabel");
        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(new Cloth(tabel+i,0));
        }

        holdCart = (ImageView) getActivity().findViewById(R.id.holdCart);
        holdChooseNum = (TextView) getActivity().findViewById(R.id.holdChooseNum);
        holdRootView = (RelativeLayout) getActivity().findViewById(R.id.holdRootView);
        GridView gvContent = (GridView) view.findViewById(R.id.gv_content);
        GridAdapter adapter = new GridAdapter(new ClothAddCallback() {
            @Override
            public void updateRedDot( ImageView clothIcon) {
                addCloth(clothIcon);
            }
        });
        gvContent.setAdapter(adapter);
        return view;
    }

    public interface ClothAddCallback {
        void updateRedDot(ImageView clothIcon);
    }

    class Cloth{
        String name;
        int checkNum;

        public Cloth(String name, int checkNum) {
            this.name = name;
            this.checkNum = checkNum;
        }

        public int getCheckNum() {
            return checkNum;
        }

        public void setCheckNum(int checkNum) {
            this.checkNum = checkNum;
        }
    }

    class GridAdapter extends BaseAdapter{

        private ClothAddCallback callback;

        public GridAdapter(ClothAddCallback callback) {
            this.callback = callback;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_grid, null);
                holder = new ViewHolder();
                holder.icon = ((ImageView) convertView.findViewById(R.id.iv_item_icon));
                holder.dot = ((TextView) convertView.findViewById(R.id.tv_item_choosedNum));
                convertView.setTag(holder);
            } else {
                holder = ((ViewHolder) convertView.getTag());
            }

            final Cloth cloth = list.get(position);
            holder.dot.setText(cloth.getCheckNum() + "");
            if (cloth.getCheckNum() == 0) {
                holder.dot.setVisibility(View.GONE);
            } else {
                holder.dot.setVisibility(View.VISIBLE);
            }
            final ViewHolder holder1 = holder;
            holder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int checkNum = cloth.getCheckNum() + 1;
                    cloth.setCheckNum(checkNum);
                    callback.updateRedDot(holder1.icon);
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        class ViewHolder{
            ImageView icon;
            TextView dot;
        }
    }

    //添加购物车动画
    private void addCloth(ImageView clothIcon) {
        int[] childCoordinate = new int[2];
        int[] parentCoordinate = new int[2];
        int[] shopCoordinate = new int[2];

        //1.分别获取被点击View、父布局、购物车在屏幕上的坐标xy。
        clothIcon.getLocationInWindow(childCoordinate);
        holdRootView.getLocationInWindow(parentCoordinate);
        holdCart.getLocationInWindow(shopCoordinate);
        //2.自定义ImageView 继承ImageView
        MoveImageView img = new MoveImageView(getActivity());
        clothIcon.setDrawingCacheEnabled(true);
        img.setImageBitmap(Bitmap.createBitmap(clothIcon.getDrawingCache()));
        clothIcon.setDrawingCacheEnabled(false);
        //3.设置img在父布局中的坐标位置
        img.setX(childCoordinate[0] - parentCoordinate[0]);
        img.setY(childCoordinate[1] - parentCoordinate[1]);
        //4.父布局添加该Img
        holdRootView.addView(img);

        //5.利用 二次贝塞尔曲线 需首先计算出 MoveImageView的2个数据点和一个控制点
        PointF startP = new PointF();
        PointF endP = new PointF();
        PointF controlP = new PointF();
        //开始的数据点坐标就是 addV的坐标
        startP.x = childCoordinate[0] - parentCoordinate[0];
        startP.y = childCoordinate[1] - parentCoordinate[1];
        //结束的数据点坐标就是 shopImg的坐标
        endP.x = shopCoordinate[0] - parentCoordinate[0];
        endP.y = shopCoordinate[1] - parentCoordinate[1];
        //控制点坐标 x等于 购物车x；y等于 addV的y
        controlP.x = endP.x;
        controlP.y = startP.y;

        //启动属性动画
        ObjectAnimator animator = ObjectAnimator.ofObject(img, "mPointF",
                new MoveImageView.PointFTypeEvaluator(controlP), startP, endP);
        animator.setDuration(1000);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //动画结束后 父布局移除 img
                Object target = ((ObjectAnimator) animation).getTarget();
                holdRootView.removeView((View) target);
                //购物车 开始一个放大动画
                Animation scaleAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.cart_scale);
                holdCart.startAnimation(scaleAnim);

                String activityChooseNum = holdChooseNum.getText().toString();
                if (TextUtils.isEmpty(activityChooseNum) || "0".equals(activityChooseNum)) {
                    mChooseNum = 0;
                } else {
                    int i = Integer.parseInt(activityChooseNum);
                    mChooseNum = i;
                }
                mChooseNum++;
                holdChooseNum.setText(mChooseNum + "");
                holdChooseNum.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }
}
