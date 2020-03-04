package com.example.hongsapp0229

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.hongsapp0229.model.ChatDTO
import com.example.hongsapp0229.model.NoticeDTO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat.view.*
import kotlinx.android.synthetic.main.item_chat.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatFragment : Fragment()
{
    var firestore: FirebaseFirestore? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firestore = FirebaseFirestore.getInstance()


        val cView = inflater.inflate(R.layout.fragment_chat, container, false)
        cView!!.chat_send_btn.setOnClickListener {

            //startActivity(Intent(context, ChatgoActivity::class.java))
            // Firebase 데이터베이스로 보내는 코드
            val chatData = ChatDTO()


            아~ 진짜 짜증나네!!!!!!!!!!!!!!!!!!!
            val timeStamp = SimpleDateFormat("yyMMddHHmmss").format(Date())
            chatData.date = timeStamp.toLong()
            chatData.writer = "hw"
            chatData.content = chat_content_et.text.toString()

            cView!!.chat_content_et.setText("")

            firestore?.collection("CHAT")?.document()?.set(chatData)?.addOnSuccessListener {

                cView!!.chat_rv.scrollToPosition( cView!!.chat_rv.adapter?.itemCount!! - 1) // 채팅 클릭시 맨 아래로 내리는 명령어 (바로뜨도록)

                //Toast.makeText(context, "채팅을 생성하였습니다,", Toast.LENGTH_LONG).show()
                //cView!!.chat_content_et.setText("")//cView!! 에서 호출을 했다는 의미 chat_content_te.setText//전송버튼 누르고 ""으로 값을 비움.


            }
        }

        cView!!.chat_rv.adapter = ChatRecyclerviewAdapter()//밑에 명시된 함수의 데이터를 가져오겠다는 의미, 이거 두개는 무조건 있어야 한다는것;.
        cView!!.chat_rv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)//??채팅처럼 layout으로 실행시키라는 거?
        return cView
    }

    inner class ChatRecyclerviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>()
    {
        val chatDTOs: ArrayList<ChatDTO>
        init { // 데이터 가져오기
            chatDTOs = ArrayList() // 초기화, 이 값은 0으로 출력됨 groupList.size = 0

            firestore?.collection("CHAT")?.orderBy("date", Query.Direction.ASCENDING)//date순서대로 내림차순(Descending)
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                    chatDTOs.clear() // 필수
                    if (querySnapshot != null) {
                        // println(querySnapshot.size())

                        for (snapshot in querySnapshot) {
                            try {
                                var item = snapshot.toObject(ChatDTO::class.java)!!
                                chatDTOs.add(item)
                            }
                            catch(e: Exception){}
                        }
                    }
                    notifyDataSetChanged() // 필수
                }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent?.context).inflate(R.layout.item_chat, parent, false)
            return CustomViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
        {
            val viewHolder = (holder as CustomViewHolder).itemView
            //보여주고싶은 데이터 개수만큼 viewHolder를 쓴다


            viewHolder.chat_content_tv.text = chatDTOs[position].content

            val dateNam=chatDTOs[position].date%1000000
            val dateMok=dateNam/100

            var dateString : String

            if(dateMok>1259){

                val hourString = (dateMok - 1200) / 100
                val minuteString = (dateMok - 1200) % 100

                dateString = "${hourString}:${minuteString} PM"
            }
            else {
                val hourString = dateMok / 100
                val minuteString = dateMok % 100

                dateString = "${hourString}:${minuteString} AM"
            }

            //auth?.currentUser?.uid
            if(chatDTOs[position].writer == "hw"){
                viewHolder.chat_date1_lo.visibility = View.VISIBLE
                viewHolder.chat_date2_lo.visibility = View.GONE

                viewHolder.chat_date1_tv.text = dateString

                viewHolder.chat_chat_lo.setBackgroundResource(R.drawable.speech_bubble_outgoing)

                var layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.setMargins(0,dpToPx(15),dpToPx(15),dpToPx(15))
                viewHolder.chat_all_lo.layoutParams = layoutParams
                viewHolder.chat_all_lo.gravity = Gravity.RIGHT
                viewHolder.chat_chat_lo.setPadding(dpToPx(15), dpToPx(10), dpToPx(25), dpToPx(10))

            }
            else{
                viewHolder.chat_date1_lo.visibility = View.GONE
                viewHolder.chat_date2_lo.visibility = View.VISIBLE

                viewHolder.chat_date2_tv.text = dateString

                viewHolder.chat_chat_lo.setBackgroundResource(R.drawable.speech_bubble_incoming)

                var layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.setMargins(dpToPx(15),dpToPx(15),0,dpToPx(15))
                viewHolder.chat_all_lo.layoutParams = layoutParams
                viewHolder.chat_all_lo.gravity = Gravity.LEFT
                viewHolder.chat_chat_lo.setPadding(dpToPx(25), dpToPx(10), dpToPx(15), dpToPx(10))

            }

        }

            //200304150330



        override fun getItemCount(): Int {
            return chatDTOs.size
        }
        private inner class CustomViewHolder(view: View?) : RecyclerView.ViewHolder(view!!)
    }

    fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return Math.round(dp.toFloat() * density)
    }
}
