package com.example.hongsapp0229

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.hongsapp0229.model.NoticeDTO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.item_notice.view.*

class HomeFragment : Fragment(){

    var firestore: FirebaseFirestore? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // RecyclerView
         //데이터 열거해서 보여주기 계속 같은놈 출력.
        //RecyclterView는 3가지 꼭 기억 (3개 아래)
        // xml에 recyclerview 추가
        // item xml 추가
        // fragment Activity에 recyclerview를 추가

        firestore = FirebaseFirestore.getInstance()

        val hView = inflater.inflate(R.layout.fragment_home, container, false)
        hView!!.home_addForm_tv.setOnClickListener {
            startActivity(Intent(context, FormActivity::class.java))
        }

        hView!!.home_rv.adapter = HomeRecyclerviewAdapter()//
        hView!!.home_rv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)//??채팅처럼 layout으로 실행시키라는 거?

        return hView
    }

    inner class HomeRecyclerviewAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>()
    {
        val noticeDTOs: ArrayList<NoticeDTO> // 그 안의 제목 설명 등을 담는 것

        init { // 데이터 가져오기
            noticeDTOs = ArrayList() // 초기화, 이 값은 0으로 출력됨 groupList.size = 0

            firestore?.collection("NOTICE")?.orderBy("date", Query.Direction.DESCENDING)//date순서대로 내림차순(Descending)
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                    noticeDTOs.clear() // 필수
                    if (querySnapshot != null) {
                        // println(querySnapshot.size())

                        for (snapshot in querySnapshot) {
                            try {
                                var item = snapshot.toObject(NoticeDTO::class.java)!!
                                noticeDTOs.add(item)
                            }
                            catch(e: Exception){}
                        }
                    }
                    notifyDataSetChanged() // 필수
                }
        }

        override fun getItemCount(): Int {
            return noticeDTOs.size
        }

        override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
            val viewHolder = (holder as CustomViewHolder).itemView

            viewHolder.notice_content_tv.text = noticeDTOs[position].title
            viewHolder.notice_writer_tv.text = noticeDTOs[position].writer

        } // OnBindView Finish

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent?.context).inflate(R.layout.item_notice, parent, false)
            return CustomViewHolder(view)
        }

        private inner class CustomViewHolder(view: View?) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view!!)
    }
}