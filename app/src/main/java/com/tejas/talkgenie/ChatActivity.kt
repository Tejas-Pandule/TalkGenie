package com.tejas.talkgenie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.tejas.talkgenie.Adaptor.MessageAdaptor
import com.tejas.talkgenie.Models.Chat.ChatRequest
import com.tejas.talkgenie.Models.MessageModel
import com.tejas.talkgenie.api.ApiUtility
import com.tejas.talkgenie.databinding.ActivityChatBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import java.lang.Exception

class ChatActivity : AppCompatActivity() {
    private lateinit var binding:ActivityChatBinding
    var list= ArrayList<MessageModel>()
    private lateinit var mLayoutManager:LinearLayoutManager
    private lateinit var adaptor :MessageAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = mLayoutManager
        mLayoutManager.stackFromEnd  = true
        adaptor = MessageAdaptor(list)
        binding.recyclerView.adapter = adaptor

        binding.backBtn.setOnClickListener { finish() }


        binding.sendbtn.setOnClickListener {
            if (binding.userMsg.text!!.isEmpty()){
                Toast.makeText(this, "Please ask your question", Toast.LENGTH_SHORT).show()

            }else{
                CallApi()
            }
        }



    }

    private fun CallApi() {

        list.add(MessageModel(true,false,binding.userMsg.text.toString()))

        adaptor.notifyItemInserted(list.size-1)
        binding.recyclerView.recycledViewPool.clear()
        binding.recyclerView.smoothScrollToPosition(list.size-1)

        val apiInterface = ApiUtility.getapiInterface()

        val requestBody = RequestBody.create(
            MediaType.parse("application/json"),
            Gson().toJson(
                ChatRequest(
                    250,
                    "text-davinci-003",
                    binding.userMsg.text.toString(),
                    0.7
                )
            )
        )

        val contentType = "application/json"
        val authorization = "Bearer ${Utils.API_KEY}"


        lifecycleScope.launch(Dispatchers.IO) {

            try {

                val response = apiInterface.getChat(
                    contentType,authorization,requestBody
                )

                val txtResponse = response.choices.first().text

                list.add(MessageModel(false,false,txtResponse))


                withContext(Dispatchers.Main){
                    adaptor.notifyItemInserted(list.size-1)
                    binding.recyclerView.recycledViewPool.clear()
                    binding.recyclerView.smoothScrollToPosition(list.size-1)
                }

                 binding.userMsg.text!!.clear()

            }catch (e:Exception){

                withContext(Dispatchers.Main){
                    Toast.makeText(this@ChatActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }





        }

    }
}