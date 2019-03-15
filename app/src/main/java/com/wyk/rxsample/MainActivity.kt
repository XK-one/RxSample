package com.wyk.rxsample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.wyk.rxsample.base.BaseFragment
import com.wyk.rxsample.module.cache_6.data.CacheFragment
import com.wyk.rxsample.module.elementary_1.ElementaryFragment
import com.wyk.rxsample.module.map_2.MapFragment
import com.wyk.rxsample.module.token_4.TokenFragment
import com.wyk.rxsample.module.token_advance_5.TokenAdvancedFragment
import com.wyk.rxsample.module.zip_3.ZipFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolBar)

        viewPager.adapter = object: FragmentPagerAdapter(supportFragmentManager){
            override fun getItem(index: Int): Fragment {
                var fragment: BaseFragment = ElementaryFragment.newInstance() //默认
                when(index){
                    0 ->{ fragment = ElementaryFragment.newInstance()}
                    1 ->{ fragment = MapFragment.newInstance()}
                    2 ->{ fragment = ZipFragment.newInstance()}
                    3 ->{ fragment = TokenFragment.newInstance()}
                    4 ->{ fragment = TokenAdvancedFragment.newInstance()}
                    5 ->{ fragment = CacheFragment.newInstance()}
                }
                return fragment
            }
            override fun getCount() = 6
            override fun getPageTitle(position: Int): CharSequence? {
                var title: String? = null
                when(position){
                    0 ->{title = getString(R.string.title_elementary)}
                    1 ->{title = getString(R.string.title_map)}
                    2 ->{title = getString(R.string.title_zip)}
                    3 ->{title = getString(R.string.title_token)}
                    4 ->{title = getString(R.string.title_token_advanced)}
                    5 ->{title = getString(R.string.title_cache)}
                }
                return title
            }
        }

        tabs.setupWithViewPager(viewPager)

    }
}
