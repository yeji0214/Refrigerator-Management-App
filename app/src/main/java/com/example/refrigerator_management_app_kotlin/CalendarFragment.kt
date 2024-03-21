package com.example.refrigerator_management_app_kotlin

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class CalendarFragment : Fragment {
    private lateinit var allItems: ArrayList<Item> // 모든 아이템들이 들어있는 ArrayList
    private lateinit var items: ArrayList<Item>
    private lateinit var itemsLiveData: MutableLiveData<ArrayList<Item>> // 유통기한이 0일 ~ day 남은 아이템들이 들어있는
    //private lateinit var adapter: CalendarItemAdapter
    private lateinit var context: Context
    private lateinit var notificationText: String

    private lateinit var notificationManager: NotificationManager
    private lateinit var calendarView: CalendarView

    private var day = 4

    constructor() : super() {
        // Required empty public constructor
    }

    constructor(context: Context) : super() {
        this.context = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textTitle = "Notification Title"
        val textContent = "Lorem ipsum dolor sit."
        // 채널 생성
        createNotificationChannel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allItems = (requireActivity() as ItemActivity).getAllItems()
        items = ArrayList()
        itemsLiveData = MutableLiveData()
        //adapter = CalendarItemAdapter(requireContext(), items)
        val listView = view.findViewById<ListView>(R.id.calendarListView)
        //listView.adapter = adapter

        calendarView = view.findViewById(R.id.calendarView)
        calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            // allItems 안에 들어있는 것 중에 선택된 날짜와 유통기한이 4일 이하로 차이나는 아이템들을 viewModel에 넣기
            items.clear()
            itemsLiveData.value = items
            try {
                getItemOnVergeOfExpiration(selectedDate)

                notificationText = ""
                for (i in items.indices) {
                    val diff = getExpirationDateDifference(items[i].expirationDate, selectedDate) // 유통기한 - 선택한 날짜
                    val diffDate = diff.toString()
                    Log.e("onSelectedDayChange", "$diffDate 남음")
                    val text = "${items[i].name}이(가) $diffDate 일 남았습니다!\n"
                    notificationText += text
                }
                sendNotification(notificationText)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            //adapter.notifyDataSetChanged()
        }

        val todayDate = getCurrentDate()
        Log.e("CalendarFragment", "오늘 날짜는 $todayDate")
        try {
            getItemOnVergeOfExpiration(todayDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        //adapter.notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate(): String {
        val now = LocalDate.now()
        return now.toString()
    }

    // 유통기한1 - 유통기한2의 차를 반환하는 함수
    @Throws(ParseException::class)
    private fun getExpirationDateDifference(date1: String, date2: String): Long {
        val format1 = SimpleDateFormat("yyyy-MM-dd").parse(date1)
        val format2 = SimpleDateFormat("yyyy-MM-dd").parse(date2)

        val diffSec = (format1.time - format2.time) / 1000
        val diffDays = diffSec / (24 * 60 * 60)

        return diffDays
    }

    // 유통기한이 인자로 받은 날짜와 days 이하로 차이나는 아이템들을 viewModel에 넣는 함수
    @Throws(ParseException::class)
    private fun getItemOnVergeOfExpiration(selectedDate: String) {
        for (i in allItems.indices) {
            val item = allItems[i]
            val itemDate = item.expirationDate // 아이템의 유통기한

            val diff = getExpirationDateDifference(itemDate, selectedDate) // 유통기한 - 선택한 날짜
            // 유통기한이 0 ~ day일 남은 경우
            if (diff <= day && diff >= 0) {
                if (!items.contains(item)) {
                    item.remainDays = diff.toInt()
                    items.add(item)
                    itemsLiveData.value = items
                    Log.e("CalendarFragment", "items size = ${items.size}")
                }
            }
        }
    }

    private fun createNotificationChannel() {
        // Android8.0 이상인지 확인
        notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.e("createNotificationChannel", "createNotificationChannel")
            // 채널에 필요한 정보 제공
            val name = "name"
            val description = "description"

            // 중요도 설정, Android7.1 이하는 다른 방식으로 지원한다.(위에서 설명)
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            // 채널 생성
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Notification Builder를 만드는 메소드
    private fun getNotificationBuilder(text: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setContentTitle("[내 손 안의 냉장고] 유통기한 알림")
            .setContentText(text)
            .setSmallIcon(R.drawable.calendar_icon)
    }

    // Notification을 보내는 메소드
    fun sendNotification(text: String) {
        Log.e("sendNotification", "sendNotification")
        // Builder 생성
        val notifyBuilder = getNotificationBuilder(text)
        // Manager를 통해 notification 디바이스로 전달
        notificationManager.notify(notificationId, notifyBuilder.build())
    }

    companion object {
        const val CHANNEL_ID = "channelId"
        const val notificationId = 1
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String): CalendarFragment {
            val fragment
                    = CalendarFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }
}
