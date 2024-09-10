package org.robotics.blinkworld.Adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.message_item.view.*
import org.robotics.blinkworld.R
import org.robotics.blinkworld.Utils.asTime
import org.robotics.blinkworld.Utils.currentUid
import org.robotics.blinkworld.Utils.fromUrl
import org.robotics.blinkworld.Utils.loadUserPhoto
import org.robotics.blinkworld.models.Message
import java.text.SimpleDateFormat


class MessagesAdapter(private val listener: Listener):RecyclerView.Adapter<MessagesAdapter.SingleChatHolder>() {
    private var mListMessagesCache = mutableListOf<Message>()
    private lateinit var mDiffResult: DiffUtil.DiffResult
    interface Listener {
        fun readMsg(id: String,uid: String)
        fun openPosts(uid:String, img: String,type:Int)
        fun notificstion(uid:String,text:String)

    }


    class SingleChatHolder(view: View) : RecyclerView.ViewHolder(view) {
        val blocUserMessage: CardView = view.bloc_user_message
        val chatUserMessage: TextView = view.chat_user_message
        //val chatUserMessageTime: TextView = view.chat_user_message_time

        val blocReceivedMessage: CardView = view.bloc_received_message
        val chatReceivedMessage: TextView = view.chat_received_message
        val photoUserReceived: ImageView = view.avatar_chat_group
        //val chatReceivedMeessageTime: TextView = view.chat_received_message_time


        val blocReceivedImageMessage: LinearLayout = view.bloc_received_image_message
        val blocUserImageMessage: LinearLayout = view.bloc_user_image_message
        val chatUserImage: ImageView = view.squareImageViewUser
        val chatUserImageAuthorPhoto:ImageView = view.bloc_user_image_author_message

        val chatReceivedImage: ImageView = view.squareImageReceivedUser
        //val chatUserImageMessageTime:TextView = view.chat_user_image_message_time
        val chatReceivedImageMessage: TextView = view.chat_received_image_message
        val chatUserImageMessage: TextView = view.chat_user_image_message
        val chatReceivedImageAuthorPhoto:ImageView = view.bloc_received_image_author_message



        //Date в чате
        val chatDateMessage: TextView = view.text_date_chat
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return SingleChatHolder(view)
    }

    override fun getItemCount(): Int = mListMessagesCache.size


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: SingleChatHolder, position: Int) {

        listener.readMsg(mListMessagesCache[position].id,mListMessagesCache[position].uid)
        holder.chatUserImageAuthorPhoto.clipToOutline = true
        holder.chatReceivedImageAuthorPhoto.clipToOutline = true
        //setTimeTextVisibility(m.fileStemp(),previousTs,holder.chatDateMessage,holder.chatDateBlock)
        when(mListMessagesCache[position].type){
            "text" -> drawMessageText(holder,position)
            "image" -> drawMessageImage(holder,position)



        }


        drawTime(holder,position)

        //listener.readMsg(mListMessagesCache[position].from,mListMessagesCache[position].id)



    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun drawTime(holder: MessagesAdapter.SingleChatHolder, position: Int) {

        if(position - 1 != -1){


            holder.chatDateMessage.text
            val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            var firstMsg= mListMessagesCache[position].fileStemp().toString().asTime()
            var secondMsg = mListMessagesCache[position-1].fileStemp().toString().asTime()

            val d1= sdf.parse(firstMsg)
            val d2 = sdf.parse(secondMsg)
            val difference_In_Time= d1.time- d2.time
            val difference_In_Hours = ((difference_In_Time
                    / (1000 * 60 * 60))
                    % 24)
            Log.d("NAGHELP",difference_In_Hours.toString())
            //val difference_In_Time=firstMsg.getTime() - secondMsg.getTime()


        if(difference_In_Hours > 3){
            val sdf2 = SimpleDateFormat("dd-MM-yyyy")
            val d3= sdf2.parse(firstMsg)
            holder.chatDateMessage.visibility = View.VISIBLE
            holder.blocUserMessage.visibility = View.GONE
            holder.blocReceivedMessage.visibility = View.GONE
            holder.blocReceivedImageMessage.visibility = View.GONE
            holder.blocUserImageMessage.visibility = View.GONE
            holder.chatDateMessage.text = d3.toString().take(10)
            //drawMessageText(holder,position+1)
            //drawMessageText(holder,position)


        }
        }
    }

    private fun drawMessageImage(holder: MessagesAdapter.SingleChatHolder, position: Int) {
        holder.blocUserMessage.visibility = View.GONE
        holder.blocReceivedMessage.visibility = View.GONE
        holder.chatDateMessage.visibility = View.GONE

        holder.blocReceivedImageMessage.visibility = View.VISIBLE
        holder.blocUserImageMessage.visibility = View.VISIBLE
        holder.chatUserImage.setOnClickListener {
            listener.openPosts(mListMessagesCache[position].uid,mListMessagesCache[position].imagePosts!!,mListMessagesCache[position].typeImage!!)
        }
        holder.chatReceivedImage.setOnClickListener {
            listener.openPosts(mListMessagesCache[position].uid,mListMessagesCache[position].imagePosts!!,mListMessagesCache[position].typeImage!!)
        }
        if (mListMessagesCache[position].uid == currentUid()) {
            holder.blocReceivedImageMessage.visibility = View.GONE
            holder.blocUserImageMessage.visibility = View.VISIBLE

            holder.chatUserImage.fromUrl(mListMessagesCache[position].imagePosts)
            holder.chatUserImageAuthorPhoto.loadUserPhoto(mListMessagesCache[position].userPhoto)
            holder.chatUserImageMessage.text = mListMessagesCache[position].authorPost
            holder.photoUserReceived.visibility = View.GONE


            holder.chatReceivedImageAuthorPhoto.visibility = View.VISIBLE

            //holder.chatUserImageMessage.text = mListMessagesCache[position].author
            //holder.chatUserImageMessageTime.text = mListMessagesCache[position].timeStamp.toString().asTime()
        } else {
            holder.blocReceivedImageMessage.visibility = View.VISIBLE
            holder.blocUserImageMessage.visibility = View.GONE
            holder.chatReceivedImageAuthorPhoto.loadUserPhoto(mListMessagesCache[position].userPhoto)
            holder.chatReceivedImageMessage.text = mListMessagesCache[position].authorPost
            holder.chatReceivedImage.fromUrl(mListMessagesCache[position].imagePosts)
            holder.photoUserReceived.visibility = View.GONE
            listener.notificstion(mListMessagesCache[position].uid,mListMessagesCache[position].text)
            holder.chatReceivedImageAuthorPhoto.visibility = View.VISIBLE


            // holder.chatReceivedImageMessageTime.text = mListMessagesCache[position].timeStamp.toString().asTime()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun drawMessageText(holder: MessagesAdapter.SingleChatHolder, position: Int) {
        holder.blocReceivedImageMessage.visibility = View.GONE
        holder.blocUserImageMessage.visibility = View.GONE
        holder.chatDateMessage.visibility = View.GONE

        holder.blocUserMessage.visibility = View.VISIBLE
        holder.blocReceivedMessage.visibility = View.VISIBLE
        if (mListMessagesCache[position].uid == currentUid()) {
            holder.blocUserMessage.visibility = View.VISIBLE
            holder.blocReceivedMessage.visibility = View.GONE
            holder.chatUserMessage.text = mListMessagesCache[position].text
            holder.photoUserReceived.visibility = View.GONE


            //holder.chatUserMessageTime.text =
            //mListMessagesCache[position].timeStamp.toString().asTime()

        } else if(mListMessagesCache[position].uid != currentUid()!!){
            holder.blocUserMessage.visibility = View.GONE
            holder.blocReceivedMessage.visibility = View.VISIBLE
            holder.chatReceivedMessage.text = mListMessagesCache[position].text
            listener.notificstion(mListMessagesCache[position].uid,mListMessagesCache[position].text)
            if(position-1 != -1){
                if(mListMessagesCache[position].uid !=mListMessagesCache[position-1].uid){
                    holder.photoUserReceived.visibility = View.VISIBLE
                    holder.photoUserReceived.loadUserPhoto(mListMessagesCache[position].photo)
                }else{
                    holder.photoUserReceived.visibility = View.GONE
                }
            }

            //holder.photoUserReceived.visibility = View.VISIBLE
            holder.photoUserReceived.clipToOutline = true

            //holder.chatReceivedMeessageTime.text =
            //mListMessagesCache[position].timeStamp.toString().asTime()

        }

    }

    fun addItemToTop(
        item: Message,
        onSuccess: () -> Unit,
    ){
        if (!mListMessagesCache.contains(item)) {
            mListMessagesCache.add(item)
            mListMessagesCache.sortBy { it.timeStamp.toString() }
            notifyItemInserted(0)
        }
        onSuccess()
    }
    fun addItemToBottom(
        item: Message,
        onSuccess: () -> Unit,
    ){
        if (!mListMessagesCache.contains(item)) {
            mListMessagesCache.add(item)
            notifyItemInserted(mListMessagesCache.size)
        }
        onSuccess()
    }


   





}