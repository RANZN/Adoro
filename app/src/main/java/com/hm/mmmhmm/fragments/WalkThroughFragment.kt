package com.hm.mmmhmm.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.hm.mmmhmm.R
import kotlinx.android.synthetic.main.fragment_walk_through.*


/**
 * A simple [Fragment] subclass.
 */
class WalkThroughFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_walk_through, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_next_walk.setOnClickListener(View.OnClickListener {
            if (view_pager_walkthrough.currentItem == 2) {
                stopFragment()
            } else {
                view_pager_walkthrough.setCurrentItem(view_pager_walkthrough.currentItem+1, true)
            }
        })

        tvSkip.setOnClickListener {
            stopFragment()
        }


        view_pager_walkthrough.adapter = MyViewPagerAdapter(activity as Context)

        view_pager_walkthrough.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                Log.d("ppppppppppp", "scoled: " + position)
            }

            override fun onPageSelected(position: Int) {
                Log.d("ppppppppppp", "slectd: " + position)
                updateDots(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    //    COMPANION OBJECTS---------------------------------------
    companion object {
        var layouts: IntArray = intArrayOf(
            R.layout.item_walkthroug_pager,
            R.layout.item_walkthroug_pager,
            R.layout.item_walkthroug_pager
        )

    }

    private fun updateDots(position: Int) {
        iv_dot1.setBackground(resources.getDrawable(R.drawable.buble_default))
        iv_dot2.setBackground(resources.getDrawable(R.drawable.buble_default))
        iv_dot3.setBackground(resources.getDrawable(R.drawable.buble_default))

        if (position == 0) {
            iv_dot1.setBackground(resources.getDrawable(R.drawable.bubble_active))
        } else if (position == 1) {
            iv_dot2.setBackground(resources.getDrawable(R.drawable.bubble_active))
        } else if (position == 2) {
            iv_dot3.setBackground(resources.getDrawable(R.drawable.bubble_active))
        }
    }

    /**
     * View pager adapter
     */
    class MyViewPagerAdapter(val context: Context) : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null
        override fun instantiateItem(collection: ViewGroup, position: Int): Any {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(layouts[position], collection, false) as ViewGroup
            collection.addView(view, 0)
            return view
        }

        override fun getCount(): Int {
            return layouts.size
        }

        override fun isViewFromObject(
            view: View,
            obj: Any
        ): Boolean {
            return view === obj
        }

        override fun destroyItem(
            container: ViewGroup,
            position: Int,
            `object`: Any
        ) {
            val view = `object` as View
            container.removeView(view)
        }
    }

    private fun stopFragment() {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.frame_layout_splash_launcher, RegisterNumberFragment())?.commit()
    }
}
