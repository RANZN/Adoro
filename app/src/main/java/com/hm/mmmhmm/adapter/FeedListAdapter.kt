package com.hm.mmmhmm.adapter

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.hm.mmmhmm.R
import com.hm.mmmhmm.fragments.CommentsFragment
import com.hm.mmmhmm.fragments.HomeFragment
import com.hm.mmmhmm.fragments.ProfileFragment
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.load
import com.hm.mmmhmm.models.*
import com.hm.mmmhmm.web_service.ApiClient
import com.hm.mmmhmm.web_service.ApiClient.BASE_URL
import kotlinx.coroutines.*


class FeedListAdapter(var ctx: FragmentActivity, private var feedList: List<ItemComment>? = null,private var sessionId:Long?=null) :
    RecyclerView.Adapter<FeedListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tv_username.text = feedList?.get(position)?.username
        // holder.tv_apply_count.text= feedList?.get(position)?.like.size()
        //holder.tv_apply_count.text= feedList?.get(position)?.shortDescription
        // holder.tv_time_left.text= "₹"+feedList?.get(position)?.timeLeft.toString()+" left"
//           holder.tv_apply_count.text= feedList?.get(position)?.comment?.size()
        var text = feedList?.get(position)?.description
        holder.tv_feed_description.text = text

        if (feedList?.get(position)?.description?.length?:0>80) {
            var text=text?.substring(0,80)+"...";
            holder.tv_feed_description.setText(Html.fromHtml(text+"<font color='red'> <u>View More</u></font>"));


            holder.tv_feed_description.setOnClickListener{
                val commentsFragment = CommentsFragment()
                val args = Bundle()
                args.putString("postId", feedList?.get(position)?._id)
                commentsFragment.arguments = args
                ctx.supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, commentsFragment).addToBackStack(null)
                    .commit()

                (HomeFragment).lastFirstVisiblePosition = position
            }
        }


        holder.iv_user_feed.load(
            feedList?.get(position)?.profile,
            R.color.text_gray,
            R.color.text_gray,
            true
        )
        holder.iv_feed.load(
            feedList?.get(position)?.image,
            R.color.text_gray,
            R.color.text_gray,
            false
        )
        holder.itemView.setOnClickListener {
            //todo

        }

        holder.iv_share.setOnClickListener {
            val intent= Intent()
            intent.action= Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,
                BASE_URL+"?"+"&postId="+ feedList?.get(position)?.id)
            intent.type="text/plain"
            ctx.startActivity(Intent.createChooser(intent,"Share To:"))
        }
        holder.iv_menu_feed.setOnClickListener {
            val popupMenu = PopupMenu(ctx, holder.iv_menu_feed)
            popupMenu.inflate(R.menu.menu)
            popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem?): Boolean {
                    when (item?.itemId) {
                        R.id.delete -> {
                            // here are the logic to delete an item from the list
//                        val tempLang = languageList[position]
//                        languageList.remove(tempLang)
//                        rvAdapter.notifyDataSetChanged()
                            return true
                        }
                        // in the same way you can implement others
//                        R.id.edit -> {
//                            // define
//                            Toast.makeText(ctx , "Item 2 clicked" , Toast.LENGTH_SHORT).show()
//                            return true
//                        }

                    }
                    return false
                }
            })
            popupMenu.show()

        }
        holder.tv_username.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val profileFragment = ProfileFragment()
                val args = Bundle()
                args.putString("path", "search")
                args.putString("userId", feedList?.get(position)?.id)
                profileFragment.arguments = args
                ctx.supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_main, profileFragment)
                    .addToBackStack(null).commit()
                (HomeFragment).lastFirstVisiblePosition = position
                SessionManager.setFeedLastPosition(position)
            }})
        holder.iv_user_feed.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val profileFragment = ProfileFragment()
                val args = Bundle()
                args.putString("path", "search")
                args.putString("userId", feedList?.get(position)?.id)
                profileFragment.arguments = args
                ctx.supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_main, profileFragment)
                    .addToBackStack(null).commit()
                (HomeFragment).lastFirstVisiblePosition = position
                SessionManager.setFeedLastPosition(position)
            }})
        holder.ll_feed_user_detail.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val profileFragment = ProfileFragment()
                val args = Bundle()
                args.putString("path", "search")
                args.putString("userId", feedList?.get(position)?.id)
                profileFragment.arguments = args
                ctx.supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_main, profileFragment)
                    .addToBackStack(null).commit()
                (HomeFragment).lastFirstVisiblePosition = position
                SessionManager.setFeedLastPosition(position)
            }})




        holder.tv_like_count.text =
            (feedList?.get(position)?.like as ArrayList<Like>).size.toString()

        holder.tv_comment_count.text = (feedList?.get(position)?.comment as List<Comment>).size.toString()

        for (Like in feedList?.get(position)?.like as ArrayList<Like>) {
            if(Like.id==SessionManager.getUserId()){
                holder.iv_like.isChecked=true

            }else{
                holder.iv_like.isChecked=false
            }
        }
        val animation1 = AnimationUtils.loadAnimation(ctx.applicationContext, R.anim.scale)
        holder.iv_like.setOnClickListener {
            var postItem: ItemComment?=feedList?.get(position)
            holder.iv_like.startAnimation(animation1)
            if (holder.iv_like.isChecked){
                var likeData: Like = Like(
                    SessionManager.getUserId(),
                    SessionManager.getUserPic(),
                    SessionManager.getUserName()
                )
                postItem?.like?.add(likeData)
                postUpdateLike(postItem!!)
            }else{
                var likeList: ArrayList<Like>? = feedList?.get(position)?.like
                for(Like in (likeList as List<Like>)){
                    if(Like.id==SessionManager.getUserId()){
                        likeList.remove(Like)
                    }
                }
                postItem?.like=likeList
                postUpdateLike(postItem!!)
            }

        }

        var doubleClick: Boolean? = false

        holder.iv_feed.setOnClickListener{
            if (doubleClick == true) {
                //binding.imag.isSelected= true
                holder.iv_like.isChecked = true
                holder.bigImage.visibility = View.VISIBLE
                ctx.lifecycleScope.launch {
                    holder.iv_like.startAnimation(animation1)
                    holder.bigImage.startAnimation(animation1)
                    delay(300)
                    holder.bigImage.visibility = View.GONE
                }
            }
            doubleClick = true
            Handler().postDelayed({ doubleClick = false }, 2000)
        }
        holder.ll_comments.setOnClickListener{
            val commentsFragment = CommentsFragment()
            val args = Bundle()
            args.putString("postId", feedList?.get(position)?._id)
            commentsFragment.arguments = args
            ctx.supportFragmentManager.beginTransaction().add(R.id.frame_layout_main, commentsFragment).addToBackStack("comment")
                .commit()
            SessionManager.setFeedLastPosition(position)
            (HomeFragment).lastFirstVisiblePosition = position
        }

        holder.tv_like_status.text= "and "+(feedList?.get(position)?.like as List<Like>).size.toString()+" others "+ ctx.getResources().getString(R.string.also_liked_the_post)
        holder.tv_like_status.visibility= if ((feedList?.get(position)?.like as List<Like>).size>1) View.VISIBLE else  View.GONE

        holder.recycler_mutual_like_user.adapter= MutualLikerAdapter(ctx,feedList?.get(position)?.like as List<Like>)
    }


    override fun getItemCount(): Int {
        return feedList?.size ?: 0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val iv_user_feed: ImageView
        val iv_share: ImageView
        val iv_feed: ImageView
        val iv_like: CheckBox
        val bigImage: ImageView
        val tv_username: TextView
        val tv_like_count: TextView
        val tv_share_count: TextView
        val tv_comment_count: TextView
        val tv_feed_description: TextView
        val tv_like_status: TextView
        val iv_menu_feed: ImageView
        val ll_comments: LinearLayout
        val ll_feed_user_detail: LinearLayout
        val recycler_mutual_like_user: RecyclerView

        //
        init {
            bigImage= v.findViewById(R.id.bigImage)
            iv_user_feed = v.findViewById(R.id.iv_user_feed)
            ll_feed_user_detail = v.findViewById(R.id.ll_feed_user_detail)
            iv_share = v.findViewById(R.id.iv_share)
            recycler_mutual_like_user = v.findViewById(R.id.recycler_mutual_like_user)
            iv_feed = v.findViewById(R.id.iv_feed)
            iv_like = v.findViewById(R.id.iv_like)
            tv_like_status = v.findViewById(R.id.tv_like_status)
            tv_username = v.findViewById(R.id.tv_username)
            tv_like_count = v.findViewById(R.id.tv_like_count)
            tv_share_count = v.findViewById(R.id.tv_share_count)
            tv_comment_count = v.findViewById(R.id.tv_comment_count)
            tv_feed_description = v.findViewById(R.id.tv_feed_description)
            iv_menu_feed = v.findViewById(R.id.iv_menu_feed)
            ll_comments = v.findViewById(R.id.ll_comments)
        }
    }

    private fun postUpdateLike(
        itemComment: ItemComment
    ) {
        // pb_group_detail.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(ctx)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.updateLike(itemComment)
                withContext(Dispatchers.Main) {
                    // pb_group_detail.visibility = View.GONE

                    try {
                        //  toast("" + response.body()?.message)
                        if (response != null) {
                          //todo
                        } else {
                            Log.d("resp", "complet else: ")
                        }

                    } catch (e: Exception) {
                        Log.d("resp", "cathch: " + e.toString())
                    }
                }

            } catch (e: Exception) {
                Log.d("weweewefw", e.toString())
            }
        }

    }


}