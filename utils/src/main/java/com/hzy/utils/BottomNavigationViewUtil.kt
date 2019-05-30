package com.hzy.utils

import android.annotation.SuppressLint
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode

/**
 * BottomNavigationView 工具类
 * @author: ziye_huang
 * @date: 2019/5/30
 */
object BottomNavigationViewUtil {
    /**
     * BottomNavigationView去除动画
     * @param view
     */
    @SuppressLint("RestrictedApi")
    fun disableShiftMode(view: BottomNavigationView) {
        val menuView = view.getChildAt(0) as BottomNavigationMenuView
        try {
            val shiftingMode = menuView::class.java.getDeclaredField("mShiftingMode")
            shiftingMode.isAccessible = true
            shiftingMode.setBoolean(menuView, false)
            shiftingMode.isAccessible = false
            for (i in 0 until menuView.childCount) {
                val item = menuView.getChildAt(i) as BottomNavigationItemView
                //去除动画
//                item.setShiftingMode(false); //api 28之前
                item.setChecked(item.itemData.isChecked)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 指定初始Item
     * @param view
     * @param pos
     */
    fun setItemSelected(view: BottomNavigationView, pos: Int) {
        view.menu.getItem(pos).isChecked = true
    }

    /**
     * 是否开启动画
     * @param view
     */
    fun openAnimation(view: BottomNavigationView, isAnimation: Boolean) {
        view.labelVisibilityMode =
            if (isAnimation) LabelVisibilityMode.LABEL_VISIBILITY_SELECTED else LabelVisibilityMode.LABEL_VISIBILITY_LABELED
    }

    /**
     * 添加Item
     * @param view
     */
    fun addItem(view: BottomNavigationView, groupID: Int, itemID: Int, orderID: Int, text: String) {
        view.menu.add(groupID, itemID, orderID, text)
    }

    /**
     * 删除Item
     * @param view
     */
    fun removeItem(view: BottomNavigationView, pos: Int) {
        view.menu.removeItem(pos)
    }
}