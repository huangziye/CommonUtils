package com.hzy.commonutils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.hzy.commonutils.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_bottom_navigation.*

/**
 *
 * @author: ziye_huang
 * @date: 2019/5/30
 */
class BottomNavigationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation)

        val icons = intArrayOf(R.mipmap.ic_util, R.mipmap.ic_view, R.mipmap.ic_other)
        val menus = arrayOf("工具", "View", "其他")
        val menu = mainBottom.menu
        for (i in menus.indices) {
            menu.add(0, i, i, menus[i])
            val item = menu.findItem(i)
            item.setIcon(icons[i])
        }

        mainBottom.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                0 -> viewPager.currentItem = 0
                1 -> viewPager.currentItem = 1
                2 -> viewPager.currentItem = 2
                3 -> viewPager.currentItem = 3
                4 -> viewPager.currentItem = 4
            }
            true
        }

        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(TestFragment())
        adapter.addFragment(TestFragment())
        adapter.addFragment(TestFragment())
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                mainBottom.selectedItemId = position
            }
        })
    }

}